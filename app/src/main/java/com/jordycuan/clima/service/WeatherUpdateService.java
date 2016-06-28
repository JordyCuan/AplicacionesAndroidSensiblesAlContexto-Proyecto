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

	public WeatherUpdateService(String name) {
		super("WeatherUpdateService");
		//this.buildGoogleApiClient();
	}

	public WeatherUpdateService() {
		super("WeatherUpdateService");
		//this.buildGoogleApiClient();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("****** Servicio ******", "--- SERVICIO INICIADO ---");

		// TO DO: VERIFICAR ESTADO DEL WIFI
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
		//mGoogleApiClient.disconnect();
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


	/*********
	 * METODOS IMPLEMENTADOS
	 ********/
	/*  */
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.d("****** CONECTADO ******", "--- CONECTADO ---");
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
				) {
			return;
		}

		Log.d("****** PERMISO ******", "--- PERMISO ---");
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
		Log.d("****** FIN ******", "--- FIN - CONECTADO ---");
	}


	@Override
	public void onConnectionFailed(ConnectionResult result) {
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
		//mGoogleApiClient.connect();
	}


	@Override
	public void onLocationChanged(Location location) {
		mLastLocation = location;
		//no need to do a null check here:
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
			} catch (ClientProtocolException e) {
				e.printStackTrace();
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
				result.dateString = DateFormat.format("dd/MM/yyyy hh:mm:ss", result.date).toString();

				Log.d(TAG, "*** Saving: " + result);
				result.save();

				// TO DO: Generar la notificación?
				mostrarNotificacion(result);
				if (MainActivity.mAdapter != null) {
					MainActivity.mAdapter.notifyDataSetChanged();
				}
			}
			mGoogleApiClient.disconnect();
		}
	}


	public void mostrarNotificacion(final Element element) {
		// Intervenimos en el UI
		handler.post(new Runnable() {
			public void run() {

				String text = "Nuevo informe climático recibido";
				NotificationCompat.BigTextStyle textStyle = new NotificationCompat.BigTextStyle().bigText(text);

				NotificationCompat.Builder mBuilder =
						new NotificationCompat.Builder(getApplicationContext())
						.setSmallIcon(R.mipmap.ic_launcher)
						.setLargeIcon((((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap()))
								// TODO: Cambiar estos 2 despues
						.setContentTitle("Nuevo Informe")
						.setContentText("Nuevo informe climático recibido para la ciudad de "
										+ element.name + ", " + element.country)
						.setStyle(textStyle)
						.setAutoCancel(true)
						.setDefaults(Notification.DEFAULT_ALL)
						.setPriority(NotificationCompat.PRIORITY_HIGH)
						.setTicker("Informe climatico nuevo recibido");


				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

				// Indicamos el intent que se abrirá al pulsar la notificación
				PendingIntent contIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				mBuilder.setContentIntent(contIntent);

				NotificationManager mNotificationManager =
						(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

				// Mostramos la notificación
				mNotificationManager.notify(239847, mBuilder.build());
			}
		});
	}
}
