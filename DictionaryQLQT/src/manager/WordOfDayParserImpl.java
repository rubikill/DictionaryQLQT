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
 * Class WordOfDayParserImpl duÌ€ng Ä‘ÃªÌ‰ lÃ¢Ì�y thÃ´ng tin Wod tÆ°Ì€ web implements interface IWordOfDayPaser
 * 
 * @author Minh Khanh
 *
 */

public class WordOfDayParserImpl implements IWordOfDayPaser{
	
	/* content của để truy suất resource */
	private Context context;
	
	public WordOfDayParserImpl(Context context){
		this.context = context;
	}
	
	/*
	 * hàm phân tích và lấy Wod từ url
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
	
	/*
	 * lấy dom của trang html
	 */
	private Document getDocument(String url) throws IOException{
		Document doc = null;
		doc = (Document) Jsoup.connect(url).get();		
		
		return doc;
	}
	
	/*
	 * lấy chuỗi từ resource
	 */
	private String getStringClassName(int id){
		String className = context.getResources()
				.getString(id);
		
		return className;
	}
	
	/*
	 * lấy thông tin về ngày trong html
	 */
	private String getDate(Document doc){			
		return getValueTagByClass(doc,
				getStringClassName(R.string.date_class_name));
	}
	
	/*
	 * lấy từ trong html
	 */
	private String getWord(Document doc){
		return getValueTagByClass(doc,
				getStringClassName(R.string.word_class_name));
	}
	
	/*
	 * lấy phiên âm
	 */
	private String getPhonetic(Document doc){
		return getValueTagByClass(doc,
				getStringClassName(R.string.phonetic_class_name));
	}
	
	/*
	 * lấy loại từ
	 */
	private String getWordFunction(Document doc){
		return getValueTagByClass(doc,
				getStringClassName(R.string.word_function_class_name));
	}
	
	/*
	 * lấy nghĩa của từ
	 */
	private String getMean(Document doc){
		return getValueTagByClass(doc,
				getStringClassName(R.string.mean_class_name));
	}
	
	/*
	 * lấy ví dụ
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
	
	/*
	 * lấy phần did you know
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
	
	/*
	 * lấy text của một thẻ html theo tên class
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
