package manager;

import java.util.Locale;

public interface JSpeaker {
	void speakOut(String text);
	boolean setLanguage(Locale language);
	Locale getLanguage();
	void setSpeechRate(float speechRate);
	void shutdown();
	boolean isAvailable();
}
