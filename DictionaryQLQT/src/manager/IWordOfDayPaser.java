package manager;

import java.io.IOException;

import model.WordOfDay;

public interface IWordOfDayPaser {
	WordOfDay parser(String url) throws IOException;
}
