package org.owasp.security.logging.util;

/**
 * Format key/value pairs for logging and log status messages.
 * @author milton
 * @see org.owasp.security.logging.util.DefaultIntervalLoggerView
 */
public interface IntervalLoggerView {

	/**
	 * Format the message to be logged.
         * @param p An array of properties to log.
	 * @return Formatted log message.
	 * @see org.owasp.security.logging.util.DefaultIntervalLoggerView
	 */
	public String formatStatusMessage( IntervalProperty[] p );
	
	/**
	 * Log the formatted message.
	 * @param message Message to log.
	 * @see org.owasp.security.logging.util.DefaultIntervalLoggerView
	 */
	public void logMessage( String message );

}
