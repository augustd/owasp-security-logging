package org.owasp.security.logging.mask;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LUHNMaskingConverterTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void should_satisfy_convert_with_masked_string() {
        ILoggingEvent iLoggingEvent = new LoggingEvent();
        ((LoggingEvent) iLoggingEvent).setMessage(getXML());
        String formatted = new LUHNMaskingConverter().convert(iLoggingEvent);
        assertThat(formatted).isNotNull().isXmlEqualTo(expectedXml());
    }

    private String expectedXml() {
        return "<?xml version='1.0' encoding='UTF-8'?>" +
                "<Sample>" +
                "<Visa>4111**MASKED**1111</Visa>" +
                "<AmericanExpress>3782**MASKED**0005</AmericanExpress>" +
                "<AmericanExpressCorporate>3787**MASKED**1000</AmericanExpressCorporate>" +
                "<AustralianBankCard>5610**MASKED**8250</AustralianBankCard>" +
                "<DinersClub>3056**MASKED**5904</DinersClub>" +
                "<MasterCard>5105**MASKED**5100</MasterCard>" +
                "<Visa>4222**MASKED**2222</Visa>" +
                "<JCB>3566**MASKED**0505</JCB>" +
                "<Test>TEXT Vlaues</Test>" +
                "<Test2>lorem lipsm lpisn</Test2>" +
                "<NonCard1>1546955340104</NonCard1>" +
                "<NonCard2>1546955347298</NonCard2>" +
                "</Sample>";
    }

    private String getXML() {
        return "<?xml version='1.0' encoding='UTF-8'?>" +
                "<Sample>" +
                "<Visa>4111 1111 1111 1111</Visa>" +
                "<AmericanExpress>3782-8224-6310-005</AmericanExpress>" +
                "<AmericanExpressCorporate>3787,3449,3671,000</AmericanExpressCorporate>" +
                "<AustralianBankCard>5610591081018250</AustralianBankCard>" +
                "<DinersClub>30569309025904</DinersClub>" +
                "<MasterCard>5105105105105100</MasterCard>" +
                "<Visa>4222222222222</Visa>" +
                "<JCB>3566002020360505</JCB>" +
                "<Test>TEXT Vlaues</Test>" +
                "<Test2>lorem lipsm lpisn</Test2>" +
                "<NonCard1>1546955340104</NonCard1>" +
                "<NonCard2>1546955347298</NonCard2>" +
                "</Sample>";
    }

}