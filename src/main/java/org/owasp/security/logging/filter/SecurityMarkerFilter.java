package org.owasp.security.logging.filter;

import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.turbo.MarkerFilter;
import ch.qos.logback.core.spi.FilterReply;

public class SecurityMarkerFilter extends MarkerFilter {

	// public class MarkerFilter extends AbstractMatcherFilter {

	public SecurityMarkerFilter() {
		setName(SecurityMarkers.SECURITY_MARKER_NAME);
		markerToMatch = SecurityMarkers.SECURITY_MARKER;
	}

	Marker markerToMatch;

	public void start() {
		if (this.markerToMatch != null) {
			super.start();
		} else {
			addError(String.format("The marker property must be set for [%s]",
					getName()));
		}
	}

	public FilterReply decide(ILoggingEvent event) {
		Marker marker = event.getMarker();
		if (!isStarted()) {
			return FilterReply.NEUTRAL;
		}

		if (marker == null) {
			return onMismatch;
		}

		if (markerToMatch.contains(marker)) {
			return onMatch;
		}
		return onMismatch;
	}

	public void setMarker(String markerStr) {
		if (markerStr != null) {
			markerToMatch = MarkerFactory.getMarker(markerStr);
		}
	}

}