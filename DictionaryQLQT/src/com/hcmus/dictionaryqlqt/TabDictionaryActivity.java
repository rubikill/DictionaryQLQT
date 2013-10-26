package com.hcmus.dictionaryqlqt;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import model.Vocabulary;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


import dao.DatabaseHelperDAOImpl;
import dao.FileHelperDAOImpl;
import dao.FinderDAOImpl;
import dao.IOHelperDAOImpl;
/**
 * 
 * @author Minh Khanh
 * 
 * Tab tra cứu từ điển
 * 
 * các thuộc tính và hàm ai làm thì comment vào nhé
 *
 */

public class TabDictionaryActivity extends Activity implements OnClickListener,
				TextWatcher, OnItemClickListener, OnEditorActionListener {

	/*
	 * RequestCode gọi và nhận kết quả từ màn hình nhận diện giọng nói
	 */
	private static final int SPEECH_RECOGNIZER_CODE = 1;	
	private int statusSearchTab = 0; // status = 0: trang thai cancel search //
										// status = 1: sau khi click vao search
										// box
	private EditText edWord;
	private ImageView btnVoiceSearch, btnCancelSearch,
			btnResetSearch, btnSearch;
	private TextView tvWord, tvResult;
	private MultiAutoCompleteTextView matchSearchText;
	public static final int REQUEST_CODE = 0;
	private DatabaseHelperDAOImpl databaseHelper;
	private FileHelperDAOImpl fileHelper;
	private FinderDAOImpl finder;
	private ArrayList<String> words;
	private ArrayList<String> index;
	private ArrayList<String> length;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_dictionary);
		initComponents();
		setDictionaryTabScreen(statusSearchTab);
		initData();
	}

	private void initData() {
		IOHelperDAOImpl ioHelper = new IOHelperDAOImpl();
		// kiem tra thu muc chua du lieu, neu chua co thi tao nhu muc moi
		ioHelper.createFolderData();
		databaseHelper = new DatabaseHelperDAOImpl(TabDictionaryActivity.this);
		try {
			databaseHelper.openDataBase();// mo databases
		} catch (Exception e) {
			// neu mo that bai => chua co data => coppy data tu asset
			databaseHelper.createDataBase();
			databaseHelper.openDataBase();
		}
		fileHelper = new FileHelperDAOImpl();
		// Kiem tra file nghia ton tai chua, neu chua thi coppy tu asset
		if (!fileHelper.checkFileExists())
			fileHelper.coppyFile(this);

		finder = new FinderDAOImpl(databaseHelper, fileHelper);
		words = new ArrayList<String>();
		index = new ArrayList<String>();
		length = new ArrayList<String>();
	}

	private void initComponents() {
		statusSearchTab = 0;
		edWord = (EditText) findViewById(R.id.mactSearchText);
		btnVoiceSearch = (ImageView) findViewById(R.id.btnVoiceSearch);
		btnCancelSearch = (ImageView) findViewById(R.id.btnCancelSearch);
		btnResetSearch = (ImageView) findViewById(R.id.btnResetsearch);
		tvWord = (TextView) findViewById(R.id.tvWord);
		tvResult = (TextView) findViewById(R.id.tvResult);
		btnSearch = (ImageView) findViewById(R.id.btnSearchbox);
		matchSearchText = (MultiAutoCompleteTextView) findViewById(R.id.mactSearchText);
		matchSearchText.setThreshold(1);
		matchSearchText.addTextChangedListener(TabDictionaryActivity.this);
		matchSearchText.setOnItemClickListener(TabDictionaryActivity.this);
		btnVoiceSearch.setOnClickListener(this);
		btnCancelSearch.setOnClickListener(this);
		edWord.setOnClickListener(this);		
		edWord.setOnEditorActionListener(this);
		btnSearch.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.mactSearchText:
			if (statusSearchTab == 0) {
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
		case R.id.btnSearchbox:
				Search(matchSearchText.getText().toString());
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
		Search(text);
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
		
	}

	private void Search(String word) {
		tvWord.setText(word);
		Vocabulary vocabulary = finder.find(word);
		if(vocabulary != null)
		{
			String mean = finder.getMean(vocabulary);
			tvResult.setText(mean);
		}
		else
		{
			tvResult.setText("Word not found !");
		}
		
	}

	private void setDictionaryTabScreen(int status) {
		btnResetSearch.setVisibility(View.INVISIBLE);
		if (status == 0) {
			findViewById(R.id.imgSearchbar).setVisibility(View.VISIBLE);
			findViewById(R.id.imgSearchbarClick).setVisibility(View.INVISIBLE);
			btnVoiceSearch.setVisibility(View.VISIBLE);
			btnCancelSearch.setVisibility(View.GONE);
			edWord.setText("");

		} else if (status == 1) {
			findViewById(R.id.imgSearchbar).setVisibility(View.INVISIBLE);
			findViewById(R.id.imgSearchbarClick).setVisibility(View.VISIBLE);
			btnVoiceSearch.setVisibility(View.VISIBLE);
			btnCancelSearch.setVisibility(View.VISIBLE);
			// btnCancelSearch.setImageResource(R.drawable.button_cancelsearch_normal);

		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		if (matchSearchText.isPerformingCompletion())
			return;
		if (matchSearchText.getText().toString().length() > 0) {
			ArrayList<Vocabulary> arr = finder.getRecommendWord(matchSearchText
					.getText().toString());
			if (arr != null) {
				words.clear();
				index.clear();
				length.clear();
				for (Vocabulary vocabulary : arr) {
					words.add(vocabulary.getWord());
					index.add(vocabulary.getIndex());
					length.add(vocabulary.getLength());
				}
				matchSearchText
						.setAdapter(new ArrayAdapter<String>(
								TabDictionaryActivity.this,
								android.R.layout.simple_list_item_single_choice,
								words));
				matchSearchText.showDropDown();
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String word = words.get(arg2);
		String idx = index.get(arg2);
		String l = length.get(arg2);
		String mean = finder.getMean(idx,l);
		tvWord.setText(word);
		tvResult.setText(mean);
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
