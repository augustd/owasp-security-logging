package org.owasp.security.logging.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jens Piegsa
 * @author August Detlefsen
 */
@RunWith(MockitoJUnitRunner.class)
public class StreamRedirectionWithCustomLoggersTest {

    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

    ch.qos.logback.classic.Logger LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("BLANK");
    ch.qos.logback.classic.Logger ROOT_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    private ListAppender<ILoggingEvent> mockAppender;

    PatternLayoutEncoder encoder;

    @Before
    public void setUp() {
        encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%message");
        encoder.start();

        mockAppender = new ListAppender();
        mockAppender.start();
        mockAppender.list.clear();

        LOGGER.addAppender(mockAppender);
    }

    @After
    public void teardown() {
        mockAppender.list.clear();
        LOGGER.detachAppender(mockAppender);
    }

    @Test
    public void doSysOutTest() {
        System.out.println("***** doSysOutTest *****");
        System.out.println("message 1");
        System.err.println("message 2");

        SecurityUtil.bindSystemStreamsToSLF4J(LOGGER, ROOT_LOGGER);

        System.out.println("message 3");
        System.err.println("message 4");

        SecurityUtil.unbindSystemStreams();

        System.out.println("message 5");
        System.err.println("message 6");

        ILoggingEvent loggingEvent = mockAppender.list.get(mockAppender.list.size() - 1);

        // Check log level is correct
        assertThat(loggingEvent.getLevel(), is(Level.INFO));

        // check that message 3 was the last to be logged
        String layoutMessage = encoder.getLayout().doLayout(loggingEvent);
        assertTrue(layoutMessage.contains("message 3"));
        System.out.println("layoutMessage: " + layoutMessage);
    }

    @Test
    public void doSysErrTest() {
        System.out.println("***** doSysErrTest *****");
        System.out.println("message 1");
        System.err.println("message 2");

        SecurityUtil.bindSystemStreamsToSLF4J(ROOT_LOGGER, LOGGER);

        System.err.println("message 3");
        System.out.println("message 4");

        SecurityUtil.unbindSystemStreams();

        System.out.println("message 5");
        System.err.println("message 6");

        ILoggingEvent loggingEvent = mockAppender.list.get(mockAppender.list.size() - 1);

        // Check log level is correct
        assertThat(loggingEvent.getLevel(), is(Level.ERROR));

        // check that message 3 was the last to be logged
        String layoutMessage = encoder.getLayout().doLayout(loggingEvent);
        assertTrue(layoutMessage.contains("message 3"));
        System.out.println("layoutMessage: " + layoutMessage);
    }

    @Test
    public void doTest() {
        System.out.println("***** doTest *****");
        System.out.println("message 1");
        System.err.println("message 2");

        SecurityUtil.bindSystemStreamsToSLF4J(LOGGER, LOGGER);

        System.out.println("message 3");
        System.err.println("message 4");

        SecurityUtil.unbindSystemStreams();

        System.out.println("message 5");
        System.err.println("message 6");

        assertThat(mockAppender.list.size(), is(2));

        ILoggingEvent systemEvent = mockAppender.list.get(0);

        // Check log level is correct
        assertThat(systemEvent.getLevel(), is(Level.INFO));

        // check that message 3 was the last to be logged
        String layoutMessage = encoder.getLayout().doLayout(systemEvent);
        assertTrue(layoutMessage.contains("message 3"));
        System.out.println("layoutMessage: " + layoutMessage);

        ILoggingEvent errorEvent = mockAppender.list.get(1);

        // Check log level is correct
        assertThat(errorEvent.getLevel(), is(Level.ERROR));

        // check that message 3 was the last to be logged
        layoutMessage = encoder.getLayout().doLayout(errorEvent);
        assertTrue(layoutMessage.contains("message 4"));
        System.out.println("layoutMessage: " + layoutMessage);

    }

}
