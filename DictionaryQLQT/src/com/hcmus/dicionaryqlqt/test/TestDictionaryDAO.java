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
	public void testGetRecommentWord(){
		ArrayList<String> actual = new ArrayList<String>();
		ArrayList<Vocabulary> vocabularies = finderDAO.getRecommendWord("love");
		for (Vocabulary vocabulary : vocabularies) {
			actual.add(vocabulary.getWord() + vocabulary.getIndex() + vocabulary.getLength());
		}
		
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("love" + "UeH9" + "V+");
		expected.add("loveable" + "wsbm" + "l");
		expected.add("love-affair" + "Ued7" + "BT");
		expected.add("love-apple" + "UefO" + "BH");
		expected.add("love-begotten" + "UegV" + "8");
		expected.add("love-bird" + "UehR" + "BJ");
		expected.add("lovebird" + "wscL" + "BJ");
		expected.add("love-child" + "Ueia" + "1");
		expected.add("loved" + "wsdU" + "r");
		expected.add("love-favour" + "wsU8" + "9");
		expected.add("love-hate relationship" + "wsV5" + "7");
		expected.add("love-knot" + "UejP" + "/");
		
		assertEquals(expected, actual);
	}

	/**
	 * Test ham getDemicalValue(String str) trong class FileHelperDAOImpl.java
	 */
	public void testConvertLengthValueToDemicalValue(){
		assertEquals(1406, fileDAO.getDemicalValue("V+"));
	}
	
	public void testConvertIndexValueToDemicalValue(){
		assertEquals(5366269, fileDAO.getDemicalValue("UeH9"));
	}
	
	/**
	 * Test ham find(String keyWord) trong finderDAOImpl.java
	 */
	public void testFindWordWithWordKey(){
		Vocabulary vocabulary = finderDAO.find("normal");
		
		Vocabulary expectedVocabulary = new Vocabulary();
		expectedVocabulary.setWord("normal");
		expectedVocabulary.setIndex("Wo25");
		expectedVocabulary.setLength("IW");
		
		assertEquals(expectedVocabulary.getWord() + expectedVocabulary.getIndex() + expectedVocabulary.getLength(), 
				vocabulary.getWord() + vocabulary.getIndex() + vocabulary.getLength());
	}
}
