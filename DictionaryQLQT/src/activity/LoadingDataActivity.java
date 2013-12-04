package activity;

import helper.IIOHelper;
import helper.IOHelperImpl;

import com.hcmus.dictionaryqlqt.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

public class LoadingDataActivity extends Activity {

	private final int DONE = 1;
	private final int ERROR = 0;

	private String pathFuzzy = "fuzzy_index";

	private boolean isDeloying = false;
	private Thread thrDeloy;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int result = msg.what;
			if (result == DONE) {
				commitDeloyData();
				startHomeActivity();
			} else {
				showDialogError();
			}
		};
	};

	private Runnable deloy = new Runnable() {
		@Override
		public void run() {
			Message msg = handler.obtainMessage();
			try {
				isDeloying = true;
				deloyData();
				msg.what = DONE;
			} catch (Exception e) {
				msg.what = ERROR;
			}
			handler.sendMessage(msg);
			isDeloying = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if (checkExistData()) {
			startHomeActivity();
		} else {
			setContentView(R.layout.activity_loadingdata);
			thrDeloy = new Thread(deloy);
			thrDeloy.start();
		}

	}

	private void startHomeActivity() {
		Intent intent = new Intent(LoadingDataActivity.this, HomeActivity.class);
		LoadingDataActivity.this.finish();
		startActivity(intent);
	}

	private void deloyData() {
		IIOHelper ioHelper = new IOHelperImpl();
		ioHelper.coypyFolder(this, pathFuzzy);
	}

	private boolean checkExistData() {
		SharedPreferences settings = getSharedPreferences("Config",
				Context.MODE_PRIVATE);	
		boolean isExist = settings.getBoolean("Deloyed", false);
		if (isExist){
			IIOHelper ioHelper = new IOHelperImpl();
			isExist = ioHelper.checkDataFileExists(pathFuzzy);
		}

		return isExist;
	}

	private void commitDeloyData() {
		SharedPreferences settings = getSharedPreferences("Config",
				Context.MODE_PRIVATE);
		Editor edit = settings.edit();
		edit.putBoolean("Deloyed", true);
		edit.commit();
	}
	
	private void showDialogError(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);		 
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Can't copy data, please check memory sdcard!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	LoadingDataActivity.this.finish();
            } 
        });
        alertDialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					LoadingDataActivity.this);

			// set title
			alertDialogBuilder.setTitle("Are you exit?");

			// set dialog message
			alertDialogBuilder
					.setMessage("Program is loading data")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									try {
										if (thrDeloy != null && isDeloying){
											thrDeloy.stop();
										}
									} catch (Exception e) {
									}
									LoadingDataActivity.this.finish();
								}

							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}