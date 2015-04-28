package org.owasp.security.logging.layout.cef;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;

public class CEFLoggingLayoutTest {

	private Logger logger = ((LoggerContext) LoggerFactory.getILoggerFactory())
			.getLogger("test");

	@Test
	public void test() {
		CEFLoggingLayout layout = new CEFLoggingLayout();
		ILoggingEvent event = new LoggingEvent("Wifi connection tampered with",
				logger, Level.DEBUG, "Someother", (Throwable) null,
				(Object[]) null);
		System.out.println(layout.doLayout(event));
	}

}
