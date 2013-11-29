package com.hcmus.dictionaryqlqt;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<String>{

	Activity  context = null;
	ArrayList<String> myarray = null;
	int layoutId ;
	public MyArrayAdapter(Activity context, int layoutid,
			ArrayList<String> arr) {
		super(context, layoutid, arr);
		this.context = context;
		this.layoutId = layoutid;
		this.myarray = arr;
		// TODO Auto-generated constructor stub
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	
		LayoutInflater inflater = context.getLayoutInflater();
		convertView = inflater.inflate(layoutId, null);
		if(myarray.size() > 0 && position >= 0){
			final TextView txtdisplay = (TextView)convertView.findViewById(R.id.txtitem);
			String keyword = myarray.get(position);
			txtdisplay.setText(keyword);
		}
		return convertView;
	}

}
