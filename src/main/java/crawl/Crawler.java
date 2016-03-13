package crawl;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import Index.Indexing;
import url.UrlLink;


public class Crawler {
	private static String url;
	private static int depth = -1;
	private static boolean isExtraction = false;
	private static Queue<UrlLink> urlQueue = new LinkedList<UrlLink>();
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
		url = UrlLink.urlTrim(url);	
		urlQueue.add(UrlLink.addLink(url));
		while(depth >= 0){
			Queue<UrlLink> urlQueue2 = new LinkedList<UrlLink>();
			while(!urlQueue.isEmpty()){
				urlQueue2.add(urlQueue.poll());
			}
	        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
	        HashMap<UrlLink, Future<Set<String>>> resultList = new HashMap<UrlLink, Future<Set<String>>>();
	        
	        while(!urlQueue2.isEmpty()){
	        	UrlLink urlLink2Crawl = urlQueue2.poll();
	        	Crawl crawler = new Crawl(urlLink2Crawl);
	        	Future<Set<String>> resultSet = executor.submit(crawler);
	        	resultList.put(urlLink2Crawl, resultSet);
	        }
			for(UrlLink urlToLink : resultList.keySet()){
				Future<Set<String>> futureSet = resultList.get(urlToLink);
				try {
					Set<String> sets = futureSet.get();
					for(String u : sets){
						UrlLink newlink = null;
						//System.out.println(depth);
						if(depth > 0){
							newlink = UrlLink.addLink(u);
						}
						if(newlink != null){
							urlToLink.addLinkTo(newlink);
							urlQueue.add(newlink);
						}else if(UrlLink.getAllLinks().containsKey(u)){
							urlToLink.addLinkTo(UrlLink.getAllLinks().get(u));
						}
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
		for(Entry<String, UrlLink> entry: UrlLink.getAllLinks().entrySet()){
			System.out.println(entry.getValue().getUrl()+" in "+entry.getValue().linkFromSize()+ " out "+entry.getValue().linkToSize());			
		}
		System.out.println("starting indexing and ranking now");
		//Indexing.indexingAndRanking();
	 }
	
}


