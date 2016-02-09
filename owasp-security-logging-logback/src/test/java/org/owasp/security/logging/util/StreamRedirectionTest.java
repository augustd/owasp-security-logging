package org.owasp.security.logging.util;

import org.junit.Test;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamRedirectionTest {
	
	private static final Logger logger = LoggerFactory.getLogger(StreamRedirectionTest.class);
	
	@Test
	public void doTest() {
		
		System.out.println("Messages not going to SLF4j.  So sad.");
		
		SecurityUtil.bindSystemStreamsToSLF4J();
		
		System.out.println("Whoot, now I'm printing to SLF4J.");
		
		logger.error(SecurityMarkers.CONFIDENTIAL, "simple unclassified logging message.");
		
		System.out.flush();
		
		SecurityUtil.unbindSystemStreams();
		
		System.out.println("Stream restored.  Messages not going to SLF4J.  Sad yet again.");
		
		System.out.flush();
		
	}
	
}
