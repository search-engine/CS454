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
	private double pageRank;
	private double refPageRank;
	private static final double lamda = 0.15;
	private final double oneMinusLamda = 1 - lamda;
	private double lamdaOverN;
	private String path;
	private String base = System.getProperty("user.dir") + "/url/";
	private String longPath;
	
	public UrlLink(String url){
		this.url = url;
		this.path = base + getPath(url);
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
	
	public static UrlLink addLink(String u){
		if(u == null || !(u.contains("https://")||u.contains("http://"))){
			return null;
		}else if(!urllinks.containsKey(u)){
			UrlLink newlink = new UrlLink(u);
			urllinks.put(u, newlink);
			return newlink;
		}else{
			return null;
		}
	}
	
	public static HashMap<String, UrlLink> getAllLinks(){
		return urllinks;
	}

	public int getTotalWordCount(){
		return totalWordCount;
	}

	public void setTotalWordCount(int totalWordCount) {
		this.totalWordCount = totalWordCount;
	}

	public double getPageRank(){
		return pageRank;
	}

	public void calculateRank(){
		this.refPageRank = 0;
		for(UrlLink link: linkFrom){
			this.refPageRank += link.getPageRank()/link.linkToSize();
		}
		this.refPageRank = this.lamdaOverN + this.oneMinusLamda*this.refPageRank;
	}
	
	public boolean confirmRank(double diff){
		if(Math.abs(this.refPageRank - this.pageRank) > diff){
			this.pageRank = this.refPageRank;
			return false;
		}else{
			this.pageRank = this.refPageRank;
			return true;
		}
	}

	public void initPageRank(double pageRank) {
		this.pageRank = pageRank;
		this.refPageRank = 10;
		this.lamdaOverN = lamda/urllinks.size();
	}
	
	private String getPath(String url2) {
		//remove https:// or http://
		url2 = url2.substring(url2.indexOf("//")+2);
		//System.out.println("url2: "+url2);
		int lastSlash = url2.lastIndexOf('/');
		//System.out.println(lastSlash);
		if(lastSlash != -1){
			//System.out.println("url2 substr 1:"+url2.substring(0, lastSlash + 1));
			return url2.substring(0, lastSlash + 1);
		}else{
			//System.out.println("url2 substr 2:"+url2 + "/");
			return url2 + "/";
		}
	}
	
	public String getPath(){
		return this.path;
	}

	public static String urlTrim(String u){
		if(u.contains("?")){
			u = u.substring(0, u.indexOf("?"));
		}
		//remove after ;
		if(u.contains(";")){
			u = u.substring(0, u.indexOf(";"));
		}
		//
		if(u.contains("#")){
			u = u.substring(0, u.indexOf("#"));
		}
		//if last one is /, then remove it
		if(u.charAt(u.length() - 1) == '/'){
			u = u.substring(0, u.length() - 1);
		}
		return u;
	}

	public String getLongPath() {
		return longPath;
	}

	public void setLongPath(String fname) {
		this.longPath = this.path + fname;
	}
	
	public static String getFname(String url2, Boolean isHTML) {
		url2 = url2.substring(url2.indexOf("//")+2);
		int lastSlash = url2.lastIndexOf('/');
		if(lastSlash != -1){
			url2 = url2.substring(lastSlash + 1); 
		}
		if(isHTML){
			if(url2.contains(".html")||url2.contains(".htm")){
				return url2;
			}else{
				return url2 + ".html";
			}
		}
		return url2;
	}
	
}
