package org.owasp.security.logging.layout.rich;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import ch.qos.logback.classic.spi.LoggingEvent;

public class RichContext {

	private long pid;
	private long clientTime;
	private String applicationName;
	private String inetAddress;

	public RichContext(LoggingEvent event) {
		pid = getPID();
		clientTime = event.getTimeStamp();
		applicationName = getApplicationName();
		inetAddress = getLocalHostLANAddress();
	}

	public static long getPID() {
		String processName = java.lang.management.ManagementFactory
				.getRuntimeMXBean().getName();
		return Long.parseLong(processName.split("@")[0]);
	}

	public String getApplicationName() {

		try (InputStream manifestStream = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream("META-INF/MANIFEST.MF")) {
			Manifest manifest = new Manifest(manifestStream);
			Attributes attrs = manifest.getMainAttributes();

			// does this work for all java applications ?
			String appName = attrs.getValue("Implementation-Title");
			if (appName == null) {
				appName = "UNKNOWN";
			}
			return appName;
		} catch (IOException E) {
			// handle
		}
		return null;
	}

	public static String getHMAC(String msg) {
		String macKey = System.getProperty("hmac.key", "HMAC KEY");

		Mac mac;
		char[] checksum = "<unknown HMAC>".toCharArray();
		try {
			mac = Mac.getInstance("HmacSHA256");
			// get the bytes of the hmac key and data string
			byte[] secretByte = macKey.getBytes("UTF-8");
			byte[] dataBytes = msg.getBytes("UTF-8");
			SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");

			mac.init(secret);
			byte[] doFinal = mac.doFinal(dataBytes);
			checksum = Hex.encodeHex(doFinal);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return String.copyValueOf(checksum);

	}

	private static String getLocalHostLANAddress() {
		try {
			InetAddress candidateAddress = null;
			// Iterate all NICs (network interface cards)...
			for (Enumeration<NetworkInterface> ifaces = NetworkInterface
					.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces
						.nextElement();
				// Iterate all IP addresses assigned to each card...
				for (Enumeration<InetAddress> inetAddrs = iface
						.getInetAddresses(); inetAddrs.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs
							.nextElement();
					if (!inetAddr.isLoopbackAddress()) {

						if (inetAddr.isSiteLocalAddress()) {
							return inetAddr.toString();
						} else if (candidateAddress == null) {
							candidateAddress = inetAddr;
						}
					}
				}
			}
			if (candidateAddress != null) {
				return candidateAddress.toString();
			}
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			if (jdkSuppliedAddress == null) {
				throw new UnknownHostException(
						"The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
			}
			return jdkSuppliedAddress.toString();
		} catch (Exception e) {
			return "<unknown ip>";
		}
	}

	@Override
	public String toString() {
		String message = "[pid=" + pid + ", applicationName=" + applicationName
				+ ", clientTime=" + clientTime + ", clientIp=" + inetAddress
				+ "]";
		String signedMessage = message + getHMAC(message);
		return signedMessage;
	}

}
