package com.hcmus.dictionaryqlqt;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class Translater {
	private static String ClientID = "SimpleDictionary";
	private static String ClientSecret = "OJLhnQ/jODwNZ6nEvYIEdqTC71S88SeXb/Rkd/rpwuI=";
	
	public Translater(){
		Translate.setClientId(ClientID);
		Translate.setClientSecret(ClientSecret);
	}
	
	public String Translate(String word, Language from, Language to){	
		String result = "";
		try {
			result = Translate.execute(word, from, to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}	
}
