package com.jordycuan.clima.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.activeandroid.ActiveAndroid;

import com.jordycuan.clima.R;
import com.jordycuan.clima.receiver.BootReceiver;

public class MainActivity extends AppCompatActivity {

	public static ArrayAdapter mAdapter;
	public static MainActivityFragment mainActivityFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActiveAndroid.initialize(this);

		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
				) {
			ActivityCompat.requestPermissions(
					this,
					new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
					0
			);
		}

		new BootReceiver().setAlarm(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (this.mAdapter != null)
			this.mAdapter.notifyDataSetChanged();
		if (this.mainActivityFragment != null)
			this.mainActivityFragment.drawItems(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
