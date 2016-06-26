/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.owasp.security.logging.log4j.filter;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.slf4j.Log4jMarker;
import org.apache.logging.slf4j.Log4jMarkerFactory;
import org.owasp.security.logging.SecurityMarkers;

/**
 * Filters logging for SECURITY markers. If a logging event has a SECURITY
 * marker attached to it, it will pass the filter. This is useful to route
 * security related events to a separate log file.
 *
 * @author August Detlefsen [augustd@codemagi.com]
 */
@Plugin(name = "SecurityMarkerFilter", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE, printObject = true)
public class SecurityMarkerFilter extends AbstractFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 610457881503552839L;

	static final Log4jMarkerFactory factory = new Log4jMarkerFactory();

	public static final List<org.slf4j.Marker> markersToMatch = new ArrayList<org.slf4j.Marker>(3);

	static {
		markersToMatch.add(SecurityMarkers.SECURITY_SUCCESS);
		markersToMatch.add(SecurityMarkers.SECURITY_FAILURE);
		markersToMatch.add(SecurityMarkers.SECURITY_AUDIT);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String msg,
			Object... params) {
		return filter(marker);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Object msg,
			Throwable t) {
		return filter(marker);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker,
			Message msg, Throwable t) {
		return filter(marker);
	}

	@Override
	public Result filter(LogEvent event) {
		// make sure the event has a marker
		org.apache.logging.log4j.Marker eventMarker = event.getMarker();
		if (eventMarker == null) {
			return Result.DENY;
		}

		return filter(eventMarker);
	}

	private Result filter(Marker marker) {
		if (!isStarted()) {
			return Result.NEUTRAL;
		}

		org.apache.logging.slf4j.Log4jMarker slf4jMarker = new Log4jMarker(
				marker);
		for (org.slf4j.Marker matcher : markersToMatch) {
			if (slf4jMarker.contains(matcher.getName())) {
				return Result.ACCEPT;
			}
		}

		return Result.DENY;
	}

	/**
	 * Create a SecurityMarkerFilter.
	 *
	 * @param acceptAll
	 * @return The created ThresholdFilter.
	 */
	@PluginFactory
	public static SecurityMarkerFilter createFilter(
			@PluginAttribute(value = "acceptAll", defaultBoolean = false) boolean acceptAll) {
		return new SecurityMarkerFilter();
	}
}
