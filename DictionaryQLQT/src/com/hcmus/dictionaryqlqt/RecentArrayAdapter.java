package com.hcmus.dictionaryqlqt;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class RecentArrayAdapter extends ArrayAdapter<String>{

	Activity  context = null;
	ArrayList<String> items = null;
	int layoutId ;
	boolean isEdit = false;
	
	public RecentArrayAdapter(Activity context, int layoutid,
			ArrayList<String> arr, boolean isEdit) {
		super(context, layoutid, arr);
		this.context = context;
		this.layoutId = layoutid;
		this.items = arr;
		this.isEdit = isEdit;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	
		LayoutInflater inflater = context.getLayoutInflater();
		convertView = inflater.inflate(layoutId, null);
		if(items.size() > 0 && position >= 0){
			TextView txtdisplay = (TextView)convertView.findViewById(R.id.recent_item_text);
			CheckBox chkbox = (CheckBox)convertView.findViewById(R.id.recent_item_chkbox);
			
			String keyword = items.get(position);
			txtdisplay.setText(keyword);
			int state = isEdit ? View.VISIBLE : View.INVISIBLE;
			chkbox.setVisibility(state);
		}
		return convertView;
	}

}
