package com.hcmus.dictionaryqlqt;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.NativeFSLockFactory;
import org.apache.lucene.util.Version;

import dao.IndexerDAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

public class LoadingDataActivity extends Activity {
	Thread loadingdata;
	Boolean flag_thread = true;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// get the bundle and extract data by key
			String strMessage = (String) msg.obj;
			if (strMessage.equals("DONE")) {

				Intent intent = new Intent(LoadingDataActivity.this,
						FullscreenActivity.class);
				startActivity(intent);
				LoadingDataActivity.this.finish();
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
					IndexWriter indexWriter = null;
					// tao indexwriter
					if (indexWriter == null) {
						try {
							// Create instance of Directory where index files
							// will be stored
							/*
							 * Create instance of analyzer, which will be used
							 * to tokenize the input data
							 */

							File f = new File(Environment
									.getExternalStorageDirectory().getPath()
									+ "/fuzzydata/");
							f.mkdir();
							Directory fsDirectory = FSDirectory.open(f);
							
							Analyzer standardAnalyzer = new StandardAnalyzer(
									Version.LUCENE_34);
							// Create a new index
							boolean create = true;
						
							// Create the instance of deletion policy
							IndexDeletionPolicy deletionPolicy = new KeepOnlyLastCommitDeletionPolicy();
							indexWriter = new IndexWriter(fsDirectory,
									standardAnalyzer, create, deletionPolicy,
									IndexWriter.MaxFieldLength.UNLIMITED);

						} catch (IOException ie) {
							System.out.println("Error in creating IndexWriter");
							throw new RuntimeException(ie);
						}
					}

					// tao indexdata

					InputStream is;
					try {
						is = getContext().getAssets().open(
								"EnglishVietnamese.index");
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is));

						String str = "";
						boolean flag_dangdoc = true;
						while (flag_dangdoc && flag_thread) {
							str = reader.readLine();
							if (str == null) {
								flag_dangdoc = false;
								break;
							}
							String[] list = str.split("\t");
							String strword = list[0];
							String strindex = list[1];
							String strlen = list[2];

							Field wordfield = new Field("word", strword,
									Field.Store.YES, Field.Index.ANALYZED);
							if (strword.toLowerCase().indexOf("pune") != -1) {
								// Display search results that contain pune in
								// their subject
								// first by setting boost factor
								wordfield.setBoost(2.2F);
							}

							Field indexfield = new Field("index", strindex,
									Field.Store.YES, Field.Index.ANALYZED);

							Field lenfield = new Field("len", strlen,
									Field.Store.YES, Field.Index.ANALYZED);
							Document doc = new Document();
							doc.add(wordfield);
							doc.add(indexfield);
							doc.add(lenfield);
						
							indexWriter.addDocument(doc);
						}
						if(flag_thread)
						{
							File f = new File(Environment
									.getExternalStorageDirectory().getPath()
									+ "/fuzzydata/");
							Directory fsDirectory = FSDirectory.open(f);
							fsDirectory.clearLock(indexWriter.WRITE_LOCK_NAME);
							fsDirectory.close();
							indexWriter.deleteAll();
							
						}
						indexWriter.optimize();
						indexWriter.close();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				String strMessage = "DONE";
				if (flag_thread) {
					Message msg = handler.obtainMessage(1, (String) strMessage);
					handler.sendMessage(msg);
				}
			}

		});
		loadingdata.start();

	}
	
	private void recursiveDelete(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                recursiveDelete(child);

        fileOrDirectory.delete();
    }

	//@Override
	/*public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) { // Back key pressed
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					LoadingDataActivity.this);

			// set title
			alertDialogBuilder.setTitle("Are you sure?");

			// set dialog message
			alertDialogBuilder
					.setMessage("Click yes to exit!")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, close
									// current activity
							//		flag_thread = false;
									try {
										Thread.sleep(3000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									File file = new File(Environment
											.getExternalStorageDirectory()
											.getPath()
											+ "/fuzzydata");
									if (file.exists()) {
										recursiveDelete(file);
									}
									LoadingDataActivity.this.finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		flag_thread = false;
		super.onDestroy();

	}

	Context getContext() {
		return this.getBaseContext();
	}
}