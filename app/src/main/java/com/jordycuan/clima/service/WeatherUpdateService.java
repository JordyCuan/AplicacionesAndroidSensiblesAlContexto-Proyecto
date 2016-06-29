package com.jordycuan.clima.service;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.jordycuan.clima.R;
import com.jordycuan.clima.db.Element;
import com.jordycuan.clima.json.JSONResponseHandler;
import com.jordycuan.clima.ui.MainActivity;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.Date;


/**
 * Servicio utilizado por la aplicación para conseguir la ubicación actual del
 * dispositivo móvil. Cuando la ubicación es obtenida (mediante un proceso
 * a sincrono de los procesos de google), esta se envía al API con el servicio
 * de clima y esperamos la respuesta (Usamos un AsynckTask). Cuando la respuesta
 * del servicio web es obtenida, se almacena en la base de datos
 */
public class WeatherUpdateService extends IntentService implements
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
		LocationListener {

	GoogleApiClient mGoogleApiClient;

	public static Location mLastLocation;
	public double mLat, mLon;
	private boolean conectado = false;
	private Handler handler;

	protected static final String TAG = "*** WeatherUpdateServi";
	protected static final String NAME = "WeatherUpdateService";

	public WeatherUpdateService(String name) {
		super(NAME);
	}

	public WeatherUpdateService() {
		super(NAME);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("****** Servicio ******", "--- SERVICIO INICIADO ---");

		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isConnected() || mobile.isConnected()) {
			Log.d("****** Esta Conectado", "--- Hay red movil --- " + wifi.isConnected() + " " + mobile.isConnected());
			this.conectado = true;
			this.buildGoogleApiClient();
			handler = new Handler();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (this.conectado) {
			if (!mGoogleApiClient.isConnected()) {
				Log.d("****** onHandleIntent", "--- Se inicia la conexión de la localización ---");
				mGoogleApiClient.connect();
			}
		}
	}

	private synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		if (!mGoogleApiClient.isConnected())
			mGoogleApiClient.connect();
	}


	@Override
	public void onConnected(Bundle connectionHint) {
		Log.d("****** CONECTADO ******", "--- CONECTADO ---");
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
				) {
			return;
		}

		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (mLastLocation != null) {
			mLat = mLastLocation.getLatitude();
			mLon = mLastLocation.getLongitude();
		}

		LocationRequest mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(1000);
		mLocationRequest.setFastestInterval(1000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
	}


	@Override
	public void onConnectionFailed(@NonNull ConnectionResult result) {
		// Refer to the javadoc for ConnectionResult to see what error codes might be returned in
		// onConnectionFailed.
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorMessage() = " + result.getErrorMessage());
	}


	@Override
	public void onConnectionSuspended(int cause) {
		// The connection to Google Play services was lost for some reason. We call connect() to
		// attempt to re-establish the connection.
		Log.i(TAG, "Connection suspended");
	}


	@Override
	public void onLocationChanged(Location location) {
		mLastLocation = location;

		mLat = location.getLatitude();
		mLon = location.getLongitude();

		//remove location updates if you just need one location:
		if (mGoogleApiClient != null) {
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		}
		Log.d("****** LatLog ******", "Lat: " + mLat + " Lon: " + mLon);
		new HttpGetTemperatureTask().execute();
		Log.d("****** AfterTask ******", "Task Launched");
	}


	/********************************************/
	/**/
	private class HttpGetTemperatureTask extends AsyncTask<Void, Void, Element> {

		private static final String API = "15008854a738af6f415cc6a1fa0b6dd5";
		private final String URL =
				"http://api.openweathermap.org/data/2.5/weather?lat="
						+ mLat + "&lon="
						+ mLon + "&appid="
						+ API;

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected Element doInBackground(Void... params) {
			Log.d("****** URL ******", "\n" + URL);
			HttpGet request = new HttpGet(URL);
			JSONResponseHandler responseHandler = new JSONResponseHandler();
			try {
				return mClient.execute(request, responseHandler);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Element result) {
			Log.d(TAG, "onPostExecute");
			if (null != mClient)
				mClient.close();

			if (result != null) {
				Date d = new Date();
				result.date = d.getTime();
				result.dateString = DateFormat.format("dd/MM/yyyy hh:mm", result.date).toString();

				Log.d(TAG, "*** Saving: " + result);
				//result._id = Element.getAll().size();
				result.save();
				Log.d(TAG, "*** DB Size: " + Element.getAll().size());

				//if ( ! MainActivity.activa ) // If the Activity is not active, the notification is show
				mostrarNotificacion(result);

				if (MainActivity.mainActivityFragment != null) {
					MainActivity.mainActivityFragment.drawItems(getApplicationContext());
				}
			}
			mGoogleApiClient.disconnect();
		}
	}


	public void mostrarNotificacion(final Element element) {
		// UI Intervention
		handler.post(new Runnable() {
			public void run() {

				String text = getString(R.string.notif_text);
				NotificationCompat.BigTextStyle textStyle = new NotificationCompat.BigTextStyle().bigText(text);

				NotificationCompat.Builder mBuilder =
						new NotificationCompat.Builder(getApplicationContext())
						.setSmallIcon(R.mipmap.ic_launcher)
						.setLargeIcon((((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap()))
						.setContentTitle(getString(R.string.notif_title))
						.setContentText(getString(R.string.notif_cont_text) + element.name + ", " + element.country)
						.setStyle(textStyle)
						.setAutoCancel(true)
						.setDefaults(Notification.DEFAULT_ALL)
						.setPriority(NotificationCompat.PRIORITY_HIGH)
						.setTicker(getString(R.string.notif_ticker));


				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

				// Specify which intent is going to be open when the notification is clicked
				PendingIntent contIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				mBuilder.setContentIntent(contIntent);

				NotificationManager mNotificationManager =
						(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

				// Show the notification
				mNotificationManager.notify(MainActivity.ID_NOTIFICATION, mBuilder.build());
			}
		});
	}
}
