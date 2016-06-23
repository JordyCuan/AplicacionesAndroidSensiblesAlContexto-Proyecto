package cuan.jordy.com.androidcontexto;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import cuan.jordy.com.androidcontexto.db.Element;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends ListFragment {

	protected static final String TAG = "MainActivity";

	public MainActivityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		setListAdapter(new ArrayAdapter<String>(
				inflater.getContext(),
				R.layout.list_item,
				Element.getAllString()));

		//return inflater.inflate(R.layout.fragment_main, container, false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
