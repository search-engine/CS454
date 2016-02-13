package crawl;

<<<<<<< HEAD
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
=======
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
>>>>>>> 017054d8a0d31fbcf0ba7c4184ff4b631088ff18

import org.apache.tika.Tika;
import org.jsoup.Jsoup;
<<<<<<< HEAD
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
=======
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.HttpConnection.Response;
>>>>>>> 017054d8a0d31fbcf0ba7c4184ff4b631088ff18

public class Crawler {
	private static String url;
	private static int depth = -1;
	private static boolean isExtraction = false;
 	public static void main(String[] args) {
 		int index = 0;
 		while(index < args.length){
 			if(args[index].equals("-d")){
 				index++;
            	try{
            		depth = Integer.parseInt(args[index]);
            		index++;
            	}catch(Exception e){
            		System.out.println("depth has to be an integer");
            		System.exit(0);
            	}
            }else if(args[index].equals("-u")){
            	index++;
            	if(args[index].contains("https://")||args[index].contains("http://")){
            		url = args[index];
            		index++;
            	}else{
            		System.out.println("Please enter an correct URL to start crawling");
            		System.exit(0);
            	}
            }else if(args[index].equals("-e")){
            	isExtraction = true;
            	index++;
            }else{
            	System.out.println("unknown command: "+args[index]);
            	index++;
            }
 		}
		if(url==null||depth<0){
			System.out.println("Either missing argument for depth or URL");
			System.exit(0);
		}
		
		System.out.println("Depth is "+depth+"; URL is "+url+"; isExtraction: "+isExtraction);
		
		HttpConnection conn = (HttpConnection) Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true);
		try {
			Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
			String test = doc.html();
			
			File origFile = new File(System.getProperty("user.dir")+"/firstCrawl.txt");
			FileWriter fileWriter = new FileWriter(origFile);
			fileWriter.write(test);
			fileWriter.close();
			
//			HashMap<String, String> baseMapLinks = new HashMap<String, String>();
			Elements links = doc.select("a[href]");
			Set<String> linkSet = new HashSet<String>();
			for(Element link : links){
				linkSet.add(link.attr("abs:href"));
			}
			System.out.println("linkSet "+linkSet.size());
			Set<String> depthLinkSet = new HashSet<String>();
			for(String depthCrawl2 : linkSet){

				Document depthDoc = Jsoup.connect(depthCrawl2).ignoreContentType(true).userAgent("Mozilla").get();
				
				Elements depthLinks = depthDoc.select("a[href]");
				for(Element link : depthLinks){
					
					depthLinkSet.add(link.attr("abs:href"));
				}
			}
			System.out.println("depth link set size "+depthLinkSet.size());
			Response response = (Response) conn.execute();
			byte[] res = response.bodyAsBytes();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		
		
		
		
	}

}
