package org.owasp.security.logging;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 *
 * @author August Detlefsen <augustd@codemagi.com>
 */
public class Utils {

    public static String toSHA1(String input) {
	return toSHA1(input.getBytes());
    }
    
    public static String toSHA1(byte[] convertme) {
	try {
	    MessageDigest md = MessageDigest.getInstance("SHA-1");
	    return byteArray2Hex(md.digest(convertme));
	} catch (NoSuchAlgorithmException nsae) {
	    //this code should never be reached! 
	}
	return null;
    }

    private static String byteArray2Hex(final byte[] hash) {
	Formatter formatter = new Formatter();
	for (byte b : hash) {
	    formatter.format("%02x", b);
	}
	return formatter.toString();
    }

}
