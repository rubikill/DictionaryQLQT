package manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import model.WordOfDay;

import bridge.AndroidBridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.webkit.WebView;

/***
 * 
 * @author Minh Khanh
 *
 * class ho tro hien thi noi dung len webview
 */

@SuppressLint("SetJavaScriptEnabled")
public class WebviewHelper {
	
	/***
	 * hien thi nghia tu len webview
	 * @param webview: webview hien thi
	 * @param meaning chuoi nghia can hien thi
	 * @param bridge doi tuong cau noi giua android va js
	 */
	public static void ShowMeaning(WebView webview, String meaning, AndroidBridge bridge){
		Context context = webview.getContext();
		String html = readHtml(context, "web/template.html");
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadDataWithBaseURL("file:///android_asset/web/",
				html.replace("@REPLACEHERE", meaning), "text/html", "UTF-8", null);
		webview.addJavascriptInterface(bridge, "android");
	}
	
	/***
	 * hien thi noi dung wod
	 * @param webview: webview hien thi
	 * @param wod: tu can hien thi
	 * @param bridge: doi tuong cau noi giua android va js
	 */
	public static void ShowWOD(WebView webview, WordOfDay wod, AndroidBridge bridge){
		Context context = webview.getContext();
		String html = readHtml(context, "web/wod.html");
		html = html.replace("@DATE", wod.getDate())
				.replace("@WORD", wod.getWord())
				.replace("@PRON", wod.getPhonetic())
				.replace("@FUNC", wod.getWordFunction())
				.replace("@MEAN", wod.getMean())
				.replace("@EXAM", wod.getExamples())
				.replace("@DYK", wod.getDidYouKnow());
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadDataWithBaseURL("file:///android_asset/web/",
				html, "text/html", "UTF-8", null);
		//webview.addJavascriptInterface(bridge, "android");
	}
	
	/***
	 * ham doc file html template
	 * @param context: context cua ung dung
	 * @param filename: ten file
	 * @return chuoi html template trong file
	 */
	private static String readHtml(Context context, String filename){
		StringBuilder buider = new StringBuilder();		
		AssetManager manager = context.getAssets();
		InputStreamReader sr;
		try {
			sr = new InputStreamReader(manager.open(filename));
			BufferedReader reader = new BufferedReader(sr);
			String line = reader.readLine();
			while (line != null){
				buider.append(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return buider.toString();		
	}
}
