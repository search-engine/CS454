package Index;


import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DataSort {
	
	private String word;
	private Map<Double, String> unsortedMap;
	private TreeMap<Double, String> scoreTree;
	


	public DataSort(String word, Map<Double, String> scoreMap) {
		this.word = word;
		this.unsortedMap = scoreMap;

	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	public Map<Double, String> getUnsortedMap() {
		return unsortedMap;
	}

	public void setUnsortedMap(Map<Double, String> unsortedMap) {
		this.unsortedMap = unsortedMap;
	}


	public TreeMap<Double, String> getScoreTree() {
		return scoreTree;
	}

	public void setScoreTree(TreeMap<Double, String> scoreTree) {
		this.scoreTree = scoreTree;
	}
	
	public void sortData(){
		
	}
	
	public void Sort(){
		this.scoreTree = new TreeMap<Double, String>(new Comparator<Double>() {
			
			public int compare(Double o1, Double o2) {
				return o2.compareTo(o1);
			}

		});
		this.scoreTree.putAll(this.unsortedMap);
		
	}
	
	
	public void displayTree(){
		System.out.println("word: "+this.word);
		for (Map.Entry<Double, String> entry : this.scoreTree.entrySet()) {
			System.out.println("Key : " + entry.getKey() 
                                      + " Value : " + entry.getValue());
		}
	}



}
