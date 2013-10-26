package dao;


import android.database.Cursor;

public interface IDataHelper {
	void createDataBase();
	boolean checkDataBase();
	void copyDataBase();
	void openDataBase();
	Cursor GetWords(String word, int type);
}
