/**
 * 
 */
package org.owasp.security.logging.util;

/**
 * The IntervalLoggerController orchestrates background thread logging behavior.
 * The controller calls IntervalLoggerModel to initialize and update property
 * data and IntervalLoggerView to format and print the status message.  To
 * create and start a background logging thread do the following,
 * <code>
 * IntervalLogger wd = SecurityLoggingFactory.getIntervalLoggerControllerInstance();
 * wd.start();
 * ...
 * //Call the following before exit or to stop background logging
 * wd.stop();
 * </code>
 * Once the logger is started with the <code>DefaultIntervalLoggerView</code> a system
 * status is logged every 15 seconds.  A sample of the default log output.
 * <code>
 * Watchdog: MemoryTotal=64.5MB, FreeMemory=58.2MB, MaxMemory=954.7MB, Threads Total=5, Threads New=0, Threads Runnable=3, Threads Blocked=0, Threads Waiting=2, Threads Terminated=0 
 * </code>
 * Developers can customize by including their own properites to log,
 * remove default properties, or change the format and priority of the
 * status message logged.
 * @author Milton Smith
 * @see org.owasp.security.logging.util.DefaultIntervalLoggerView
 *
 */
public interface IntervalLoggerController {

	/**
	 * Start the IntervalLogger instance and log status each default 15-second interval.
	 */
	public void start();
	
	/**
	 * Start the IntervalLogger instance and log status each user defined interval.
	 * @param interval Status logging interval in milliseconds.
	 */
	public void start(int interval); 
		
	/**
	 * Stop background thread at earliest opportunity.
	 */
	public void stop();
	
	/**
	 * Set the status message view.  The status message view is used by the
	 * IntervalLogger framework to format the status message for logging and
	 * to perform the logging.
	 * @param v IntervalLoggerView instance.  If no view assigned, a
	 * DefaultIntervalLoggerView instance is assigned by default.
	 * @see org.owasp.security.logging.util.DefaultIntervalLoggerView
	 */
	public void setStatusMessageView(IntervalLoggerView v); 
	
	/**
	 * Set the status message model.  The status message model is used by the
	 * IntervalLogger framework to manage and refresh underlying property
	 * data.
	 * @param m IntervalLoggerModel instance.  If no model is assigned, the
	 * DefaultIntervalLoggerModel is assigned by default.  To change the
	 * @see org.owasp.security.logging.util.DefaultIntervalLoggerModel
	 */
	public void setStatusMessageModel(IntervalLoggerModel m); 
	
}
