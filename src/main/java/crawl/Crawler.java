package crawl;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
		url = urlTrim(url);
		addURL(url);		
		while(depth >= 0){
			Queue<String> urlQueue2 = new LinkedList<String>();
			while(!urlQueue.isEmpty()){
				urlQueue2.add(urlQueue.poll());
			}
	        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
	        HashMap<String, Future<Set<String>>> resultList = new HashMap<String, Future<Set<String>>>();
	        
	        while(!urlQueue2.isEmpty()){
	        	String link2Crawl = urlQueue2.poll();
	        	Crawl crawler = new Crawl(link2Crawl, depth);
	        	Future<Set<String>> resultSet = executor.submit(crawler);
	        	resultList.put(link2Crawl, resultSet);
	        }
			for(String urlToLink : resultList.keySet()){
				Future<Set<String>> futureSet = resultList.get(urlToLink);
				try {
					Set<String> sets = futureSet.get();
					for(String u : sets){
						System.out.println("found " + u);
						u = urlTrim(u);
						System.out.println("found trimed: " + u);
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
			
	   }


	private static boolean addURL(String u) {
		if(u == null || !(u.contains("https://")||u.contains("http://"))){
			return false;
		}else if(urlSet.add(u)){
			return urlQueue.add(u);
		}else{
			return false;
		}
	}
	
	private static String urlTrim(String u){
		if(u.contains("?")){
			u = u.substring(0, u.indexOf("?"));
		}
		//remove after ;
		if(u.contains(";")){
			u = u.substring(0, u.indexOf(";"));
		}
		//
		if(u.contains("#")){
			u = u.substring(0, u.indexOf("#"));
		}
		//if last one is /, then remove it
		if(u.charAt(u.length() - 1) == '/'){
			u = u.substring(0, u.length() - 1);
		}
		return u;
	}
}


