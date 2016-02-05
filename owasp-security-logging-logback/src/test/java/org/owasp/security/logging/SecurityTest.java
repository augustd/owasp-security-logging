package org.owasp.security.logging;

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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;

@RunWith(MockitoJUnitRunner.class)
public class SecurityTest {

	LoggerContext loggerContext = (LoggerContext) LoggerFactory
			.getILoggerFactory();
	Logger LOGGER;

	@Mock
	private RollingFileAppender<ILoggingEvent> mockAppender = new RollingFileAppender<ILoggingEvent>();

	// Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

	@Before
	public void setup() {
		// mockAppender = new RollingFileAppender();
		mockAppender.setContext(loggerContext);
		mockAppender.setFile("testFile.log");

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(loggerContext);
		encoder.setPattern("%-4relative [%thread] %-5level %logger{35} - %msg%n");
		encoder.start();

		mockAppender.setEncoder(encoder);
		mockAppender.start();
		LOGGER = loggerContext.getLogger("Main");
		LOGGER.addAppender(mockAppender);

	}

	@After
	public void teardown() {
		LOGGER.detachAppender(mockAppender);
	}

	@Test
	public void injectionTest() {

		LOGGER.info("This message contains \r\n line feeds");

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

		System.out.println("MESSAGE: " + loggingEvent.getFormattedMessage());
		// assertThat(loggingEvent.getFormattedMessage(),
		// is("This message contains line feeds"));
	}
}
