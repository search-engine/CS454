package crawl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawl implements Callable<Set<String>>{
	private String url;
	private int depth;
	private boolean isExtraction;
	private static final String path = System.getProperty("user.dir") + "/src/url/";

	public Crawl(String url, int depth, boolean isExtraction) {
		this.url = url;
		this.depth = depth;
		this.isExtraction = isExtraction;
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
				if(isExtraction){
					String html = doc.html();
					String title = doc.title();

					Document extractDoc = Jsoup.parse(html);
					
					String text = extractDoc.text();

					JSONObject extractJsonObj = new JSONObject();
					try {
						extractJsonObj.put("title",title);
						extractJsonObj.put("url", url);
						extractJsonObj.put("type", content);
						extractJsonObj.put("text", text);
						extractJsonObj.put("path", path + fname + ".html");
						JSONArray imageArray = new JSONArray();
						for(Element img : doc.select("img")){
													
							if(img.baseUri().equals(doc.baseUri())){
								imageArray.put(img.attr("src"));
							}
						}
						extractJsonObj.put("images", imageArray);
						

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println(extractJsonObj.toString());

					
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
