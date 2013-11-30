package com.hcmus.dictionaryqlqt;

import java.util.ArrayList;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * 
 * @author Minh Khanh
 *
 * Actitvity nhan dien giong noi
 * 
 */

public class SpeechRecognizerActivity extends Activity implements OnClickListener, RecognitionListener {

	/*
	 * enum trang thai
	 */
	enum State{
		/*
		 * trang thai dang khoi tao bo nhan dien
		 */
		Preparing,
		
		/*
		 * trang thai san sang
		 */		
		Recording,
		
		/*
		 * trang thai phan tich gion noi
		 */
		Analyzing,
	}
	
	/*
	 * bo nhan dien giong noi
	 */
	private SpeechRecognizer recognizer;
	
	/*
	 * textview the hien trang thai
	 */
	private TextView tvTitle;
	
	/*
	 * progressBar hien thi am luong
	 */
	private ProgressBar pgbVolume;
	
	/*
	 * button huy
	 */
	private Button btCancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(R.color.bg_dialog);
		setContentView(R.layout.activity_speech_recognizer);
		
		if (initComponents()){
			startSpeechRecognizer();
		}
		else{
			showDialogNotAvailable();
		}
	}
	
	/**
	 * khoi tao cac thanh phan
	 */
	private boolean initComponents(){
		tvTitle = (TextView) findViewById(R.id.activity_speech_recognizer_title);
		pgbVolume = (ProgressBar) findViewById(R.id.activity_speech_recognizer_progressbar);
		btCancel = (Button) findViewById(R.id.activity_activity_speech_recognizer_button_cancel);		

		// kiem tra bo nhan dien giong noi tren thiet bi
		if (!SpeechRecognizer.isRecognitionAvailable(getApplicationContext())){		
			return false;
		}
		
		recognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());		
		btCancel.setOnClickListener(this);
		recognizer.setRecognitionListener(this);	
		
		return true;
	}
	
	/**
	 * khoi dong bo nhan dien giong noi
	 */
	private void startSpeechRecognizer(){
		// thiet lap trang thai chuan bi
		setState(State.Preparing);
		
		Intent localIntent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
	    localIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
	    localIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplication().getPackageName());
	    localIntent.putExtras(getIntent());
	    this.recognizer.startListening(localIntent);
	}	

	/**
	 * lang nghe su kien nhan huy
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.activity_activity_speech_recognizer_button_cancel){
			cancel();
		}			
	}
	
	/**
	 * xu ly su kien huy
	 */
	private void cancel(){
		recognizer.cancel();
		setResult(RESULT_CANCELED);
		finish();
	}
	
	/**
	 * thiet lap trang thai nhan dien giong noi
	 */
	private void setState(State state){
		switch (state) {		
		case Preparing:
			tvTitle.setText(R.string.preparing_status);
			pgbVolume.setIndeterminate(true);
			break;
		case Recording:
			tvTitle.setText(R.string.recording_status);
			pgbVolume.setIndeterminate(false);
			break;
		case Analyzing:
			tvTitle.setText(R.string.analyzing_status);
			pgbVolume.setIndeterminate(true);
			break;
		}
	}
	
	/**
	 * thong bao bo nhan dien giong noi khong duoc ho tro
	 */
	private void showDialogNotAvailable(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);		 
        alertDialog.setTitle(R.string.speechrecognizer_title_dialog);
        alertDialog.setMessage(getString(R.string.speechrecognizer_not_available));
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	SpeechRecognizerActivity.this.setResult(RESULT_CANCELED);
            	SpeechRecognizerActivity.this.finish();
            } 
        });
        alertDialog.show();
	}

	/**
	 * hien thi loi khi nhan dien giong noi
	 */
	private void showDialog(String msg){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);		 
        alertDialog.setTitle(R.string.speechrecognizer_title_dialog);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	startSpeechRecognizer();
            } 
        });
 
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	cancel();
            }
        });
        alertDialog.show();
	}
	
	/**
	 * lang nghe su kien bo nhan dien giong noi san sang
	 */
	@Override
	public void onReadyForSpeech(Bundle params) {
		setState(State.Recording);
	}
	
	/**
	 * su kien ket thuc noi
	 */
	@Override
	public void onEndOfSpeech() {
		setState(State.Analyzing);		
	}	
	
	/**
	 * su kien loi khi nhan dien giong noi
	 */
	@Override
	public void onError(int error) {
		String msg = "";
		switch (error) {
		case SpeechRecognizer.ERROR_NETWORK:
		case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
			msg = getString(R.string.speechrecognizer_network_error);
			break;
		default:
			msg = getString(R.string.speechrecognizer_understand_error);
			break;
		}
		showDialog(msg);
	}
	
	/**
	 * su kien phan tich giong noi hoan tat
	 */
	@Override
	public void onResults(Bundle results) {
		ArrayList<String> listTexts = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		Intent resultIntent = new Intent();
		resultIntent.putExtra(RecognizerIntent.EXTRA_RESULTS, listTexts);
		setResult(RESULT_OK, resultIntent);
		finish();				
	}
	
	/**
	 * su kien am luong thay doi
	 */
	@Override
	public void onRmsChanged(float rmsdB) {
		int value = (int)Math.max(0, 3 * rmsdB);
		pgbVolume.setProgress(value);
	}	
	
	/**
	 * giai phong tai nguyen khi dong man hinh 
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (recognizer != null){
			recognizer.destroy();
		}
	}

	
	@Override
	public void onBeginningOfSpeech() {}

	@Override
	public void onBufferReceived(byte[] buffer) {}

	@Override
	public void onEvent(int eventType, Bundle params) {}

	@Override
	public void onPartialResults(Bundle params) {}
	
}
