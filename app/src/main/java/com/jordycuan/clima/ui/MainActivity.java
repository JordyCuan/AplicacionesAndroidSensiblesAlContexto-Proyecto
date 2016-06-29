package com.jordycuan.clima.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.activeandroid.ActiveAndroid;
import com.jordycuan.clima.R;
import com.jordycuan.clima.receiver.BootReceiver;

public class MainActivity extends AppCompatActivity {

	public static BaseAdapter mAdapter;
	public static MainActivityFragment mainActivityFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActiveAndroid.initialize(this);
		setContentView(R.layout.activity_main);

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
	protected void onResume() {
		super.onResume();
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();
		if (mainActivityFragment != null)
			mainActivityFragment.drawItems(this);
	}
}
