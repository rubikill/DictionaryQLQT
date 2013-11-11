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
 * Actitvity nhận diện giọng nói
 * 
 */

public class SpeechRecognizerActivity extends Activity implements OnClickListener, RecognitionListener {

	/*
	 * enum các trạng thái
	 */
	enum State{
		/*
		 * khởi tạo nhận diện giọng nói
		 */
		Preparing,
		
		/*
		 * đang ghi âm giọng nói
		 */		
		Recording,
		
		/*
		 * phân tích giọng nói
		 */
		Analyzing,
	}
	
	/*
	 * trình nhận diện giọng nói
	 */
	private SpeechRecognizer recognizer;
	
	/*
	 * textview trạng thái
	 */
	private TextView tvTitle;
	
	/*
	 * progressBar âm lượng
	 */
	private ProgressBar pgbVolume;
	
	/*
	 * button hủy 
	 */
	private Button btCancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(R.color.bg_dialog);
		setContentView(R.layout.activity_speech_recognizer);
		
		initComponents();
		startSpeechRecognizer();
	}
	
	/*
	 * khỏi tạo các thành phần giao diện và trình nhận diện giọng nói
	 */
	private void initComponents(){
		tvTitle = (TextView) findViewById(R.id.activity_speech_recognizer_title);
		pgbVolume = (ProgressBar) findViewById(R.id.activity_speech_recognizer_progressbar);
		btCancel = (Button) findViewById(R.id.activity_activity_speech_recognizer_button_cancel);
		recognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
		
		btCancel.setOnClickListener(this);
		recognizer.setRecognitionListener(this);	
	}
	
	/*
	 * khởi động nhận diện giọng nói
	 */
	private void startSpeechRecognizer(){
		// set trạng thái là chuẩn bị
		setState(State.Preparing);
		
		Intent localIntent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
	    localIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
	    localIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplication().getPackageName());
	    localIntent.putExtras(getIntent());
	    this.recognizer.startListening(localIntent);
	}	

	/*
	 * bắt sự sự kiện bấm hủy
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.activity_activity_speech_recognizer_button_cancel){
			cancel();
		}			
	}
	
	/*
	 * hủy và trả về kết quả RESULT_CANCELED
	 */
	private void cancel(){
		recognizer.cancel();
		setResult(RESULT_CANCELED);
		finish();
	}
	
	/*
	 * thiết lập trạng thái
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
	
	/*
	 * hiển thị dialog lỗi
	 */
	private void showDialog(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);		 
        alertDialog.setTitle("Error");
        alertDialog.setMessage("da co loi xay ra");
        alertDialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	// khởi động lại nhận diện giọng nói
            	startSpeechRecognizer();
            } 
        });
 
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	// hủy
            	cancel();
            }
        });
        alertDialog.show();
	}
	
	/*
	 * sự kiện khi trình nhận diện giọng nói khỏi động xong
	 */
	@Override
	public void onReadyForSpeech(Bundle params) {
		// thay đổi trạng thái là ghi âm
		setState(State.Recording);
	}
	
	/*
	 * sự kiện người dùng ngưng nói
	 */
	@Override
	public void onEndOfSpeech() {
		// chuyển sang trạng thái phân tích giọng nói
		setState(State.Analyzing);		
	}	
	
	/*
	 * sự kiện có lỗi xảy ra
	 */
	@Override
	public void onError(int error) {
		// hiển thị thông báo
		showDialog();
	}
	
	/*
	 * sự kiện khi có kết quả nhận diện giọng nói
	 */
	@Override
	public void onResults(Bundle results) {
		// lấy danh sách text nhận được
		ArrayList<String> listTexts = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		Intent resultIntent = new Intent();
		resultIntent.putExtra(RecognizerIntent.EXTRA_RESULTS, listTexts);
		// set trạng thái OK và trả về kết quả
		setResult(RESULT_OK, resultIntent);
		finish();				
	}
	
	/*
	 * sự kiện khi âm lượng thay đổi
	 */
	@Override
	public void onRmsChanged(float rmsdB) {
		// thiết lập trạng thái âm lượng lên progressbar
		int value = (int)Math.max(0, 3 * rmsdB);
		pgbVolume.setProgress(value);
	}	
	
	/*
	 * Hủy tài nguyên
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		recognizer.destroy();
	}

	/*
	 * Các sự kiện của trình nhận diện giọng nói không xử lý 
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
