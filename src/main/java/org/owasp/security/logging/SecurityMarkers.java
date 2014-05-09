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
	public static final String CONFIDENTAL_MARKER_NAME = "CONFIDENTIAL";
	public static final String SECRET_MARKER_NAME = "SECRET";
	public static final String TOP_SECRET_MARKER_NAME = "TOPSECRET";

	public static Marker SECURITY_MARKER = MarkerFactory
			.getMarker(SECURITY_MARKER_NAME);

	public static Marker RESTRICTED_MARKER = MarkerFactory
			.getMarker(RESTRICTED_MARKER_NAME);

	public static Marker CONFIDENTIAL_MARKER = MarkerFactory
			.getMarker(CONFIDENTAL_MARKER_NAME);

	public static Marker SECRET_MARKER = MarkerFactory
			.getMarker(SECRET_MARKER_NAME);

	public static Marker TOP_SECRET_MARKER = MarkerFactory
			.getMarker(TOP_SECRET_MARKER_NAME);

}
