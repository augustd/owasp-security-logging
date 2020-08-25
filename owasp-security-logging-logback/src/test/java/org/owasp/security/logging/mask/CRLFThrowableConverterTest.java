/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.owasp.security.logging.mask;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CRLFThrowableConverterTest {

	private LoggerContext loggerContext = (LoggerContext) LoggerFactory
			.getILoggerFactory();

	private Logger LOGGER;

	private PatternLayoutEncoder encoder;

	private PatternLayout layout;

	@Mock
	private RollingFileAppender<ILoggingEvent> mockAppender = new RollingFileAppender<>();

	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

	@Before
	public void setUp() {
		PatternLayout.defaultConverterMap.put("ex",
				CRLFThrowableConverter.class.getName());

		encoder = new PatternLayoutEncoder();
		encoder.setContext(loggerContext);
		encoder.setPattern("%-4relative [%thread] %-5level %logger{35} - %msg %ex%n");
		encoder.start();

		mockAppender.setContext(loggerContext);
		mockAppender.setEncoder(encoder);
		mockAppender.start();

		LOGGER = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);
		LOGGER.addAppender(mockAppender);
	}

	@After
	public void tearDown() {
		LOGGER.detachAppender(mockAppender);
	}

	@Test
	public void test_with_1_exception() {
		try {
			throw new IllegalArgumentException("This message contains \r\n line feeds");
		} catch (IllegalArgumentException ex) {
			LOGGER.error("", ex);
		}

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(Level.ERROR));

		// Check the message being logged is correct
		String layoutMessage = encoder.getLayout().doLayout(loggingEvent);
		assertTrue(layoutMessage
				.contains("This message contains __ line feeds"));
	}

	@Test
	public void test_with_nested_exception() {
		try {
			try {
				throw new IllegalArgumentException("This message contains \r\n line feeds");
			} catch (IllegalArgumentException ex) {
				// replace message with line feeds to ensure the message is correctly replaced in first exception
				throw new RuntimeException("Invalid argument", ex);
			}
		} catch (RuntimeException ex) {
			LOGGER.error("", ex);
		}

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(Level.ERROR));

		// Check the message being logged is correct
		String layoutMessage = encoder.getLayout().doLayout(loggingEvent);
		assertTrue(layoutMessage
				.contains("This message contains __ line feeds"));
	}

}
