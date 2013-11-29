package dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

public class FavoriteHistoryImp implements IFavoriteHistory{
	DatabaseHelperDAOImpl dataHelper;
	public FavoriteHistoryImp(Context context)
	{
		dataHelper = new DatabaseHelperDAOImpl(context);
	}

	@Override
	public void InsertWord(String word, String nametable) {
		// TODO Auto-generated method stub
		dataHelper.Insert(word, nametable);
		
	}

	@Override
	public ArrayList<String> ReadTable(String nametable) {
		// TODO Auto-generated method stub
		Cursor cur = dataHelper.GetAll(nametable);
		ArrayList<String> arrResult = new ArrayList<String>();
		while(cur.moveToNext()){
			String temp = cur.getString(1);
			arrResult.add(temp);
		}
		return arrResult;
	}

	@Override
	public void DeleteItem(String word, String nametable) {
		// TODO Auto-generated method stub
		dataHelper.DeleteItem(word, nametable);
		
	}

	@Override
	public void DeleteAll(String nametable) {
		// TODO Auto-generated method stub
		dataHelper.DelteAllItem(nametable);
	}

	@Override
	public Boolean IsExists(String word,String nametable) {
		Cursor cursor = dataHelper.getWordInTable(word, nametable);
		return cursor.moveToNext();
	}

}
