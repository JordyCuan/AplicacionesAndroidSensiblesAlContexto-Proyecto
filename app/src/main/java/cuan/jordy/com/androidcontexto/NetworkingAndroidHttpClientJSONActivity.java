package cuan.jordy.com.androidcontexto;

import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cuan.jordy.com.androidcontexto.json.JSONResponseHandler;


public class NetworkingAndroidHttpClientJSONActivity extends ListActivity implements
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
		LocationListener {

	GoogleApiClient mGoogleApiClient;

	public static Location mLastLocation;
	public double mLat, mLon;

	protected static final String TAG = "MainActivity";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.buildGoogleApiClient();
	}

	private class HttpGetTemperatureTask extends AsyncTask<Void, Void, List<String>> {

		private static final String API = "15008854a738af6f415cc6a1fa0b6dd5";
		private final String URL =
				"http://api.openweathermap.org/data/2.5/weather?lat="
						+ mLat + "&lon="
						+ mLon + "&appid="
						+ API;

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<String> doInBackground(Void... params) {
			Log.d("****** URL ******", "\n"+ URL);
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
		protected void onPostExecute(List<String> result) {
			if (null != mClient)
				mClient.close();
			setListAdapter(new ArrayAdapter<String>(
					NetworkingAndroidHttpClientJSONActivity.this,
					R.layout.list_item, result));
		}
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}

	/********* CICLO DE VIDA ********/
	/*  */
	protected void onStart() {
		super.onStart();

		mGoogleApiClient.connect();

//		Log.d("****** LatLog ******", "Lat: " + mLat + " Lon: " + mLon);
//		new HttpGetTemperatureTask().execute();
	}

	protected void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}


	/********* METODOS IMPLEMENTADOS ********/
	/*  */
	@Override
	public void onConnected(Bundle connectionHint) {

		Log.d("****** CONECTADO ******", "--- CONECTADO ---");
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				//&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
				) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			//return;
		}
			Log.d("****** PERMISO ******", "--- PERMISO ---");
			mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			if (mLastLocation != null) {
				mLat = mLastLocation.getLatitude();
				mLon = mLastLocation.getLongitude();

//				Log.d("****** LatLog ******", "Lat: " + mLat + " Lon: " + mLon);
//				new HttpGetTemperatureTask().execute();
			}


			LocationRequest mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(1000);
			mLocationRequest.setFastestInterval(1000);
			mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

			//mLocationRequest.setSmallestDisplacement(0.1F);
			//if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
				/*ActivityCompat.requestPermissions(
						this,
						new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
						0
				);*/
				//Log.d("****** Otro ******", "--- Otro ---");
				LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
			//}
		//}
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
		mGoogleApiClient.connect();
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
	}
}