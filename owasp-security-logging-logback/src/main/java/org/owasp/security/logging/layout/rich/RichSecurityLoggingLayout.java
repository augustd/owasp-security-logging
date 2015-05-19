package org.owasp.security.logging.layout.rich;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.LayoutBase;

public class RichSecurityLoggingLayout extends LayoutBase<LoggingEvent> {

	public String doLayout(LoggingEvent event) {
		RichContext rctx = new RichContext(event);
		return rctx.toString();
	}
}
