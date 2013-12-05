package util;

import android.os.Environment;

/**
 * 
 * Class chứa hằng số
 * 
 * @author Thanh Toan
 * 
 */
public class Constant {
	public static final float NORMAL = 1.0f;
	public static final float LOW = 0.5f;
	public static final float FAST = 2.0f;

	public static final String WEB_SERVICE_URL = "http://www.qlqtpmn2.somee.com/api/androidapp";
	public static final String DB_NAME = "EVDict.db";

	public static final int GET_ONE = 0;
	public static final int GET_MULTI = 1;

	public static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/data/";
	public static final String ClientID = "SimpleDictionary";
	public static final String ClientSecret = "OJLhnQ/jODwNZ6nEvYIEdqTC71S88SeXb/Rkd/rpwuI=";
	
	/*
	 * ma Code de nhan ket qua tra ve tu voice search
	 */
	public static final int SPEECH_RECOGNIZER_CODE = 1;
	
	public static final int WOD = 0;
	public static final int ERROR = 1;
	
	public static final int EMAIL = 0;
	public static final int FACEBOOK = 1;
	public static final int TWITTER = 2;
}
