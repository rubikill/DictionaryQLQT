package com.hcmus.dictionaryqlqt;

import manager.WebviewHelper;
import manager.WordOfDayHandler;
import model.WordOfDay;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 
 * @author Minh Khanh
 * 
 * Tab xem tÆ°Ì€ cuÌ‰a ngaÌ€y
 */

public class TabDailyActivity extends Activity {

	/*
	 * webview hiÃªÌ‰n thiÌ£ nÃ´Ì£i dung
	 */
	private WebView wbvContent;
	
	/*
	 * progress dialog khi load dÆ° liÃªÌ£u
	 */
	private ProgressDialog pgbLoading;
	
	private final int WOD = 0;
	private final int ERROR = 1;
	private boolean isLoaded = false;

	/*
	 * xÆ°Ì‰ lyÌ� cÃ¢Ì£p nhÃ¢Ì£t giao diÃªÌ£n tÆ°Ì€ thread load dÆ°Ìƒ liÃªÌ£u
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == WOD){
				WordOfDay wod = (WordOfDay) msg.obj;
				showContent(wod);
				isLoaded = true;
			}
			else if (msg.what == ERROR){
				if (pgbLoading.isShowing()){
					pgbLoading.dismiss();
				}
				showDialogCheckNetwork();				
			}
		};
	};

	/*
	 * runnable lÃ¢Ì�y dÆ°Ìƒ liÃªÌ£u tÆ°Ì€ web
	 */
	private Runnable getWod = new Runnable() {

		@Override
		public void run() {
			try {			
				// lÃ¢Ì�y wod tÆ°Ì€ internet
				WordOfDayHandler wodHandler = new WordOfDayHandler(TabDailyActivity.this);
				WordOfDay wod = wodHandler.getWod();
				
				// goÌ£i handler cÃ¢Ì£p nhÃ¢Ì£t giao diÃªÌ£n
				Message msg = handler.obtainMessage(WOD, wod);
				handler.sendMessage(msg);

			} catch (Exception e) {
				Log.e("Daily", "Parser error: " + e.getMessage());
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_daily);

		wbvContent = (WebView) findViewById(R.id.wbvContent);
		pgbLoading = new ProgressDialog(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isLoaded){
			loadData();
		}
	}
	
	/*
	 * load dÆ°Ìƒ liÃªÌ£u
	 */
	private void loadData() {
		if (isOnline()) {
			// load dÆ°Ìƒ liÃªÌ£u nÃªÌ�u coÌ� kÃªÌ�t nÃ´Ì�i maÌ£ng
			showLoadingDialog();
			Thread thrLoadData = new Thread(getWod);
			thrLoadData.start();
		} else {
			// thÃ´ng baÌ�o kiÃªÌ‰m tra kÃªÌ�t nÃ´Ì�i
			showDialogCheckNetwork();
		}
	}

	/*
	 * kiÃªÌ‰m tra kÃªÌ�t nÃ´Ì�i maÌ£ng
	 */
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	/*
	 * show dialog loading
	 */
	private void showLoadingDialog(){
		String msg = getResources().getString(R.string.wod_message_loading_dialog);
		pgbLoading.setMessage(msg);
		pgbLoading.show();
	}

	/*
	 * show dialog baÌ�o lÃ´Ìƒi truy cÃ¢Ì£p maÌ£ng
	 */
	private void showDialogCheckNetwork() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);		 
        alertDialog.setTitle(R.string.wod_title_check_network_dialog);
        alertDialog.setMessage(R.string.wod_message_check_network_dialog);
        alertDialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	loadData();
            } 
        });
 
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.dismiss();
            }
        });
        alertDialog.show();
	}
	
	/*
	 * show nÃ´Ì£i dung lÃªn webview
	 */
	private void showContent(WordOfDay wod) {
		WebviewHelper.ShowWOD(wbvContent, wod, null);
		if (pgbLoading.isShowing()){
			pgbLoading.dismiss();
		}
	}
}
