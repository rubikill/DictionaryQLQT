package dao;


import java.util.ArrayList;

import android.database.Cursor;
//Lop thao tac sqlite
public interface IDataHelper {
	//tao database
	void createDataBase();
	
	
	//kiem tra data co trong sdcard chua
	boolean checkDataBase();
	
	//coppy data tu assets vao sdcard
	void copyDataBase();
	
	//mo data de su dung
	void openDataBase();
	
	//tim danh sach tu goi y
	Cursor GetWords(String word, int type);
	
	void  Insert(String word , String nameTable);
	Cursor GetAll(String nameTable);
	Cursor getWordInTable(String word, String nameTable);
	void DeleteItem(String word , String nametable);
	void DelteAllItem (String nametable);
}
