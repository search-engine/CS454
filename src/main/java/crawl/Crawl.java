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


public class Crawl implements Callable<Set<String>>{
	private String url;
	private int depth;
	private static final String base = System.getProperty("user.dir") + "/url/";

    private static Set<String> images = new HashSet<String>();
	
	public Crawl(String url, int depth) {
		this.url = url;
		this.depth = depth;
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
			byte[] bytes = Jsoup.connect(url).maxBodySize(0).ignoreContentType(true).execute().bodyAsBytes();
			Response response = Jsoup.connect(url).ignoreContentType(true).execute();
			String content = response.contentType();
			String fname = url.replace("://", "_").replace("/", "_").replace("?", "_");
			if(content.contains("text/html")) {
				FileOutputStream fos = new FileOutputStream(base + fname + ".html");
				fos.write(bytes);
				fos.close();
				JSONArray imageArray = new JSONArray();
				for(Element img : doc.select("img")){
					if(img.baseUri().equals(doc.baseUri())){
						String imageUrl = img.absUrl("src");
						if(imageUrl.contains(";")){
							imageUrl = imageUrl.substring(0, imageUrl.indexOf(";"));
						}
							if(!images.contains(imageUrl)){
								BufferedImage image = null;
								URL url = new URL(imageUrl);
					            image = ImageIO.read(url);
					            ImageIO.write(image, "png",new File(base + imageUrl.replace("://", "_").replace("/", "_").replace("?", "_")));
								images.add(imageUrl);
								imageArray.put(imageUrl.replace("://", "_").replace("/", "_").replace("?", "_").substring(0, imageUrl.lastIndexOf(".") - 1)+"png");
							}
						
					}
				}
			}
			else {
				FileOutputStream fos = new FileOutputStream(base + fname);
				fos.write(bytes);
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linkSet;
	}						
}
