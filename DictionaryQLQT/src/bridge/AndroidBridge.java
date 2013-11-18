package bridge;

import android.os.Handler;

/**
 * 
 * @author Minh Khanh
 * 
 * class cau noi giua android va javascript
 *
 */
public class AndroidBridge {
	/*
	 * listener lang nghe su kien
	 */
	private AndroidBridgeListener listener = null;
	private final Handler handler = new Handler();
	
	/**
	 * thiet lap listener
	 * @param listener
	 */
	public void setListener(AndroidBridgeListener listener){
		this.listener = listener;		
	}
	
	/**
	 * call back tu javascript goi phat am
	 * @param text: tu phat am
	 */
	public void speakOut(final String text){
		if (listener != null){
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					listener.speakOut(text.trim());				
				}
			});
		}
	}
	
	/**
	 * call back tu javascript goi tim tu
	 * @param text: tu can tra
	 */
	public void search(final String word){
		if (listener != null){
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					listener.lookup(word.trim());				
				}
			});
		}
	}
	
	/**
	 * call back tu javascript goi them favorite
	 * @param text: tu
	 */
	public void setFavorite(final String word){
		if (listener != null){
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					listener.setFavorite(word.trim());				
				}
			});
		}
	}
	
	/**
	 * call back tu javascript goi xoa favorite
	 * @param text: tu
	 */
	public void removeFavorite(final String word){
		if (listener != null){
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					listener.removeFavorite(word.trim());				
				}
			});
		}
	}
	
	/**
	 * call back tu javascript khi load complete
	 */
	public void onLoadComplete(){
		if (listener != null){
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					listener.onLoadComplete();				
				}
			});
		}
	}
}
