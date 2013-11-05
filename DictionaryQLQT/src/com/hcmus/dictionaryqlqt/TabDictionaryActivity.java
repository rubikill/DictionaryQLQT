package com.hcmus.dictionaryqlqt;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import model.Vocabulary;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
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
 * Tab tra cÆ°Ì�u tÆ°Ì€ Ä‘iÃªÌ‰n
 * 
 * caÌ�c thuÃ´Ì£c tiÌ�nh vaÌ€ haÌ€m ai laÌ€m thiÌ€ comment vaÌ€o nheÌ�
 *
 */

public class TabDictionaryActivity extends Activity implements OnClickListener,
				TextWatcher, OnItemClickListener, OnEditorActionListener {

	/*
	 * RequestCode goÌ£i vaÌ€ nhÃ¢Ì£n kÃªÌ�t quaÌ‰ tÆ°Ì€ maÌ€n hiÌ€nh nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i
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
	
	// webview chua nghia ca tu can tra
	private WebView wvMean;
	private String word;
	private String mean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_dictionary);
		initComponents();
		setDictionaryTabScreen(statusSearchTab);
		initData();
		
		mean = "Dự án từ điển Android";
		//String word = edWord.getText().toString();
		
		
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
		if(!ioHelper.checkDataFileExists("History.txt")){
			ioHelper.coppyDataFile(this, "History.txt");
		}
		if(!ioHelper.checkDataFileExists("Favorite.txt")){
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
		
		wvMean = (WebView) findViewById(R.id.wvMean);
		wvMean.setVisibility(View.INVISIBLE);
		
		
		// 
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.mactSearchText:
			if (statusSearchTab == 0) {
				statusSearchTab = 1;
				setDictionaryTabScreen(statusSearchTab);
				
				/*isEnter = true;
				wvMean.setVisibility(View.VISIBLE);
				touchMeanning(word, mean);*/
			}
			break;
		case R.id.btnVoiceSearch:
			// mÆ¡Ì‰ maÌ€n hiÌ€nh nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i
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
	 * mÆ¡Ì‰ maÌ€n hiÌ€nh nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i
	 */
	private void startSpeechRecognizer(){
		Intent intent = new Intent(this, SpeechRecognizerActivity.class);
		startActivityForResult(intent, SPEECH_RECOGNIZER_CODE);
	}	
	
	/*
	 * lÃ¢Ì�y kÃªÌ�t quaÌ‰ traÌ‰ vÃªÌ€ cuÌ‰a maÌ€n hiÌ€nh nhÃ¢Ì£n diÃªÌ£n gioÌ£ng noÌ�i
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SPEECH_RECOGNIZER_CODE){
			if (resultCode == RESULT_OK && data != null){
				// lÃ¢Ì�y kÃªÌ�t quaÌ‰
				ArrayList<String> listText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				
				// nÃªÌ�u chiÌ‰ coÌ� 1 tÆ°Ì€ goÌ£i haÌ€m tiÌ€m
				if (listText.size() == 1){
					lookUp(listText.get(0));
				}
				// hiÃªÌ‰n thiÌ£ danh saÌ�ch kÃªÌ�t quaÌ‰ lÃªn dialog
				else {				
					String[] arrText = convertArrayListToArray(listText);
					showResultDialog(arrText);
				}
			}
		}
	}
	
	/*
	 * haÌ€m tra tÆ°Ì€
	 */
	private void lookUp(String text){
		Toast.makeText(this, "Looking up " + text, Toast.LENGTH_SHORT).show();
		Search(text);
	}
		
	/*
	 * hiÃªÌ‰n thiÌ£ dialog danh saÌ�ch caÌ�c tÆ°Ì€
	 */
	private void showResultDialog(final String[] arrText){
		AlertDialog.Builder builder =  
	            new AlertDialog.Builder(this);
		builder.setTitle(arrText.length + " possible words");
		builder.setItems(arrText,
	            new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int pos) {
	        	//goÌ£i haÌ€m tra tÆ°Ì€ khi tÆ°Ì€ Ä‘Æ°Æ¡Ì£c choÌ£n
	        	lookUp(arrText[pos]);
	        }
	    });
		
	}

	public String Search(String word) {
		tvWord.setText(word);
		Vocabulary vocabulary = finder.find(word);
		String meaning = "";
		if(vocabulary != null)
		{
			meaning = finder.getMean(vocabulary);
		}
		else
		{
			meaning = "Word not found!";
		}
		tvResult.setText(meaning);
		return meaning;
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
			
			// Nghia 
			// hiện thi webview
			wvMean.setVisibility(View.VISIBLE);
			word = edWord.getText().toString();
			touchMeanning(word, mean);

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
	 * chuyÃªÌ‰n Arraylist sang String[]
	 */
	private String[] convertArrayListToArray(ArrayList<String> source){
		String[] result = new String[source.size()];
		source.toArray(result);
		
		return result;
	}

	/*
	 * sÆ°Ì£ kiÃªÌ£n khi ngÆ°Æ¡Ì€i duÌ€ng nhÃ¢Ì�n nuÌ�t search trÃªn baÌ€n phiÌ�m khi Ä‘ang nhÃ¢Ì£p tÆ°Ì€	 * 
	 */
	@Override
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			// goÌ£i haÌ€m tra tÆ°Ì€
            lookUp(this.edWord.getText().toString());
            return true;
        }
        return false;
	}
	
	private final Handler handler1 = new Handler();
	
	// Thủ chuỗi hai -- Nghĩa của từ chúng ta touch vào
	String mean2 = "Nhóm Phát triển: nhóm 2";
	
	private class AndroidBridge{
		public void callAndroid(final String msg){
			handler1.post(new Runnable() {
				
				@Override
				public void run() {
					TabDictionaryActivity.this.edWord.setText(msg.toString());	
					String word = TabDictionaryActivity.this.edWord.getText().toString(); 
					touchMeanning(word, mean2);
				}
			});
		}
		
		public void callEventAddFavorite(final String msg){
			handler1.post(new Runnable() {					
				@Override
				public void run() {
					
					// Viet su kien them vao danh sach yeu thich
					Toast.makeText(TabDictionaryActivity.this, msg + "add favorite", Toast.LENGTH_SHORT).show();
				
				}
			});
		}
		public void callEventAAudio(final String msg){
			handler1.post(new Runnable() {					
				@Override
				public void run() {
					
					// Viet su kien nut audio
					Toast.makeText(TabDictionaryActivity.this, msg + "audio", Toast.LENGTH_SHORT).show();
				}
			});
		}		
	}

