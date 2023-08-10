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
package org.owasp.security.logging.log4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.test.appender.ListAppender;
import org.apache.logging.log4j.core.test.junit.LoggerContextRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.owasp.security.logging.SecurityMarkers;
import org.owasp.security.logging.log4j.filter.SecurityMarkerFilter;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 *
 * @author adetlefsen
 */
public class SecurityMarkerFilterTest {

	private static final String CONFIG = "log4j2.xml";

	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(SecurityMarkerFilterTest.class);

	@ClassRule
	public static LoggerContextRule context = new LoggerContextRule(CONFIG);

	ListAppender appender;

	@Before
	public void setUp() {
		System.out.println("CONTEXT: " + context);
		appender = context.getListAppender("List");
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test() {
		LOGGER.trace("This is a log statement");
		LOGGER.debug("There is a monster at the end of this block");
		LOGGER.info("Monster activity detected");
		LOGGER.warn("This is your last warning");
		LOGGER.error("Monster!");
	}

	@Test
	public void getMarkersTest() {
		Marker test1 = SecurityMarkers
				.getMarker(SecurityMarkers.SECURITY_AUDIT);
		System.out.println("getMarkers(): test1: " + test1);
		assertTrue(test1.contains(SecurityMarkers.SECURITY_AUDIT));
		assertFalse(test1.contains(SecurityMarkers.CONFIDENTIAL));

		Marker test2 = SecurityMarkers.getMarker(
				SecurityMarkers.SECURITY_AUDIT,
				SecurityMarkers.SECURITY_FAILURE);
		System.out.println("getMarkers(): test2: " + test2);
		assertTrue(test2.contains(SecurityMarkers.SECURITY_AUDIT));
		assertTrue(test2.contains(SecurityMarkers.SECURITY_FAILURE));

		Marker test3 = SecurityMarkers.getMarker(
				SecurityMarkers.SECURITY_AUDIT, SecurityMarkers.CONFIDENTIAL);
		System.out.println("getMarkers(): test3: " + test3);

		assertTrue(test3.contains(SecurityMarkers.SECURITY_AUDIT));
		assertTrue(test3.contains(SecurityMarkers.CONFIDENTIAL));
		assertFalse(test3.contains(SecurityMarkers.SECURITY_FAILURE));
	}

	@Test
	public void testRaw() {
		// create a new marker filter
		SecurityMarkerFilter mkt = SecurityMarkerFilter.createFilter(false);
		mkt.start();

		assertTrue(mkt.isStarted());

		// test a logging event with no markers
		LOGGER.info("This statement has no markers");
		System.out.println("appender: " + appender);
		System.out.println("    size: " + appender.getEvents().size());
		LogEvent nulEvent = appender.getEvents().get(0);
		assertEquals(Filter.Result.DENY, mkt.filter(nulEvent));

		// test a logging event with the SECURITY_SUCCESS marker
		LOGGER.info(SecurityMarkers.SECURITY_SUCCESS,
				"This statement is a security success");
		LogEvent successEvent = appender.getEvents().get(1);
		assertEquals(Filter.Result.ACCEPT, mkt.filter(successEvent));

		// test a logging event with the SECURITY_FAILURE marker
		LOGGER.info(SecurityMarkers.SECURITY_FAILURE,
				"This statement is a security failure");
		LogEvent failureEvent = appender.getEvents().get(2);
		assertEquals(Filter.Result.ACCEPT, mkt.filter(failureEvent));

		// test a logging event with the SECURITY_SUCCESS marker
		LOGGER.info(SecurityMarkers.SECURITY_AUDIT,
				"This statement is a security audit");
		LogEvent auditEvent = appender.getEvents().get(3);
		assertEquals(Filter.Result.ACCEPT, mkt.filter(auditEvent));

		// test a logging event with the CONFIDENTIAL marker
		LOGGER.info(SecurityMarkers.CONFIDENTIAL,
				"This statement is confidential");
		LogEvent confidentialEvent = appender.getEvents().get(4);
		assertEquals(Filter.Result.DENY, mkt.filter(confidentialEvent));

		// test a logging event with multiple non-security markers
		LOGGER.info(SecurityMarkers.getMarker(SecurityMarkers.CONFIDENTIAL,
				SecurityMarkers.RESTRICTED),
				"This statement is confidential and restricted");
		LogEvent multiEvent = appender.getEvents().get(5);
		assertEquals(Filter.Result.DENY, mkt.filter(multiEvent));

		// test a logging event with multiple markers, including security
		LOGGER.info(SecurityMarkers.getMarker(SecurityMarkers.SECURITY_FAILURE,
				SecurityMarkers.RESTRICTED),
				"This statement is a security failure and restricted");
		LogEvent multiSecurityEvent = appender.getEvents().get(6);
		assertEquals(Filter.Result.ACCEPT, mkt.filter(multiSecurityEvent));
	}

	/*
	 * @Test public void testRawAcceptAll() { //create a new marker filter
	 * SecurityMarkerFilter mkt = new SecurityMarkerFilter();
	 * mkt.setContext(loggerContext); mkt.setAcceptAll("true"); mkt.start();
	 * 
	 * assertTrue(mkt.isStarted());
	 * 
	 * //test a logging event with no markers ILoggingEvent nulEvent = new
	 * LoggingEvent(); assertEquals(FilterReply.DENY, mkt.decide(nulEvent));
	 * 
	 * //test a logging event with the CONFIDENTIAL marker LoggingEvent
	 * confidentialEvent = new LoggingEvent();
	 * confidentialEvent.setMarker(SecurityMarkers.SECURITY_SUCCESS);
	 * assertEquals(FilterReply.ACCEPT, mkt.decide(confidentialEvent));
	 * 
	 * //test a logging event with the RESTRICTED marker LoggingEvent
	 * restrictedEvent = new LoggingEvent();
	 * restrictedEvent.setMarker(SecurityMarkers.SECURITY_FAILURE);
	 * assertEquals(FilterReply.ACCEPT, mkt.decide(restrictedEvent));
	 * 
	 * //test a logging event with the SECRET marker LoggingEvent secretEvent =
	 * new LoggingEvent();
	 * secretEvent.setMarker(SecurityMarkers.SECURITY_AUDIT);
	 * assertEquals(FilterReply.ACCEPT, mkt.decide(secretEvent)); }
	 */

}
