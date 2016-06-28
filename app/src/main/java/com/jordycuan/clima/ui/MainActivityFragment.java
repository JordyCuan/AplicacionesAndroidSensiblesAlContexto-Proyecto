package com.jordycuan.clima.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jordycuan.clima.R;
import com.jordycuan.clima.db.Element;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends ListFragment {

	protected static final String TAG = "_Old_MainActivity";

	public MainActivityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		MainActivity.mAdapter = new ArrayAdapter<String>(
				inflater.getContext(),
				R.layout.list_item,
				Element.getAllString());
		setListAdapter(MainActivity.mAdapter);

		//return inflater.inflate(R.layout.fragment_main, container, false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
