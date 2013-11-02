package com.hcmus.dictionaryqlqt;

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
 * Tab xem từ của ngày
 */

public class TabDailyActivity extends Activity {

	/*
	 * webview hiển thị nội dung
	 */
	private WebView wbvContent;
	
	/*
	 * progress dialog khi load dư liệu
	 */
	private ProgressDialog pgbLoading;

	/*
	 * xử lý cập nhật giao diện từ thread load dữ liệu
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			WordOfDay wod = (WordOfDay) msg.obj;
			showContent(wod);
			if (pgbLoading.isShowing()){
				pgbLoading.dismiss();
			}
		};
	};

	/*
	 * runnable lấy dữ liệu từ web
	 */
	private Runnable getWod = new Runnable() {

		@Override
		public void run() {
			try {			
				// lấy wod từ internet
				WordOfDayHandler wodHandler = new WordOfDayHandler(TabDailyActivity.this);
				WordOfDay wod = wodHandler.getWod();
				
				// gọi handler cập nhật giao diện
				Message msg = handler.obtainMessage(0, wod);
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
		
		// gọi load dữ liệu
		loadData();
	}

	/*
	 * load dữ liệu
	 */
	private void loadData() {
		if (isOnline()) {
			// load dữ liệu nếu có kết nối mạng
			showLoadingDialog();
			Thread thrLoadData = new Thread(getWod);
			thrLoadData.start();
		} else {
			// thông báo kiểm tra kết nối
			showDialogCheckNetwork();
		}
	}

	/*
	 * kiểm tra kết nối mạng
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
	 * show dialog báo lỗi truy cập mạng
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
	 * show nội dung lên webview
	 */
	private void showContent(WordOfDay wod) {
		StringBuilder body = new StringBuilder();
		body.append("<h3>" + wod.getDate() + "</h3>");
		body.append("<h1>" + wod.getWord() + "</h1>");
		body.append("<strong>" + wod.getPhonetic() + "</strong>");
		body.append("<br/><em>" + wod.getWordFunction() + "</em>");
		body.append("<br/><span>" + wod.getMean() + "</span>");
		body.append("<h2>Examples:</h2>");
		body.append("<p>" + wod.getExamples() + "</p>");
		body.append("<h2>Did you know?</h2>");
		body.append("<p>" + wod.getDidYouKnow() + "</p>");

		String html = "<html><body>" + body.toString() + "</body></html>";
		wbvContent.loadDataWithBaseURL("file:///android_asset/", html,
				"text/html", "UTF-8", null);
	}
}
