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
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.slf4j.Log4jMarker;
import org.apache.logging.slf4j.Log4jMarkerFactory;
import org.owasp.security.logging.SecurityMarkers;

/**
 * Filters logging for information classification markers. If a logging event
 * has a an information classification marker (RESTRICTED, CONFIDENTIAL, SECRET,
 * TOP_SECRET) attached to it, it will fail the filter.
 *
 * This is useful to <i>exclude</i> classified information from a general log
 * file.
 *
 * @author August Detlefsen [augustd@codemagi.com]
 */
@Plugin(name = "ExcludeClassifiedMarkerFilter", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE, printObject = true)
public class ExcludeClassifiedMarkerFilter extends AbstractFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -532744149133457152L;

	static final Log4jMarkerFactory factory = new Log4jMarkerFactory();

	public static final List<org.slf4j.Marker> markersToMatch = new ArrayList<org.slf4j.Marker>(
			4);

	static {
		markersToMatch.add(SecurityMarkers.RESTRICTED);
		markersToMatch.add(SecurityMarkers.CONFIDENTIAL);
		markersToMatch.add(SecurityMarkers.SECRET);
		markersToMatch.add(SecurityMarkers.TOP_SECRET);
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
			return Result.NEUTRAL;
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
				return Result.DENY;
			}
		}

		return Result.NEUTRAL;
	}

	/**
	 * Create a SecurityMarkerFilter.
	 *
	 * @return The created ThresholdFilter.
	 */
	@PluginFactory
	public static ExcludeClassifiedMarkerFilter createFilter() {
		return new ExcludeClassifiedMarkerFilter();
	}
}
