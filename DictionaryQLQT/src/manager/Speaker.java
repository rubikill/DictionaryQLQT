package manager;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class Speaker implements JSpeaker, OnInitListener{

	public static final float NORMAL = 1.0f;
	public static final float LOW = 0.5f;
	public static final float FAST = 2.0f;
	
	private TextToSpeech tts;
	private Locale language;
	private boolean available = false;
	
	public Speaker(Context context, Locale language){
		tts = new TextToSpeech(context, this);
		this.language = language;
	}
	
	@Override
	public void speakOut(String text) {
		if (isAvailable()){
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	@Override
    public void onInit(int status) {
 
        if (status == TextToSpeech.SUCCESS) { 
            setLanguage(language);
        } else {
        	available = false;
            Log.e("TTS", "Initilization Failed!");
        } 
    }

	@Override
	public boolean isAvailable(){
		return available;
	}
	
	@Override
	public Locale getLanguage() {
		return language;
	}
	
	@Override
	public boolean setLanguage(Locale language){		
		int result = tts.isLanguageAvailable(language);  		
        if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "This Language is not supported");
            return false;
        } else {
        	this.language = language;
        	tts.setLanguage(language);
            available = true;            
        } 
        return true;
	}
	
	@Override
	public void setSpeechRate(float speechRate){
		tts.setSpeechRate(speechRate);
	}
	
	@Override
	public void shutdown(){
		tts.shutdown();
	}
}
