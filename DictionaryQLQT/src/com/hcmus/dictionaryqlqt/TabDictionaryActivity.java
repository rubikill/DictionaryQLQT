package com.hcmus.dictionaryqlqt;

import java.util.ArrayList;
import java.util.Locale;

import bridge.AndroidBridge;
import bridge.AndroidBridgeListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import manager.SpeakerImpl;
import manager.WebviewHelper;
import model.Vocabulary;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import dao.DatabaseHelperDAOImpl;
import dao.FileHelperDAOImpl;
import dao.FinderDAOImpl;
import dao.IOHelperDAOImpl;

/**
 * 
 * @author Minh Khanh
 * 
 */

public class TabDictionaryActivity extends Activity implements OnClickListener,
		TextWatcher, OnItemClickListener, AndroidBridgeListener {

	/*
	 * 
	 */
	private static final int SPEECH_RECOGNIZER_CODE = 1;
	private int statusSearchTab = 0; // status = 0: trang thai cancel search //
										// status = 1: sau khi click vao search
										// box
	private EditText edWord;
	private ImageView btnVoiceSearch, btnCancelSearch, btnResetSearch,
			btnSearch;
	private AutoCompleteTextView matchSearchText;
	public static final int REQUEST_CODE = 0;
	private DatabaseHelperDAOImpl databaseHelper;
	private FileHelperDAOImpl fileHelper;
	private FinderDAOImpl finder;
	private ArrayList<String> words;
	private ArrayList<String> index;
	private ArrayList<String> length;

	// webview chua nghia ca tu can tra
	private WebView wvMean;
	private AndroidBridge bridge;

	private SpeakerImpl speaker;

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
		if (!ioHelper.checkDataFileExists("History.txt")) {
			ioHelper.coppyDataFile(this, "History.txt");
		}
		if (!ioHelper.checkDataFileExists("Favorite.txt")) {
			ioHelper.coppyDataFile(this, "Favorite.txt");
		}
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
		btnSearch = (ImageView) findViewById(R.id.btnSearchbox);
		matchSearchText = (AutoCompleteTextView) findViewById(R.id.mactSearchText);
		matchSearchText.setThreshold(1);
		matchSearchText.addTextChangedListener(TabDictionaryActivity.this);
		matchSearchText.setOnItemClickListener(TabDictionaryActivity.this);
		btnVoiceSearch.setOnClickListener(this);
		btnCancelSearch.setOnClickListener(this);
		edWord.setOnClickListener(this);
		btnSearch.setOnClickListener(this);

		wvMean = (WebView) findViewById(R.id.wvMeaning);
		bridge = new AndroidBridge();
		bridge.setListener(this);

		speaker = new SpeakerImpl(getApplicationContext(), Locale.ENGLISH);
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
	 * 
	 */
	private void startSpeechRecognizer() {
		Intent intent = new Intent(this, SpeechRecognizerActivity.class);
		startActivityForResult(intent, SPEECH_RECOGNIZER_CODE);
	}

	/*
	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SPEECH_RECOGNIZER_CODE) {
			if (resultCode == RESULT_OK && data != null) {
				//
				ArrayList<String> listText = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				//
				if (listText.size() == 1) {
					Search(listText.get(0));
				}
				//
				else {
					String[] arrText = convertArrayListToArray(listText);
					showResultDialog(arrText);
				}
			}
		}
	}

	/*
	 * 
	 */
	private void showResultDialog(final String[] arrText) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(arrText.length + " possible words");
		builder.setItems(arrText, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int pos) {
				//
				Search(arrText[pos]);
			}
		});
		builder.show();
	}

	public void Search(String word) {
		edWord.setText(word);
		Vocabulary vocabulary = finder.find(word.trim());
		String meaning = "";
		if (vocabulary != null) {
			meaning = finder.getMean(vocabulary);
		} else {
			meaning = "Word not found!";
		}
		showMeaning(meaning);
	}

	private void showMeaning(String meaning) {
		WebviewHelper.ShowMeaning(wvMean, meaning, bridge);
	}

	private void setDictionaryTabScreen(int status) {
		btnResetSearch.setVisibility(View.INVISIBLE);
		if (status == 0) {
//			findViewById(R.id.imgSearchbar).setVisibility(View.VISIBLE);
//			findViewById(R.id.imgSearchbarClick).setVisibility(View.INVISIBLE);
			btnVoiceSearch.setVisibility(View.VISIBLE);
			btnCancelSearch.setVisibility(View.GONE);
			edWord.setText("");

		} else if (status == 1) {
//			findViewById(R.id.imgSearchbar).setVisibility(View.INVISIBLE);
//			findViewById(R.id.imgSearchbarClick).setVisibility(View.VISIBLE);
			btnVoiceSearch.setVisibility(View.VISIBLE);
			btnCancelSearch.setVisibility(View.VISIBLE);
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
		// String word = words.get(arg2);
		String idx = index.get(arg2);
		String l = length.get(arg2);
		String mean = finder.getMean(idx, l);
		showMeaning(mean);
	}

	/*
	 * 
	 */
	private String[] convertArrayListToArray(ArrayList<String> source) {
		String[] result = new String[source.size()];
		source.toArray(result);

		return result;
	}

	@Override
	public void speakOut(String text) {
		speaker.speakOut(text);
		Log.i("Dictionary - Speakout: ", text);
	}

	@Override
	public void lookup(String text) {
		Search(text);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		speaker.shutdown();
	}
}
