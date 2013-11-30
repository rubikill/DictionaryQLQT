package com.hcmus.dictionaryqlqt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecommendedAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
 
	public RecommendedAdapter(Context context, String[] values) {
		super(context, R.layout.item_more, values);
		this.context = context;
		this.values = values;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.item_more, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.item_more_text);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.item_more_icon);
		textView.setText(values[position]);
 
		// Change icon based on name
		String s = values[position];
 
		//System.out.println(s);
 
		if (s.equals("Merriam-Webster Dictionary - Premium")) {
			imageView.setImageResource(R.drawable.premium_icon);
		} else if (s.equals("Britannica Encyclopedia 2012")) {
			imageView.setImageResource(R.drawable.eb_2012_eng);
		} else if (s.equals("Enciclopedia Britannica 2012")) {
			imageView.setImageResource(R.drawable.eb_2012_spa);
		} else if (s.equals("Britannica 2011")) {
			imageView.setImageResource(R.drawable.eb_2011_rus);
		}
 
		return rowView;
	}
}