package dao;

import java.util.ArrayList;

public interface IFavoriteHistory {
	
	void InsertWord(String word , String nametable);
	ArrayList<String> ReadTable(String nametable);
	void DeleteItem(String word , String nametable);
	void DeleteAll(String nametable);
	Boolean IsExists(String word , String nametable);
}
