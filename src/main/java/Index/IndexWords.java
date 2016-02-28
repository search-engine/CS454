package Index;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IndexWords {

	private HashMap<String, Integer> document =  new HashMap<String, Integer>();
	
	public HashMap<String, Integer> getDocument() {
		return document;
	}
	public void setDocument(String url) {
		int count = 0;
		for(Map.Entry<String,Integer> entry : document.entrySet()) {
			if(entry.getKey().equals(url)) {
				int cnt = entry.getValue();
				document.put(url, cnt++);
			}
		}
		document.put(url, count++);
	}


	private String word;
	private int position;

	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	

	public IndexWords() {
		
	}
}
