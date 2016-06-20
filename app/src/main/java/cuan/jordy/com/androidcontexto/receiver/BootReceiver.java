package cuan.jordy.com.androidcontexto.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import cuan.jordy.com.androidcontexto.service.WeatherUpdateService;

public class BootReceiver extends BroadcastReceiver {
	public BootReceiver() {
	}

	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			Toast.makeText(context, "Inicio del sistema:Weather", Toast.LENGTH_LONG).show();
			setAlarm(context);
		}
	}

	public void setAlarm(Context context) {
		Log.d("****** SetAlarm ******", "Setting the alarm");
		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmIntent = PendingIntent.getService(
				context,
				0,
				new Intent(context, WeatherUpdateService.class),
				0);
/*
		// Set the alarm to start at 8:30 a.m.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 6);
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.HOUR_OF_DAY, 14);
		calendar.set(Calendar.HOUR_OF_DAY, 16);
		calendar.set(Calendar.HOUR_OF_DAY, 18);
		calendar.set(Calendar.HOUR_OF_DAY, 20);
		calendar.set(Calendar.HOUR_OF_DAY, 22);
		//calendar.set(Calendar.MINUTE, 30);

		// setRepeating() lets you specify a precise custom interval--in this case,
		// 20 minutes.
		int minutes = 120; // 2 hours
		minutes = 2; // TODO: Test
		long time_interval = 1000 * 60 * minutes;
		alarmMgr.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
				time_interval, alarmIntent);*/
		alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				1000 * 60 * 2, // Se disparará después de dos minutos
				1000 * 60 * 2, // Se repetirá a cada dos minutos
				               // Lo ideal es que se repita a cada dos horas, no tiene tanto sentido
							   // que esto sea muy frecuente
				alarmIntent);
	}
}
