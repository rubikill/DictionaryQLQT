package com.hcmus.dictionaryqlqt.test;

import com.hcmus.dictionaryqlqt.TabDictionaryActivity;

import android.test.AndroidTestCase;

public class TestTabDictionaryActivity extends AndroidTestCase {

	TabDictionaryActivity dictionary;
	
	protected void setUp() throws Exception {
		dictionary = new TabDictionaryActivity();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected void testSearch(){
		String wordkey = "love";
		String meaning = dictionary.Search(wordkey);
		String expectMeaning = "tình yêu";
		int exMeaningLen = 8;
		String ouputMeaning = meaning.substring(0, exMeaningLen);
		assertEquals(expectMeaning, ouputMeaning);
	}
	
	protected void testSearch2(){
		String wordkey = "supermarket";
		String meaning = dictionary.Search(wordkey);
		String expectMeaning = "siêu thị";
		int exMeaningLen = 8;
		String ouputMeaning = meaning.substring(0, exMeaningLen);
		assertEquals(expectMeaning, ouputMeaning);
	}
	
	protected void testSearch3(){
		String wordkey = "volcano";
		String meaning = dictionary.Search(wordkey);
		String expectMeaning = "núi lửa";
		int exMeaningLen = 7;
		String ouputMeaning = meaning.substring(0, exMeaningLen);
		assertEquals(expectMeaning, ouputMeaning);
	}
	
	protected void testSearchNotFound(){
		String wordkey = "00day";
		String meaning = dictionary.Search(wordkey);
		String expectMeaning = "Word not found!";
		assertEquals(expectMeaning, meaning);
	}
	
	protected void testSearchNotFound1(){
		String wordkey = "no problem";
		String meaning = dictionary.Search(wordkey);
		String expectMeaning = "Word not found!";
		assertEquals(expectMeaning, meaning);
	}
	
	protected void testSearchNotFound2(){
		String wordkey = "overhere";
		String meaning = dictionary.Search(wordkey);
		String expectMeaning = "Wor";
		assertEquals(expectMeaning, meaning);
	}
	
}
