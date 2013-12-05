package helper;

import android.database.Cursor;

//Lop thao tac sqlite
public interface IDataHelper {
	// tao database
	void createDataBase();

	// kiem tra data co trong sdcard chua
	boolean checkDataBase();

	// coppy data tu assets vao sdcard
	void copyDataBase();

	// mo data de su dung
	void openDataBase();

	// tim danh sach tu goi y
	Cursor GetWords(String word, int type);

	void insert(String word, String nameTable);

	Cursor getAll(String nameTable);

	Cursor getWordInTable(String word, String nameTable);

	void deleteItem(String word, String nametable);

	void delteAllItem(String nametable);
}
