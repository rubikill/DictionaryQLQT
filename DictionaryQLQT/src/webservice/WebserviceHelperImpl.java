package webservice;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import util.Constant;

public class WebserviceHelperImpl implements IWebserviceHelper {

	@Override
	public boolean rate(double rate) {

		return false;
	}

	@Override
	public boolean feedbeak(String username, String message) {
		HttpClient client = new DefaultHttpClient();

		try {
			message = message.replaceAll(" ", "%20");
			String path = Constant.WEB_SERVICE_URL + "/0-" + username + "-"
					+ message;
			HttpGet httpget = new HttpGet(path);
			client.execute(httpget);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

//	private String get() {
//		String result = null;
//		HttpClient client = new DefaultHttpClient();
//		HttpGet httpget = new HttpGet(Constant.WEB_SERVICE_URL);
//
//		// Execute the request
//		HttpResponse response;
//		try {
//
//			response = client.execute(httpget);
//			// Examine the response status
//			Log.i("Praeda", response.getStatusLine().toString());
//
//			// Get hold of the response entity
//			HttpEntity entity = response.getEntity();
//			// If the response does not enclose an entity, there is no need
//			// to worry about connection release
//
//			if (entity != null) {
//
//				// A Simple JSON Response Read
//				InputStream instream = entity.getContent();
//				result = convertStreamToString(instream);
//				// now you have the string representation of the HTML request
//				instream.close();
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	private static String convertStreamToString(InputStream is) {
//		/*
//		 * To convert the InputStream to String we use the
//		 * BufferedReader.readLine() method. We iterate until the BufferedReader
//		 * return null which means there's no more data to read. Each line will
//		 * appended to a StringBuilder and returned as String.
//		 */
//		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//		StringBuilder sb = new StringBuilder();
//
//		String line = null;
//		try {
//			while ((line = reader.readLine()) != null) {
//				sb.append(line + "\n");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				is.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return sb.toString();
//	}
}
