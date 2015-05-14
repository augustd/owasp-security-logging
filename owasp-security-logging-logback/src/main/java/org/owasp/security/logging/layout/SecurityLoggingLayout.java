package org.owasp.security.logging.layout;

import org.owasp.security.logging.mdc.MDCFilter;
import org.slf4j.MDC;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

/**
 * 
 * Layout for security related logging
 *
 */
public class SecurityLoggingLayout extends LayoutBase<ILoggingEvent> {

	private static final String LINE_SEP = System.getProperty("line.separator");
	private String prefix = "Security";

	public String doLayout(ILoggingEvent event) {
		StringBuilder sbuf = new StringBuilder(128);
		if (prefix != null) {
			sbuf.append(prefix).append(": ");
		}
		sbuf.append(event.getTimeStamp()
				- event.getLoggerContextVO().getBirthTime());
		sbuf.append(' ');
		sbuf.append(event.getLevel());
		sbuf.append(' ');
		sbuf.append(event.getMarker());
		sbuf.append(' ');
		sbuf.append(event.getLoggerName());
		sbuf.append(" - ");
		sbuf.append(MDC.get(MDCFilter.LOGIN_ID));
		sbuf.append('@');
		sbuf.append(MDC.get(MDCFilter.IPADDRESS));
		sbuf.append(' ');
		sbuf.append(event.getFormattedMessage());
		sbuf.append(LINE_SEP);
		return sbuf.toString();
	}
}
