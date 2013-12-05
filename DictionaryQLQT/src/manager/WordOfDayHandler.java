package manager;

import model.WordOfDay;
import com.hcmus.dictionaryqlqt.R;
import android.content.Context;
import android.util.Log;
/**
 * 
 * @author Minh Khanh
 * 
 * Class xu ly lay Word of day
 *
 */
public class WordOfDayHandler {
	/*
	 * context cua ung dung
	 */
	private Context context;

	public WordOfDayHandler(Context context) {
		this.context = context;
	}

	/**
	 * lay wod
	 */
	public WordOfDay getWod() {
		// uu tien lay tu service
		WordOfDay wod = getFromService();		
		if (wod == null) {
			// lay truc tiep tu website
			wod = getFromWeb();
		}
		
		return wod;
	}

	/**
	 * lau wod tu service
	 */
	private WordOfDay getFromService() {
		String url = context.getString(R.string.wod_service_url);
		WordOfDayServiceImpl service = new WordOfDayServiceImpl();
		WordOfDay wod = null;
		try {
			wod = service.getWod(url);
		} catch (Exception e) {
			Log.e("WOD_Service", "error:" + e.getMessage());
		}

		return wod;
	}

	/*
	 * lay wod tu web
	 */
	private WordOfDay getFromWeb() {
		WordOfDay wod = null;
		try {
			IWordOfDayPaser paser = new WordOfDayParserImpl(context);
			String url = context.getString(R.string.wod_url);
			wod = paser.parser(url);
		} catch (Exception e) {
			Log.e("WOD_Parser", "error:" + e.getMessage());
		}
		
		return wod;
	}
}
