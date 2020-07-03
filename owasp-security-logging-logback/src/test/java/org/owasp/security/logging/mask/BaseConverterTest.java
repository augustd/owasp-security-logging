/*
 * Copyright 2019 adetlefsen.
 *
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
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.Spy;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adetlefsen
 */
public abstract class BaseConverterTest {
    
    LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
    
    Logger LOGGER = (Logger)LoggerFactory.getLogger("CONSOLE");
    
    PatternLayoutEncoder encoder;
    PatternLayout layout;
    
    abstract String getConverterClass();
    
    @Spy
    protected RollingFileAppender<ILoggingEvent> mockAppender = new RollingFileAppender<ILoggingEvent>();
    
    // Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
    @Captor
    protected ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Before
    public void setUp() {
        PatternLayout.defaultConverterMap.put("testmask", getConverterClass());
        encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("[%thread] %-5level -masked- %testmask%n");
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

    protected String log(String message) {
        LOGGER.info(message);
        return getLayoutMessage();
    }

    protected String log(String pattern, String ... parameter) {
        LOGGER.info(pattern, parameter);
        return getLayoutMessage();
    }

    protected String getLayoutMessage() {
        // Now verify our logging interactions
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        // Get the logging event from the captor
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        // Check log level is correct
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        // Check the message being logged is correct
        String layoutMessage = encoder.getLayout().doLayout(loggingEvent);
        System.out.println("layoutMessage: " + layoutMessage);
        return layoutMessage;
    }
    
}
