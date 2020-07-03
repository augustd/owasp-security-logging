package org.owasp.security.logging.util;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Various logging features to consider adding to your own programs.
 * @author Milton Smith
 * @see "http://stackoverflow.com/questions/11187461/redirect-system-out-and-system-err-to-slf4j"
 */
public class SecurityUtil {

	private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
	
	private static Logger sysOutLogger = LoggerFactory.getLogger(SecurityUtil.class);
	private static Logger sysErrLogger = LoggerFactory.getLogger(SecurityUtil.class);	
	
	public static final PrintStream sysout = System.out;
	public static final PrintStream syserr = System.err;
	
	private SecurityUtil() {}
	
	/**
	 * Redirect <code>System.out</code> and <code>System.err</code> streams to SLF4J logger.
	 * This is a benefit if you have a legacy console logger application.  Does not provide
	 * benefit of a full implementation.  For example, no hierarchical or logger inheritence
	 * support but there are some ancilarity benefits like, 1) capturing messages that would
	 * otherwise be lost, 2) redirecting console messages to centralized log services, 3)
	 * formatting console messages in other types of output (e.g., HTML).
	 */
	public static void bindSystemStreamsToSLF4J() {
	    // Enable autoflush
	    System.setOut(new PrintStream(new OutputStreamRedirector(sysOutLogger, false), true));
	    System.setErr(new PrintStream(new OutputStreamRedirector(sysErrLogger, true), true));
	}
	
	/**
	 * Redirect <code>System.out</code> and <code>System.err</code> streams to the given SLF4J loggers.
	 * This is a benefit if you have a legacy console logger application.  Does not provide
	 * benefit of a full implementation.  For example, no hierarchical or logger inheritence
	 * support but there are some ancilarity benefits like, 1) capturing messages that would
	 * otherwise be lost, 2) redirecting console messages to centralized log services, 3)
	 * formatting console messages in other types of output (e.g., HTML).
	 * 
	 * @param sysOutLogger
	 * @param sysErrLogger
	 */
	public static void bindSystemStreamsToSLF4J(Logger newSysOutLogger, Logger newSysErrLogger) {
		if (newSysOutLogger != null) SecurityUtil.sysOutLogger = newSysOutLogger;
		if (newSysErrLogger != null) SecurityUtil.sysErrLogger = newSysErrLogger;
		bindSystemStreamsToSLF4J();
	}
	
	/**
	 * Unbined bound system loggers and restore these streams to their original state.
	 * @see #bindSystemStreamsToSLF4J()
	 */
	public static void unbindSystemStreams() {
	    System.setOut(sysout);
	    System.setErr(syserr);
	}

	/**
	 * Log command line arguments.  Mostly for use inside a <code>main()</code> method
	 * to quickly print arguments for future diagnostics.
	 * @param args Command line arguments
	 */
	public static void logCommandLineArguments( String[] args ) {
		if( args == null || args.length < 1 ) return;
		for( int i=0; i < args.length; i++ ) {
			logMessage("Cmd line arg["+i+"]="+args[i]);
		}
	}
	
	/**
	 * Log shell environment variables associated with Java process.
	 */
	public static void logShellEnvironmentVariables() {
		Map<String, String> env = System.getenv();
		Iterator<String> keys = env.keySet().iterator();
		while (keys.hasNext() ) {
			String key = keys.next();
			String value = env.get(key);
			logMessage("Env, "+key+"="+value.trim());
		}
	}
	
	/**
	 * Log Java system properties.
	 */
	public static void logJavaSystemProperties() {
		Properties properties = System.getProperties();
		Iterator<Object> keys = properties.keySet().iterator();
		while (keys.hasNext() ) {
			Object key = keys.next();
			Object value = properties.get(key);
			logMessage("SysProp, "+key+"="+value.toString().trim());
		}
	}
	
	/**
	 * Log the formatted message at the INFO priority.  Override this method to log your
	 * status using security markers or different log levels like the following,
	 * <code>
     * tbd
	 * </code>
	 * @param message Message to log.
	 */
	public static void logMessage( String message ) {
		
		logger.info( message );
		
	}
	
}
