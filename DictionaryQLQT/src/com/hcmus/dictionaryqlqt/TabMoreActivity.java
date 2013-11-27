package com.hcmus.dictionaryqlqt;

import java.util.ArrayList;
import java.util.List;

import webservice.WebserviceHelperImpl;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

public class TabMoreActivity extends ListActivity implements OnClickListener {

	static final String[] TAB_MORE = new String[] { "Feedback",
			"Rate This App", "Share This App", "About Dictionary",
			"Recommended Apps", "Copyrights" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_tab_more);
		setListAdapter(new CustomArrayAdapter(this, TAB_MORE));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// get selected items
		String selectedValue = (String) getListAdapter().getItem(position);
		// Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
		if (selectedValue.equals("Feedback")) {
			WebserviceHelperImpl web = new WebserviceHelperImpl();
			web.feedbeak("asda", "adasdadad");
		} else if (selectedValue.equals("Rate This App")) {
			initRatePopup();
		} else if (selectedValue.equals("Share This App")) {
			Intent i = new Intent(this, ShareThisAppActivity.class);
			startActivity(i);
		} else if (selectedValue.equals("About Dictionary")) {
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
		} else if (selectedValue.equals("Recommended Apps")) {
			Intent i = new Intent(this, RecommendedActivity.class);
			startActivity(i);
		} else {
			Intent i = new Intent(this, CopyrightsActivity.class);
			startActivity(i);
		}

	}

	/***
	 * rate popup variable scores: list images 1->10
	 * currentScore rate hien tai tren may nguoi dung
	 */
	private PopupWindow ratePopup;
	private List<ImageView> scores;
	private Button rateOk;
	private Button rateClose;
	private int currentScore=0;
	/***
	 * Show rate popup
	 */
	private void initRatePopup() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) TabMoreActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.popup_rate,
				(ViewGroup) findViewById(R.id.rate_element));
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = (int) (size.x * 0.9);
		int height = (int) (size.y * 0.3);
		ratePopup = new PopupWindow(layout, width, height, true);
		ratePopup.showAtLocation(layout, Gravity.CENTER, 0, 0);
		// khoi tao button
		rateOk = (Button) layout.findViewById(R.id.btnRateOk);
		rateClose = (Button) layout.findViewById(R.id.btnRateCancel);
		rateOk.setOnClickListener(this);
		rateClose.setOnClickListener(this);
		//khoi tao image
		scores = new ArrayList<ImageView>();
		ImageView img1 = (ImageView)layout.findViewById(R.id.score1);
		ImageView img2 = (ImageView)layout.findViewById(R.id.score2);
		ImageView img3 = (ImageView)layout.findViewById(R.id.score3);
		ImageView img4 = (ImageView)layout.findViewById(R.id.score4);
		ImageView img5 = (ImageView)layout.findViewById(R.id.score5);
		scores.add(img1);
		scores.add(img2);
		scores.add(img3);
		scores.add(img4);
		scores.add(img5);
		for (ImageView score : scores) {
			score.setOnClickListener(this);
		}
		changeScore(currentScore);
	}
	void  changeScore(int score)
	{
		currentScore = score;
		for(int i=0;i<scores.size();i++)
		{
			if(i+1 <= score)
				scores.get(i).setImageResource(R.drawable.one);
			else
				scores.get(i).setImageResource(R.drawable.zero);
		}
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btnRateOk:

			break;
		case R.id.btnRateCancel:
			ratePopup.dismiss();
			break;
		case R.id.score1:
			changeScore(1);
			break;
		case R.id.score2:
			changeScore(2);
			break;
		case R.id.score3:
			changeScore(3);
			break;
		case R.id.score4:
			changeScore(4);
			break;
		case R.id.score5:
			changeScore(5);
			break;
		default:
			break;
		}
		
	}
	
}
