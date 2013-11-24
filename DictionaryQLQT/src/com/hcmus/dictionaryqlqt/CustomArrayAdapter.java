package com.hcmus.dictionaryqlqt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
 
	public CustomArrayAdapter(Context context, String[] values) {
		super(context, R.layout.activity_tab_more, values);
		this.context = context;
		this.values = values;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.activity_tab_more, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.tvLabel);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imgViewLogo);
		textView.setText(values[position]);
 
		// Change icon based on name
		String s = values[position];
 
		if (s.equals("Feedback")) {
			imageView.setImageResource(R.drawable.feedback);
		} else if (s.equals("Rate This App")) {
			imageView.setImageResource(R.drawable.rate_this_app);
		} else if (s.equals("Share This App")) {
			imageView.setImageResource(R.drawable.share_this_app);
		} else if (s.equals("About Dictionary")) {
			imageView.setImageResource(R.drawable.about);
		} else if (s.equals("Recommended Apps")) {
			imageView.setImageResource(R.drawable.recommend);
		} else if (s.equals("Copyrights")){
			imageView.setImageResource(R.drawable.copyright);
		}
 
		return rowView;
	}
}