package org.owasp.security.logging.mask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
/**
 * Masking configured values in logs. To configure in you logback file here is an example:
 * Step 1 add conversion word: 
 * 
 * &lt;conversionRule conversionWord="maskedMsg" converterClass="org.owasp.security.logging.mask.DefinedRegexMaskingConverter" /&gt;
 * 
 * Step 2 add to the pattern like this. There 4 pre-defined values for CompleteMask, MaskLastFour, MaskFirstFour, emailMasking
 * 
 * %maskedMsg{password|signature
 *			username,
 *			orderNumber|giftCardNum|,
 *			email
 *			}
 * 
 * @author Rahul Agarwal
 *
 */
public class DefinedRegexMaskingConverter extends ClassicConverter {
	private Map<Pattern, String> patternMap = new HashMap<>();
	private static final String MASK = "*****";
	
	@Override
	public String convert(ILoggingEvent logEvent) {
		String message = logEvent.getMessage();
		Set<Pattern> patternSet = patternMap.keySet();

		if (message!=null && !message.equals("")) {	
			for (Pattern pattern : patternSet) {
				message = pattern.matcher(message).replaceAll(patternMap.get(pattern));
			}
		}

		return message;
	}

	@Override
	public void start() {
		List<String> options = getOptionList();
		if (options != null && !options.isEmpty()) {
			// 0 = CompleteMask
			patternMap.put(Pattern.compile("(?x)([\"]?(" + options.get(0) + ")[\"]?\\s*[:=]{1}\\s*[\"]?)(?:[^\"\\n]+)"), "$1" + MASK);

			// 1 = MaskLastFour
			patternMap.put(Pattern.compile("(?x)([\"]?(" + options.get(1) + ")[\"]?[:=]{1}[\"]?[\\w.+/=]+)(?:[\\w.+/=]{4})"), "$1" + MASK);
			
			// 2 = MaskFirstFour
			patternMap.put(Pattern.compile("(?x)([\"]?(" + options.get(2) + ")[\"]?[:=]{1}[\"]?)(?:[\\w.+/=]+(?=\\w{4}))"), "$1" + MASK);
			
			// 3 = emailMasking
			patternMap.put(Pattern.compile("(?x)([\"]?(" + options.get(3) + ")[\"]?\\s*[:=]{1}\\s*[\"]?[\\w.]+(?=@[\\w.]+))(?:@[\\w.]+)"), "$1" + MASK);
		}
		super.start();
	}
}