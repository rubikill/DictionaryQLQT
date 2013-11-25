package webservice;

import java.io.Console;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class WebserviceHelperImpl implements IWebserviceHelper {

	@Override
	public boolean rate(double rate) {

		return false;
	}

	@Override
	public boolean feedbeak(String username, String message) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://www.qlqtpmn2.somee.com/api/androidapp");
		post.setHeader("Content-type", "application/json");
		post.setHeader("Accept", "application/json");
		JSONObject obj = new JSONObject();
		try {
			obj.put("id", "0");
			obj.put("username", "abcd");
			obj.put("message", "1234");
			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

}
