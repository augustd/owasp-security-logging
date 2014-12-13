package org.owasp.security.logging.filter;

import org.slf4j.Marker;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import java.util.ArrayList;
import java.util.List;
import org.owasp.security.logging.SecurityMarkers;

/**
 * Filters logging for information classification markers. If a logging event
 * has a an information classification marker (RESTRICTED, CONFIDENTIAL, SECRET,
 * TOP_SECRET) attached to it, it will fail the filter.
 *
 * This is useful to <i>exclude</i> classified information from a general log file.
 *
 * @author August Detlefsen <augustd@codemagi.com>
 */
public class ExcludeClassifiedMarkerFilter extends AbstractMatcherFilter<ILoggingEvent> {

    public static final List<Marker> markersToMatch = new ArrayList<Marker>(4);

    static {
        markersToMatch.add(SecurityMarkers.RESTRICTED);
        markersToMatch.add(SecurityMarkers.CONFIDENTIAL);
        markersToMatch.add(SecurityMarkers.SECRET);
        markersToMatch.add(SecurityMarkers.TOP_SECRET);
    }

    @Override
    public void start() {
        super.start();
    }

    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        //make sure the event has a marker
        Marker eventMarker = event.getMarker();
        if (eventMarker == null) {
            return FilterReply.NEUTRAL;
        }

        if (eventMarker.hasReferences()) {
            //check for events with multiple markers
            for (Marker marker : markersToMatch) {
                if (eventMarker.contains(marker)) {
                    return FilterReply.DENY;
                }
            }
        } else {
            //handle simple case of an event with a single marker
            if (markersToMatch.contains(eventMarker)) {
                return FilterReply.DENY;
            }
        }

        //no classified markers found
        return FilterReply.NEUTRAL;
    }

}
