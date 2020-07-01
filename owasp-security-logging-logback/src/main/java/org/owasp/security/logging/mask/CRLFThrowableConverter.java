package org.owasp.security.logging.mask;

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;

/**
 * This converter is used to encode any carriage returns and line feeds to
 * prevent log injection attacks in exception messages
 *
 * This converter uses a CRLFThrowableProxy that acts as a proxy to intercept
 * calls to IThrowable.getMessage to replace the characters in the message
 */
public class CRLFThrowableConverter extends ThrowableProxyConverter {

    @Override
    protected String throwableProxyToString(IThrowableProxy tp) {
        return super.throwableProxyToString(new CRLFThrowableProxy(tp));
    }
}
