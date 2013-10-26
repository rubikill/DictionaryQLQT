package com.hcmus.dictionaryqlqt;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
/**
 * 
 * @author Minh Khanh
 * 
 * Tab tra cứu từ điển
 * 
 * các thuộc tính và hàm ai làm thì comment vào nhé
 *
 */
public class TabDictionaryActivity extends Activity implements OnClickListener, OnEditorActionListener {

	/*
	 * RequestCode gọi và nhận kết quả từ màn hình nhận diện giọng nói
	 */
	private static final int SPEECH_RECOGNIZER_CODE = 1;	
	
	private int statusSearchTab = 0; // status = 0: trang thai cancel search  // status = 1: sau khi click vao search box
	private EditText edWord;
	private ImageView btnVoiceSearch, btnCancelSearch, btnResetSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_dictionary);
		initComponents();
		setDictionaryTabScreen(statusSearchTab);
	}
	
	private void initComponents(){
		statusSearchTab = 0;
		edWord = (EditText)findViewById(R.id.mactSearchText);
		btnVoiceSearch = (ImageView)findViewById(R.id.btnVoiceSearch);
		btnCancelSearch = (ImageView)findViewById(R.id.btnCancelSearch);
		btnResetSearch = (ImageView)findViewById(R.id.btnResetsearch);
		
		btnVoiceSearch.setOnClickListener(this);
		btnCancelSearch.setOnClickListener(this);
		edWord.setOnClickListener(this);		
		edWord.setOnEditorActionListener(this);
	}

	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		switch (id) {
		case R.id.mactSearchText:
			if (statusSearchTab == 0){
				statusSearchTab = 1;
				setDictionaryTabScreen(statusSearchTab);
			}
			break;
		case R.id.btnVoiceSearch:
			// mở màn hình nhận diện giọng nói
			startSpeechRecognizer();
			break;
		case R.id.btnCancelSearch:
			statusSearchTab = 0;
			setDictionaryTabScreen(statusSearchTab);
			break;
		case R.id.btnResetsearch:
			edWord.setText("");
			btnResetSearch.setVisibility(View.INVISIBLE);
			break;
		}
	}	
	
	/*
	 * mở màn hình nhận diện giọng nói
	 */
	private void startSpeechRecognizer(){
		Intent intent = new Intent(this, SpeechRecognizerActivity.class);
		startActivityForResult(intent, SPEECH_RECOGNIZER_CODE);
	}	
	
	/*
	 * lấy kết quả trả về của màn hình nhận diện giọng nói
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SPEECH_RECOGNIZER_CODE){
			if (resultCode == RESULT_OK && data != null){
				// lấy kết quả
				ArrayList<String> listText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				
				// nếu chỉ có 1 từ gọi hàm tìm
				if (listText.size() == 1){
					lookUp(listText.get(0));
				}
				// hiển thị danh sách kết quả lên dialog
				else {				
					String[] arrText = convertArrayListToArray(listText);
					showResultDialog(arrText);
				}
			}
		}
	}
	
	/*
	 * hàm tra từ
	 */
	private void lookUp(String text){
		Toast.makeText(this, "Looking up " + text, Toast.LENGTH_SHORT).show();
	}
		
	/*
	 * hiển thị dialog danh sách các từ
	 */
	private void showResultDialog(final String[] arrText){
		AlertDialog.Builder builder =  
	            new AlertDialog.Builder(this);
		builder.setTitle(arrText.length + " possible words");
		builder.setItems(arrText,
	            new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int pos) {
	        	//gọi hàm tra từ khi từ được chọn
	        	lookUp(arrText[pos]);
	        }
	    });
		builder.show();
	}
	
	private void setDictionaryTabScreen(int status){
		btnResetSearch.setVisibility(View.INVISIBLE);
		if (status == 0){
			findViewById(R.id.imgSearchbar).setVisibility(View.VISIBLE);
			findViewById(R.id.imgSearchbarClick).setVisibility(View.INVISIBLE);
			btnVoiceSearch.setVisibility(View.VISIBLE);
			btnCancelSearch.setVisibility(View.GONE);
			edWord.setText("");
			
		} else if (status == 1){
			findViewById(R.id.imgSearchbar).setVisibility(View.INVISIBLE);
			findViewById(R.id.imgSearchbarClick).setVisibility(View.VISIBLE);
			btnVoiceSearch.setVisibility(View.VISIBLE);
			btnCancelSearch.setVisibility(View.VISIBLE);
			//btnCancelSearch.setImageResource(R.drawable.button_cancelsearch_normal);
			
		}
	}
	
	/*
	 * chuyển Arraylist sang String[]
	 */
	private String[] convertArrayListToArray(ArrayList<String> source){
		String[] result = new String[source.size()];
		source.toArray(result);
		
		return result;
	}

	/*
	 * sự kiện khi người dùng nhấn nút search trên bàn phím khi đang nhập từ	 * 
	 */
	@Override
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			// gọi hàm tra từ
            lookUp(this.edWord.getText().toString());
            return true;
        }
        return false;
	}
}
