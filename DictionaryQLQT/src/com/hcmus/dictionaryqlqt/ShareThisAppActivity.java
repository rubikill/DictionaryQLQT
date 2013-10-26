package com.hcmus.dictionaryqlqt;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ShareThisAppActivity extends ListActivity {

	static final String[] TAB_MORE = 
            new String[] { "Email", "Facebook", "Twitter"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_tab_more);
		setListAdapter(new ShareThisAppAdapter(this, TAB_MORE));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		//get selected items
		String selectedValue = (String) getListAdapter().getItem(position);
		Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

	}

}
