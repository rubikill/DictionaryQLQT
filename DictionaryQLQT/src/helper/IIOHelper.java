package helper;

import android.content.Context;

public interface IIOHelper {
	void createFolderData();

	boolean checkDataFileExists(String FileName);

	void coppyDataFile(Context context, String FileName);
	
	void coypyFolder(Context context,String path);
}