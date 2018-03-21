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
import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.junit.LoggerContextRule;
import org.apache.logging.log4j.test.appender.ListAppender;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.owasp.security.logging.SecurityMarkers;
import org.owasp.security.logging.log4j.filter.ExcludeClassifiedMarkerFilter;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adetlefsen
 */
public class ExcludeClassifiedMarkerFilterTest {

	private static final String CONFIG = "log4j2.xml";

	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(ExcludeClassifiedMarkerFilterTest.class);

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
	public void testRaw() {
		// create a new marker filter
		ExcludeClassifiedMarkerFilter mkt = ExcludeClassifiedMarkerFilter
				.createFilter();
		mkt.start();

		assertTrue(mkt.isStarted());

		// test a logging event with no markers
		LOGGER.info("This statement has no markers");
		LogEvent nulEvent = appender.getEvents().get(0);
		assertEquals(Filter.Result.NEUTRAL, mkt.filter(nulEvent));

		// test a logging event with the SECURITY_SUCCESS marker
		LOGGER.info(SecurityMarkers.SECURITY_SUCCESS,
				"This statement is a security success");
		LogEvent successEvent = appender.getEvents().get(1);
		assertEquals(Filter.Result.NEUTRAL, mkt.filter(successEvent));

		// test a logging event with the SECURITY_FAILURE marker
		LOGGER.info(SecurityMarkers.SECURITY_FAILURE,
				"This statement is a security failure");
		LogEvent failureEvent = appender.getEvents().get(2);
		assertEquals(Filter.Result.NEUTRAL, mkt.filter(failureEvent));

		// test a logging event with the SECURITY_SUCCESS marker
		LOGGER.info(SecurityMarkers.SECURITY_AUDIT,
				"This statement is a security audit");
		LogEvent auditEvent = appender.getEvents().get(3);
		assertEquals(Filter.Result.NEUTRAL, mkt.filter(auditEvent));

		// test a logging event with the CONFIDENTIAL marker
		LOGGER.info(SecurityMarkers.CONFIDENTIAL,
				"This statement is confidential");
		LogEvent confidentialEvent = appender.getEvents().get(4);
		assertEquals(Filter.Result.DENY, mkt.filter(confidentialEvent));

		// test a logging event with the CONFIDENTIAL marker
		LOGGER.info(SecurityMarkers.RESTRICTED,
				"This statement is confidential");
		LogEvent restrictedEvent = appender.getEvents().get(5);
		assertEquals(Filter.Result.DENY, mkt.filter(restrictedEvent));

		// test a logging event with the CONFIDENTIAL marker
		LOGGER.info(SecurityMarkers.SECRET, "This statement is confidential");
		LogEvent secretEvent = appender.getEvents().get(6);
		assertEquals(Filter.Result.DENY, mkt.filter(secretEvent));

		// test a logging event with the CONFIDENTIAL marker
		LOGGER.info(SecurityMarkers.TOP_SECRET,
				"This statement is confidential");
		LogEvent topSecretEvent = appender.getEvents().get(7);
		assertEquals(Filter.Result.DENY, mkt.filter(topSecretEvent));

		// test a logging event with multiple non-classified markers
		LOGGER.info(SecurityMarkers.getMarker(SecurityMarkers.SECURITY_SUCCESS,
				SecurityMarkers.EVENT_SUCCESS),
				"This statement is a security success and an event success");
		LogEvent multiEvent = appender.getEvents().get(8);
		assertEquals(Filter.Result.NEUTRAL, mkt.filter(multiEvent));

		// test a logging event with multiple markers, including data
		// classification
		LOGGER.info(SecurityMarkers.getMarker(SecurityMarkers.SECURITY_FAILURE,
				SecurityMarkers.RESTRICTED),
				"This statement is a security failure and restricted");
		LogEvent multiSecurityEvent = appender.getEvents().get(9);
		assertEquals(Filter.Result.DENY, mkt.filter(multiSecurityEvent));
	}

}
