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
 * Actitvity nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i
 * 
 */

public class SpeechRecognizerActivity extends Activity implements OnClickListener, RecognitionListener {

	/*
	 * enum caÌ�c traÌ£ng thaÌ�i
	 */
	enum State{
		/*
		 * khÆ¡Ì‰i taÌ£o nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i
		 */
		Preparing,
		
		/*
		 * Ä‘ang ghi Ã¢m gioÌ£ng noÌ�i
		 */		
		Recording,
		
		/*
		 * phÃ¢n tiÌ�ch gioÌ£ng noÌ�i
		 */
		Analyzing,
	}
	
	/*
	 * triÌ€nh nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i
	 */
	private SpeechRecognizer recognizer;
	
	/*
	 * textview traÌ£ng thaÌ�i
	 */
	private TextView tvTitle;
	
	/*
	 * progressBar Ã¢m lÆ°Æ¡Ì£ng
	 */
	private ProgressBar pgbVolume;
	
	/*
	 * button huÌ‰y 
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
	
	/*
	 * khoÌ‰i taÌ£o caÌ�c thaÌ€nh phÃ¢Ì€n giao diÃªÌ£n vaÌ€ triÌ€nh nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i
	 */
	private boolean initComponents(){
		tvTitle = (TextView) findViewById(R.id.activity_speech_recognizer_title);
		pgbVolume = (ProgressBar) findViewById(R.id.activity_speech_recognizer_progressbar);
		btCancel = (Button) findViewById(R.id.activity_activity_speech_recognizer_button_cancel);		

		if (!SpeechRecognizer.isRecognitionAvailable(getApplicationContext())){		
			return false;
		}
		
		recognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());		
		btCancel.setOnClickListener(this);
		recognizer.setRecognitionListener(this);	
		
		return true;
	}
	
	/*
	 * khÆ¡Ì‰i Ä‘Ã´Ì£ng nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i
	 */
	private void startSpeechRecognizer(){
		// set traÌ£ng thaÌ�i laÌ€ chuÃ¢Ì‰n biÌ£
		setState(State.Preparing);
		
		Intent localIntent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
	    localIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
	    localIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplication().getPackageName());
	    localIntent.putExtras(getIntent());
	    this.recognizer.startListening(localIntent);
	}	

	/*
	 * bÄƒÌ�t sÆ°Ì£ sÆ°Ì£ kiÃªÌ£n bÃ¢Ì�m huÌ‰y
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.activity_activity_speech_recognizer_button_cancel){
			cancel();
		}			
	}
	
	/*
	 * huÌ‰y vaÌ€ traÌ‰ vÃªÌ€ kÃªÌ�t quaÌ‰ RESULT_CANCELED
	 */
	private void cancel(){
		recognizer.cancel();
		setResult(RESULT_CANCELED);
		finish();
	}
	
	/*
	 * thiÃªÌ�t lÃ¢Ì£p traÌ£ng thaÌ�i
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
	
	private void showDialogNotAvailable(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);		 
        alertDialog.setTitle(R.string.speechrecognizer_title_dialog);
        alertDialog.setMessage(getString(R.string.speechrecognizer_not_available));
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	// khÆ¡Ì‰i Ä‘Ã´Ì£ng laÌ£i nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i
            	SpeechRecognizerActivity.this.setResult(RESULT_CANCELED);
            	SpeechRecognizerActivity.this.finish();
            } 
        });
        alertDialog.show();
	}

	/*
	 * hiÃªÌ‰n thiÌ£ dialog lÃ´Ìƒi
	 */
	private void showDialog(String msg){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);		 
        alertDialog.setTitle(R.string.speechrecognizer_title_dialog);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	// khÆ¡Ì‰i Ä‘Ã´Ì£ng laÌ£i nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i
            	startSpeechRecognizer();
            } 
        });
 
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	// huÌ‰y
            	cancel();
            }
        });
        alertDialog.show();
	}
	
	/*
	 * sÆ°Ì£ kiÃªÌ£n khi triÌ€nh nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i khoÌ‰i Ä‘Ã´Ì£ng xong
	 */
	@Override
	public void onReadyForSpeech(Bundle params) {
		// thay Ä‘Ã´Ì‰i traÌ£ng thaÌ�i laÌ€ ghi Ã¢m
		setState(State.Recording);
	}
	
	/*
	 * sÆ°Ì£ kiÃªÌ£n ngÆ°Æ¡Ì€i duÌ€ng ngÆ°ng noÌ�i
	 */
	@Override
	public void onEndOfSpeech() {
		// chuyÃªÌ‰n sang traÌ£ng thaÌ�i phÃ¢n tiÌ�ch gioÌ£ng noÌ�i
		setState(State.Analyzing);		
	}	
	
	/*
	 * sÆ°Ì£ kiÃªÌ£n coÌ� lÃ´Ìƒi xaÌ‰y ra
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
	
	/*
	 * sÆ°Ì£ kiÃªÌ£n khi coÌ� kÃªÌ�t quaÌ‰ nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i
	 */
	@Override
	public void onResults(Bundle results) {
		// lÃ¢Ì�y danh saÌ�ch text nhÃ¢Ì£n Ä‘Æ°Æ¡Ì£c
		ArrayList<String> listTexts = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		Intent resultIntent = new Intent();
		resultIntent.putExtra(RecognizerIntent.EXTRA_RESULTS, listTexts);
		// set traÌ£ng thaÌ�i OK vaÌ€ traÌ‰ vÃªÌ€ kÃªÌ�t quaÌ‰
		setResult(RESULT_OK, resultIntent);
		finish();				
	}
	
	/*
	 * sÆ°Ì£ kiÃªÌ£n khi Ã¢m lÆ°Æ¡Ì£ng thay Ä‘Ã´Ì‰i
	 */
	@Override
	public void onRmsChanged(float rmsdB) {
		// thiÃªÌ�t lÃ¢Ì£p traÌ£ng thaÌ�i Ã¢m lÆ°Æ¡Ì£ng lÃªn progressbar
		int value = (int)Math.max(0, 3 * rmsdB);
		pgbVolume.setProgress(value);
	}	
	
	/*
	 * HuÌ‰y taÌ€i nguyÃªn
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (recognizer != null){
			recognizer.destroy();
		}
	}

	/*
	 * CaÌ�c sÆ°Ì£ kiÃªÌ£n cuÌ‰a triÌ€nh nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i khÃ´ng xÆ°Ì‰ lyÌ� 
	 */
	@Override
	public void onBeginningOfSpeech() {}

	@Override
	public void onBufferReceived(byte[] buffer) {}

	@Override
	public void onEvent(int eventType, Bundle params) {}

	@Override
	public void onPartialResults(Bundle params) {}
	
}
