package crawl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.tika.Tika;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	private static String url;
	private static int depth = -1;
	private static boolean isExtraction = false;
 	public static void main(String[] args) {
		for (String arg: args) {
            String[] arguments = arg.split(" ");
            if(arguments[0].equals("d")){
            	try{
            		depth = Integer.parseInt(arguments[1]);
            	}catch(Exception e){
            		System.out.println("depth has to be an integer");
            		System.exit(0);
            	}
            }else if(arguments[0].equals("u")){
            	if(arguments.length>1&&arguments[1].length()!=0){
            		url = arguments[1];
            	}else{
            		System.out.println("Please enter an URL to start crawling");
            		System.exit(0);
            	}
            }else if(arguments[0].equals("e")){
            	isExtraction = true;
            }else{
            	System.out.println("unknown argument: "+arg);
            }
        }
		if(url==null||depth<0){
			System.out.println("Either missing argument for depth or URL");
			System.exit(0);
		}
		System.out.println("Depth is "+depth+"; URL is "+url+"; isExtraction: "+isExtraction);
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
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

	}

}
