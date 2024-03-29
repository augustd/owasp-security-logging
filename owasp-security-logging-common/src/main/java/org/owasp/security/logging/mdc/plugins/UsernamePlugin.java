package org.owasp.security.logging.mdc.plugins;

import org.owasp.security.logging.mdc.IPlugin;
import org.owasp.security.logging.mdc.MDCFilter;
import org.slf4j.MDC;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * This is an example MDC plugin that gets a username from the HTTP session and
 * places it in the MDC context for access by the logging system.
 * 
 * @author August Detlefsen [augustd@codemagi.com]
 */
public class UsernamePlugin implements IPlugin {

	public void init(FilterConfig config) {
	}

	public void execute(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		if (username != null) {
			MDC.put(MDCFilter.LOGIN_ID, username);
		}
	}

}
