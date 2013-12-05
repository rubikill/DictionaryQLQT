package helper;

import android.content.Context;

public interface IFileHelper {
	// Kiem tra file mean ton tai chua
	boolean checkFileExists();

	// coppy file tu assets vao sdcard
	void coppyFile(Context context);

	// doc file mean, lay nghia
	String getMean(String index, String length);

	// doi co so 64 sang co so 10
	int getDemicalValue(String str);
}
