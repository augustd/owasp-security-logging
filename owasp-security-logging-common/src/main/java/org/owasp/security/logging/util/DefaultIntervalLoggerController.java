package org.owasp.security.logging.util;

/**
 * Default implementation of the DefaultIntervalLoggerController.  The controller
 * orcastrates the workflow for logging status.  To obtain an instance of the controller
 * and start logging status do the following,
 * <code>
 * IntervalLoggerController wd = SecurityLoggingFactory.getControllerInstance();
 * wd.start();
 * ...
 * // before your program exists or you want to shutdown status thread do this
 * wd.stop();
 * </code>
 * @author Milton Smith
 *
 */
class DefaultIntervalLoggerController implements IntervalLoggerController, Runnable {

	/** Default loging interval, 15 seconds. */
	private static final int INTERVAL_DEFAULT = 15000;
	
	/** Assign the default interval as the desired interval. */
	private int interval = INTERVAL_DEFAULT;
	
	/** Flag to track thread execution. */
	private boolean running = false;
	
	/** Create a default view. The view knows how to format and log status messages. */
	private IntervalLoggerView view = new DefaultIntervalLoggerView();

	/** Create a default model. The models knows how to create and refresh property status data. */
	private IntervalLoggerModel model = new DefaultIntervalLoggerModel();
	
	/**
	 * Starts the controller
	 * @param interval User specified interval in milliseconds to log 
	 * each status message.
	 * @see org.owasp.security.logging.util.IntervalLoggerController
	 */
	public synchronized void start(int interval) {
		
		this.interval = interval;
		start();
		
	}
	
	/**
	 * Starts the controller at the default interval (15 seconds).
	 * @see org.owasp.security.logging.util.IntervalLoggerController
	 */
	public synchronized void start() {
		
		if( !running ) {
			
			new Thread(this).start();
			running = true;
		}
	}

	/**
	 * Stops the background at the earlist opportunity.
	 * @see org.owasp.security.logging.util.IntervalLoggerController
	 */
	public synchronized void stop() {
				
		running = false;

	}
	
	/**
	 * Checks if the controller is running.
	 * @return true, background thread running. false, background thread ended.
	 */
	synchronized boolean isRunning() {
				
		return running;

	}

	/**
	 * Main thread workflow.  Fires fireIntervalElapsed() each interval.
	 */
	public void run() {
		
		while( running ) {
			
			long interval_end = System.currentTimeMillis() + interval;
			
			while( interval_end > System.currentTimeMillis() ) {
				
				Thread.yield();
				
			}
			
			fireIntervalElapsed();
			
		}
		
		running = false;
		
	}
	
	/**
	 * Assign a IntervalLoggerView
	 * @param IntervalLoggerView instance to use.  DefaultIntervalLoggerView is
	 * assigned by default.
	 * @see org.owasp.security.logging.util.IntervalLoggerController
	 * @see org.owasp.security.logging.util.DefaultIntervalLoggerView
	 */
	public synchronized void setStatusMessageView(IntervalLoggerView view) {

		this.view = view;
		
	}
	
	/**
	 * Assign a IntervalLoggerModel
	 * @param IntervalLoggerModel instance to use.  DefaultIntervalLoggerModel is
	 * assigned by default.
	 * @see org.owasp.security.logging.util.IntervalLoggerController
	 * @see org.owasp.security.logging.util.DefaultIntervalLoggerModel
	 */
	public synchronized void setStatusMessageModel(IntervalLoggerModel model) {
		
		this.model = model;
		
	}
	
	/**
	 * This event is fired each elapsed interval.  The default controller
	 * retrives the properties, refreshes the model(and properties),
	 * formats the status for logging, and finally logs the message.
	 */
	private void fireIntervalElapsed() {
		
		IntervalProperty[] properties = model.getProperties();
		
		model.refresh();
		
		String msg = view.formatStatusMessage(properties);
		
		view.logMessage(msg);
		
	}
	

	
}
