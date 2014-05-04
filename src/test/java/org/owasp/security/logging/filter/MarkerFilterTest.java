package org.owasp.security.logging.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

public class MarkerFilterTest {

	@Test
	public void testDecideILoggingEvent() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		// lc.start();
		MarkerFilter mkt = new MarkerFilter();
		mkt.setContext(lc);
		mkt.setMarker(SecurityMarkers.SECURITY_MARKER_NAME);
		mkt.setOnMatch(FilterReply.ACCEPT);
		mkt.setOnMismatch(FilterReply.DENY);
		mkt.start();
		assertTrue(mkt.isStarted());
		ILoggingEvent nulEvent = new LoggingEvent();
		assertEquals(FilterReply.DENY, mkt.decide(nulEvent));
		// assertEquals(FilterReply.ACCEPT,
		// mkt.decide(SECURITY_MARKER, null, null, null, null, null));
	}
}
