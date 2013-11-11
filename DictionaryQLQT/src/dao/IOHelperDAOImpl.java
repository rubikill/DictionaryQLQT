package dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;

public class IOHelperDAOImpl implements IIOHelper {
	private static String SDcardPath = Environment
			.getExternalStorageDirectory().getPath() +"/data/";

	@Override
	public void createFolderData() {
		File f = new File(Environment
				.getExternalStorageDirectory().getPath() +"/data/");
		if(f.exists())
			return;
		else
			{
				f.mkdir();
			}
	}

	@Override
	public boolean checkDataFileExists(String FileName) {
		File f = new File(SDcardPath +"/"+ FileName);
		if(f.exists())
			return true;
		else
			return false;
	}

	@Override
	public void coppyDataFile(Context context, String FileName) {
		InputStream myInput;
		try {
			myInput = context.getAssets().open(FileName);
			String outFileName = SDcardPath + "/" + FileName;
			OutputStream myOutput = new FileOutputStream(outFileName);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}
