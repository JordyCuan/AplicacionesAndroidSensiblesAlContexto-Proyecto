package com.jordycuan.clima;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by JordyCuan on 08/06/16.
 */
public class Utils {

	public static int kelvinToCelsius(double kelvin) {
		return (int) Math.round(kelvin - 273.15);
	}

	public static double truncate_3(double n) {
		return Math.round(n * 1000) / 1000;
	}

	public static String getHourFromEpochDateSeconds(long timeSeconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeSeconds * 1000);

		return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
	}
}