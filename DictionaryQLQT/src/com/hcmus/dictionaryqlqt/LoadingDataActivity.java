package com.hcmus.dictionaryqlqt;

import java.io.File;

import dao.IndexerDAO;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class LoadingDataActivity extends Activity {
	Thread loadingdata;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// get the bundle and extract data by key
			String strMessage = (String) msg.obj;
			if (strMessage.equals("DONE")) {

				Intent intent = new Intent(LoadingDataActivity.this,
						FullscreenActivity.class);
				startActivity(intent);
				finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loadingdata);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// create a new thread
		loadingdata = new Thread(new Runnable() {

			@Override
			public void run() {
				// kiem tra xem indexdata fuzzy da ton tai hay chua
				// neu chua thi tao indexdata
				File file = new File(Environment.getExternalStorageDirectory()
						.getPath() + "/fuzzydata");
				if (!file.exists()) {
					IndexerDAO indexer = new IndexerDAO(getContext());
					indexer.createIndexWriter();
					indexer.indexData();
				}
				String strMessage = "DONE";
				Message msg = handler.obtainMessage(1, (String) strMessage);
				handler.sendMessage(msg);
			}
		});
		loadingdata.start();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	Context getContext() {
		return this.getBaseContext();
	}
}