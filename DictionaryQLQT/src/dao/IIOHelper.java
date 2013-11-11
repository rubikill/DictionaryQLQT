package dao;

import android.content.Context;



public interface IIOHelper {
	void createFolderData();
	boolean checkDataFileExists(String FileName);
	void coppyDataFile(Context context,String FileName);
}
