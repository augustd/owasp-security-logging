package org.owasp.security.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class SecurityMarkers {

	public static final String SECURITY_MARKER_NAME = "SECURITY";

	public static Marker SECURITY_MARKER = MarkerFactory
			.getMarker(SECURITY_MARKER_NAME);
}
