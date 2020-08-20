package org.owasp.security.logging.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.FilterReply;

/**
 *
 * @author August Detlefsen [augustd@codemagi.com]
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityMarkerFilterTest {

	LoggerContext loggerContext = (LoggerContext) LoggerFactory
			.getILoggerFactory();

	Logger LOGGER = (Logger) LoggerFactory.getLogger("CONSOLE"); 

	@Spy
	private final Appender<ILoggingEvent> mockAppender = LOGGER
			.getAppender("SECURITY_CONSOLE");

	// Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

	@Before
	public void setUp() {
		LOGGER.addAppender(mockAppender);
	}

	@After
	public void teardown() {
		LOGGER.detachAppender(mockAppender);
	}

	@Test
	public void testAppenderNormalEvent() {
		LOGGER.info("This statement is NOT a security event");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		System.out.println("testAppender(): loggingEvent: " + loggingEvent);

		// check the filter chain decision for this event
		assertEquals(FilterReply.DENY,
				mockAppender.getFilterChainDecision(loggingEvent));
	}

	@Test
	public void testAppenderSecuritySuccess() {
		LOGGER.info(SecurityMarkers.SECURITY_SUCCESS,
				"This statement is a security success");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		System.out.println("testAppender(): loggingEvent: " + loggingEvent);

		// check the filter chain decision for this event
		assertEquals(FilterReply.ACCEPT,
				mockAppender.getFilterChainDecision(loggingEvent));
	}

	@Test
	public void testAppenderSecurityFailure() {
		LOGGER.info(SecurityMarkers.SECURITY_FAILURE,
				"This statement is a security failure");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		System.out.println("testAppender(): loggingEvent: " + loggingEvent);

		// check the filter chain decision for this event
		assertEquals(FilterReply.ACCEPT,
				mockAppender.getFilterChainDecision(loggingEvent));
	}

	@Test
	public void testAppenderSecurityAudit() {
		LOGGER.info(SecurityMarkers.SECURITY_AUDIT,
				"This statement is a security audit");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		System.out.println("testAppender(): loggingEvent: " + loggingEvent);

		// check the filter chain decision for this event
		assertEquals(FilterReply.ACCEPT,
				mockAppender.getFilterChainDecision(loggingEvent));
	}

	@Test
	public void testAppenderMultipleEvent() {
		Marker multi = SecurityMarkers.getMarker(
				SecurityMarkers.SECURITY_AUDIT, SecurityMarkers.CONFIDENTIAL);
		LOGGER.info(multi,
				"This statement contains multiple markers: security audit and confidential");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		System.out.println("testAppender(): loggingEvent: " + loggingEvent);

		// check the filter chain decision for this event
		assertEquals(FilterReply.ACCEPT,
				mockAppender.getFilterChainDecision(loggingEvent));
	}

	@Test
	public void testAppenderMultipleNonSecurityEvent() {
		Marker multi = SecurityMarkers.getMarker(SecurityMarkers.EVENT_SUCCESS,
				SecurityMarkers.CONFIDENTIAL);
		System.out.println("MARKER: " + multi);
		LOGGER.info(multi,
				"This statement contains multiple markers: event success and confidential");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		System.out.println("testAppender(): loggingEvent: " + loggingEvent);

		// check the filter chain decision for this event
		assertEquals(FilterReply.DENY,
				mockAppender.getFilterChainDecision(loggingEvent));
	}

	@Test
	public void testRaw() {
		// create a new marker filter
		SecurityMarkerFilter mkt = new SecurityMarkerFilter();
		mkt.setContext(loggerContext);
		mkt.start();

		assertTrue(mkt.isStarted());

		// test a logging event with no markers
		ILoggingEvent nulEvent = new LoggingEvent();
		assertEquals(FilterReply.DENY, mkt.decide(nulEvent));

		// test a logging event with the CONFIDENTIAL marker
		LoggingEvent confidentialEvent = new LoggingEvent();
		confidentialEvent.setMarker(SecurityMarkers.SECURITY_SUCCESS);
		assertEquals(FilterReply.NEUTRAL, mkt.decide(confidentialEvent));

		// test a logging event with the RESTRICTED marker
		LoggingEvent restrictedEvent = new LoggingEvent();
		restrictedEvent.setMarker(SecurityMarkers.SECURITY_FAILURE);
		assertEquals(FilterReply.NEUTRAL, mkt.decide(restrictedEvent));

		// test a logging event with the SECRET marker
		LoggingEvent secretEvent = new LoggingEvent();
		secretEvent.setMarker(SecurityMarkers.SECURITY_AUDIT);
		assertEquals(FilterReply.NEUTRAL, mkt.decide(secretEvent));
	}

	@Test
	public void testRawAcceptAll() {
		// create a new marker filter
		SecurityMarkerFilter mkt = new SecurityMarkerFilter();
		mkt.setContext(loggerContext);
		mkt.setAcceptAll("true");
		mkt.start();

		assertTrue(mkt.isStarted());

		// test a logging event with no markers
		ILoggingEvent nulEvent = new LoggingEvent();
		assertEquals(FilterReply.DENY, mkt.decide(nulEvent));

		// test a logging event with the CONFIDENTIAL marker
		LoggingEvent confidentialEvent = new LoggingEvent();
		confidentialEvent.setMarker(SecurityMarkers.SECURITY_SUCCESS);
		assertEquals(FilterReply.ACCEPT, mkt.decide(confidentialEvent));

		// test a logging event with the RESTRICTED marker
		LoggingEvent restrictedEvent = new LoggingEvent();
		restrictedEvent.setMarker(SecurityMarkers.SECURITY_FAILURE);
		assertEquals(FilterReply.ACCEPT, mkt.decide(restrictedEvent));

		// test a logging event with the SECRET marker
		LoggingEvent secretEvent = new LoggingEvent();
		secretEvent.setMarker(SecurityMarkers.SECURITY_AUDIT);
		assertEquals(FilterReply.ACCEPT, mkt.decide(secretEvent));
	}

}
