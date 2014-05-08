package org.owasp.security.logging.mdc;

import java.io.IOException;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.owasp.security.logging.Utils;
import org.slf4j.MDC;

public class MDCFilter implements Filter {

	public static final String IPADDRESS = "ipAdress";
	public static final String LOGIN_ID = "loginId";

	private FilterConfig filterConfig;
	private static String TZ_NAME = "timezoneOffset";
	private String productName; 

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		productName = filterConfig.getInitParameter("ProductName");
	}

	/**
	 * Sample filter that populates the MDC on every request.
	 */
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		MDC.put("ipAddress", ipAddress);
		HttpSession session = request.getSession(false);
		TimeZone timeZone = null;
		if (session != null) {
			//capture (a hash of) the session ID
			MDC.put("session", Utils.toSHA1(session.getId()));
			// Something should set this after authentication completes
			String loginId = (String) session.getAttribute("LoginId");
			if (loginId != null) {
				MDC.put("loginId", loginId);
			}
			// This assumes there is some javascript on the user's page to
			// create the cookie.
			if (session.getAttribute(TZ_NAME) == null) {
				if (request.getCookies() != null) {
					for (Cookie cookie : request.getCookies()) {
						if (TZ_NAME.equals(cookie.getName())) {
							int tzOffsetMinutes = Integer.parseInt(cookie
									.getValue());
							timeZone = TimeZone.getTimeZone("GMT");
							timeZone.setRawOffset((int) (tzOffsetMinutes * DateUtils.MILLIS_PER_MINUTE));
							request.getSession().setAttribute(TZ_NAME,
									tzOffsetMinutes);
							cookie.setMaxAge(0);
							response.addCookie(cookie);
						}
					}
				}
			}
		}
		MDC.put("hostname", servletRequest.getServerName());
		if (productName != null) MDC.put("productName", productName);
		MDC.put("locale", servletRequest.getLocale().getDisplayName());
		if (timeZone == null) {
			timeZone = TimeZone.getDefault();
		}
		MDC.put("timezone", timeZone.getDisplayName());
		filterChain.doFilter(servletRequest, servletResponse);
		MDC.clear();
	}

	public void destroy() {
	}
}