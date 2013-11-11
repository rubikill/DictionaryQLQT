package manager;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import model.WordOfDay;

public interface IWordOfDayService {
	WordOfDay getWod(String url) throws ClientProtocolException, IOException, ParseException, JSONException;
}
