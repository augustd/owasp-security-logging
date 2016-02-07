package org.owasp.security.logging.mdc.plugins;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

import org.owasp.security.logging.mdc.IPlugin;
import org.owasp.security.logging.mdc.MDCFilter;
import org.slf4j.MDC;

/**
 * This plugin adds the request's remote IP address to the MDC by using the
 * value of the X-Forwarded-For header appended by a load balancer. The value
 * can be accessed in a PatternLayout by using the specifier: %X{ipAddress}
 * 
 * IMPORTANT: If your environment does not use a load balancer, it is
 * recommended to not use this plugin since an attacker could easily add spoofed
 * X-Forwarded-For headers in any request.
 * 
 * @author August Detlefsen [augustd@codemagi.com]
 */
public class ForwardedIPAddressPlugin implements IPlugin {

	public void init(FilterConfig config) {
	}

	public void execute(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		MDC.put(MDCFilter.IPADDRESS, ipAddress);
	}

}
