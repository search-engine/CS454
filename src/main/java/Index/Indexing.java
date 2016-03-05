package Index;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class Indexing {
	public static void main( String args[] ) {

		File f = null;
	      File[] paths;
	      try{     
	          // create new file
	          f = new File(System.getProperty("user.home")+"/extra/wiki-small");
	          
	          // returns pathnames for files and directory
	          paths = f.listFiles();
	          List<File> files = (List<File>) FileUtils.listFiles(f, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	          // for each pathname in pathname array
	          for(File file:files)
	          {
	             // prints file and directory paths
	             System.out.println(file.getCanonicalPath());
	          }
	       }catch(Exception e){
	          // if any error occurs
	          e.printStackTrace();
	       }		   
	}
}
