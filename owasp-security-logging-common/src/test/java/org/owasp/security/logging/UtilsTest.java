package org.owasp.security.logging;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void shouldReplaceCRWithUnderscore() {
        Assert.assertEquals("hello_world", Utils.replaceCRLFWithUnderscore("hello\rworld"));
    }

    @Test
    public void shouldReplaceLFWithUnderscore() {
        Assert.assertEquals("hello_world", Utils.replaceCRLFWithUnderscore("hello\nworld"));
    }

    @Test
    public void shouldReplaceCRLFWithUnderscore() {
        Assert.assertEquals("hello__world", Utils.replaceCRLFWithUnderscore("hello\r\nworld"));
    }

}
