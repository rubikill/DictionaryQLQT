package com.hcmus.dictionaryqlqt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareThisAppAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
 
	public ShareThisAppAdapter(Context context, String[] values) {
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
 
		if (s.equals("Email")) {
			imageView.setImageResource(R.drawable.e_mail);
		} else if (s.equals("Facebook")) {
			imageView.setImageResource(R.drawable.facebook);
		} else if (s.equals("Twitter")){
			imageView.setImageResource(R.drawable.twitter);
		}
 
		return rowView;
	}
}