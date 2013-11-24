package com.hcmus.dictionaryqlqt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CopyrightsActivity extends Activity{

	TextView tvCopyrights;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_copyrights);
		
		tvCopyrights = (TextView)findViewById(R.id.tvCopyrights);
		tvCopyrights.setText("Developed by 2013 QLQTPM-TH10-N2 - University of Science - Ho Chi Minh City");
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
