package com.hcmus.dictionaryqlqt;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SpeechRecognizerDialog extends Dialog implements RecognitionListener, android.view.View.OnClickListener {

	enum State{
		Preparing,
		Recording,
		Analyzing,
	}
	
	private SpeechRecognizer recognizer;
	private ImageView btnDSCancel;
	private State state;
	private TabDictionaryActivity parent;
	
	public SpeechRecognizerDialog(Context context) {
		super(context);
		parent = (TabDictionaryActivity)context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_speech_recognizer);
		
		btnDSCancel = (ImageView)findViewById(R.id.btnDSCancel);
		btnDSCancel.setOnClickListener(this);
		//initComponents();
	}
	
	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (recognizer != null){			
			recognizer.destroy();
		}
	}
	
	private void initComponents(){
		setTitle("Preparing...");
		recognizer = SpeechRecognizer.createSpeechRecognizer(FullscreenActivity.getInstance());
		
		Intent localIntent = new Intent("android.speech.action.WEB_SEARCH");
	    localIntent.putExtra("android.speech.extra.LANGUAGE", "en-US");
	    localIntent.putExtra("calling_package", FullscreenActivity.getInstance().getApplication().getPackageName());
	    localIntent.putExtras(FullscreenActivity.getInstance().getIntent());
		recognizer.setRecognitionListener(this);
		recognizer.startListening(localIntent);		
	}

	@Override
	public void onBeginningOfSpeech() {
		
	}

	@Override
	public void onBufferReceived(byte[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEndOfSpeech() {
		setTitle("Analyzing...");
		//this.pgbVolume.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onError(int error) {
		String strError = "";
		switch (error) {
		case SpeechRecognizer.ERROR_AUDIO:
			strError = "Error audio";
			break;
		case SpeechRecognizer.ERROR_NETWORK:
			strError = "Error network";
			break;
		case SpeechRecognizer.ERROR_SERVER:
			strError = "Error server";
			break;
		case SpeechRecognizer.ERROR_NO_MATCH:
			strError = "Don't understand, try again!";
			break;
		default:
			strError = "Error";
			break;
		}
	}

	@Override
	public void onEvent(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPartialResults(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReadyForSpeech(Bundle arg0) {
		setTitle("Recording...");		
		//this.pgbVolume.setVisibility(View.VISIBLE);
	}

	@Override
	public void onResults(Bundle result) {
		parent.onResultFromSR(result.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
		this.dismiss();
	}

	@Override
	public void onRmsChanged(float arg0) {
		//pgbVolume.setProgress((int)arg0 * 3);
	}
	
	private void changeState(State newState){
		LayoutParams params = new LayoutParams(0, 0, 0f);
		if (newState == State.Analyzing || newState == State.Preparing){
			//pgbVolume.setLayoutParams(params);			
		} else if (newState == State.Recording){
		}	
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnDSCancel:
			
			break;
		}
	}
}
