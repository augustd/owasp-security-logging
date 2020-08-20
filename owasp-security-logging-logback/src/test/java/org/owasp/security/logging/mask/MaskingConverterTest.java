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
import static org.hamcrest.CoreMatchers.is;

import org.junit.After;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 *
 * @author August Detlefsen [augustd@codemagi.com]
 */
@RunWith(MockitoJUnitRunner.class)
public class MaskingConverterTest {

	LoggerContext loggerContext = (LoggerContext) LoggerFactory
			.getILoggerFactory();

	Logger LOGGER = (Logger) LoggerFactory.getLogger("CONSOLE");

	PatternLayoutEncoder encoder;

	@Mock
	private RollingFileAppender<ILoggingEvent> mockAppender = new RollingFileAppender<ILoggingEvent>();

	// Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

	@Before
	public void setUp() {
		PatternLayout.defaultConverterMap.put("mask",
				MaskingConverter.class.getName());

		encoder = new PatternLayoutEncoder();
		encoder.setContext(loggerContext);
		encoder.setPattern("%-4relative [%thread] %-5level %logger{35} - %mask%n");
		encoder.start();

		mockAppender.setContext(loggerContext);
		mockAppender.setEncoder(encoder);
		mockAppender.start();

		LOGGER.addAppender(mockAppender);
	}

	@After
	public void teardown() {
		LOGGER.detachAppender(mockAppender);
	}

	@Test
	public void test() {
		String userid = "myId";
		String password = "secret";
		LOGGER.info(SecurityMarkers.CONFIDENTIAL, "userid={}, password='{}'",
				userid, password);

		// Now verify our logging interactions
		verify(mockAppender).doAppend(captorLoggingEvent.capture());

		// Get the logging event from the captor
		final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

		// Check log level is correct
		assertThat(loggingEvent.getLevel(), is(Level.INFO));

		// Check the message being logged is correct
		String layoutMessage = encoder.getLayout().doLayout(loggingEvent);
		assertTrue(layoutMessage.contains("userid="
				+ MaskingConverter.MASKED_PASSWORD + ", password='"
				+ MaskingConverter.MASKED_PASSWORD + "'"));
		assertFalse(layoutMessage.contains("secret"));
	}

    /**
     * Test that masking works for combinations of markers and not just
     * SecurityMarkers.CONFIDENTIAL
     *
     * @see https://github.com/javabeanz/owasp-security-logging/issues/19
     */
    @Test
    public void markerTest() {
        Marker multiMarker = SecurityMarkers.getMarker(SecurityMarkers.CONFIDENTIAL, SecurityMarkers.SECURITY_FAILURE);

        String ssn = "123-45-6789";
        LOGGER.info(multiMarker, "ssn={}", ssn);

        // Now verify our logging interactions
        verify(mockAppender).doAppend(captorLoggingEvent.capture());

        // Get the logging event from the captor
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

        // Check the message being logged is correct
        String layoutMessage = encoder.getLayout().doLayout(loggingEvent);
        assertTrue(layoutMessage.contains("ssn=" + MaskingConverter.MASKED_PASSWORD));
    }
        
}
