package com.jordycuan.clima.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.util.Log;
import com.jordycuan.clima.R;
import com.jordycuan.clima.Utils;
import com.jordycuan.clima.db.Element;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JordyCuan on 29/06/16.
 */
public class WeatherAdapter extends BaseAdapter {
	private List<Element> elements;
	private LayoutInflater inflater = null;
	private final String TAG = "*** ADAPTER ***";

	/**
	 * Constructor de TataAdapter para la ListView
	 *
	 * @param context Contexto de la aplicación a tomar los parametros
	 * @param elements Lista de los elements a ser mostrados
	 */
	public WeatherAdapter(Context context, List<Element> elements) {
		this.elements = elements;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		Log.d(TAG, "--- getCount ---");
		return elements.size();
	}

	@Override
	public Element getItem(int position) {
		Log.d(TAG, "--- getItem ---");
		return elements.get(position);
	}

	@Override
	public long getItemId(int position) {
		Log.d(TAG, "--- getItemId ---");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "--- getView ---");
		View view;
		ViewHolder holder;

		// Verify if the view item exists
		if(convertView == null) {
			view = inflater.inflate(R.layout.list_item, parent, false);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		// Generate the report depending on the position
		Element element = elements.get(position);


		holder.temp.setText(((int)element.temp) + "ºC");
		holder.fecha.setText(element.dateString);
		holder.contenido.setText( element.name + ", " + element.country );

		String s = element.description.toLowerCase();
		if (s.equals("clear sky")) {
			holder.icono.setImageResource(R.drawable.sol);
		} else if (s.contains("rain")) {
			holder.icono.setImageResource(R.drawable.lluvia);
		} else if (s.contains("cloud")) {
			holder.icono.setImageResource(R.drawable.nube);
		}

		return view;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	static class ViewHolder {
		@Bind(R.id.temp) TextView temp;
		@Bind(R.id.tv_fecha) TextView fecha;
		@Bind(R.id.tv_contenido) TextView contenido;
		@Bind(R.id.icon) ImageView icono;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
