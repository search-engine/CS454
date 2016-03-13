package Index;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import url.UrlLink;

public class Indexing {
	private static final double tfidfRatio = 0.85;
	private static final double pageRankRatio = 0.15;
	
	public static void indexingAndRanking() {
		File f = null;
	      try{     
	    	  
	    	  // To connect to mongodb server
		         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		         // Now connect to your databases
		         DB db = mongoClient.getDB( "search_engine" );
	    
					
		         DBCollection coll = db.getCollection("index");
					
		         DBCursor cursor = coll.find();
		         	
		         while (cursor.hasNext()) { 
		        	 coll.remove(cursor.next());
		         }
		         
	          // create new file
	          //f = new File(System.getProperty("user.home")+"/extra/wiki-small/en/articles/");
	          //Indexer.indexer("/Users/anandsuresh/Desktop/100th_Anniversary_deb0.html");
	          /*
		      List<File> files = (List<File>) FileUtils.listFiles(f, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	          Set<String> filepath = new HashSet<String>();
	          for(File file: files){
	        	  String path = file.getCanonicalPath();
	        	  if(filepath.add(path)){
	        		  UrlLink.addLink(path);
	        	  }
	          }
	          */
	          // for each pathname in pathname array
	          for(Entry<String, UrlLink> en: UrlLink.getAllLinks().entrySet()){
	             // prints file and directory paths
	        	 try{
	        		 Indexer.indexer(en.getValue());
	        	 }catch(Exception e){
	        		 System.out.println("cannot index " + en.getKey());
	        		 e.printStackTrace();
	        	 }
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
	        	  BasicDBObject documentDetail = new BasicDBObject();
	        	  String word = entry.getKey();
	        	  IndexWords iw = entry.getValue();
	        	  double idf = iw.getIDF(UrlLink.getAllLinks().size());
	        	  Map<Double, String> scoreMap = new HashMap<Double, String>();
	        	  for(Entry<String, Integer> entry2 : iw.getDocument().entrySet()){
	        		  String doc = entry2.getKey();
	        		  double tf = entry2.getValue().doubleValue();
	        		  UrlLink docLink = UrlLink.getAllLinks().get(doc);
	        		  int totalcount = docLink.getTotalWordCount();
	        		  double tfidf = tf/totalcount * idf;
	        		  double finalRank = tfidf*tfidfRatio + docLink.getPageRank()*pageRankRatio; 
	        		  scoreMap.put(finalRank, doc);
	        	  }
	        	  DataSort sortData = new DataSort(word,scoreMap);
	        	  sortData.Sort();
	        	  //sortData.displayTree();
	        	  //String toJson = gson.toJson(sortData.getScoreTree()); 
	        	  JSONArray jsonArray = new JSONArray();
	        	  for(Entry<Double, String> data : sortData.getScoreTree().entrySet()){
	        		  JSONObject obj = new JSONObject();
	        		  obj.put("url", data.getValue());
	        		  obj.put("score", data.getKey().doubleValue());
	        		  jsonArray.add(obj);
	        	  }
	        	  documentDetail.put("word", word);
	        	  documentDetail.put("ranks", jsonArray);
	        	  coll.insert(documentDetail);
	        	  //System.out.println(toJson);
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
