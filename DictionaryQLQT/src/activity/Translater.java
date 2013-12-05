package activity;

import util.Constant;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class Translater {

	public Translater() {
		Translate.setClientId(Constant.ClientID);
		Translate.setClientSecret(Constant.ClientSecret);
	}

	public String Translate(String word, Language from, Language to) {
		String result = "";
		try {
			result = Translate.execute(word, from, to);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
