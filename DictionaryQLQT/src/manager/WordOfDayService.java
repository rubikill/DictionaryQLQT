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
 *	Class lay wod tu service http://khanhminh.somee.com/wordofday
 */

public class WordOfDayService implements IWordOfDayService{
	
	/***
	 * lay wod tu service
	 * @param url: duong dan toi service
	 * @return wod cua ngay
	 */
	@Override
	public WordOfDay getWod(String url) throws ClientProtocolException, IOException,
			ParseException, JSONException {
		// tao request ket noi den service
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response = httpClient.execute(get);
		HttpEntity entity = response.getEntity();

		WordOfDay word = null;					
		if (entity != null) {			
			// lay du lieu tra ve tu response voi dinh dang json
			JSONObject respObject = new JSONObject(EntityUtils.toString(entity));
			boolean isSuccess = respObject.getBoolean("success");
			// kiem tra trang thai la success hay fail
			if (isSuccess){				
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
