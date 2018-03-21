package org.owasp.security.logging.log4j.mask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.junit.LoggerContextRule;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.apache.logging.log4j.test.appender.ListAppender;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.owasp.security.logging.SecurityMarkers;
import org.owasp.security.logging.log4j.Log4JMarkerConverter;
import org.slf4j.Marker;

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

	private static final String SSN = "123-45-6789";

	@ClassRule
	public static LoggerContextRule context = new LoggerContextRule(CONFIG);

	Logger LOGGER;
	ListAppender appender;

	@Before
	public void setUp() {
		LOGGER = LogManager.getLogger(MaskingRewritePolicyTest.class, new ParameterizedMessageFactory());
		appender = context.getListAppender("List");
	}

	@After
	public void tearDown() {
		appender.clear();
	}

	@Test
	public void testRewriteMultiMarker() {
		System.out.println("running testRewriteMultiMarker()");

		//get multi marker
		Marker multiMarker = SecurityMarkers.getMarker(SecurityMarkers.CONFIDENTIAL, SecurityMarkers.SECURITY_FAILURE);

		// test a logging event with the multi-marker
		LOGGER.info(Log4JMarkerConverter.convertMarker(multiMarker), "ssn={}", SSN);
		LogEvent failEvent = appender.getEvents().get(0);
		Message message = failEvent.getMessage();

		System.out.println("Formatted message: " + message.getFormattedMessage());
		assertTrue(message.getFormattedMessage().contains("ssn=" + MaskingRewritePolicy.MASKED_PASSWORD));
	}

	/**
	 * This test case has the CONFIDENTIAL marker so the results should be
	 * masked
	 */
	@Test
	public void testRewriteConfidentialWithParams() {
		System.out.println("running testRewriteConfidentialWithParams()");

		// test a logging event with the CONFIDENTIAL marker
		LOGGER.info(Log4JMarkerConverter.convertMarker(SecurityMarkers.CONFIDENTIAL), "ssn={}", SSN);
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
		LOGGER.info(Log4JMarkerConverter.convertMarker(SecurityMarkers.CONFIDENTIAL), "ssn=" + SSN);
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
		LOGGER.info(Log4JMarkerConverter.convertMarker(SecurityMarkers.SECURITY_SUCCESS), "ssn={}", SSN);
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
		String nullString = null;
		LOGGER.info(nullString);
		LogEvent failEvent = appender.getEvents().get(0);
		Message message = failEvent.getMessage();

		System.out.println("Formatted message: " + message.getFormattedMessage());
		assertEquals(message.getFormattedMessage(), "null");
	}
}
