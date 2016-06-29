package com.jordycuan.clima.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.jordycuan.clima.service.WeatherUpdateService;

public class BootReceiver extends BroadcastReceiver {
	public BootReceiver() {
	}

	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			setAlarm(context);
		}
	}

	public void setAlarm(Context context) {
		Log.d("****** SetAlarm ******", "Setting the alarm");
		alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmIntent = PendingIntent.getService(context, 0,
				new Intent(context, WeatherUpdateService.class),
				PendingIntent.FLAG_NO_CREATE);


		Log.d("****** PendInt ******", "PendInt " + alarmIntent);

		if (alarmIntent != null) {
			Log.d("****** Al_arm ******", "La alarma ya existe");
			return;
		} else {
			alarmIntent = PendingIntent.getService(context, 0,
					new Intent(context, WeatherUpdateService.class), 0);
		}


		alarmMgr.setInexactRepeating(
				AlarmManager.ELAPSED_REALTIME_WAKEUP,
				1000 * 60 * 1,
				1000 * 60 * 3,
				alarmIntent);
	}
}
