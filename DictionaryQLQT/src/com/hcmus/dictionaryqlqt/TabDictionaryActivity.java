package com.hcmus.dictionaryqlqt;

import java.util.ArrayList;
import java.util.Locale;

import model.Vocabulary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.memetix.mst.language.Language;

import dao.DatabaseHelperDAOImpl;
import dao.FileHelperDAOImpl;
import dao.FinderDAOImpl;
import dao.IOHelperDAOImpl;

public class TabDictionaryActivity extends Activity implements OnClickListener,
		OnInitListener, TextWatcher, OnItemClickListener {

	private int statusSearchTab = 0; // status = 0: trang thai cancel search //
										// status = 1: sau khi click vao search
										// box
	private EditText edWord;
	private ImageView btnVoiceSearch, btnAudio, btnCancelSearch,
			btnResetSearch, btnSearch;
	private TextView tvWord, tvResult;
	private MultiAutoCompleteTextView matchSearchText;
	public static final int REQUEST_CODE = 0;
	private TextToSpeech tts;
	private String word;
	private DatabaseHelperDAOImpl databaseHelper;
	private FileHelperDAOImpl fileHelper;
	private FinderDAOImpl finder;
	private ArrayList<String> words;
	private ArrayList<String> index;
	private ArrayList<String> length;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			TabDictionaryActivity.this.tvResult.setText(result);
		};
	};

	private Runnable translateRunable = new Runnable() {
		@Override
		public void run() {
			Translater tl = new Translater();
			String result = tl.Translate(word, Language.ENGLISH,
					Language.VIETNAMESE);
			Message msg = handler.obtainMessage(0, result);
			handler.sendMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_dictionary);
		initComponents();
		setDictionaryTabScreen(statusSearchTab);
		checkSpeechRecognizerAvailable();
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
		btnAudio = (ImageView) findViewById(R.id.btnAudio);
		tvResult = (TextView) findViewById(R.id.tvResult);
		btnSearch = (ImageView) findViewById(R.id.btnSearchbox);
		matchSearchText = (MultiAutoCompleteTextView) findViewById(R.id.mactSearchText);
		matchSearchText.setThreshold(1);
		matchSearchText.addTextChangedListener(TabDictionaryActivity.this);
		matchSearchText.setOnItemClickListener(TabDictionaryActivity.this);
		btnVoiceSearch.setOnClickListener(this);
		btnAudio.setOnClickListener(this);
		btnAudio.setVisibility(View.INVISIBLE);
		btnCancelSearch.setOnClickListener(this);
		btnSearch.setOnClickListener(this);

		edWord.setOnClickListener(this);

		edWord.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int actionId,
					KeyEvent arg2) {

				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					onWordSelected(TabDictionaryActivity.this.edWord.getText()
							.toString());
					return true;
				}
				return false;
			}
		});

		tts = new TextToSpeech(FullscreenActivity.getInstance(), this);
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
			startSpeechToText();
			break;
		case R.id.btnCancelSearch:
			statusSearchTab = 0;
			setDictionaryTabScreen(statusSearchTab);
			break;
		case R.id.btnResetsearch:
			edWord.setText("");
			btnResetSearch.setVisibility(View.INVISIBLE);
			break;
		case R.id.btnAudio:
			String text = this.tvWord.getText().toString().trim();
			if (text != null && !text.equals("")) {
				textToSpeech(text);
			}
			break;
		case R.id.btnSearchbox:
			Search(matchSearchText.getText().toString());
			break;
		}
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

	private void checkSpeechRecognizerAvailable() {
		if (SpeechRecognizer.isRecognitionAvailable(this)) {
			Toast.makeText(this, "Recognition Available", 500).show();
		} else {
			Toast.makeText(this, "Recognition Unavailable", 500).show();
		}
	}

	private void startSpeechToText() {
		SpeechRecognizerDialog dialog = new SpeechRecognizerDialog(this);
		dialog.show();
	}

	private void textToSpeech(String text) {
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	public void onResultFromSR(ArrayList<String> words) {
		if (words.size() > 1) {
			showResultDialog(words.toArray(new String[words.size()]));
		} else {
			onWordSelected(words.get(0));
		}
	}

	public void onWordSelected(String word) {
		this.tvWord.setText(word);
		this.word = word;
		this.btnAudio.setVisibility(View.VISIBLE);
		this.tvResult.setText("");
		Thread thread = new Thread(translateRunable);
		thread.start();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			}
		} else {
			Log.e("TTS", "Initilization Failed!");
		}
	}

	@Override
	public void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	private void showResultDialog(final String[] words) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(words.length + " possible words");
		builder.setItems(words, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int pos) {
				onWordSelected(words[pos]);
			}
		});
		builder.show();
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
}
