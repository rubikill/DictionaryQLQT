package manager;

import model.WordOfDay;
import com.hcmus.dictionaryqlqt.R;
import android.content.Context;
import android.util.Log;
/**
 * 
 * @author Minh Khanh
 * 
 * Class xử lý việc lấy Word of day
 *
 */
public class WordOfDayHandler {
	/*
	 * context của Activity cha
	 */
	private Context context;

	public WordOfDayHandler(Context context) {
		this.context = context;
	}

	/*
	 * hàm trả về wod
	 */
	public WordOfDay getWod() {
		// thử lấy từ service trước
		WordOfDay wod = getFromService();		
		if (wod == null) {
			// nếu không thành công sẽ lấy trực tiếp từ web
			wod = getFromWeb();
		}
		
		return wod;
	}

	/*
	 * lấy wod từ service
	 */
	private WordOfDay getFromService() {
		String url = context.getString(R.string.wod_service_url);
		WordOfDayService service = new WordOfDayService();
		WordOfDay wod = null;
		try {
			wod = service.getWod(url);
		} catch (Exception e) {
			Log.e("WOD_Service", "error:" + e.getMessage());
		}

		return wod;
	}

	/*
	 * lấy wod từ web
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
