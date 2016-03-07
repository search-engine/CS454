package Index;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import url.UrlLink;

public class Indexing {
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
	          IndexWords iw;
	          int totalcount;
	          double idf, tfidf;
	          double tf;
	           
	          
	          for(String word : Indexer.getALlTerms().keySet()){
	        	  TreeMap<Double, String> scoreTree = new TreeMap<Double, String>();
	        	  iw = Indexer.getALlTerms().get(word);
	        	  idf = iw.getIDF(UrlLink.getAllLinks().size());
	        	  List<Double> tfIdfList = new ArrayList<Double>();
	        	  List<String> docList = new ArrayList<String>();
	        	  
	        	  Map<Double, String> scoreMap = new HashMap<Double, String>();
	        	  
	        	  for(String doc : iw.getDocument().keySet()){
	        		  tf = iw.getDocument().get(doc).doubleValue();
	        		  totalcount = UrlLink.getAllLinks().get(doc).getTotalWordCount();
	        		  tfidf = tf/totalcount * idf;
//	        		  System.out.println(doc+" word: "+ word+" "+tfidf);
	        		  scoreMap.put(tfidf, doc);
	        		  
	        		  
	        	  }
	        	  DataSort sortData = new DataSort(word,scoreMap);
	        	  sortData.Sort();
	        	  sortData.displayTree();
	        	  
	        	  
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
