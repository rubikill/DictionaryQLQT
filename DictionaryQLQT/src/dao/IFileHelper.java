package dao;

import android.content.Context;

public interface IFileHelper {
	boolean checkFileExists();
	void coppyFile(Context context);
	String getMean(String index, String length);
	int getDemicalValue (String str);
}
