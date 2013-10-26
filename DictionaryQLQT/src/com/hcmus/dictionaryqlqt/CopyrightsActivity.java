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
		tvCopyrights.setText("All content of this Application is 2012 Merriam-Webster, Incorporated. Some of" +
				"the software in this Application is provided bhy licensors. Merriam-Webster claims no compilation" +
				"copyright in materials that are subject to open source licenses. As required, MW makes the following open" +
				"source licenses disclosures:");
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
