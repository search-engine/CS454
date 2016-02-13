package crawl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class Crawl implements Callable<Set<String>>{
	private String url;
	private int depth;
	private boolean isExtraction;
	private static final String path = System.getProperty("user.dir") + "/url/";

	
	{	 
	File file = new File(System.getProperty("user.dir")+"/url/");
	if(!file.exists()){
		if(file.mkdir()){
			System.out.println("created url directory");
		}
	}
	}
	private static DB db = getMongoDB();
    private static Set<String> images = new HashSet<String>();
	
	public Crawl(String url, int depth, boolean isExtraction) {
		this.url = url;
		this.depth = depth;
		this.isExtraction = isExtraction;
	}


	private static DB getMongoDB() {
		DB dbm = null;
		try{
		    MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		    dbm = mongoClient.getDB( "search_engine" );
			}catch(Exception e){
		        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    }
		return dbm;
	}


	public Set<String> call() throws Exception {
		Set<String> linkSet = new HashSet<String>();
		try {
			Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla").get();
			if(depth > 0){
				Elements links = doc.select("a[href]");
				for(Element link : links){
					linkSet.add(link.attr("abs:href"));
				}
			}
			byte[] bytes = Jsoup.connect(url).maxBodySize(2000000).ignoreContentType(true).execute().bodyAsBytes();
			Response response = Jsoup.connect(url).ignoreContentType(true).execute();
			String content = response.contentType();
			String fname = url.replace("://", "_").replace("/", "_");
			if(content.contains("text/html")) {
				FileOutputStream fos = new FileOutputStream(path + fname + ".html");
				fos.write(bytes);
				fos.close();
				JSONArray imageArray = new JSONArray();
				for(Element img : doc.select("img")){
											
					if(img.baseUri().equals(doc.baseUri())){
						String imageUrl = img.absUrl("src");
						synchronized(images){
							if(!images.contains(imageUrl)){
								BufferedImage image = null;
								URL url = new URL(imageUrl);
					            image = ImageIO.read(url);
					            ImageIO.write(image, "png",new File(path + imageUrl.replace("/", "_")));
								images.add(imageUrl);
								imageArray.put(imageUrl.replace("://", "_").replace("/", "_").substring(0, imageUrl.lastIndexOf(".") - 1)+"png");
							}
						}
					}
				}
				if(isExtraction){
					String html = doc.html();
					String title = doc.title();

					Document extractDoc = Jsoup.parse(html);
					
					String text = extractDoc.text();
					
					try{  		   
						DBCollection coll = db.getCollection("mycol");	
						BasicDBObject obj = new BasicDBObject("title", title).
							            append("url", url).
							            append("type", content).
							            append("text", text).
							            append("path", path + fname + ".html").
							            append("images", imageArray);
					         
					    coll.insert(obj);
					    System.out.println("Document inserted successfully");
					}catch(Exception e){
					    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
					}					
				}
			}
			else {
				FileOutputStream fos = new FileOutputStream(path + fname);
				fos.write(bytes);
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linkSet;
	}						
}
