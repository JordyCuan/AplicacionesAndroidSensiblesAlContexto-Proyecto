package com.jordycuan.clima.json;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.jordycuan.clima.Utils;
import com.jordycuan.clima.db.Element;

/**
 * Created by JordyCuan on 19/06/16.
 */
public class JSONResponseHandler implements ResponseHandler<Element> {
	/**
		INTERESANTES:
		http://openweathermap.org/current
			{
				"weather": [{
					... ,
					"description": "clear sky",
					...
				}],
				"main": {
					"temp": 279.388,
					"pressure": 1039.04,
					"humidity": 91,
					"temp_min": 279.388,
					"temp_max": 279.388,
					...
				},
				"wind": {
					"speed": 0.58,
					...
				},
				"sys": {
					... ,
					"country": "AR",
					"sunrise": 1466249213,
					"sunset": 1466283253
				},
				"name": "Bahia Blanca",
				...
			}
	*/
	private static final String JSON_KEY_WEATHER = "weather";
	private static final String JSON_KEY_DESCRIPTION = "description";

	private static final String JSON_KEY_MAIN = "main";
	private static final String JSON_KEY_MAIN_TEMP = "temp";
	private static final String JSON_KEY_MAIN_PRESSURE = "pressure";
	private static final String JSON_KEY_MAIN_HUMIDITY = "humidity";
	private static final String JSON_KEY_MAIN_TEMP_MIN = "temp_min";
	private static final String JSON_KEY_MAIN_TEMP_MAX = "temp_max";

	private static final String JSON_KEY_WIND = "wind";
	private static final String JSON_KEY_WIND_SPEED = "speed";

	private static final String JSON_KEY_SYS = "sys";
	private static final String JSON_KEY_SYS_COUNTRY = "country";
	private static final String JSON_KEY_SYS_SUNRISE = "sunrise";
	private static final String JSON_KEY_SYS_SUNSET = "sunset";

	private static final String JSON_KEY_NAME = "name";

	@Override
	public Element handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		Element e = null;

		Log.d("**** StatusCode ****", " " + response.getStatusLine().getStatusCode() + " " );
		if (response.getStatusLine().getStatusCode() != 200) { return null; }

		String JSONResponse = new BasicResponseHandler().handleResponse(response);
		try {
			Log.d("****** Response ******", JSONResponse);
			 e = generateItem(new JSONObject(JSONResponse));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return e;
	}

	private Element generateItem(JSONObject responseObject) throws JSONException {
		Element element = new Element();

		// Extraemos los datos del objeto
		element.description  = ((JSONObject) responseObject.getJSONArray(JSON_KEY_WEATHER).get(0)).getString(JSON_KEY_DESCRIPTION);;

		JSONObject main = responseObject.getJSONObject(JSON_KEY_MAIN);
		element.temp = Utils.kelvinToCelsius(main.getDouble(JSON_KEY_MAIN_TEMP));
		element.pressure = main.getDouble(JSON_KEY_MAIN_PRESSURE);
		element.humidity = main.getInt(JSON_KEY_MAIN_HUMIDITY);
		element.temp_min = Utils.kelvinToCelsius(main.getDouble(JSON_KEY_MAIN_TEMP_MIN));
		element.temp_max = Utils.kelvinToCelsius(main.getDouble(JSON_KEY_MAIN_TEMP_MAX));

		// metros/segundo
		element.speed = responseObject.getJSONObject(JSON_KEY_WIND).getDouble(JSON_KEY_WIND_SPEED);

		JSONObject sys = responseObject.getJSONObject(JSON_KEY_SYS);
		element.country = sys.getString(JSON_KEY_SYS_COUNTRY);
		element.sunrise = sys.getLong(JSON_KEY_SYS_SUNRISE);
		element.sunset = sys.getLong(JSON_KEY_SYS_SUNSET);

		element.name = responseObject.getString(JSON_KEY_NAME);

		return element;
	}
}