package org.owasp.security.logging.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

public class MarkerFilterTest {

	@Test
	public void testDecideILoggingEvent() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		// create a new marker filter
		MarkerFilter mkt = new MarkerFilter();
		mkt.setContext(lc);
		mkt.setMarker(SecurityMarkers.CONFIDENTIAL_MARKER_NAME);
		mkt.setOnMatch(FilterReply.ACCEPT);
		mkt.setOnMismatch(FilterReply.DENY);
		mkt.start();
		assertTrue(mkt.isStarted());

		// test a logging event with no markers
		ILoggingEvent nulEvent = new LoggingEvent();
		assertEquals(FilterReply.DENY, mkt.decide(nulEvent));

		// test a logging event with the CONFIDENTIAL marker
		LoggingEvent confidentialEvent = new LoggingEvent();
		confidentialEvent.setMarker(SecurityMarkers.CONFIDENTIAL);
		assertEquals(FilterReply.ACCEPT, mkt.decide(confidentialEvent));

		// test a logging event without the CONFIDENTIAL marker
		LoggingEvent normalEvent = new LoggingEvent();
		normalEvent.setMarker(SecurityMarkers.EVENT_SUCCESS);
		assertEquals(FilterReply.DENY, mkt.decide(nulEvent));

		Logger LOGGER = lc.getLogger(MarkerFilterTest.class);
		LOGGER.debug(SecurityMarkers.TOP_SECRET, "You should not see this!");
		LOGGER.debug(SecurityMarkers.CONFIDENTIAL,
				"Look at this confidential information!");
	}
}
