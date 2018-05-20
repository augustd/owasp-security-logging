package org.owasp.security.logging;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Utilities methods for logging.
 *
 * @author August Detlefsen [augustd@codemagi.com]
 */
public class Utils {

	/**
	 * Converts an input String to a SHA hash. The actual hash strength is
	 * hidden by the method name to allow for future-proofing this API, but the
	 * current default is SHA-256.
	 *
	 * @param input
	 *            The string to hash
	 * @return SHA hash of the input String, hex encoded.
	 */
	public static String toSHA(String input) {
		return toSHA(input.getBytes());
	}

	/**
	 * Converts an input byte array to a SHA hash. The actual hash strength is
	 * hidden by the method name to allow for future-proofing this API, but the
	 * current default is SHA-256.
	 *
	 * @param input
	 *            Byte array to hash
	 * @return SHA hash of the input String, hex encoded.
	 */
	public static String toSHA(byte[] input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			return byteArray2Hex(md.digest(input));
		} catch (NoSuchAlgorithmException nsae) {
			// this code should never be reached!
		}
		return null;
	}

	/**
	 * Converts an input byte array to a hex encoded String.
	 *
	 * @param input
	 *            Byte array to hex encode
	 * @return Hex encoded String of the input byte array
	 */
	private static String byteArray2Hex(final byte[] hash) {
		try (Formatter formatter = new Formatter();) {
			for (byte b : hash) {
				formatter.format("%02x", b);
			}
			String hex = formatter.toString();
			return hex;
		}
	}

	/**
	 * Determines if a string is null or empty
	 *
	 * @param value
	 *            string to test
	 * @return <code>true</code> if the string is empty or null;
	 *         <code>false</code> otherwise
	 */
	public static boolean isEmpty(String value) {
		return (value == null || value.trim().length() == 0);
	}

	/**
	 * Replace any carriage returns and line feeds with an underscore to prevent log injection attacks.
	 *
	 * @param value
	 *            string to convert
	 * @return converted string
	 */
	public static String replaceCRLFWithUnderscore(String value) {
		return value.replace('\n', '_').replace('\r', '_');
	}

}
