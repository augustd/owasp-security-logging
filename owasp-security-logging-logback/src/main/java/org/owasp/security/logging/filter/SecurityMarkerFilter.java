package org.owasp.security.logging.filter;

import java.util.ArrayList;
import java.util.List;

import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Marker;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Filters logging for SECURITY markers. If a logging event has a SECURITY
 * marker attached to it, it will pass the filter. This is useful to route
 * security related events to a separate log file.
 * 
 * The default behavior of this filter is to deny all non-security events and
 * pass security events to the rest of the filter chain. If acceptAll is true,
 * then all security related events will pass this filter, regardless of other
 * filters on the filter chain. To enable acceptAll, configure the filter as
 * follows:
 * 
 * <pre>
 * {@code
 * <filter class="org.owasp.security.logging.filter.SecurityMarkerFilter">
 *     <acceptAll>true</acceptAll>
 * </filter>
 * }
 * </pre>
 * 
 * @author August Detlefsen [augustd@codemagi.com]
 */
public class SecurityMarkerFilter extends AbstractMatcherFilter<ILoggingEvent> {

	public static final List<Marker> markersToMatch = new ArrayList<Marker>(3);
	static {
		markersToMatch.add(SecurityMarkers.SECURITY_SUCCESS);
		markersToMatch.add(SecurityMarkers.SECURITY_FAILURE);
		markersToMatch.add(SecurityMarkers.SECURITY_AUDIT);
	}

	private boolean acceptAll = false;

	public FilterReply decide(ILoggingEvent event) {
		if (!isStarted()) {
			return FilterReply.NEUTRAL;
		}

		// make sure the event has a marker
		Marker eventMarker = event.getMarker();

		if (eventMarker == null) {
			return FilterReply.DENY;
		}

		if (eventMarker.hasReferences()) {
			// check for events with multiple markers
			for (Marker marker : markersToMatch) {
				if (eventMarker.contains(marker)) {
					return acceptAll ? FilterReply.ACCEPT : FilterReply.NEUTRAL;
				}
			}
		} else {
			// handle simple case of an event with a single marker
			if (markersToMatch.contains(eventMarker)) {
				return acceptAll ? FilterReply.ACCEPT : FilterReply.NEUTRAL;
			}
		}

		// no match found for security markers
		return FilterReply.DENY;
	}

	public void setAcceptAll(String input) {
		if (input != null) {
			acceptAll = Boolean.valueOf(input);
		}
	}
}
