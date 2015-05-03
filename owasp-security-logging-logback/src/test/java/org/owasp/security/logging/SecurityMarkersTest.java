package org.owasp.security.logging;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(MockitoJUnitRunner.class)
public class SecurityMarkersTest {

	private static final Logger LOGGER = (Logger) LoggerFactory
			.getLogger(SecurityMarkersTest.class);

	@Mock
	private Appender<ILoggingEvent> mockAppender;

	// Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

	@Before
	public void setup() {
		LOGGER.addAppender(mockAppender);
	}

	@After
	public void teardown() {
		LOGGER.detachAppender(mockAppender);
	}

	@Test
	public void getMarkersTest() {
		Marker test1 = SecurityMarkers.SECURITY_AUDIT;
		System.out.println("getMarkersTest(): test1: " + test1);
		assertTrue(test1.contains(SecurityMarkers.SECURITY_AUDIT));
		assertFalse(test1.contains(SecurityMarkers.CONFIDENTIAL));

		Marker test2 = SecurityMarkers.getMarker(
				SecurityMarkers.SECURITY_AUDIT,
				SecurityMarkers.SECURITY_FAILURE);
		System.out.println("getMarkersTest(): test2: " + test2);
		assertTrue(test2.contains(SecurityMarkers.SECURITY_AUDIT));
		assertTrue(test2.contains(SecurityMarkers.SECURITY_FAILURE));

		Marker test3 = SecurityMarkers.getMarker(
				SecurityMarkers.SECURITY_AUDIT, SecurityMarkers.CONFIDENTIAL);
		System.out.println("getMarkersTest(): test3: " + test3);
		assertTrue(test3.contains(SecurityMarkers.SECURITY_AUDIT));
		assertTrue(test3.contains(SecurityMarkers.CONFIDENTIAL));
		assertFalse(test3.contains(SecurityMarkers.SECURITY_FAILURE));
	}

	@Test
	public void confidentialTest() {
		Marker confidential = SecurityMarkers.CONFIDENTIAL;
		confidential.add(SecurityMarkers.SECURITY_AUDIT);
		String userid = "myId";
		String password = "password";
		LOGGER.info(confidential, "userid={}, password='{}'", userid, password);

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(Level.INFO));

		// check that markers are proper
		Marker test = loggingEvent.getMarker();
		assertTrue(test.contains(SecurityMarkers.SECURITY_AUDIT));
		assertTrue(test.contains(SecurityMarkers.CONFIDENTIAL));

		// cleanup for following tests
		confidential.remove(SecurityMarkers.SECURITY_AUDIT);
	}

	@Test
	public void multiMarkerTest() {
		Marker marker = SecurityMarkers.getMarker(
				SecurityMarkers.SECURITY_SUCCESS, SecurityMarkers.CONFIDENTIAL);
		LOGGER.info(marker, "Multi-marker test");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(Level.INFO));

		// Check the message being logged is correct
		assertThat(loggingEvent.getFormattedMessage(), is("Multi-marker test"));

		// check that markers are proper
		Marker test = loggingEvent.getMarker();
		assertTrue(test.contains(SecurityMarkers.SECURITY_SUCCESS));
		assertTrue(test.contains(SecurityMarkers.CONFIDENTIAL));
		assertFalse(test.contains(SecurityMarkers.EVENT_FAILURE));
	}
}
