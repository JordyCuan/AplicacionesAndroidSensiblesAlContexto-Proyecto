package cuan.jordy.com.androidcontexto.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;


public class WeatherUpdateService extends IntentService {

	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 *
	 * @param name Used to name the worker thread, important only for debugging.
	 */
	public WeatherUpdateService(String name) {
		super("WeatherUpdateService");
	}

	public WeatherUpdateService() {
		super("WeatherUpdateService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}
}
