package crawl;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	private static String url;
	private static int depth = -1;
	private static boolean isExtraction = false;
	private static Set<String> urlSet= new HashSet<String>();
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
		urlSet.add(url);
		crawlURL(url, 0);
		
		//Set<String> urls = crawl(url, isExtraction);	
	}
 	

	private static void crawlURL(String url2, int depth2) {
		System.out.println("visited: "+ url2 + " depth: " + depth2);
		if(depth2 < depth){
			try {
				Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
				Elements links = doc.select("a[href]");
				for(Element link : links){
					if(urlSet.add(link.attr("abs:href"))){
						crawlURL(link.attr("abs:href"), depth2+1);
					}
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	private static Set<String> crawl(String url2, boolean isExtraction2){
		Set<String> linkSet = new HashSet<String>();
		try {
			Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
			Elements links = doc.select("a[href]");
			for(Element link : links){
				linkSet.add(link.attr("abs:href"));
			}
			
			if(isExtraction2){
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linkSet;
	}

}
