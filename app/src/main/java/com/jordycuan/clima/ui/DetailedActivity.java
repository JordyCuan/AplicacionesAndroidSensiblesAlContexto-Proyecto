package com.jordycuan.clima.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jordycuan.clima.R;
import com.jordycuan.clima.Utils;
import com.jordycuan.clima.db.Element;

import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailedActivity extends AppCompatActivity {

	private Element element;

	@Bind(R.id.icono) ImageView icono;
	@Bind(R.id.description) TextView description;
	@Bind(R.id.temp) TextView temp;
	@Bind(R.id.pressure) TextView pressure;
	@Bind(R.id.humidity) TextView humidity;
	@Bind(R.id.min_temp) TextView min_temp;
	@Bind(R.id.max_temp) TextView max_temp;
	@Bind(R.id.speed) TextView speed;
	@Bind(R.id.sunrise) TextView sunrise;
	@Bind(R.id.sunset) TextView sunset;
	@Bind(R.id.lugar) TextView lugar;
	@Bind(R.id.date_string) TextView date_string;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed);
		ButterKnife.bind(this);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

		long pos = Element.getAll().size() - 1 - getIntent().getExtras().getLong("pos");
		this.element = Element.getElementById(pos);

		mapElementToLayout();
	}

	public void mapElementToLayout() {
		String s = element.description.toLowerCase();
		if (s.equals("clear sky")) {
			icono.setImageResource(R.drawable.sol);
		} else if (s.contains("rain")) {
			icono.setImageResource(R.drawable.lluvia);
		} else if (s.contains("cloud")) {
			icono.setImageResource(R.drawable.nube);
		}

		description.setText(element.description);
		temp.setText(element.temp + "ºC");
		pressure.setText(element.pressure + "");
		humidity.setText(element.humidity + "%");
		min_temp.setText(element.temp_min + "ºC");
		max_temp.setText(element.temp_max + "ºC");
		speed.setText(element.speed + "");
		sunrise.setText(Utils.getHourFromEpochDateSeconds(element.sunrise));
		sunset.setText(Utils.getHourFromEpochDateSeconds(element.sunset));
		lugar.setText(element.name + ", " + element.country);
		date_string.setText(element.dateString);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
