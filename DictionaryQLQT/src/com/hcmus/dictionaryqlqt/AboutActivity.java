package com.hcmus.dictionaryqlqt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity{

	TextView tvAbout, tvAbout1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		tvAbout = (TextView)findViewById(R.id.tvAbout);
		tvAbout1 = (TextView)findViewById(R.id.tvAbout1);
		tvAbout.setText("For more than 150 years, Merriam-Webster has been America's leading and most" +
				"trusted provider of language information." +
				"\n\nIn print, online, and in mobile products like this, we're proud to offer guidance to millions of people every day.");
		
		tvAbout1.setText("All content in this app is 2012 Merriam-Webster, Inc");
	}
	
}
