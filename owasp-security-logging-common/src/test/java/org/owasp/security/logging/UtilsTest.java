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

	@Test
	public void escapeNLFCharsShouldEscapeAllNLF() {
		Assert.assertEquals("line0\\nline1", Utils.escapeNLFChars("line0\nline1"));
		Assert.assertEquals("line0\\rline1", Utils.escapeNLFChars("line0\rline1"));
		Assert.assertEquals("line0\\u0085line1", Utils.escapeNLFChars("line0\u0085line1"));
		Assert.assertEquals("line0\\u000Bline1", Utils.escapeNLFChars("line0\u000Bline1"));
		Assert.assertEquals("line0\\u000Cline1", Utils.escapeNLFChars("line0\u000Cline1"));
		Assert.assertEquals("line0\\u2028line1", Utils.escapeNLFChars("line0\u2028line1"));
		Assert.assertEquals("line0\\u2029line1", Utils.escapeNLFChars("line0\u2029line1"));
	}

}
