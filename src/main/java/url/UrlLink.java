package url;

import java.util.HashSet;
import java.util.Set;

public class UrlLink {
	private String url;
	private Set<UrlLink> linkFrom;
	private Set<UrlLink> linkTo;
	private String path;
	
	public UrlLink(String url){
		this.url = url;
		linkFrom = new HashSet<UrlLink>();
		linkTo = new HashSet<UrlLink>();
		
	}

	public String getUrl() {
		return url;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
