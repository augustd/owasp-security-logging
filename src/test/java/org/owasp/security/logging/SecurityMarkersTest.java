package org.owasp.security.logging;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityMarkersTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SecurityMarkersTest.class);

	@Test
	public void test() {
		LOGGER.info(SecurityMarkers.SECURITY_MARKER, "some security event");
		LOGGER.info("some other event");

	}

}
