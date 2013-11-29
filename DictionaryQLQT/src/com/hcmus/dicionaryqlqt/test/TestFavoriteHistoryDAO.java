package com.hcmus.dicionaryqlqt.test;

import java.io.IOException;
import java.util.ArrayList;


import dao.FavoriteHistoryImp;
import android.test.AndroidTestCase;

public class TestFavoriteHistoryDAO extends AndroidTestCase {

	FavoriteHistoryImp fhDAO;
	
	protected void setUp() throws Exception {
	//	fhDAO = new FavoriteHistoryImp();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test FileOpen(int idAction)
	 */
	public void testOpenHistoryFileSuccess(){
		//assertEquals("History.txt", fhDAO.FileOpen(1));
	}
	
	public void testOpenFavoriteFileSuccess(){
		//assertEquals("Favorite.txt", fhDAO.FileOpen(2));
	}
	
	/**
	 * Test WriteFile(String keyWord, int idAction)
	 * @throws IOException 
	 */
	public void testWriteToFavoriteFileSuccess() throws IOException{
		String keyWord = "Favorite";
		ArrayList<String> expected = new ArrayList<String>();
		expected.add(keyWord);
		
		fhDAO.DeleteAll("Favorite");
		//fhDAO.WriteFile(keyWord, 2);
		ArrayList<String> actual = fhDAO.ReadTable("Favorite");
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test DeleteItem(String word, int idAction)
	 * @throws IOException 
	 */
	public void testDeleteItemInHistoryFileSuccess() throws IOException{
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("Hello");
		expected.add("Man");
		
		fhDAO.DeleteAll("Recent");
		fhDAO.InsertWord("Hello", "Recent");
		fhDAO.InsertWord("School", "Recent");
		fhDAO.InsertWord("Man", "Recent");
		fhDAO.DeleteItem("School", "Recent");
		ArrayList<String> actual = fhDAO.ReadTable("Recent");
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test DeleteAll(int idAction)
	 * @throws IOException 
	 */
	public void testDeleteAllItemInHistoryFileSuccess() throws IOException{
		ArrayList<String> expected = new ArrayList<String>();
		
		fhDAO.DeleteAll("Recent");
		ArrayList<String> actual = fhDAO.ReadTable("Recent");
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test Isexists(String word, int idAction)
	 * @throws IOException 
	 */
	public void testWordIsExistsFileInFavorite() throws IOException{
		fhDAO.DeleteAll("Favorite");
		fhDAO.InsertWord("Hello", "Favorite");
		fhDAO.InsertWord("School", "Favorite");
		fhDAO.InsertWord("Man", "Favorite");
		
		
		boolean actual = fhDAO.IsExists("School", "Favorite");
		boolean expected = true;
		assertEquals(expected, actual);
	}
	
	public void testWordIsNotExistsFileInFavorite() throws IOException{
		fhDAO.DeleteAll("Favorite");
		fhDAO.InsertWord("Hello", "Favorite");
		
		fhDAO.InsertWord("Man", "Favorite");
		boolean actual = fhDAO.IsExists("School", "Favorite");
		boolean expected = false;
		assertEquals(expected, actual);
	}
}
