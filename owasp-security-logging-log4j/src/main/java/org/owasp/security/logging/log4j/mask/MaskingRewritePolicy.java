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
package org.owasp.security.logging.log4j.mask;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.slf4j.Log4jMarker;
import org.owasp.security.logging.SecurityMarkers;

/**
 *
 * @author adetlefsen
 */
@Plugin(name = "MaskingRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
public class MaskingRewritePolicy implements RewritePolicy {

	public static final Object MASKED_PASSWORD = "********";

	@PluginFactory
	public static MaskingRewritePolicy createPolicy() {
		return new MaskingRewritePolicy();
	}

	/**
	 * Rewrite the event.
	 *
	 * @param source a logging event that may be returned or used to create a
	 * new logging event.
	 * @return The LogEvent after rewriting.
	 */
	@Override
	public LogEvent rewrite(LogEvent source) {
		// get the markers for the log event. If no markers, nothing can be
		// tagged confidential and we can return
		Marker sourceMarker = source.getMarker();
		if (sourceMarker == null) {
			return source;
		}

		// get the message. If no message we can return
		final Message msg = source.getMessage();
		if (msg == null || !(msg instanceof ParameterizedMessage)) {
			return source;
		}

		// get the parameters. If no params we can return
		Object[] params = msg.getParameters();
		if (params == null || params.length == 0) {
			return source;
		}

		// check if this event is actually marked as confidential. If not,
		// return
		Log4jMarker eventMarker = new Log4jMarker(sourceMarker);
		if (!eventMarker.contains(SecurityMarkers.CONFIDENTIAL)) {
			return source;
		}

		// we have a message, parameters, a marker, and it is confidential.
		// Process
		for (int i = 0; i < params.length; i++) {
			params[i] = MASKED_PASSWORD;
		}

		// create new message
		Message outMessage = new ParameterizedMessage(msg.getFormat(), params,
				msg.getThrowable());

		// build new log event for output
		LogEvent output = new Log4jLogEvent.Builder(source)
				.setMessage(outMessage).build();

		return output;
	}

}
