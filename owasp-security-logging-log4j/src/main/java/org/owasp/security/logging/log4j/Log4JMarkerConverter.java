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
package org.owasp.security.logging.log4j;

import java.util.Iterator;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 *
 * @author August Detlefsen [augustd@codemagi.com]
 */
public class Log4JMarkerConverter {
	
	public static Marker convertMarker(org.slf4j.Marker input) {
		Marker output = MarkerManager.getMarker(input.getName());
		
		if (input.hasReferences()) {
			Iterator i = input.iterator();
			while (i.hasNext()) {
				org.slf4j.Marker ref = (org.slf4j.Marker)i.next();
				output.addParents(convertMarker(ref));
			}
		}
		return output;
	}
	
}
