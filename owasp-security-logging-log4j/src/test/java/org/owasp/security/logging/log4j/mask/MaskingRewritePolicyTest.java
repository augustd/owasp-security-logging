package org.owasp.security.logging.log4j.mask;

import junit.framework.TestCase;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent.Builder;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.owasp.security.logging.SecurityMarkers;

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
public class MaskingRewritePolicyTest extends TestCase {

	/**
	 * Construct new test instance
	 *
	 * @param name
	 *            the test name
	 */
	public MaskingRewritePolicyTest(String name) {
		super(name);
	}

	public void testRewriteConfidentialNoParams() {
		MaskingRewritePolicy fixture = new MaskingRewritePolicy();
		Marker marker = new MarkerManager.Log4jMarker(
				SecurityMarkers.CONFIDENTIAL.getName());
		Log4jLogEvent event = createEvent(marker, new SimpleMessage());
		LogEvent result = fixture.rewrite(event);
		assertEquals(event, result);
	}

	public void testRewriteConfidentialWithParams() {
		MaskingRewritePolicy fixture = new MaskingRewritePolicy();
		Marker marker = new MarkerManager.Log4jMarker(
				SecurityMarkers.CONFIDENTIAL.getName());
		Message message = new ParameterizedMessage("ddd", "gladiator");
		LogEvent event = createEvent(marker, message);
		LogEvent result = fixture.rewrite(event);
		assertNotSame(event, result);
	}

	public void testRewriteNotConfidential() {
		MaskingRewritePolicy fixture = new MaskingRewritePolicy();
		Marker marker = new MarkerManager.Log4jMarker(
				SecurityMarkers.EVENT_FAILURE_MARKER_NAME);
		Message message = new ParameterizedMessage("ddd", "gladiator");
		LogEvent event = createEvent(marker, message);
		LogEvent result = fixture.rewrite(event);
		assertEquals(event, result);
	}

	public void testRewriteConfidentialWithZeroParams() {
		MaskingRewritePolicy fixture = new MaskingRewritePolicy();
		Marker marker = new MarkerManager.Log4jMarker(
				SecurityMarkers.CONFIDENTIAL.getName());
		Message message = new ParameterizedMessage("ddd", null);
		LogEvent event = createEvent(marker, message);
		LogEvent result = fixture.rewrite(event);
		assertEquals(event, result);
	}

	public void testRewriteConfidentialNoMessage() {
		MaskingRewritePolicy fixture = new MaskingRewritePolicy();
		Marker marker = new MarkerManager.Log4jMarker(
				SecurityMarkers.CONFIDENTIAL.getName());
		Log4jLogEvent event = createEvent(marker, null);
		LogEvent result = fixture.rewrite(event);
		assertEquals(event, result);
	}

	public void testRewriteNoMarker() {
		MaskingRewritePolicy fixture = new MaskingRewritePolicy();
		Message message = new ParameterizedMessage("ddd", "gladiator");
		Log4jLogEvent event = createEvent(null, message);
		LogEvent result = fixture.rewrite(event);
		assertEquals(event, result);
	}

	private Log4jLogEvent createEvent(Marker marker, Message message) {
		Log4jLogEvent.Builder builder = new Builder();
		builder.setMarker(marker).setLevel(Level.DEBUG).setLoggerName("jjj")
				.setLoggerFqcn("ggg").setMessage(message);
		Log4jLogEvent event = builder.build();
		return event;
	}
}
