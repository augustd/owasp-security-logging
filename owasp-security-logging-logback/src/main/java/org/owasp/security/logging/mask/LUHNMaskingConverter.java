package org.owasp.security.logging.mask;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LUHNMaskingConverter extends ClassicConverter {

    private static final String CREDIT_CARD_PATTERN = "s*\\d{1,5}\\s*(?:-*\\s*,*\\d{1,5}\\s*){1,4}";
    private static final Pattern pattern = Pattern.compile(CREDIT_CARD_PATTERN);

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return mask(iLoggingEvent.getFormattedMessage());
    }

    private String mask(String formattedMessage) {
        Matcher matcher = pattern.matcher(formattedMessage);
        while (matcher.find()) {
            String found = matcher.group();
            String cardNumber = removeSpecials(found);
            if (luhnCheck(cardNumber)) {
                int length = cardNumber.length();
                StringBuilder builder = new StringBuilder();
                builder.append(cardNumber.substring(0, 4));
                builder.append("**MASKED**");
                builder.append(cardNumber.substring(length-4,length));
                formattedMessage = formattedMessage.replaceAll(found, builder.toString());
            }
        }
        return formattedMessage;
    }

    private String removeSpecials(String cardNumber) {
        cardNumber = cardNumber.replaceAll("[^\\d ]", "").replaceAll("\\s", "");
        return cardNumber;
    }

    private boolean luhnCheck(String cardNumber) {
        int sum = 0;
        boolean doubled = false;
        for (int i = cardNumber.length() - 1; i >= 0; --i) {
            int digit = Integer.parseInt(cardNumber.substring(i, i + 1));
            int addend;
            if (doubled) {
                addend = digit * 2;
                if (addend > 9) {
                    addend -= 9;
                }
            } else {
                addend = digit;
            }
            sum += addend;
            doubled = !doubled;
        }
        return sum % 10 == 0;
    }
}
;
