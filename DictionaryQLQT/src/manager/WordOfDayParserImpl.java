package manager;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.hcmus.dictionaryqlqt.R;

import android.content.Context;
import android.util.Log;

import model.WordOfDay;

/**
 * Class WordOfDayParserImpl parse wod tu web implements interface IWordOfDayPaser
 * 
 * @author Minh Khanh
 *
 */

public class WordOfDayParserImpl implements IWordOfDayPaser{
	
	/* content de truy xuat resource */
	private Context context;
	
	/***
	 * ham khoi tao
	 * @param context: context cua ung dung
	 */
	public WordOfDayParserImpl(Context context){
		this.context = context;
	}
	
	/***
	 * parse wod tu trang web
	 * @param url: link toi dia chi trang web
	 * @return wod cua ngay
	 */
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
	
	/***
	 * ham lay ve tai lieu html
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private Document getDocument(String url) throws IOException{
		Document doc = null;
		doc = (Document) Jsoup.connect(url).get();		
		
		return doc;
	}
	
	/***
	 * lay string tu resource
	 * @param id: id cua string	 * 
	 * @return string
	 */
	private String getStringClassName(int id){
		String className = context.getResources()
				.getString(id);
		
		return className;
	}
	
	/***
	 * lay ngay thang
	 * @param doc
	 * @return
	 */
	private String getDate(Document doc){			
		return getValueTagByClass(doc,
				getStringClassName(R.string.date_class_name));
	}
	
	/***
	 * lay tu
	 * @param doc
	 * @return
	 */
	private String getWord(Document doc){
		return getValueTagByClass(doc,
				getStringClassName(R.string.word_class_name));
	}
	
	/**
	 * lay phien am
	 * @param doc
	 * @return
	 */
	private String getPhonetic(Document doc){
		return getValueTagByClass(doc,
				getStringClassName(R.string.phonetic_class_name));
	}
	
	/**
	 * lay loai tu
	 * @param doc
	 * @return
	 */
	private String getWordFunction(Document doc){
		return getValueTagByClass(doc,
				getStringClassName(R.string.word_function_class_name));
	}
	
	/***
	 * lay nghia tu
	 * @param doc
	 * @return
	 */
	private String getMean(Document doc){
		return getValueTagByClass(doc,
				getStringClassName(R.string.mean_class_name));
	}
	
	/**
	 * lay example
	 * @param doc
	 * @return
	 */
	private String getExamples(Document doc){
		String result = "";
		Elements tags = doc.getElementsByClass(
				getStringClassName(R.string.examples_class_name));
		if (tags != null && tags.size() > 0){
			result = tags.get(0).html();
		}
		return result;
	}
	
	/**
	 * lay didyouknow
	 * @param doc
	 * @return
	 */
	private String getDidYouKnow(Document doc){
		String result = "";
		Elements tags = doc.getElementsByClass(
				getStringClassName(R.string.didyouknow_class_name));
		if (tags != null && tags.size() > 1){
			result = tags.get(1).html();
			int index = result.indexOf("<!-- BEGIN QUIZ -->");
			if (index != -1){
				result = result.substring(0, index - 1);
			}
		}
		return result;
	}
	
	/**
	 * lay noi dung the html dua vao class
	 * @param doc
	 * @param className
	 * @return
	 */
	private String getValueTagByClass(Document doc, String className){
		String result = "";
		Elements tags = doc.getElementsByClass(className);
		if (tags != null && !tags.isEmpty()){
			result = tags.get(0).text();
		}
		else{
			Log.e("WOD Parser", "format changed: " + className);
		}
		
		return result;
	}

}
