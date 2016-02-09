package org.owasp.security.logging.util;

import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Milton Smith
 * @see http://stackoverflow.com/questions/11187461/redirect-system-out-and-system-err-to-slf4j
 */
public class SecurityUtil {

	private static Logger sysOutLogger = LoggerFactory.getLogger(SecurityUtil.class);
	private static Logger sysErrLogger = LoggerFactory.getLogger(SecurityUtil.class);	
	
	public static final PrintStream sysout = System.out;
	public static final PrintStream syserr = System.err;
	
	public static void bindSystemStreamsToSLF4J() {
	    // Enable autoflush
	    System.setOut(new PrintStream(new OutputStreamRedirector(sysOutLogger, false), true));
	    System.setErr(new PrintStream(new OutputStreamRedirector(sysErrLogger, true), true));
	}
	
	public static void unbindSystemStreams() {
	    System.setOut(sysout);
	    System.setErr(syserr);
	}


}
