package webservice;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class WebserviceHelperImpl implements IWebserviceHelper {
	String url= "http://www.qlqtpmn2.somee.com/api/androidapp";

	@Override
	public boolean rate(double rate) {

		return false;
	}

	@Override
	public boolean feedbeak(String username, String message) {
		HttpClient client = new DefaultHttpClient();
		 HttpGet httpget = new HttpGet(url); 

		    // Execute the request
		    HttpResponse response;
		    try {
		        response = client.execute(httpget);
		        // Examine the response status
		        Log.i("Praeda",response.getStatusLine().toString());

		        // Get hold of the response entity
		        HttpEntity entity = response.getEntity();
		        // If the response does not enclose an entity, there is no need
		        // to worry about connection release

		        if (entity != null) {

		            // A Simple JSON Response Read
		            InputStream instream = entity.getContent();
		            //String result= convertStreamToString(instream);
		            // now you have the string representation of the HTML request
		            instream.close();
		        }


		    } catch (Exception e) {
		    	return false;
		    }
		return true;
	}

}
