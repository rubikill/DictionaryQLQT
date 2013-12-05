package bridge;

public interface AndroidBridgeListener {
	void speakOut(String text);
	void lookup(String word);
	void setFavorite(String word);
	void removeFavorite(String word);
	void onLoadComplete();
}
