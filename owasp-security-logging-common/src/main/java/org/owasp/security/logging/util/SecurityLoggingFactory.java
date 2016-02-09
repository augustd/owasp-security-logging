package org.owasp.security.logging.util;

/**
 * Factory for obtaining security logging singleton utility
 * classes.
 * @author Milton Smith
 *
 */
public class SecurityLoggingFactory {
	
	// Store the unwrapped controller instance so we can check isRunning()
	private static DefaultIntervalLoggerController instance;
	
	// Prevent caller created instances.
	private SecurityLoggingFactory() {}
	
	/**
	 * Return a single instance of the IntervalLoggerController.
	 * @return IntervalLoggerController instance to use.
	 */
	public synchronized static final IntervalLoggerController getControllerInstance() {
		
		IntervalLoggerController ic = null;
		
		// If no controller, create a bew instance.
		if( instance == null )  {
			instance = new DefaultIntervalLoggerController();
			ic =  new IntervalLoggerControllerWrapper( instance );
		}else{
			// If background logged stopped then return a new runnable thread.  An
			// edge case since once a background thread is started it will likely
			// run until the application exits.
			if( !instance.isRunning() ) {
				instance = new DefaultIntervalLoggerController();
				ic =  new IntervalLoggerControllerWrapper( instance );
			}
		}
			
		return ic;
		
	}

}