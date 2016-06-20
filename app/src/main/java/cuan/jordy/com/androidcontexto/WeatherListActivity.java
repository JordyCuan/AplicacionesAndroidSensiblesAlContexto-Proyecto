package cuan.jordy.com.androidcontexto;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import cuan.jordy.com.androidcontexto.db.Element;


public class WeatherListActivity extends ListActivity {

	protected static final String TAG = "MainActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(
				WeatherListActivity.this,
				R.layout.list_item, Element.getAllString()));
	}
}