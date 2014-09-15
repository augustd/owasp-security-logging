package org.owasp.security.logging;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class SecurityMarkersTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SecurityMarkersTest.class);

	@Test
	public void test() {
		LOGGER.info(SecurityMarkers.SECURITY_MARKER, "some security event");
		LOGGER.info("some other event");
		Marker confidential = MarkerFactory.getMarker("CONFIDENTIAL");
		confidential.add(SecurityMarkers.SECURITY_MARKER);
		String userid = "myId";
		String password = "password";
		LOGGER.info(confidential, "userid={}, password='{}'", userid, password);
	}
}
