package bridge;

import android.os.Handler;

public class AndroidBridge {
	private AndroidBridgeListener listener = null;
	private final Handler handler = new Handler();
	
	public void setListener(AndroidBridgeListener listener){
		this.listener = listener;		
	}
	
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
}
