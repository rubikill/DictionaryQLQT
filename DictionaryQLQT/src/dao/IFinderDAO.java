package dao;

import model.Vocabulary;

public interface IFinderDAO {
	Vocabulary find(String keyWord);
	
}
