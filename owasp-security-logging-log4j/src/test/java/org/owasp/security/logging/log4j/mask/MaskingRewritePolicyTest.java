package org.owasp.security.logging.log4j.mask;

import junit.framework.TestCase;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent.Builder;
import org.apache.logging.log4j.junit.InitialLoggerContext;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.test.appender.ListAppender;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.LoggerFactory;

/**
 * The class <code>MaskingRewritePolicyTest</code> contains tests for the class
 * {@link <code>MaskingRewritePolicy</code>}
 *
 * @pattern JUnit Test Case
 *
 * @generatedBy CodePro at 20-2-16 22:58
 *
 * @author sytze
 *
 * @version $Revision$
 */
public class MaskingRewritePolicyTest { 

	private static final String CONFIG = "log4j2.xml";

	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(MaskingRewritePolicyTest.class);

        private static final String SSN = "123-45-6789";

	@ClassRule
	public static InitialLoggerContext context = new InitialLoggerContext(CONFIG);

	ListAppender appender;

	@Before
	public void setUp() {
            System.out.println("CONTEXT: " + context);
            appender = context.getListAppender("List");
	}

        @After
        public void tearDown() {
            appender.clear();
        }
        
        @Test
        public void testRewriteMultiMarker() {
            System.out.println("running testRewriteMultiMarker()");
            org.slf4j.Marker multiMarker = SecurityMarkers.getMarker(SecurityMarkers.CONFIDENTIAL, SecurityMarkers.SECURITY_FAILURE);
            
            // test a logging event with the multi-marker
            LOGGER.info(multiMarker, "ssn={}", SSN);
            LogEvent failEvent = appender.getEvents().get(0);
            Message message = failEvent.getMessage();
            
            System.out.println("Formatted message: " + message.getFormattedMessage());
            assertTrue(message.getFormattedMessage().contains("ssn=" + MaskingRewritePolicy.MASKED_PASSWORD));
        }
 
        /**
         * This test case has the CONFIDENTIAL marker so the results should be masked
         */
        @Test
        public void testRewriteConfidentialWithParams() {
            System.out.println("running testRewriteConfidentialWithParams()");

            // test a logging event with the CONFIDENTIAL marker
            LOGGER.info(SecurityMarkers.CONFIDENTIAL, "ssn={}", SSN);
            LogEvent failEvent = appender.getEvents().get(0);
            Message message = failEvent.getMessage();
            
            System.out.println("Formatted message: " + message.getFormattedMessage());
            assertTrue(message.getFormattedMessage().contains("ssn=" + MaskingRewritePolicy.MASKED_PASSWORD));
        }
        
        /**
         * This test case has the CONFIDENTIAL marker, but it is not parameterized 
         * so masking cannot take place.
         */
        @Test
        public void testRewriteConfidentialNoParams() {
            System.out.println("running testRewriteConfidentialNoParams()");

            // test a logging event with the CONFIDENTIAL marker
            LOGGER.info(SecurityMarkers.CONFIDENTIAL, "ssn=" + SSN);
            LogEvent failEvent = appender.getEvents().get(0);
            Message message = failEvent.getMessage();
            
            System.out.println("Formatted message: " + message.getFormattedMessage());
            assertTrue(message.getFormattedMessage().contains("ssn=" + SSN));
        }
 
        /**
         * This test case is parameterized, but does not have the CONFIDENTIAL 
         * marker, so it should not be masked
         */
        @Test
        public void testRewriteNotConfidential() {
            System.out.println("running testRewriteSingleMarker()");

            // test a logging event with the CONFIDENTIAL marker
            LOGGER.info(SecurityMarkers.SECURITY_SUCCESS, "ssn={}", SSN);
            LogEvent failEvent = appender.getEvents().get(0);
            Message message = failEvent.getMessage();
            
            System.out.println("Formatted message: " + message.getFormattedMessage());
            assertTrue(message.getFormattedMessage().contains("ssn=" + SSN));
        }
 
        @Test
        public void testRewriteNoMarker() {
            System.out.println("running testRewriteNoMarker()");

            // test a logging event with no marker
            LOGGER.info("ssn={}", SSN);
            LogEvent failEvent = appender.getEvents().get(0);
            Message message = failEvent.getMessage();
            
            System.out.println("Formatted message: " + message.getFormattedMessage());
            assertTrue(message.getFormattedMessage().contains("ssn=" + SSN));
        }
 
	@Test
        public void testRewriteConfidentialNoMessage() {
            System.out.println("running testRewriteConfidentialNoMessage()");

            // test a logging event with null marker
            LOGGER.info(null);
            LogEvent failEvent = appender.getEvents().get(0);
            Message message = failEvent.getMessage();
            
            System.out.println("Formatted message: " + message.getFormattedMessage());
            assertTrue(message.getFormattedMessage() == null);
	}
}
