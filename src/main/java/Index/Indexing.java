package Index;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class Indexing {
	public static void main( String args[] ) {

		File f = null;
	      try{     
	          // create new file
	          f = new File(System.getProperty("user.home")+"/extra/wiki-small");
	          List<File> files = (List<File>) FileUtils.listFiles(f, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	          // for each pathname in pathname array
	          for(File file:files)
	          {
	             // prints file and directory paths
	        	 Indexer.indexer(file.getCanonicalPath());
	        	 
	             //System.out.println(file.getCanonicalPath());
	          }
	          for(String word : Indexer.getALlTerms().keySet()) {
	        	  System.out.println(word);
	        	  IndexWords iw = Indexer.getALlTerms().get(word);
	        	  System.out.println(iw.getDocument());
	          }
	       }catch(Exception e){
	          // if any error occurs
	          e.printStackTrace();
	       }		   
	}
}
