package org.owasp.security.logging.mdc.plugins;

import org.owasp.security.logging.Utils;
import org.owasp.security.logging.mdc.IPlugin;
import org.slf4j.MDC;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * This plugin adds a hash of the session ID to the MDC. The value can be
 * accessed in a PatternLayout by using the specifier: %X{session}
 *
 * @author August Detlefsen [augustd@codemagi.com]
 */
public class SessionPlugin implements IPlugin {

	public void init(FilterConfig config) {
	}

	public void execute(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session != null) {
			// capture (a hash of) the session ID
			String hashedSession = Utils.toSHA(session.getId());
			MDC.put("session", hashedSession);
		}
	}

}
