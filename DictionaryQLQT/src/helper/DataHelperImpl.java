package helper;

import util.Constant;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelperImpl extends SQLiteOpenHelper implements
		IDataHelper {

	private final Context myContext;
	private static SQLiteDatabase myDataBase;

	public DataHelperImpl(Context context) {
		super(context, Constant.DB_NAME, null, 1);
		this.myContext = context;
	}

	@Override
	public void createDataBase() {

		boolean dbExist = checkDataBase();
		SQLiteDatabase db_Read = null;
		if (dbExist) {
			// do nothing - database already exist
		} else {

			db_Read = this.getReadableDatabase();
			db_Read.close();
			copyDataBase();
		}
	}

	@Override
	public boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = Constant.SDCARD_PATH + "/" + Constant.DB_NAME;
			checkDB = SQLiteDatabase
					.openDatabase(
							myPath,
							null,
							(SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS));

		} catch (SQLiteException e) {
			// database does't exist yet.
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	@Override
	public void copyDataBase() {
		IOHelperImpl ioHelper = new IOHelperImpl();
		ioHelper.coppyDataFile(myContext, Constant.DB_NAME);
	}

	@Override
	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = Constant.SDCARD_PATH + "/" + Constant.DB_NAME;
		myDataBase = SQLiteDatabase
				.openDatabase(
						myPath,
						null,
						(SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS));

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public Cursor GetWords(String word, int type) {
		word = word.toLowerCase();
		String tableName;
		if (word.length() > 0) {
			String fc = word.substring(0, 1).toUpperCase();
			if (fc.equals("A") || fc.equals("B") || fc.equals("C"))
				tableName = "WordABC";
			else if (fc.equals("D") || fc.equals("E") || fc.equals("F"))
				tableName = "WordDEF";
			else if (fc.equals("G") || fc.equals("H") || fc.equals("I"))
				tableName = "WordGHI";
			else if (fc.equals("J") || fc.equals("K") || fc.equals("L"))
				tableName = "WordJKL";
			else if (fc.equals("M") || fc.equals("N") || fc.equals("O"))
				tableName = "WordMNO";
			else if (fc.equals("P") || fc.equals("Q") || fc.equals("R"))
				tableName = "WordPQR";
			else if (fc.equals("S") || fc.equals("T") || fc.equals("U"))
				tableName = "WordSTU";
			else if (fc.equals("V") || fc.equals("W") || fc.equals("Y"))
				tableName = "WordVWY";
			else
				tableName = "WordOther";
			String condition = (type == Constant.GET_ONE) ? "= '" + word + "'"
					: "like '" + word + "%' limit 12";
			String sql = "select ID,Word,Idx,Length from " + tableName
					+ " where Word " + condition;
			Cursor cur = myDataBase.rawQuery(sql, null);
			return cur;
		}
		return null;
	}

	@Override
	public void insert(String word, String nametable) {
		String sql = "Insert into " + nametable + " (word)  values ('" + word
				+ "')";
		myDataBase.execSQL(sql);
	}

	@Override
	public Cursor getAll(String nameTable) {
		String sql = "select * from " + nameTable;
		Cursor cur = myDataBase.rawQuery(sql, null);
		return cur;
	}

	@Override
	public Cursor getWordInTable(String word, String nameTable) {
		String sql = "select word from " + nameTable + " where word = '" + word
				+ "'";
		Cursor cur = myDataBase.rawQuery(sql, null);
		return cur;
	}

	@Override
	public void deleteItem(String word, String nametable) {
		String sql = "DELETE FROM " + nametable + " WHERE word = '" + word
				+ "'";
		myDataBase.execSQL(sql);
	}

	@Override
	public void delteAllItem(String nametable) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM " + nametable;
		myDataBase.execSQL(sql);
	}

}