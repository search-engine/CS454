package crawl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


public class Crawler {
	private static String url;
	private static int depth = -1;
	private static boolean isExtraction = false;
	private static Set<String> urlSet = new HashSet<String>();
	private static Queue<String> urlQueue = new LinkedList<String>();
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
		addURL(url);
		
		while(depth >= 0){
			Queue<String> urlQueue2 = new LinkedList<String>();
			while(!urlQueue.isEmpty()){
				urlQueue2.add(urlQueue.poll());
			}
	        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
	        List<Future<Set<String>>> resultList = new ArrayList<Future<Set<String>>>();
			
	        while(!urlQueue2.isEmpty()){
	        	String url2Crawl = urlQueue2.poll();
	        	Crawl crawler = new Crawl(url2Crawl, depth, isExtraction);
	        	Future<Set<String>> resultSet = executor.submit(crawler);
	        	resultList.add(resultSet);
	        }
			for(Future<Set<String>> futureSet : resultList){
				try {
					Set<String> sets = futureSet.get();
					for(String u : sets){
						addURL(u);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			executor.shutdown();
			while(!executor.isTerminated()){}
			depth--;
		}
		System.out.println(urlSet.size());
		
		
	   }
		
 	

	private static boolean addURL(String u) {
		if(urlSet.add(u)){
			return urlQueue.add(u);
		}else{
			return false;
		}
	}
}


