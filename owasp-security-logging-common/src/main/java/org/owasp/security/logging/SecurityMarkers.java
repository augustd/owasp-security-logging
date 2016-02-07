package org.owasp.security.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * SLF4J markers to mark security related events
 * 
 */
public class SecurityMarkers {

	public static final String SECURITY_MARKER_NAME = "SECURITY";

	public static final String RESTRICTED_MARKER_NAME = "RESTRICTED";
	public static final String CONFIDENTIAL_MARKER_NAME = "CONFIDENTIAL";
	public static final String SECRET_MARKER_NAME = "SECRET";
	public static final String TOP_SECRET_MARKER_NAME = "TOPSECRET";

	public static final String SECURITY_SUCCESS_MARKER_NAME = "SECURITY SUCCESS";
	public static final String SECURITY_FAILURE_MARKER_NAME = "SECURITY FAILURE";
	public static final String SECURITY_AUDIT_MARKER_NAME = "SECURITY AUDIT";

	public static final String EVENT_SUCCESS_MARKER_NAME = "EVENT SUCCESS";
	public static final String EVENT_FAILURE_MARKER_NAME = "EVENT FAILURE";
	public static final String EVENT_UNSPECIFIED_MARKER_NAME = "EVENT UNSPECIFIED";

	// information classification
	public static final Marker RESTRICTED = MarkerFactory
			.getDetachedMarker(RESTRICTED_MARKER_NAME);

	public static final Marker CONFIDENTIAL = MarkerFactory
			.getDetachedMarker(CONFIDENTIAL_MARKER_NAME);

	public static final Marker SECRET = MarkerFactory
			.getDetachedMarker(SECRET_MARKER_NAME);

	public static final Marker TOP_SECRET = MarkerFactory
			.getDetachedMarker(TOP_SECRET_MARKER_NAME);

	// security events
	public static final Marker SECURITY_SUCCESS = MarkerFactory
			.getDetachedMarker(SECURITY_SUCCESS_MARKER_NAME);

	public static final Marker SECURITY_FAILURE = MarkerFactory
			.getDetachedMarker(SECURITY_FAILURE_MARKER_NAME);

	public static final Marker SECURITY_AUDIT = MarkerFactory
			.getDetachedMarker(SECURITY_AUDIT_MARKER_NAME);

	// non-security events
	public static final Marker EVENT_SUCCESS = MarkerFactory
			.getDetachedMarker(EVENT_SUCCESS_MARKER_NAME);

	public static final Marker EVENT_FAILURE = MarkerFactory
			.getDetachedMarker(EVENT_FAILURE_MARKER_NAME);

	public static Marker getMarker(Marker... markers) {
		Marker output = new MultiMarker(markers);
		return output;
	}
}
