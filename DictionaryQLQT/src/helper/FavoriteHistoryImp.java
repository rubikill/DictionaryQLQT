package helper;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

public class FavoriteHistoryImp implements IFavoriteHistory{
	DataHelperImpl dataHelper;
	public FavoriteHistoryImp(Context context)
	{
		dataHelper = new DataHelperImpl(context);
	}

	@Override
	public void insertWord(String word, String nametable) {
		// TODO Auto-generated method stub
		dataHelper.insert(word, nametable);
		
	}

	@Override
	public ArrayList<String> readTable(String nametable) {
		// TODO Auto-generated method stub
		Cursor cur = dataHelper.getAll(nametable);
		ArrayList<String> arrResult = new ArrayList<String>();
		while(cur.moveToNext()){
			String temp = cur.getString(1);
			arrResult.add(temp);
		}
		return arrResult;
	}
	
	@Override	
	public boolean isEmpty(String nametable){
		Cursor cur = dataHelper.getAll(nametable);
		return !cur.moveToNext();
	}

	@Override
	public void deleteItem(String word, String nametable) {
		// TODO Auto-generated method stub
		dataHelper.deleteItem(word, nametable);
		
	}

	@Override
	public void deleteAll(String nametable) {
		// TODO Auto-generated method stub
		dataHelper.delteAllItem(nametable);
	}

	@Override
	public Boolean isExists(String word,String nametable) {
		Cursor cursor = dataHelper.getWordInTable(word, nametable);
		return cursor.moveToNext();
	}

}
