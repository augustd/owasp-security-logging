package org.owasp.security.logging.util;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * The DefaultIntervalLoggerView formats the key/value pairs for logging
 * using the following format,
 * <code>
 * Watchdog: property1=value1, property2=value3, ...
 * </code>
 * All values are logged on a single line.
 * @author Milton Smith
 *
 */
public class DefaultIntervalLoggerView implements IntervalLoggerView {

	private static final Logger logger = LoggerFactory.getLogger(DefaultIntervalLoggerView.class);
	
	/**
	 * Format the message to be logged.
	 * @param properties An array of properties to log.
	 * @return Formatted log message.
	 */
	@Override
	public String formatStatusMessage(IntervalProperty[] properties) {

		StringBuffer buff = new StringBuffer(500);	
		buff.append( "Watchdog: ");
		
		for( IntervalProperty p : properties ) {
			buff.append(p.getName());
			buff.append("=");
			buff.append(p.getValue());
			buff.append(", ");
		}
		
		if( buff.toString().endsWith(","))
			buff.setLength(buff.length()-1);

		return buff.toString();
		
	}
	
	/**
	 * Log the formatted message at the INFO priority.  Override this method to log your
	 * status using security markers or different log levels like the following,
	 * <code>
	 * IntervalLoggerController wd = SecurityLoggingFactory.getControllerInstance();
	 * // Add support for security markers
	 * wd.setStatusMessageView( new DefaultIntervalLoggerView() {
	 *   public void logMessage( String message ) {
	 *     logger.debug( SecurityMarkers.RESTRICTED, message );
	 *   }
	 * });
	 * </code>
	 * @param message Message to log.
	 */
        @Override
	public void logMessage( String message ) {
		
		logger.info( message );
		
	}
	
	
}
