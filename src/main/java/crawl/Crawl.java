package crawl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import url.UrlLink;


public class Crawl implements Callable<Set<String>>{
	private UrlLink link;
	private int depth;
	private String base = System.getProperty("user.dir") + "/url/";
    private static Set<String> images = new HashSet<String>();
	
	public Crawl(UrlLink urlLink2Crawl, int depth) {
		this.link = urlLink2Crawl;
		//System.out.println("url to crawl" + urlLink2Crawl.getUrl());
		this.depth = depth;
	}

	public Set<String> call() throws Exception {
		Set<String> linkSet = new HashSet<String>();
		try {
			String path = base + link.getPath();
			//System.out.println(link + " path---> " + path);
			File file = new File(path);
			if(!file.exists()){if(file.mkdirs()){/*System.out.println("path created "+path);*/}}
			Document doc = Jsoup.connect(link.getUrl()).ignoreContentType(true).userAgent("Mozilla").get();
			if(depth > 0){
				Elements links = doc.select("a[href]");
				for(Element link : links){	
					try {
						Response res = Jsoup.connect(link.attr("abs:href")).execute();
						linkSet.add(UrlLink.urlTrim(res.url().toString()));
					}catch(Exception e) {
						
					}
				}
			}
			byte[] bytes = Jsoup.connect(link.getUrl()).maxBodySize(0).ignoreContentType(true).execute().bodyAsBytes();
			Response response = Jsoup.connect(link.getUrl()).ignoreContentType(true).execute();
			String contentType = response.contentType();
			Boolean isHTML = contentType.contains("text/html");
			String fname = getFname(link.getUrl(), isHTML);
			//System.out.println(link+" is downloaded");
			FileOutputStream fos = new FileOutputStream(path+fname);
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
						if(imageUrl.contains("?")){
							imageUrl = imageUrl.substring(0,imageUrl.indexOf("?"));
						}
						if(!images.contains(imageUrl)){
							
							String mPath = base + getPath(imageUrl);
							String mName = getFname(imageUrl, false);
//							System.out.println("image url:"+imageUrl+"---"+mPath);
							File imFile = new File(mPath);
							if(!imFile.exists()){
								if(imFile.mkdirs()){
								}
							}
							URL imgUrl = new URL(imageUrl);
							InputStream in = new BufferedInputStream(imgUrl.openStream());
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] buf = new byte[4096];

							int n = 0;
							while (-1!=(n=in.read(buf)))
							{
							   out.write(buf, 0, n);
							}
							out.close();
							in.close();
							
							byte[] imageResponse = out.toByteArray();
//							System.out.println("m path "+mPath + mName);
							FileOutputStream imageFos = new FileOutputStream(mPath + mName);
							imageFos.write(imageResponse);
							imageFos.close();
							
						}
						
					}
				}
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linkSet;
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
