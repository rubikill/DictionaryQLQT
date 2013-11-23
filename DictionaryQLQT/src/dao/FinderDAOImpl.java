package dao;


import java.io.IOException;
import java.util.ArrayList;

import model.Vocabulary;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

public class FinderDAOImpl implements IFinderDAO{
	DatabaseHelperDAOImpl myDatabaseHelper;
	FileHelperDAOImpl myFileHelper;
	public FinderDAOImpl(DatabaseHelperDAOImpl databaseHelper,FileHelperDAOImpl fileHelper)
	{
		this.myDatabaseHelper = databaseHelper;
		this.myFileHelper = fileHelper;
	}
	@Override
	public Vocabulary find(String keyWord) {
		Vocabulary vocabulary = null;
		Cursor cur = myDatabaseHelper.GetWords(keyWord,DatabaseHelperDAOImpl.GET_ONE);
		if (cur.moveToFirst()) {
			vocabulary=new Vocabulary();;
			vocabulary.setWord(cur.getString(1));
			vocabulary.setIndex(cur.getString(2));
			vocabulary.setLength(cur.getString(3));
		}
		return vocabulary;
	}

	@Override
	public ArrayList<Vocabulary> getRecommendWord(String keyWord) {
		ArrayList<Vocabulary> vocabularies = new ArrayList<Vocabulary>();
		try {
			Cursor cur = myDatabaseHelper.GetWords(keyWord,DatabaseHelperDAOImpl.GET_MULTI);
			if (cur.moveToFirst()) {
				do {
					Vocabulary newVocabulary = new Vocabulary();
					newVocabulary.setWord(cur.getString(1));
					newVocabulary.setIndex(cur.getString(2));
					newVocabulary.setLength(cur.getString(3));
					vocabularies.add(newVocabulary);
				} while (cur.moveToNext());
			}

		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		if (vocabularies.size() == 0) { //kiem tra size cua vocabularies recommend
			//rong thi tao ra similar de fuzzy search
			Vocabulary vocSimilar = new Vocabulary();
			vocSimilar.setWord(keyWord + "---Not found, you can try similar");
			vocSimilar.setIndex("A");
			vocSimilar.setLength("A");
			vocabularies.add(vocSimilar);
		}
		return vocabularies;
	}
	FavoriteHistoryDAO favoriteHistoryDao = new FavoriteHistoryDAO();
	@Override
	public String getMean(Vocabulary vocabulary) {
		
		try {
			favoriteHistoryDao.WriteFile(vocabulary.getWord(), 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myFileHelper.getMean(vocabulary.getIndex(), vocabulary.getLength());
	}
	@Override
	public String getMean(String index, String length) {
		return myFileHelper.getMean(index, length);
	}
	
	
}
