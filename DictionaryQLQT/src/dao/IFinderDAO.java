package dao;

import java.util.ArrayList;

import model.Vocabulary;

public interface IFinderDAO {
	//tim tu
	Vocabulary find(String keyWord);
	
	//lay danh sach tu goi y
	ArrayList<Vocabulary> getRecommendWord(String keyWord);
	
	//Lay nghia cu tu
	String getMean(Vocabulary vocabulary);
	String getMean(String index,String length);
}
