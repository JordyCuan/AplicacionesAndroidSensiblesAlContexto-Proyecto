package cuan.jordy.com.androidcontexto;

import android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.activeandroid.ActiveAndroid;

import cuan.jordy.com.androidcontexto.receiver.BootReceiver;

public class MainActivity extends Activity {

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
		final Button loadButton = (Button) findViewById(R.id.button1);
		loadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						NetworkingAndroidHttpClientJSONActivity.class));
			}
		});
	}
}