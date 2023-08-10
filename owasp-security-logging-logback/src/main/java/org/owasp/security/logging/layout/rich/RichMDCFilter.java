package org.owasp.security.logging.layout.rich;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import jakarta.servlet.ServletRequest;

public class RichMDCFilter extends MDCInsertingServletFilter {

	// TODO : implement
	void insertIntoMDC(ServletRequest request) {
		// super.insertIntoMDC(request);
	}
}
