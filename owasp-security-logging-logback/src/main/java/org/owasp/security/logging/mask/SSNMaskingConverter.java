package org.owasp.security.logging.mask;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Masks social security numbers in log messages. SSNs consist of digits separated 
 * by dashes in the form <b>###-##-####</b>. 
 * 
 * The following famous/test SSNs are not masked: 
 * <li>219-09-9999 (Social Security Board pamphlet)
 * <li>078-05-1120 (Woolworth wallets: Mrs. Hilda Schrader Whitcher)
 * <li>matches starting with 000 or 666
 * <li>matches containing 00 as the middle two digits
 * <li>matches containing 0000 as the final four digits
 * 
 * @author augustd
 */
public class SSNMaskingConverter extends ClassicConverter {

    private static final Pattern SSN_PATTERN = Pattern.compile("((?!219-09-9999|078-05-1120)(?!666|000|9\\d{2})\\d{3}-(?!00)\\d{2}-(?!0{4})\\d{4})");

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return mask(iLoggingEvent.getFormattedMessage());
    }

    private String mask(String formattedMessage) {
        Matcher matcher = SSN_PATTERN.matcher(formattedMessage);
        while (matcher.find()) {
            String found = matcher.group();
            StringBuilder builder = new StringBuilder();
            builder.append("***-**-");
            builder.append(found.substring(7));
            formattedMessage = formattedMessage.replaceAll(found, builder.toString());
        }
        return formattedMessage;
    }
}