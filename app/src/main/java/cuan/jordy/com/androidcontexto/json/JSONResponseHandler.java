package cuan.jordy.com.androidcontexto.json;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cuan.jordy.com.androidcontexto.Utils;

/**
 * Created by JordyCuan on 19/06/16.
 */
public class JSONResponseHandler implements ResponseHandler<List<String>> {
	/* INTERESANTES:
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
	public List<String> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		List<String> result = new ArrayList<String>();

		Log.d("**** StatusCode ****", " " + response.getStatusLine().getStatusCode() + " " );
		if (response.getStatusLine().getStatusCode() != 200) {
			result.add("NULO");
			return result;
		}

		String JSONResponse = new BasicResponseHandler()
				.handleResponse(response);

		try {
			JSONObject responseObject = new JSONObject(JSONResponse);
			Log.d("****** Response ******", JSONResponse);

			// Extraemos los datos del objeto
			String desc = ((JSONObject) responseObject.getJSONArray(JSON_KEY_WEATHER).get(0)).getString(JSON_KEY_DESCRIPTION);
			JSONObject main = responseObject.getJSONObject(JSON_KEY_MAIN);

			double speed_mps = responseObject.getJSONObject(JSON_KEY_WIND).getDouble(JSON_KEY_WIND_SPEED);

			JSONObject sys = responseObject.getJSONObject(JSON_KEY_SYS);
			String country = sys.getString(JSON_KEY_SYS_COUNTRY);

			String name = responseObject.getString(JSON_KEY_NAME);


			result.add("temp: " + Utils.kelvinToCelsius(main.getDouble(JSON_KEY_MAIN_TEMP))
					+ " - " + name + ", " + country);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return result;
	}
}