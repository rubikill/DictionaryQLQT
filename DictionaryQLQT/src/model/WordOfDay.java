package model;


/**
 * Model Class WordOfDay
 * 
 * @author Minh Khanh
 */

public class WordOfDay {
	
	/**
	 * tu
	 */
	private String word;
	
	/**
	 * phien am
	 */
	private String phonetic;
	
	/**
	 * loai tu
	 */
	private String wordFunction;
	
	/**
	 * nghia cua tu
	 */
	private String mean;
	
	/**
	 * cac vi du
	 */
	private String examples;
	
	/**
	 * phan do you know?
	 */
	private String didYouKnow;
	
	/**
	 * ngay
	 */
	private String date;
		
	public WordOfDay(){
		
	}	
	
	public WordOfDay(String word, String phonetic, String wordFunction,
			String mean, String examples,
			String didYouKnow, String date) {
		super();
		this.word = word;
		this.phonetic = phonetic;
		this.wordFunction = wordFunction;
		this.mean = mean;
		this.examples = examples;
		this.didYouKnow = didYouKnow;
		this.date = date;
	}
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getPhonetic() {
		return phonetic;
	}
	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}
	public String getWordFunction() {
		return wordFunction;
	}
	public void setWordFunction(String wordFunction) {
		this.wordFunction = wordFunction;
	}
	public String getMean() {
		return mean;
	}
	public void setMean(String mean) {
		this.mean = mean;
	}
	public String getExamples() {
		return examples;
	}
	public void setExamples(String examples) {
		this.examples = examples;
	}
	public String getDidYouKnow() {
		return didYouKnow;
	}
	public void setDidYouKnow(String doYouKnow) {
		this.didYouKnow = doYouKnow;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
