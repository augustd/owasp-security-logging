package org.owasp.security.logging.layout.cef;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

public class CEFLoggingLayout extends LayoutBase<ILoggingEvent> {

	public String doLayout(ILoggingEvent event) {
		Prefix prefix = new Prefix();
		prefix.name = event.getMessage();
		return prefix.toString();
	}

}
