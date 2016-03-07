package Index;

public class Frequency {
	
	private Integer occurences;
	private Double tfidfScore;
	
	public Frequency(Integer occurences, Double tfidfScore) {
		this.occurences = occurences;
		this.tfidfScore = tfidfScore;
	}
	
	public Integer getOccurences() {
		return occurences;
	}
	public void setOccurences(Integer occurences) {
		this.occurences = occurences;
	}
	public Double getTfidfScore() {
		return tfidfScore;
	}
	public void setTfidfScore(Double tfidfScore) {
		this.tfidfScore = tfidfScore;
	}
	
	

}
