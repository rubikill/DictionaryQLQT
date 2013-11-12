package com.hcmus.dictionaryqlqt.test;

import java.util.ArrayList;

import com.hcmus.dictionaryqlqt.TabDictionaryActivity;

import model.Vocabulary;

import dao.DatabaseHelperDAOImpl;
import dao.FileHelperDAOImpl;
import dao.FinderDAOImpl;

import android.test.AndroidTestCase;

public class TestFileHelperDAO extends AndroidTestCase {

	FinderDAOImpl finderDAO;
	FileHelperDAOImpl fileDAO = new FileHelperDAOImpl();
	Vocabulary newVocabulary;
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

//	public void testGetRecommentWord(){
//		ArrayList<Vocabulary> vocabularies = finderDAO.getRecommendWord("love");
//		ArrayList<Vocabulary> expectedVocabularies = new ArrayList<Vocabulary>();
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("love");
//		newVocabulary.setIndex("UeH9");
//		newVocabulary.setLength("V+");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("loveable");
//		newVocabulary.setIndex("wsbm");
//		newVocabulary.setLength("l");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("love-affair");
//		newVocabulary.setIndex("Ued7");
//		newVocabulary.setLength("BT");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("love-apple");
//		newVocabulary.setIndex("UefO");
//		newVocabulary.setLength("BH");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("love-begotten");
//		newVocabulary.setIndex("UegV");
//		newVocabulary.setLength("8");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("love-bird");
//		newVocabulary.setIndex("UehR");
//		newVocabulary.setLength("BJ");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("lovebird");
//		newVocabulary.setIndex("wscL");
//		newVocabulary.setLength("BJ");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("love-child");
//		newVocabulary.setIndex("Ueia");
//		newVocabulary.setLength("1");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("loved");
//		newVocabulary.setIndex("wsdU");
//		newVocabulary.setLength("r");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("love-favour");
//		newVocabulary.setIndex("wsU8");
//		newVocabulary.setLength("9");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("love-hate relationship");
//		newVocabulary.setIndex("wsV5");
//		newVocabulary.setLength("7");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("love-knot");
//		newVocabulary.setIndex("UejP");
//		newVocabulary.setLength("/");
//		expectedVocabularies.add(newVocabulary);
//		
//		assertEquals(expectedVocabularies, vocabularies);
//	}
//	
//	public void testGetRecommentWord1(){
//		ArrayList<Vocabulary> vocabularies = finderDAO.getRecommendWord("h");
//		ArrayList<Vocabulary> expectedVocabularies = new ArrayList<Vocabulary>();
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("h");
//		newVocabulary.setIndex("O0BE");
//		newVocabulary.setLength("BQ");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("ha");
//		newVocabulary.setIndex("O0FH");
//		newVocabulary.setLength("C2");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("ha ha");
//		newVocabulary.setIndex("O0H9");
//		newVocabulary.setLength("CJ");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("haaf");
//		newVocabulary.setIndex("O0NQ");
//		newVocabulary.setLength("BD");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("haangi");
//		newVocabulary.setIndex("vxVz");
//		newVocabulary.setLength("Br");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("haar");
//		newVocabulary.setIndex("vxXe");
//		newVocabulary.setLength("BY");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("Haavelmo, Trygve");
//		newVocabulary.setIndex("qwIQ");
//		newVocabulary.setLength("ah");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("habanera");
//		newVocabulary.setIndex("O0OT");
//		newVocabulary.setLength("B4");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("habdalah");
//		newVocabulary.setIndex("vxY2");
//		newVocabulary.setLength("Bg");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("habeas corpus");
//		newVocabulary.setIndex("O0QL");
//		newVocabulary.setLength("ED");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("haberdasher");
//		newVocabulary.setIndex("O0UO");
//		newVocabulary.setLength("CZ");
//		expectedVocabularies.add(newVocabulary);
//		
//		newVocabulary = new Vocabulary();
//		newVocabulary.setWord("haberdashery");
//		newVocabulary.setIndex("O0Wn");
//		newVocabulary.setLength("DZ");
//		expectedVocabularies.add(newVocabulary);
//		
//		assertEquals(expectedVocabularies, vocabularies);
//	}
	
	public void testGetDemicalValue(){
		assertEquals(1406, fileDAO.getDemicalValue("V+"));
	}
	
	public void testGetDemicalValue1(){
		assertEquals(5366269, fileDAO.getDemicalValue("UeH9"));
	}
	
	public void testGetMean(){
		String expectedMean = "tình yêu";
		assertEquals(expectedMean, fileDAO.getMean("V+", "UeH9"));
	}
}
