package model;

import java.util.List;


public class App {
	private String Id ="0";
	private String Name;
	private double Rate;
	private int RateCount;
	private List<Comment> Comments;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public double getRate() {
		return Rate;
	}
	public void setRate(double rate) {
		Rate = rate;
	}
	public int getRateCount() {
		return RateCount;
	}
	public void setRateCount(int rateCount) {
		RateCount = rateCount;
	}
	public String getId() {
		return Id;
	}
	public List<Comment> getComments() {
		return Comments;
	}
	public void setComments(List<Comment> comments) {
		Comments = comments;
	}
	
}
