package com.hcmus.dictionaryqlqt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Stack;

import bridge.AndroidBridge;
import bridge.AndroidBridgeListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.KeyEvent;
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
import android.widget.Toast;

import dao.DatabaseHelperDAOImpl;
import dao.FileHelperDAOImpl;
import dao.FinderDAOImpl;
import dao.IOHelperDAOImpl;

/**
 * 
 * @author Minh Khanh
 * 
 * ham nao ai lam thi comment them nhe
 * 
 */

public class TabDictionaryActivity extends Activity implements OnClickListener,
		TextWatcher, OnItemClickListener, AndroidBridgeListener {

	/*
	 * ma Code de nhan ket qua tra ve tu voice search
	 */
	private static final int SPEECH_RECOGNIZER_CODE = 1;
	private int statusSearchTab = 0; // status = 0: trang thai cancel search //
										// status = 1: sau khi click vao search
										// box
	private EditText edWord;
	private ImageView btnVoiceSearch, btnCancelSearch, btnResetSearch,
			btnSearch;
	
	/*
	 * progressbar load nghia tu
	 */
	private ProgressDialog pgbLoading;
	
	private AutoCompleteTextView matchSearchText;
	private DatabaseHelperDAOImpl databaseHelper;
	private FileHelperDAOImpl fileHelper;
	private FinderDAOImpl finder;
	private ArrayList<String> words;
	private ArrayList<String> index;
	private ArrayList<String> length;
	
	/*
	 * tu hien tai duoc hien thi
	 */
	private Vocabulary currentWord;

	/*
	 * webview hien thi nghia tu
	 */
	private WebView wvMean;
	
	/*
	 * doi tuong de goi qua javascript trong wbMean
	 */
	private AndroidBridge bridge;

	/*
	 * bo phat am
	 */
	private SpeakerImpl speaker;
	
	/*
	 * luu lich su tra tu hien tai
	 */
	private Stack<Vocabulary> history;

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

	/**
	 * khoi tao cac thanh phan can thiet
	 */
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
		history = new Stack<Vocabulary>();
		pgbLoading = new ProgressDialog(this);
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

	/**
	 * mo man hinh nhan dien giong noi
	 */
	private void startSpeechRecognizer() {
		Intent intent = new Intent(this, SpeechRecognizerActivity.class);
		startActivityForResult(intent, SPEECH_RECOGNIZER_CODE);
	}

	/**
	 * su kien nhan ket qua tra ve tu voice search
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SPEECH_RECOGNIZER_CODE) {
			if (resultCode == RESULT_OK && data != null) {
				// lay danh sach tu nhan dien duoc
				ArrayList<String> listText = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				// neu chi co 1 tu goi ham search
				if (listText.size() == 1) {
					Search(listText.get(0));
				}
				// neu co nhieu tu hien thi len dialog cho nguoi dung chon
				else {
					String[] arrText = convertArrayListToArray(listText);
					showResultDialog(arrText);
				}
			}
		}
	}

	/**
	 * hien thi danh sach ca tu voice search
	 * @param arrText: mang String
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
			saveHistory(new Vocabulary(word, meaning));
			showMeaning(meaning);
		} else {
			meaning = "Word not found!";
		}
	}

	/**
	 * hien thi nghia tu len webview
	 * @param meaning: chuoi nghia cua tu
	 */
	private void showMeaning(String meaning) {
		// show dialog loading
		showLoadingDialog();
		WebviewHelper.ShowMeaning(wvMean, meaning, bridge);
	}

	/**
	 * lua lai tu tra cuu hien tai
	 * @param word: tu hien tai
	 */
	private void saveHistory(Vocabulary word) {
		// neu tu da ton tai trong lich su, xoa bo
		Iterator<Vocabulary> iterator = history.iterator();
		while (iterator.hasNext()){
			Vocabulary vocab = iterator.next();
			if (vocab.getWord().equals(word.getWord())){
				history.remove(vocab);
				break;
			}
		}
		// dua tu vua tra truoc do vao lich su
		if (currentWord != null){			
			history.push(currentWord);
		}
		// gan lai tu hien tai
		currentWord = word;
	}

	private void setDictionaryTabScreen(int status) {
		btnResetSearch.setVisibility(View.INVISIBLE);
		if (status == 0) {
			// findViewById(R.id.imgSearchbar).setVisibility(View.VISIBLE);
			// findViewById(R.id.imgSearchbarClick).setVisibility(View.INVISIBLE);
			btnVoiceSearch.setVisibility(View.VISIBLE);
			btnCancelSearch.setVisibility(View.GONE);
			edWord.setText("");

		} else if (status == 1) {
			// findViewById(R.id.imgSearchbar).setVisibility(View.INVISIBLE);
			// findViewById(R.id.imgSearchbarClick).setVisibility(View.VISIBLE);
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
		String word = words.get(arg2);
		String idx = index.get(arg2);
		String l = length.get(arg2);
		String mean = finder.getMean(idx, l);
		saveHistory(new Vocabulary(word, mean));
		showMeaning(mean);
	}

	/**
	 * chuyen tu arraylist String sang mang String
	 * @param source: arraylist can chuyen
	 * @return mang String
	 */
	private String[] convertArrayListToArray(ArrayList<String> source) {
		String[] result = new String[source.size()];
		source.toArray(result);

		return result;
	}

	/**
	 * su kien tu webview goi phat am
	 * @param text: tu can phat am
	 */
	@Override
	public void speakOut(String text) {
		speaker.speakOut(text);
		Log.i("Dictionary - Speakout: ", text);
	}

	/**
	 * su kien tu webview goi tim tu
	 * @param text: tu can tra
	 */
	@Override
	public void lookup(String text) {
		Search(text);
	}

	/**
	 * su kien huy activity
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// giai phong speaker
		speaker.shutdown();
	}

	/**
	 * su kien nhan back
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (handleKeyBack()){
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * xu ly su kien nhan back
	 * @return false neu lich su tra tu hien tai trong, true neu nguoc lai
	 */
	private boolean handleKeyBack() {
		if (history.isEmpty()) {
			return false;
		}
		// lay tu tren cung cua lich su ra va xoa bo
		Vocabulary word = history.pop();
		currentWord = word;
		// hien thi nghia cua tu
		showMeaning(word.getStMean());

		return true;
	}

	/**
	 * su kien tu webview khi nhan them favorite
	 */
	@Override
	public void setFavorite(String word) {
		// them tu yeu thich
		Toast.makeText(this, "set favorite: " + word, Toast.LENGTH_SHORT).show();
	}

	/**
	 * su kien tu webview khi nhan xoa favorite
	 */
	@Override
	public void removeFavorite(String word) {
		// xoa tu yeu thich
		Toast.makeText(this, "delete favorite: " + word, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * su kien resume activity
	 */
	@Override
	protected void onResume() {		
		super.onResume();
		// kiem tra tu dang hien thi co la favorite hay ko de hien thi
		checkFavoriteWord();
	}
	
	/**
	 * ham xu ly kiem tra tu favorite
	 */
	private void checkFavoriteWord(){
		if (currentWord != null){
			// kiem tra tu hien tai co phai la tu yeu thich
			//.....
			// demo
			if (currentWord.getWord().equals("school")){
				// goi qua webview hien thi favorite
				wvMean.loadUrl("javascript:setFavorite(true)");
				Toast.makeText(this, "set favorite: school", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * hien thi dialog loading khi hien thi nghia tu
	 */
	private void showLoadingDialog(){
		String msg = getResources().getString(R.string.dict_message_loading_dialog);
		pgbLoading.setMessage(msg);
		pgbLoading.show();
	}

	/**
	 * su kien load xong nghia tu tren webview
	 */
	@Override
	public void onLoadComplete() {
		if (pgbLoading.isShowing()){
			pgbLoading.dismiss();
			checkFavoriteWord();
		}		
	}
}
