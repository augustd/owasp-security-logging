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
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.FilterReply;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import static org.mockito.Mockito.verify;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Marker;

/**
 *
 * @author August Detlefsen [augustd@codemagi.com]
 */
@RunWith(MockitoJUnitRunner.class)
public class ExcludeClassifiedMarkerFilterTest {

	LoggerContext loggerContext = (LoggerContext) LoggerFactory
			.getILoggerFactory();

	Logger LOGGER = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

	@Spy
	private final Appender<ILoggingEvent> mockAppender = LOGGER
			.getAppender("NOT_CLASSIFIED_CONSOLE");

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
		LOGGER.info("This statement is NOT confidential");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		System.out.println("testAppender(): loggingEvent: " + loggingEvent);

		// check the filter chain decision for this event
		assertEquals(FilterReply.NEUTRAL,
				mockAppender.getFilterChainDecision(loggingEvent));
	}

	@Test
	public void testAppenderSecurityEvent() {
		LOGGER.info(SecurityMarkers.SECURITY_SUCCESS,
				"This statement is a security event");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		LoggingEvent loggingEvent = captorLoggingEvent.getValue();
		System.out.println("testAppender(): loggingEvent: " + loggingEvent);

		// check the filter chain decision for this event
		assertEquals(FilterReply.NEUTRAL,
				mockAppender.getFilterChainDecision(loggingEvent));
	}

	@Test
	public void testAppenderConfidentialEvent() {
		LOGGER.info(SecurityMarkers.CONFIDENTIAL,
				"This statement is confidential");

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
	public void testAppenderRestrictedEvent() {
		LOGGER.info(SecurityMarkers.RESTRICTED, "This statement is restricted");

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
	public void testAppenderSecretEvent() {
		LOGGER.info(SecurityMarkers.SECRET, "This statement is secret");

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
	public void testAppenderTopSecretEvent() {
		LOGGER.info(SecurityMarkers.TOP_SECRET, "This statement is top secret");

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
	public void testAppenderMultipleEvent() {
		Marker multi = SecurityMarkers.getMarker(
				SecurityMarkers.SECURITY_AUDIT, SecurityMarkers.CONFIDENTIAL);
		LOGGER.info(multi,
				"This statement contains multiple markers: audit and confidential");

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
		ExcludeClassifiedMarkerFilter mkt = new ExcludeClassifiedMarkerFilter();
		mkt.setContext(loggerContext);
		mkt.start();

		assertTrue(mkt.isStarted());

		// test a logging event with no markers
		ILoggingEvent nulEvent = new LoggingEvent();
		assertEquals(FilterReply.NEUTRAL, mkt.decide(nulEvent));

		// test a logging event with the CONFIDENTIAL marker
		LoggingEvent confidentialEvent = new LoggingEvent();
		confidentialEvent.setMarker(SecurityMarkers.CONFIDENTIAL);
		assertEquals(FilterReply.DENY, mkt.decide(confidentialEvent));

		// test a logging event with the RESTRICTED marker
		LoggingEvent restrictedEvent = new LoggingEvent();
		restrictedEvent.setMarker(SecurityMarkers.RESTRICTED);
		assertEquals(FilterReply.DENY, mkt.decide(restrictedEvent));

		// test a logging event with the SECRET marker
		LoggingEvent secretEvent = new LoggingEvent();
		secretEvent.setMarker(SecurityMarkers.SECRET);
		assertEquals(FilterReply.DENY, mkt.decide(secretEvent));

		// test a logging event with the TOP_SECRET marker
		LoggingEvent topSecretEvent = new LoggingEvent();
		topSecretEvent.setMarker(SecurityMarkers.TOP_SECRET);
		assertEquals(FilterReply.DENY, mkt.decide(topSecretEvent));

		// test a logging event without the CONFIDENTIAL marker
		LoggingEvent normalEvent = new LoggingEvent();
		normalEvent.setMarker(SecurityMarkers.EVENT_SUCCESS);
		assertEquals(FilterReply.NEUTRAL, mkt.decide(nulEvent));
	}
}