@SuppressLint("JavascriptInterface")
public void touchMeanning(String wordText, String meanText){
			
	String html = "<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>"
			+ "<head>" + "<meta charset='utf-8' />"
				+ "<title>Title</title>"
				+ "<link href='file:///android_asset/demo.css' type='text/css' rel='stylesheet' />"
				+ "<script src='file:///android_asset/jquery-1.9.1.js' type='text/javascript'></script>"
			+ "</head>" 
			+ "<body>" 
				+ "<div class='word'>"+wordText
				+	"<img class = 'audio' src = 'file:///android_asset/definition-icon-audio.png'/>"
				+	"<img class = 'favorite' src = 'file:///android_asset/definition-favorite-star-off.png'/>"
				+"</div>"
				+"&nbsp;&nbsp;"
				+ "<div class='wotd'>" 
				+ getXMLStr(meanText)
				+ "</div>"
				+ "<script src='file:///android_asset/callandroid.js' type='text/javascript'></script>"
			+ "</body>" + "</html>";
	
	
	wvMean.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
	
	wvMean.getSettings().setJavaScriptEnabled(true);
	wvMean.addJavascriptInterface(new AndroidBridge(), "android");
}

public String getXMLStr(String mean){
	
	String[] submean = mean.split(" ");
	String meanXML = "";
	for (int i = 0; i < submean.length; i ++){
		boolean isSpan = true;			
		if (isSpan)
			meanXML +=  "<span class='mean'>"+submean[i].toString()+"</span>&nbsp;";
		else
			meanXML += submean[i].toString() +"&nbsp;";
	}
	
	
	return meanXML;
}
}
