package org.owasp.security.logging.mask;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This converter is used to encode any carriage returns and line feeds to prevent log
 * injection attacks
 *
 * It is not possible to replace the actual formatted message, instead this converter
 * returns a masked version of the message that can be accessed using the conversionWord
 * specified in the conversionRule definition in logback.xml. 
 * 
 * @author August Detlefsen [augustd@codemagi.com]
 */
public class CRLFConverter extends CompositeConverter<ILoggingEvent> {

    @Override
    protected String transform(ILoggingEvent event, String in) {
        String clean = in.replace('\n', '_').replace('\r', '_');

        return clean;
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
