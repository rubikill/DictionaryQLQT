package com.hcmus.dictionaryqlqt;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;

public class RecommendedActivity extends ListActivity{

	static final String[] TAB_MORE = 
            new String[] { "Merriam-Webster Dictionary - Premium", "Britannica Encyclopedia 2012",
						 "Enciclopedia Britannica 2012", "Britannica 2011"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_recommended);
		setListAdapter(new RecommendedAdapter(this, TAB_MORE));
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
