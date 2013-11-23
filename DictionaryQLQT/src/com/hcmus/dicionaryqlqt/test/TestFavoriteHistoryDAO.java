package com.hcmus.dicionaryqlqt.test;

import java.io.IOException;
import java.util.ArrayList;

import dao.FavoriteHistoryDAO;
import android.test.AndroidTestCase;

public class TestFavoriteHistoryDAO extends AndroidTestCase {

	FavoriteHistoryDAO fhDAO;
	
	protected void setUp() throws Exception {
		fhDAO = new FavoriteHistoryDAO();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test FileOpen(int idAction)
	 */
	public void testOpenHistoryFile(){
		assertEquals("History.txt", fhDAO.FileOpen(1));
	}
	
	public void testOpenFavoriteFile(){
		assertEquals("Favorite.txt", fhDAO.FileOpen(2));
	}
	
	/**
	 * Test WriteFile(String keyWord, int idAction)
	 * @throws IOException 
	 */
	public void testWriteToFavoriteFile() throws IOException{
		String keyWord = "Favorite";
		ArrayList<String> expected = new ArrayList<String>();
		expected.add(keyWord);
		
		fhDAO.DeleteAll(2);
		fhDAO.WriteFile(keyWord, 2);
		ArrayList<String> actual = fhDAO.ReadFile(2);
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test DeleteItem(String word, int idAction)
	 * @throws IOException 
	 */
	public void testDeleteItemInHistoryFile() throws IOException{
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("Hello");
		expected.add("Man");
		
		fhDAO.DeleteAll(1);
		fhDAO.WriteFile("Hello", 1);
		fhDAO.WriteFile("School", 1);
		fhDAO.WriteFile("Man", 1);
		fhDAO.DeleteItem("School", 1);
		ArrayList<String> actual = fhDAO.ReadFile(1);
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test DeleteAll(int idAction)
	 * @throws IOException 
	 */
	public void testDeleteAllItemInHistoryFile() throws IOException{
		ArrayList<String> expected = new ArrayList<String>();
		
		fhDAO.DeleteAll(1);
		ArrayList<String> actual = fhDAO.ReadFile(1);
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test Isexists(String word, int idAction)
	 * @throws IOException 
	 */
	public void testWordIsExistsFileInFavorite() throws IOException{
		fhDAO.DeleteAll(2);
		fhDAO.WriteFile("Hello", 2);
		fhDAO.WriteFile("School", 2);
		fhDAO.WriteFile("Man", 2);
		boolean actual = fhDAO.Isexists("School", 2);
		boolean expected = true;
		assertEquals(expected, actual);
	}
	
	public void testWordIsNotExistsFileInFavorite() throws IOException{
		fhDAO.DeleteAll(2);
		fhDAO.WriteFile("Hello", 2);
		fhDAO.WriteFile("Man", 2);
		boolean actual = fhDAO.Isexists("School", 2);
		boolean expected = false;
		assertEquals(expected, actual);
	}
}
