package crawl;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.HttpConnection.Response;

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
			Response response = (Response) conn.execute();
			byte[] res = response.bodyAsBytes();
			
			FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir"));
			fos.write(res);
			fos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		
		
		
		
	}

}
