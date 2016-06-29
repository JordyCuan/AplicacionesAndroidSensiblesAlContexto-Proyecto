package com.jordycuan.clima.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.jordycuan.clima.db.Element;

public class MainActivityFragment extends ListFragment implements OnItemClickListener {

	public MainActivityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		drawItems(inflater.getContext());
		MainActivity.mainActivityFragment = this;

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setOnItemClickListener(this);
	}

	public void drawItems(Context context) {
		Log.d("*** DrawItems ***", "--- Se settea el adapter ---");
		MainActivity.mAdapter = new WeatherAdapter(context, Element.getAll());

		setListAdapter(MainActivity.mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
//		Intent intent = new Intent(getActivity(), DetailedActivity.class);
//		Bundle b = new Bundle();
//		b.putLong("pos", pos);
//		intent.putExtras(b);
//		startActivity(intent);
	}
}
