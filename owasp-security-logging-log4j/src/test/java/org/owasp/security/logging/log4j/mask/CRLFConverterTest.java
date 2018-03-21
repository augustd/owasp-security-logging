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
package org.owasp.security.logging.log4j.mask;

import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.pattern.EncodingPatternConverter;
import org.apache.logging.log4j.junit.LoggerContextRule;
import org.apache.logging.log4j.test.appender.ListAppender;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

/**
 * Log4j already includes a converter to escape carriage returns and line feeds.
 * This test just verifies that it works as expected.
 *
 * @author August Detlefsen [augustd@codemagi.com]
 */
@RunWith(MockitoJUnitRunner.class)
public class CRLFConverterTest {

	private static final String CONFIG = "log4j2.xml";

	@ClassRule
	public static final LoggerContextRule context = new LoggerContextRule(CONFIG);

	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(CRLFConverterTest.class);

	ListAppender appender;

	@Before
	public void setUp() {
		System.out.println("CONTEXT: " + context);
		appender = context.getListAppender("List");
	}

	@After
	public void teardown() {
	}

	@Test
	public void test() {
		LOGGER.info("This message contains \r\n line feeds");

		// Check the message being logged is correct
		LogEvent nulEvent = appender.getEvents().get(0);

		final String[] options = new String[] { "%msg" };
		final EncodingPatternConverter converter = EncodingPatternConverter
				.newInstance(context.getConfiguration(), options);
		final StringBuilder sb = new StringBuilder();
		converter.format(nulEvent, sb);

		assertTrue(sb.toString().contains(
				"This message contains \\r\\n line feeds"));
	}

}
