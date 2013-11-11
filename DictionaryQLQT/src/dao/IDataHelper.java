package dao;


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
}
