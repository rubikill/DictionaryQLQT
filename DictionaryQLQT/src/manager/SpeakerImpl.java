package manager;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

/**
 * 
 * Class Speaker dùng để phát âm implements từ Interface ISpeaker và OnInitListener
 * 
 * @author Thanh Toan
 *
 */
public class SpeakerImpl implements ISpeaker, OnInitListener{
	
	/**
	 * text to speech
	 */
	private TextToSpeech tts;
	
	/**
	 * ngôn ngữ
	 */
	private Locale language;
	
	/**
	 * tồn tại
	 */
	private boolean available = false;
	
	/**
	 * Khởi tạo
	 * @param context nội dung.
	 * @param language ngôn ngữ.
	 */
	public SpeakerImpl(Context context, Locale language){
		tts = new TextToSpeech(context, this);
		this.language = language;
	}
	
	/**
	 * 
	 */
	@Override
	public void speakOut(String text) {
		if (isAvailable()){
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	/**
	 * Khởi tạo ban đầu
	 */
	@Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) { 
            setLanguage(language);
        } else {
        	available = false;
            Log.e("TTS", "Initilization Failed!");
        } 
    }

	/**
	 * Có tồn tại không
	 */
	@Override
	public boolean isAvailable(){
		return available;
	}
	
	/**
	 * Set ngôn ngữ
	 */
	@Override
	public Locale getLanguage() {
		return language;
	}
	
	/**
	 * 
	 */
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
	
	/**
	 * 
	 */
	@Override
	public void setSpeechRate(float speechRate){
		tts.setSpeechRate(speechRate);
	}
	
	/**
	 * 
	 */
	@Override
	public void shutdown(){
		tts.shutdown();
	}
}
