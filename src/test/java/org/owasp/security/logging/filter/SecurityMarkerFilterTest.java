package org.owasp.security.logging.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.owasp.security.logging.SecurityMarkers.SECURITY_MARKER;

import org.junit.Test;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.turbo.MarkerFilter;
import ch.qos.logback.core.spi.FilterReply;

public class SecurityMarkerFilterTest {

	@Test
	public void testDecideILoggingEvent() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		lc.start();
		MarkerFilter mkt = new SecurityMarkerFilter();
		mkt.setContext(lc);
		mkt.setMarker(SecurityMarkers.SECURITY_MARKER_NAME);
		mkt.setOnMatch("ACCEPT");
		mkt.setOnMismatch("DENY");
		mkt.start();
		assertTrue(mkt.isStarted());
		assertEquals(FilterReply.DENY,
				mkt.decide(null, null, null, null, null, null));
		assertEquals(FilterReply.ACCEPT,
				mkt.decide(SECURITY_MARKER, null, null, null, null, null));
	}

}
