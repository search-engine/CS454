package crawl;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
			System.out.println(test);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
