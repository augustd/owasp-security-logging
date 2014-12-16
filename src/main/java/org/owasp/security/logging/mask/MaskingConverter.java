package org.owasp.security.logging.mask;

import org.owasp.security.logging.SecurityMarkers;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.ReplacingCompositeConverter;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;

/**
 *
 *
 */
public class MaskingConverter extends
        ReplacingCompositeConverter<ILoggingEvent> {

    @Override
    public String convert(ILoggingEvent event) {
        Marker eventMarker = event.getMarker();

        Object[] args = event.getArgumentArray();
        if (eventMarker != null && eventMarker.equals(SecurityMarkers.CONFIDENTIAL)) {            
            for (int i = 0; i < args.length; i++) {
                String arg = args[i].toString();
                arg = arg.replaceAll(".", "*");
                args[i] = arg;
            }
        }
        
        String maskedMessage = MessageFormatter.arrayFormat(event.getMessage(), args).getMessage();

        return maskedMessage;
    }

    /**
     * Override start method because the superclass ReplacingCompositeConverter
     * requires at least two options and this class has none.
     */
    @Override
    public void start() {
        started = true;
    }

}
