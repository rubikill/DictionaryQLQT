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

@SuppressLint("SetJavaScriptEnabled")
public class WebviewHelper {
	
	public static void ShowMeaning(WebView webview, String meaning, AndroidBridge bridge){
		Context context = webview.getContext();
		String html = readHtml(context, "web/template.html");
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadDataWithBaseURL("file:///android_asset/web/",
				html.replace("@REPLACEHERE", meaning), "text/html", "UTF-8", null);
		webview.addJavascriptInterface(bridge, "android");
	}
	
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
