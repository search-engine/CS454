package url;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UrlLink {
	private String url;
	private Set<UrlLink> linkFrom;
	private Set<UrlLink> linkTo;
	private int totalWordCount = 0;
	private static HashMap<String, UrlLink> urllinks = new HashMap<String, UrlLink>();
	
	public UrlLink(String url){
		this.url = url;
		linkFrom = new HashSet<UrlLink>();
		linkTo = new HashSet<UrlLink>();
		setTotalWordCount(0);
		
	}

	public String getUrl() {
		return url;
	}
	
	public void addLinkTo(String urlFrom){
		this.addLinkTo(urllinks.get(urlFrom));
	}
	
	public void addLinkTo(UrlLink urlFrom){
		linkTo.add(urlFrom);
		urlFrom.addLinkFrom(this);
	}
	
	public void addLinkFrom(UrlLink urlTo){
		this.linkFrom.add(urlTo);
	}
	
	public int linkFromSize(){
		return this.linkFrom.size();
	}
	
	public int linkToSize(){
		return this.linkTo.size();
	}
	
	public static boolean addLink(String u){
		if(urllinks.containsKey(u)){
			return false;
		}else{
			return (urllinks.put(u, new UrlLink(u)) != null);
		}
	}
	
	public static HashMap<String, UrlLink> getAllLinks(){
		return urllinks;
	}

	public int getTotalWordCount() {
		return totalWordCount;
	}

	public void setTotalWordCount(int totalWordCount) {
		this.totalWordCount = totalWordCount;
	}
}
