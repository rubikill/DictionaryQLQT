package helper;

import java.util.ArrayList;

public interface IFavoriteHistory {

	void insertWord(String word, String nametable);

	ArrayList<String> readTable(String nametable);

	void deleteItem(String word, String nametable);

	void deleteAll(String nametable);

	Boolean isExists(String word, String nametable);

	boolean isEmpty(String nametable);
}
