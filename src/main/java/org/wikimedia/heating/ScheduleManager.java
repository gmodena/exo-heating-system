package org.wikimedia.heating;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;

/**
 * The system obtains temperature data from a remote source,
 * compares it with a given threshold and controls a remote heating
 * unit by switching it on and off. It does so only within a time
 * period configured on a remote service (or other source)
 * 
 * This is purpose-built crap
 *
 */
public class ScheduleManager {
	private final static String ENDPOINT = "http://probe.home:9990/";
	/**
	 * This method is the entry point into the code. You can assume that it is
	 * called at regular interval with the appropriate parameters.
	 */
	public static void manage(HeatingManagerImpl heatingManager, String threshold) throws Exception {
		Double temperature = getTemperature();
		if(isActive()) {
			Boolean toggle = temperature > Double.valueOf(threshold);
			heatingManager.adjust(toggle);
		}
	}

	private static boolean isActive() throws IOException {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > startHour() && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < endHour();
	}

	private static Double getTemperature() throws IOException {
		return  Double.valueOf(stringFromURL(String.format("%s/%s", ENDPOINT, "temp"), 4));
	}

	private static int endHour() throws NumberFormatException, IOException {
		return new Integer(stringFromURL(String.format("%s/%s", ENDPOINT, "end"), 2));
	}

	private static int startHour() throws NumberFormatException, IOException {
		return new Integer(stringFromURL(String.format("%s/%s", ENDPOINT, "start"), 2));
	}

	private static String stringFromURL(String urlString, int bufferSize) throws
			IOException {
		URL url = new URL(urlString);
		InputStream is = url.openStream();
		byte[] tempBuffer = new byte[bufferSize];
		is.read(tempBuffer);
		String t = new String(tempBuffer);
		is.close();
		return t;
	}
}
