package com.jordycuan.clima.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.util.Log;
import com.jordycuan.clima.R;
import com.jordycuan.clima.db.Element;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends ListFragment implements OnItemClickListener {

	public MainActivityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		drawItems(inflater.getContext());
		MainActivity.mainActivityFragment = this;

		//return inflater.inflate(R.layout.fragment_main, container, false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void drawItems(Context context) {
		Log.d("*** DrawItems ***", "--- Se settea el adapter ---");
		MainActivity.mAdapter = new WeatherAdapter(context, Element.getAll());

		setListAdapter(MainActivity.mAdapter);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
/*
		Intent intent = new Intent(getActivity(), );
		Bundle b = new Bundle();
		b.putSerializable("reporte", reports.get(position));
		intent.putExtras(b);
		setSeenReport(reports.get(position), view);
		startActivity(intent);*/
	}
}
