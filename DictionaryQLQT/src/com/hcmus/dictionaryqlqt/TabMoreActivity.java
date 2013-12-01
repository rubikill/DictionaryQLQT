package com.hcmus.dictionaryqlqt;

import java.util.ArrayList;
import java.util.List;

import webservice.WebserviceHelperImpl;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class TabMoreActivity extends ListActivity implements OnClickListener {

	static final String[] TAB_MORE = new String[] { "Feedback",
			"Rate This App", "Share This App", "About Dictionary",
			"Recommended Apps", "Copyrights" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new MoreArrayAdapter(this, TAB_MORE));
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// get selected items
		String selectedValue = (String) getListAdapter().getItem(position);
		// Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
		if (selectedValue.equals("Feedback")) {
			initFeedbackPopup();
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
	 * Feedback popup
	 */
	private PopupWindow feedbackPopup;
	private Button feedbackSend;
	private Button feedbackClose;
	private EditText feedbackUsername;
	private EditText feedbackMessage;
	private void initFeedbackPopup() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) TabMoreActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.popup_feedback,
				(ViewGroup) findViewById(R.id.feedback_element));
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = (int) (size.x * 0.8);
		int height = (int) (size.y * 0.6);
		feedbackPopup = new PopupWindow(layout, width, height, true);
		feedbackPopup.showAtLocation(layout, Gravity.CENTER, 0, 0);
		//khoi tao controll
		feedbackSend = (Button)layout.findViewById(R.id.btn_feedback_send);
		feedbackClose = (Button)layout.findViewById(R.id.btn_feedback_close);
		feedbackUsername = (EditText)layout.findViewById(R.id.feedback_username);
		feedbackMessage= (EditText)layout.findViewById(R.id.feedback_message);
		//khai bao su kien
		feedbackSend.setOnClickListener(this);
		feedbackClose.setOnClickListener(this);
	}
	/***
	 * Gui feedback
	 */
	private void sendFeedback() {
		// TODO Auto-generated method stub
		String username = feedbackUsername.getText().toString();
		String message = feedbackMessage.getText().toString();
		
		WebserviceHelperImpl web = new WebserviceHelperImpl();
		String result;
		if(web.feedbeak(username, message))
		{
			result ="Thanks for your feedback!";
		}
		else
		{
			result ="Error when send feedback. Please check your network connection!";
		}
		AlertDialog ad=new AlertDialog.Builder(this)
        .setMessage(result)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() 
        {                   
            @Override
            public void onClick(DialogInterface arg0, int arg1) 
            {
                feedbackPopup.dismiss();
            }//end onClick()
        }).create();     
		ad.show();
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
		//rate
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
		//feedback
		case R.id.btn_feedback_send:
			sendFeedback();
			break;
		case R.id.btn_feedback_close:
			feedbackPopup.dismiss();
			break;
		default:
			break;
		}
		
	}

	
	
}
