package org.owasp.security.logging.layout.cef;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * prefix of the CEF logging format example :
 * 
 * Sep 19 08:26:10 host CEF:0|security|threatmanager|1.0|100|worm successfully
 * stopped|10|src=10.0.0.1 dst=2.1.2.2 spt=1232
 * 
 * @author sytze
 *
 */
public class Prefix {

	private final static String CEF_PREFIX = "CEF:";

	/**
	 * version of the CEF format
	 */
	int version;

	/**
	 * device that sends the logging events
	 */
	Device device = new Device();

	/**
	 * represents the type of event, for instance for intrusion detection
	 */
	String signatureId = "signatureId";

	/**
	 * human-readable description of the event
	 */
	String name = "name";

	/**
	 * severity/importance of the event, range 0 - 10. 10 is the most important
	 */
	int severity;

	/**
	 * key/value pairs with extra
	 */
	Map<String, String> extension = new ExtensionMap();

	class Device {
		String vendor = "vendor", product = "product", version = "version";

		public String toString() {
			return vendor + "|" + product + "|" + version;
		}
	}

	public String toString() {
		return CEF_PREFIX + version + "|" + device + "|" + signatureId + "|"
				+ name + "|" + severity + "|" + extension;
	}

	class ExtensionMap extends HashMap<String, String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1499695924597182375L;

		public ExtensionMap() {
			put("extension", "value");
		}

		/**
		 * TODO : encoding, multi-line
		 */
		public String toString() {
			Set<String> keys = this.keySet();
			StringBuilder builder = new StringBuilder();
			for (Object key : keys) {
				builder.append(key);
				builder.append('=');
				builder.append(get(key));
				builder.append(' ');
			}
			return builder.toString();
		}
	}
}
