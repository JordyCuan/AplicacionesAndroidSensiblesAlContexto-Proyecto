package com.jordycuan.clima.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jordycuan.clima.R;
import com.jordycuan.clima.db.Element;

public class DetailedActivity extends AppCompatActivity {

	private Element element;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

		this.element = Element.getElementById(getIntent().getExtras().getLong("pos"));

		Toast.makeText(this,"Element: " + element.toString(), Toast.LENGTH_LONG).show();

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
