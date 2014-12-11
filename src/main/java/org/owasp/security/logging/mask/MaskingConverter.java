package org.owasp.security.logging.mask;

import org.owasp.security.logging.SecurityMarkers;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.ReplacingCompositeConverter;

/**
 * 
 *
 */
public class MaskingConverter extends
		ReplacingCompositeConverter<ILoggingEvent> {

	@Override
	protected String transform(ILoggingEvent event, String in) {
		System.out.println("converting ..." + in);
		if (SecurityMarkers.CONFIDENTIAL.equals(event.getMarker())) {
			return event.getArgumentArray()[0].toString().replaceAll(".", "*");
		}
		return super.transform(event, in);
	}

}
