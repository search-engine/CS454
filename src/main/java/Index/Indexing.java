package Index;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

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
	        	  filepath.add(file.getCanonicalPath());
	          }
	          
	          // for each pathname in pathname array
	          for(String file: filepath)
	          {
	             // prints file and directory paths
	        	 Indexer.indexer(file, filepath);
	        	 
	             //System.out.println(file.getCanonicalPath());
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
