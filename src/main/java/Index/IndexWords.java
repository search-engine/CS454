package Index;

import java.util.HashMap;

public class IndexWords {

	private HashMap<String, Integer> document =  new HashMap<String, Integer>();
	
	public HashMap<String, Integer> getDocument() {
		return document;
	}
	public void setDocument(String url) {
		if(document.containsKey(url)){
			document.put(url, document.get(url)+1);
		}else{
			document.put(url, 1);
		}
	}

}
