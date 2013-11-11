package manager;

import java.io.IOException;

import model.WordOfDay;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Minh Khanh
 *	
 *	Class lấy wod từ service
 */

public class WordOfDayService implements IWordOfDayService{
	
	/*
	 * lấy wod từ url
	 */
	@Override
	public WordOfDay getWod(String url) throws ClientProtocolException, IOException,
			ParseException, JSONException {
		// kết nối đến server để lấy dữ liệu
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response = httpClient.execute(get);
		HttpEntity entity = response.getEntity();

		WordOfDay word = null;					
		if (entity != null) {			
			// nếu lấy dữ liệu thành công
			JSONObject respObject = new JSONObject(EntityUtils.toString(entity));
			boolean isSuccess = respObject.getBoolean("success");
			// kiểm tra kết quả trả về từ server
			if (isSuccess){
				// nếu thành công, lấy từ trong dữ liệu nhận về
				JSONObject wod = respObject.getJSONObject("word");
				word = new WordOfDay();
				word.setWord(wod.getString("word"));
				word.setMean(wod.getString("mean"));
				word.setDate(wod.getString("date"));
				word.setDidYouKnow(wod.getString("didyouknow"));
				word.setExamples(wod.getString("examples"));
				word.setPhonetic(wod.getString("phonetic"));
				word.setWordFunction(wod.getString("wordfunction"));
			}
		}
		
		return word;
	}
}
