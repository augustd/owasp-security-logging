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
 * @author August Detlefsen [augustd@codemagi.com]
 */
public class SecurityMarkerFilter extends AbstractMatcherFilter<ILoggingEvent> {

	public static final List<Marker> markersToMatch = new ArrayList<Marker>(3);
	static {
		markersToMatch.add(SecurityMarkers.SECURITY_SUCCESS);
		markersToMatch.add(SecurityMarkers.SECURITY_FAILURE);
		markersToMatch.add(SecurityMarkers.SECURITY_AUDIT);
	}

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
					return FilterReply.NEUTRAL;
				}
			}
		} else {
			// handle simple case of an event with a single marker
			if (markersToMatch.contains(eventMarker)) {
				return FilterReply.NEUTRAL;
			}
		}

		// no match found for security markers
		return FilterReply.DENY;
	}

}
