package activity;

import com.hcmus.dictionaryqlqt.R;

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
		tvAbout.setText("This dictionary can help you to improve your vocabularies.");
		tvAbout1.setText("2013 QLQTPM-TH10-N2");
	}
	
}
