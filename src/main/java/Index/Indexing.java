package Index;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import url.UrlLink;

public class Indexing {
	private static final double tfidfRatio = 0.85;
	private static final double pageRankRatio = 0.15;
	public static void main( String args[] ) {
		File f = null;
	      try{     
	          // create new file
	          f = new File(System.getProperty("user.home")+"/extra/wiki-small/en/articles/");
	          //Indexer.indexer("/Users/anandsuresh/Desktop/100th_Anniversary_deb0.html");
	          List<File> files = (List<File>) FileUtils.listFiles(f, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	          Set<String> filepath = new HashSet<String>();
	          for(File file: files){
	        	  String path = file.getCanonicalPath();
	        	  if(filepath.add(path)){
	        		  UrlLink.addLink(path);
	        	  }
	          }
	          
	          // for each pathname in pathname array
	          for(String link: UrlLink.getAllLinks().keySet()){
	             // prints file and directory paths
	        	 Indexer.indexer(link);
	             //System.out.println(file.getCanonicalPath());
	          }
	          
	          
	        //init rank
	          double initRank = 1.0/UrlLink.getAllLinks().size();
	          for (Entry<String, UrlLink> entry : UrlLink.getAllLinks().entrySet()) {
	              entry.getValue().initPageRank(initRank);
	          }
	          
	          boolean refined = false;
	          while(!refined){
	        	  for (Entry<String, UrlLink> entry : UrlLink.getAllLinks().entrySet()) {
		              entry.getValue().calculateRank();
		          }
	        	  refined = true;
	        	  for (Entry<String, UrlLink> entry : UrlLink.getAllLinks().entrySet()) {
		              refined = entry.getValue().confirmRank(0.01) && refined;
		          }
	          }
	          
	          for(Entry<String, IndexWords> entry : Indexer.getALlTerms().entrySet()){
	        	  String word = entry.getKey();
	        	  IndexWords iw = entry.getValue();
	        	  double idf = iw.getIDF(UrlLink.getAllLinks().size());
	        	  for(Entry<String, Integer> entry2 : iw.getDocument().entrySet()){
	        		  String doc = entry2.getKey();
	        		  double tf = entry2.getValue().doubleValue();
	        		  UrlLink docLink = UrlLink.getAllLinks().get(doc);
	        		  int totalcount = docLink.getTotalWordCount();
	        		  double tfidf = tf/totalcount * idf;
	        		  double finalRank = tfidf*tfidfRatio + docLink.getPageRank()*pageRankRatio; 
	        		  System.out.println(doc+" word: "+ word+" "+finalRank);
	        	  }
	          }
	          
	          /*
	          for(String word : Indexer.getALlTerms().keySet()) {
	        	  System.out.println(word);
	        	  IndexWords iw = Indexer.getALlTerms().get(word);
	        	  System.out.println(iw.getDocument());
	          }*/
	       }catch(Exception e){
	          // if any error occurs
	          e.printStackTrace();
	       }		   
	}
	
}
