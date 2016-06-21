package org.owasp.security.logging.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityUtilLoggingTest {

	private static final Logger logger = LoggerFactory.getLogger(SecurityUtilLoggingTest.class);
	
	@Test
	public void doBareBonesTest() {
		
		System.out.println("Print some sample command line arguments.");
		
		String[] fakeargs = new String[4];
		fakeargs[0] = "arg0";
		fakeargs[1] = "arg1";
		fakeargs[2] = "arg2";
		fakeargs[3] = "arg3";
		SecurityUtil.logCommandLineArguments(fakeargs);
		
		System.out.println("Print shell environment variables.");
		SecurityUtil.logShellEnvironmentVariables();
		
		System.out.println("Print Java system properties.");
		SecurityUtil.logJavaSystemProperties();
		
		logger.info( "barebones test finished");
		
	}
	
}
