package dao;

import java.io.IOException;
import java.io.RandomAccessFile;

import android.content.Context;
import android.os.Environment;

public class FileHelperDAOImpl implements IFileHelper {
	private static String FileName = "EnglishVietnamese.dict";
	@Override
	public boolean checkFileExists() {
		IOHelperDAOImpl io = new IOHelperDAOImpl();
		return io.checkDataFileExists(FileName);
	}

	@Override
	public void coppyFile(Context context) {
		IOHelperDAOImpl io = new IOHelperDAOImpl();
		io.coppyDataFile(context, FileName);
	}
	
	@Override
	public String getMean(String index, String length)
	{
		int l =getDemicalValue(length);
		int idx = getDemicalValue(index);
		byte[] buff = null;
		try {
			RandomAccessFile raf = new RandomAccessFile(Environment.getExternalStorageDirectory().getPath()+"/data/EnglishVietnamese.dict","r");
			raf.seek(idx);
			buff = new byte[l];
			raf.read(buff);
			raf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
		String result = new String(buff);
		return result;
	}
	
	@Override
	public int getDemicalValue (String str){
		String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		int decValue = 0;
		int len = str.length();
		for (int i = 0; i<len; i++){
			int pos = base64.indexOf(str.charAt(i),0);
		    decValue += (int)Math.pow(64,len-i-1)*pos;
		}
		return decValue;
	}
}
