package org.owasp.security.logging.util;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.slf4j.Logger;

/**
 * @author Jens Piegsa
 *
 */
public class StreamRedirectionWithCustomLoggersTest {

	@Test
	public void doTest() {

		System.out.println("message 1");
		System.err.println("message 2");

		final Logger sysOutLogger = spy(Logger.class);
		final Logger sysErrLogger = spy(Logger.class);

		SecurityUtil.bindSystemStreamsToSLF4J(sysOutLogger, sysErrLogger);

		System.out.println("message 3");
		System.err.println("message 4");

		SecurityUtil.unbindSystemStreams();

		System.out.println("message 5");
		System.err.println("message 6");

		verify(sysOutLogger, only()).info("message 3");
		verify(sysErrLogger, only()).error("message 4");
	}

}
