package crawl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
		
		Set<String> urls = crawl(url);	
		for(String u: urls){
			System.out.println(u);
		}
	}
 	
	private static Set<String> crawl(String url2) {
		Set<String> linkSet = new HashSet<String>();
		try {
			Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
			Elements links = doc.select("a[href]");
			for(Element link : links){
				linkSet.add(link.attr("abs:href"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linkSet;
	}

}
