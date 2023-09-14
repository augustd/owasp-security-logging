package org.owasp.security.logging.mask;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import org.owasp.security.logging.Utils;

/**
 * Throwable proxy that replaces NLF (newline function) chars in the message to avoid log injection
 * in exception messages.
 * Calls to getMessage are intercepted to replace NLF in the message
 * Calls to getCause are intercepted to ensure all exceptions in the stack are treated
 * All other other methods are directly sent through the proxied instance.
 */
public class NLFThrowableProxy implements IThrowableProxy {
	private final IThrowableProxy proxied;

	public NLFThrowableProxy(IThrowableProxy proxied) {
		this.proxied = proxied;
	}

	@Override
	public String getMessage() {
		if (proxied.getMessage() == null) {
			return null;
		}
		return Utils.replaceCRLFWithUnderscore(proxied.getMessage());
	}

	@Override
	public IThrowableProxy getCause() {
		if (proxied.getCause() == null) {
			return null;
		}
		if (proxied.getCause() == proxied || proxied.getCause() == this) {
			return this;
		}
		return new NLFThrowableProxy(proxied.getCause());
	}

	@Override
	public String getClassName() {
		return proxied.getClassName();
	}

	@Override
	public StackTraceElementProxy[] getStackTraceElementProxyArray() {
		return proxied.getStackTraceElementProxyArray();
	}

	@Override
	public int getCommonFrames() {
		return proxied.getCommonFrames();
	}

	@Override
	public IThrowableProxy[] getSuppressed() {
		return proxied.getSuppressed();
	}

	@Override
	public boolean isCyclic() {
		return proxied.isCyclic();
	}
}
