package com.hcmus.dictionaryqlqt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
 
	public MoreArrayAdapter(Context context, String[] values) {
		super(context, R.layout.item_more, values);
		this.context = context;
		this.values = values;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		if (view == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.item_more, null);
        }
		
		ImageView icon = (ImageView) view.findViewById(R.id.item_more_icon);
		TextView text = (TextView) view.findViewById(R.id.item_more_text);	
		
		String s = values[position];
		text.setText(s);
 
		if (s.equals("Feedback")) {
			icon.setImageResource(R.drawable.feedback);
		} else if (s.equals("Rate This App")) {
			icon.setImageResource(R.drawable.rate_this_app);
		} else if (s.equals("Share This App")) {
			icon.setImageResource(R.drawable.share_this_app);
		} else if (s.equals("About Dictionary")) {
			icon.setImageResource(R.drawable.about);
		} else if (s.equals("Recommended Apps")) {
			icon.setImageResource(R.drawable.recommend);
		} else if (s.equals("Copyrights")){
			icon.setImageResource(R.drawable.copyright);
		}
 
		return view;
	}
}