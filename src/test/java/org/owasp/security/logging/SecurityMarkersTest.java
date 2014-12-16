package org.owasp.security.logging;

import ch.qos.logback.access.PatternLayoutEncoder;
import ch.qos.logback.core.Appender;
import ch.qos.logback.classic.Level;
import org.junit.Test;
import org.mockito.Mock;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@RunWith(MockitoJUnitRunner.class)
public class SecurityMarkersTest {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(SecurityMarkersTest.class);

    @Mock
    private Appender mockAppender;

    //Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
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
    public void test() {
        LOGGER.info(SecurityMarkers.SECURITY_SUCCESS, "some security event");
        LOGGER.info("some other event");
    }
    
    @Test
    public void confidentialTest() {
        Marker confidential = MarkerFactory.getMarker("CONFIDENTIAL");
        confidential.add(SecurityMarkers.SECURITY_AUDIT);
        String userid = "myId";
        String password = "password";
        LOGGER.info(confidential, "userid={}, password='{}'", userid, password);
        
        //Now verify our logging interactions
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        
        //Get the logging event from the captor
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        
        //Check log level is correct
        assertThat(loggingEvent.getLevel(), is(Level.INFO));

        //check that markers are proper
        Marker test = loggingEvent.getMarker();
        assertTrue(test.contains(SecurityMarkers.SECURITY_AUDIT));
        assertTrue(test.contains(SecurityMarkers.CONFIDENTIAL));
    }

    @Test
    public void multiMarkerTest() {
        Marker marker = SecurityMarkers.getMarker(SecurityMarkers.SECURITY_SUCCESS, SecurityMarkers.CONFIDENTIAL);
        LOGGER.info(marker, "Multi-marker test");

        //Now verify our logging interactions
        verify(mockAppender).doAppend(captorLoggingEvent.capture());

        //Get the logging event from the captor
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

        //Check log level is correct
        assertThat(loggingEvent.getLevel(), is(Level.INFO));

        //Check the message being logged is correct
        assertThat(loggingEvent.getFormattedMessage(), is("Multi-marker test"));

        //check that markers are proper
        Marker test = loggingEvent.getMarker();
        assertTrue(test.contains(SecurityMarkers.SECURITY_SUCCESS));
        assertTrue(test.contains(SecurityMarkers.CONFIDENTIAL));
    }
}
