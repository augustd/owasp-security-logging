package org.owasp.security.logging.mask;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.qos.logback.classic.spi.LoggingEvent;
/**
 * Test regex masking convertor
 * @author Rahul Agarwal
 *
 */
public class DefinedRegexMaskingConverterTest {
	private DefinedRegexMaskingConverter mc;
	
	@Before
	public void init(){
		mc = new DefinedRegexMaskingConverter();
		List<String> optionsList = new ArrayList<>();
		//full mask
		optionsList.add("password|signature");
		//mask last 4
		optionsList.add("username");
		//mask first 4
		optionsList.add("orderNumber|giftCardNum");
		//email
		optionsList.add("email|customerEmail");
		
		mc.setOptionList(optionsList);
		mc.start();
	}

	@Test
	public void testCompleteMask(){
		//blank message
		String masked = mc.convert(getEvent(""));
		assertEquals("", masked);
		
		//equal separator
		masked = mc.convert(getEvent("password=abc123"));
		assertEquals("password=*****", masked);

		//colon seprator
		masked = mc.convert(getEvent("password:abc123"));
		assertEquals("password:*****", masked);
		
		//json
		masked = mc.convert(getEvent("\"password\":\"abc123\""));
		assertEquals("\"password\":\"*****\"", masked);
		
		//multiple
		masked = mc.convert(getEvent("\"password\":\"abc123\",signature=foo"));
		assertEquals("\"password\":\"*****\",signature=*****", masked);
	}

	@Test
	public void testLast4Mask(){
		//equal separator
		String masked = mc.convert(getEvent("username=abc123"));
		assertEquals("username=ab*****", masked);

		//colon seprator
		masked = mc.convert(getEvent("username:abc123"));
		assertEquals("username:ab*****", masked);
		
		//json
		masked = mc.convert(getEvent("\"username\":\"abc123\""));
		assertEquals("\"username\":\"ab*****\"", masked);
		
		//multiple
		masked = mc.convert(getEvent("\"username\":\"abc123\",signature=foo"));
		assertEquals("\"username\":\"ab*****\",signature=*****", masked);
	}
	
	@Test
	public void testFirst4Mask(){
		//equal separator
		String masked = mc.convert(getEvent("orderNumber=77887765567abc123"));
		assertEquals("orderNumber=*****c123", masked);

		//colon seprator
		masked = mc.convert(getEvent("orderNumber:abc123"));
		assertEquals("orderNumber:*****c123", masked);
		
		//json
		masked = mc.convert(getEvent("\"orderNumber\":\"abc123\""));
		assertEquals("\"orderNumber\":\"*****c123\"", masked);
		
		//multiple
		masked = mc.convert(getEvent("\"orderNumber\":\"abc123\",signature=foo"));
		assertEquals("\"orderNumber\":\"*****c123\",signature=*****", masked);
	}
	
	@Test
	public void testEmail(){
		//equal separator
		String masked = mc.convert(getEvent("email=foo@bar.com"));
		assertEquals("email=foo*****", masked);

		//colon seprator
		masked = mc.convert(getEvent("email:foo@bar.com.sg"));
		assertEquals("email:foo*****", masked);
		
		//json
		masked = mc.convert(getEvent("\"email\":\"foo.baz@bar.com\""));
		assertEquals("\"email\":\"foo.baz*****\"", masked);
		
		//multiple
		masked = mc.convert(getEvent("\"email\":\"foo@bar.com.sg\",signature=foo"));
		assertEquals("\"email\":\"foo*****\",signature=*****", masked);
	}
	
	private LoggingEvent getEvent(String message) {
		LoggingEvent event = new LoggingEvent();
		event.setMessage(message);
		return event;
	}
}
