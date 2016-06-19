package cuan.jordy.com.androidcontexto;

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
}
