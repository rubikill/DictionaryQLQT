package model;

/**
 * Model class vocabulary
 * 
 * 
 * @author Thanh Toan
 * 
 */
public class Vocabulary {
	
	/**
	 * Từ khóa
	 */
	private String word;
	
	/**
	 * Nghĩa thứ nhất
	 */
	private String stMean;
	
	/**
	 * Nghĩa thứ hai
	 */
	private String ndMean;
	
	/**
	 * Nghĩa thứ ba
	 */
	private String rdMean;

	/**************************************
	 * 
	 * 			Getter and setter 
	 * 
	 **************************************/
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getStMean() {
		return stMean;
	}

	public void setStMean(String stMean) {
		this.stMean = stMean;
	}

	public String getNdMean() {
		return ndMean;
	}

	public void setNdMean(String ndMean) {
		this.ndMean = ndMean;
	}

	public String getRdMean() {
		return rdMean;
	}

	public void setRdMean(String rdMean) {
		this.rdMean = rdMean;
	}

	public Vocabulary(String word, String stMean, String ndMean, String rdMean) {
		super();
		this.word = word;
		this.stMean = stMean;
		this.ndMean = ndMean;
		this.rdMean = rdMean;
	}

	public Vocabulary() {
		super();
		// TODO Auto-generated constructor stub
	}
}
