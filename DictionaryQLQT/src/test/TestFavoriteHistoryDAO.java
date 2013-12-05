package test;

import helper.IFavoriteHistory;

import java.io.IOException;
import java.util.ArrayList;

import android.test.AndroidTestCase;

public class TestFavoriteHistoryDAO extends AndroidTestCase {

	IFavoriteHistory fhDAO;
	
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
		
		fhDAO.deleteAll("Favorite");
		//fhDAO.WriteFile(keyWord, 2);
		ArrayList<String> actual = fhDAO.readTable("Favorite");
		
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
		
		fhDAO.deleteAll("Recent");
		fhDAO.insertWord("Hello", "Recent");
		fhDAO.insertWord("School", "Recent");
		fhDAO.insertWord("Man", "Recent");
		fhDAO.deleteItem("School", "Recent");
		ArrayList<String> actual = fhDAO.readTable("Recent");
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test DeleteAll(int idAction)
	 * @throws IOException 
	 */
	public void testDeleteAllItemInHistoryFileSuccess() throws IOException{
		ArrayList<String> expected = new ArrayList<String>();
		
		fhDAO.deleteAll("Recent");
		ArrayList<String> actual = fhDAO.readTable("Recent");
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test Isexists(String word, int idAction)
	 * @throws IOException 
	 */
	public void testWordIsExistsFileInFavorite() throws IOException{
		fhDAO.deleteAll("Favorite");
		fhDAO.insertWord("Hello", "Favorite");
		fhDAO.insertWord("School", "Favorite");
		fhDAO.insertWord("Man", "Favorite");
		
		
		boolean actual = fhDAO.isExists("School", "Favorite");
		boolean expected = true;
		assertEquals(expected, actual);
	}
	
	public void testWordIsNotExistsFileInFavorite() throws IOException{
		fhDAO.deleteAll("Favorite");
		fhDAO.insertWord("Hello", "Favorite");
		
		fhDAO.insertWord("Man", "Favorite");
		boolean actual = fhDAO.isExists("School", "Favorite");
		boolean expected = false;
		assertEquals(expected, actual);
	}
}
