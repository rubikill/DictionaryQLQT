package helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import util.Constant;
import android.content.Context;

public class IOHelperImpl implements IIOHelper {
	
	@Override
	public void createFolderData() {
		File f = new File(Constant.SDCARD_PATH);
		if (f.exists())
			return;
		else {
			f.mkdir();
		}
	}

	@Override
	public boolean checkDataFileExists(String FileName) {
		File f = new File(Constant.SDCARD_PATH + "/" + FileName);
		return f.exists();
	}

	@Override
	public void coppyDataFile(Context context, String FileName) {
		InputStream myInput;
		try {
			myInput = context.getAssets().open(FileName);
			String outFileName = Constant.SDCARD_PATH + "/" + FileName;
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
