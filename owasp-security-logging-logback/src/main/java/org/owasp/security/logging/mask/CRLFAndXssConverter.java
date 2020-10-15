package org.owasp.security.logging.mask;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import org.owasp.security.logging.Utils;

/**
 * This converter encodes carriage returns, line feeds and angle brackets to
 * prevent log forging attacks using new line characters or script execution.
 *
 * It is not possible to replace the actual formatted message, instead this
 * converter returns a masked version of the message that can be accessed using
 * the conversionWord specified in the conversionRule definition in logback.xml.
 *
 * Based on the CRLFConverter by August Detlefsen
 *
 * @author Stijn Slaets [stijn.slaets@gmail.com]
 */
public class CRLFAndXssConverter extends CompositeConverter<ILoggingEvent> {

    @Override
    protected String transform(ILoggingEvent event, String in) {
        String withoutCrlf =  Utils.replaceCRLFWithUnderscore(in);
        return Utils.escapeAngleBrackets(withoutCrlf);
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
