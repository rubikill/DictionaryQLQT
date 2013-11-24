package com.hcmus.dictionaryqlqt;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

public class TabMoreActivity extends ListActivity {

	static final String[] TAB_MORE = 
            new String[] { "Feedback", "Rate This App", "Share This App", "About Dictionary"
						, "Recommended Apps", "Copyrights"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_tab_more);
		setListAdapter(new CustomArrayAdapter(this, TAB_MORE));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		//get selected items
		String selectedValue = (String) getListAdapter().getItem(position);
		//Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
		if (selectedValue.equals("Feedback")){
			
		} else if (selectedValue.equals("Rate This App")){
			
		} else if (selectedValue.equals("Share This App")){
			Intent i = new Intent(this, ShareThisAppActivity.class);
			startActivity(i);
		} else if (selectedValue.equals("About Dictionary")){
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
		} else if (selectedValue.equals("Recommended Apps")){
			Intent i = new Intent(this, RecommendedActivity.class);
			startActivity(i);
		} else {
			Intent i = new Intent(this, CopyrightsActivity.class);
			startActivity(i);
		}

	}

}
