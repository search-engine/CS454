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

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawl implements Callable<Set<String>>{
	private String url;
	private int depth;
	private String base = System.getProperty("user.dir") + "/url/";
    private static Set<String> images = new HashSet<String>();
	
	public Crawl(String url, int depth) {
		this.url = url;
		this.depth = depth;
	}

	public Set<String> call() throws Exception {
		Set<String> linkSet = new HashSet<String>();
		try {
			String path = base + getPath(url);
			System.out.println(url + " path---> " + path);
			File file = new File(path);
			if(!file.exists()){if(file.mkdirs()){System.out.println("path created "+path);}}
			Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla").get();
			if(depth > 0){
				Elements links = doc.select("a[href]");
				for(Element link : links){	
					try {
						Response res = Jsoup.connect(link.attr("abs:href")).execute();
						linkSet.add(res.url().toString());
					}catch(Exception e) {
						
					}
				}
			}
			byte[] bytes = Jsoup.connect(url).maxBodySize(0).ignoreContentType(true).execute().bodyAsBytes();
			Response response = Jsoup.connect(url).ignoreContentType(true).execute();
			String contentType = response.contentType();
			Boolean isHTML = contentType.contains("text/html");
			String fname = getFname(url, isHTML);
			FileOutputStream fos = new FileOutputStream(path + fname);
			fos.write(bytes);
			fos.close();
			/*
			if(isHTML) {
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
					        String mPath = base + getPath(url.toString());
					        File mfile = new File(mPath);
							if(!mfile.exists()){if(mfile.mkdir()){}}
					        String mName = getFname(url.toString(), false);
					        ImageIO.write(image, "png",new File(mPath + mName));
					        images.add(imageUrl);
						}
						
					}
				}
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linkSet;
	}

	private String getPath(String url2) {
		//remove https:// or http://
		url2 = url2.substring(url2.indexOf("//")+2);
		int lastSlash = url2.lastIndexOf('/');
		if(lastSlash != -1){
			return url2.substring(0, lastSlash + 1);
		}else{
			return url2 + "/";
		}
	}

	private String getFname(String url2, Boolean isHTML) {
		url2 = url2.substring(url2.indexOf("//")+2);
		int lastSlash = url2.lastIndexOf('/');
		if(lastSlash != -1){
			url2 = url2.substring(lastSlash + 1); 
		}
		if(isHTML){
			if(url2.contains(".html")||url2.contains(".htm")){
				return url2;
			}else{
				return url2 + ".html";
			}
		}
		return url2;
	}						
}
