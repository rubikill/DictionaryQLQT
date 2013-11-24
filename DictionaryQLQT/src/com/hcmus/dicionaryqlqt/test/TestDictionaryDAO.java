package com.hcmus.dicionaryqlqt.test;

import java.util.ArrayList;
import model.Vocabulary;
import dao.DatabaseHelperDAOImpl;
import dao.FileHelperDAOImpl;
import dao.FinderDAOImpl;
import android.test.AndroidTestCase;

public class TestDictionaryDAO extends AndroidTestCase {
	
	FinderDAOImpl finderDAO;
	DatabaseHelperDAOImpl databaseDAO;
	FileHelperDAOImpl fileDAO;
	Vocabulary newVocabulary;
	
	protected void setUp() throws Exception {
		databaseDAO = new DatabaseHelperDAOImpl(this.mContext);
		try {
			databaseDAO.openDataBase();// mo databases
		} catch (Exception e) {
			// neu mo that bai => chua co data => coppy data tu asset
			databaseDAO.createDataBase();
			databaseDAO.openDataBase();
		}
		
		fileDAO = new FileHelperDAOImpl();
		
		finderDAO = new FinderDAOImpl(databaseDAO, fileDAO);
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test ham getRecommendWord(String keyWord) trong class FinderDAOImpl.java
	 */
	public void testGetRecommentWordWithKeywordSuccess(){
		String keyWord = "love";
		ArrayList<String> actual = new ArrayList<String>();
		ArrayList<Vocabulary> vocabularies = finderDAO.getRecommendWord(keyWord);
		for (Vocabulary vocabulary : vocabularies) {
			actual.add(vocabulary.getWord());
		}
		
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("love");
		expected.add("loveable");
		expected.add("love-affair");
		expected.add("love-apple");
		expected.add("love-begotten");
		expected.add("love-bird");
		expected.add("lovebird");
		expected.add("love-child");
		expected.add("loved");
		expected.add("love-favour");
		expected.add("love-hate relationship");
		expected.add("love-knot");
		
		assertEquals(expected, actual);
	}

	/**
	 * Test ham getDemicalValue(String str) trong class FileHelperDAOImpl.java
	 */
	public void testConvertLengthValueToDemicalValueSuccess(){
		assertEquals(1406, fileDAO.getDemicalValue("V+"));
	}
	
	public void testConvertIndexValueToDemicalValueSuccess(){
		assertEquals(5366269, fileDAO.getDemicalValue("UeH9"));
	}
	
	/**
	 * Test ham find(String keyWord) trong finderDAOImpl.java
	 */
	public void testFindWordWithWordKeySuccess(){
		Vocabulary vocabulary = finderDAO.find("normal");
		
		Vocabulary expectedVocabulary = new Vocabulary();
		expectedVocabulary.setWord("normal");
		expectedVocabulary.setIndex("Wo25");
		expectedVocabulary.setLength("IW");
		
		assertEquals(expectedVocabulary.getWord() + expectedVocabulary.getIndex() + expectedVocabulary.getLength(), 
				vocabulary.getWord() + vocabulary.getIndex() + vocabulary.getLength());
	}
}
