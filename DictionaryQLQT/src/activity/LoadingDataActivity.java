package activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.hcmus.dictionaryqlqt.R;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat", "DefaultLocale" })
public class LoadingDataActivity extends Activity {
	Thread loadingdata;
	Boolean flag_thread = true;
	Boolean flag_isRun = false;
	SQLiteDatabase db;
	String strdirectory;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// get the bundle and extract data by key
			String strMessage = (String) msg.obj;
			if (strMessage.equals("DONE")) {

				Intent intent = new Intent(LoadingDataActivity.this,
						HomeActivity.class);
				LoadingDataActivity.this.finish();
				startActivity(intent);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// khoi tao datavase
		db = this.openOrCreateDatabase("mydatafuzzy", MODE_PRIVATE, null);

		// tao bang neu chua ton tai
		String sql = "create table if not exists tbfuzzy ("
				+ "id integer PRIMARY KEY autoincrement,"
				+ "namedirectory text," + "arecreate integer);";
		db.execSQL(sql);
		String sqlselect = "select * from tbfuzzy";
		Cursor c1 = db.rawQuery(sqlselect, null);
		if (c1.getCount() == 0) {
			flag_isRun = true;
			String sqlCreate = "insert into tbfuzzy(namedirectory, arecreate) values ('"
					+ "demo" + "', " + 0 + " );";
			db.execSQL(sqlCreate);
		} else {
			c1.moveToPosition(0);
			int isCreated = c1.getInt(2);
			if (isCreated == 0)
				flag_isRun = true;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		strdirectory = "fz" + dateFormat.format(date);
		c1.close();
		setContentView(R.layout.activity_loadingdata);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// create a new thread
		loadingdata = new Thread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				// kiem tra du lieu trong database
				if (flag_isRun) {
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
									+ "/" + strdirectory + "/");
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
						
						indexWriter.optimize();
						indexWriter.close();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (flag_thread) {
						ContentValues data = new ContentValues();
						data.put("namedirectory", strdirectory);
						data.put("arecreate", 1);
						db.update("tbfuzzy", data, "id= 1", null);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
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
									try {
										Thread.sleep(2000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									flag_thread = false;
									File file = new File(Environment
											.getExternalStorageDirectory()
											.getPath()
											+ "/" + strdirectory);
									if (file.exists()) {
										System.out.println("Xoa file roi");
										recursiveDelete(file);
									}
									ContentValues data = new ContentValues();
									data.put("namedirectory", "demo");
									data.put("arecreate", 0);
									db.update("tbfuzzy", data, "id= 1", null);
									LoadingDataActivity.this.finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) { // if this button is clicked,
													// just close // the dialog
													// box and do nothing
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
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// flag_thread = false;
		super.onDestroy();

	}

	Context getContext() {
		return this.getBaseContext();
	}
}