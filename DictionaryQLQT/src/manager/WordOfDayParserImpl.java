package manager;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import model.WordOfDay;

/**
 * Class WordOfDayParserImpl dùng để lấy thông tin Wod từ web implements interface IWordOfDayPaser
 * 
 * @author Minh Khanh
 *
 */

public class WordOfDayParserImpl implements IWordOfDayPaser{

	private static String Word_Class_Name = "";
	private static String Date_Class_Name = "";
	private static String Phonetic_Class_Name = "pron";
	private static String Mean_Class_Name = "ssens";
	private static String Examples_Class_Name = "word_example_didu";
	private static String DidYouKnow_Class_Name = "word_example_didu";
	private static String WordFunction_Class_Name = "word_function";
	
	@Override
	public WordOfDay parser(String url) throws IOException {
		
		Document doc = getDocument(url);
		WordOfDay wod = new WordOfDay();
		wod.setWord(getWord(doc));
		wod.setMean(getMean(doc));
		wod.setDate(getDate(doc));
		wod.setDidYouKnow(getDidYouKnow(doc));
		wod.setExamples(getExamples(doc));
		wod.setPhonetic(getPhonetic(doc));
		wod.setWordFunction(getWordFunction(doc));
		
		return wod;
	}
	
	private Document getDocument(String url) throws IOException{
		Document doc = null;
		doc = (Document) Jsoup.connect(url).get();
		
		return doc;
	}
	
	private String getDate(Document doc){		
		return getValueTagByClass(doc, Date_Class_Name);
	}
	
	private String getWord(Document doc){
		return getValueTagByClass(doc, Word_Class_Name);
	}
	
	private String getPhonetic(Document doc){
		return getValueTagByClass(doc, Phonetic_Class_Name);
	}
	
	private String getWordFunction(Document doc){
		return getValueTagByClass(doc, WordFunction_Class_Name);
	}
	
	private String getMean(Document doc){
		return getValueTagByClass(doc, Mean_Class_Name);
	}
	
	private ArrayList<String> getExamples(Document doc){


		return null;
	}
	
	private ArrayList<String> getDidYouKnow(Document doc){


		return null;
	}
	
	private String getValueTagByClass(Document doc, String className){
		String result = "";
		Elements tags = doc.getElementsByClass(className);
		if (tags != null && !tags.isEmpty()){
			result = tags.get(0).text();
		}
		
		return result;
	}

}
