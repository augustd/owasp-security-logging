package org.owasp.security.logging.layout.rich;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.qos.logback.classic.spi.LoggingEvent;

public class RichContextTest {

	private LoggingEvent event = new LoggingEvent();
	{
		event.setTimeStamp(999);
	}
	private RichContext ctx = new RichContext(event);

	@Test
	public void testGetPID() {
		assertTrue(RichContext.getPID() > 0);
	}

	// @Test
	public void testGetApplicationName() {
		assertEquals("JUnit", ctx.getApplicationName());
	}

	@Test
	public void testToString() {
		System.out.println(ctx.toString());
	}

}
