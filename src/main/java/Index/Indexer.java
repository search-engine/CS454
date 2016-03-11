package Index;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import url.UrlLink;

public class Indexer {
	
	private static HashMap<String, IndexWords> index = new HashMap<String, IndexWords>();
	private static Set<String> stopwordset = new HashSet<String>();

	public static String[] stopwords = { "a", "as", "able", "about", "above",
			"according", "accordingly", "across", "actually", "after",
			"afterwards", "again", "against", "aint", "all", "allow", "allows",
			"almost", "alone", "along", "already", "also", "although",
			"always", "am", "among", "amongst", "an", "and", "another", "any",
			"anybody", "anyhow", "anyone", "anything", "anyway", "anyways",
			"anywhere", "apart", "appear", "appreciate", "appropriate", "are",
			"arent", "around", "as", "aside", "ask", "asking", "associated",
			"at", "available", "away", "awfully", "be", "became", "because",
			"become", "becomes", "becoming", "been", "before", "beforehand",
			"behind", "being", "believe", "below", "beside", "besides", "best",
			"better", "between", "beyond", "both", "brief", "but", "by",
			"cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause",
			"causes", "certain", "certainly", "changes", "clearly", "co",
			"com", "come", "comes", "concerning", "consequently", "consider",
			"considering", "contain", "containing", "contains",
			"corresponding", "could", "couldnt", "course", "currently",
			"definitely", "described", "despite", "did", "didnt", "different",
			"do", "does", "doesnt", "doing", "dont", "done", "down",
			"downwards", "during", "each", "edu", "eg", "eight", "either",
			"else", "elsewhere", "enough", "entirely", "especially", "et",
			"etc", "even", "ever", "every", "everybody", "everyone",
			"everything", "everywhere", "ex", "exactly", "example", "except",
			"far", "few", "ff", "fifth", "first", "five", "followed",
			"following", "follows", "for", "former", "formerly", "forth",
			"four", "from", "further", "furthermore", "get", "gets", "getting",
			"given", "gives", "go", "goes", "going", "gone", "got", "gotten",
			"greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt",
			"have", "havent", "having", "he", "hes", "hello", "help", "hence",
			"her", "here", "heres", "hereafter", "hereby", "herein",
			"hereupon", "hers", "herself", "hi", "him", "himself", "his",
			"hither", "hopefully", "how", "howbeit", "however", "i", "id",
			"ill", "im", "ive", "ie", "if", "ignored", "immediate", "in",
			"inasmuch", "inc", "indeed", "indicate", "indicated", "indicates",
			"inner", "insofar", "instead", "into", "inward", "is", "isnt",
			"it", "itd", "itll", "its", "its", "itself", "just", "keep",
			"keeps", "kept", "know", "knows", "known", "last", "lately",
			"later", "latter", "latterly", "least", "less", "lest", "let",
			"lets", "like", "liked", "likely", "little", "look", "looking",
			"looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean",
			"meanwhile", "merely", "might", "more", "moreover", "most",
			"mostly", "much", "must", "my", "myself", "name", "namely", "nd",
			"near", "nearly", "necessary", "need", "needs", "neither", "never",
			"nevertheless", "new", "next", "nine", "no", "nobody", "non",
			"none", "noone", "nor", "normally", "not", "nothing", "novel",
			"now", "nowhere", "obviously", "of", "off", "often", "oh", "ok",
			"okay", "old", "on", "once", "one", "ones", "only", "onto", "or",
			"other", "others", "otherwise", "ought", "our", "ours",
			"ourselves", "out", "outside", "over", "overall", "own",
			"particular", "particularly", "per", "perhaps", "placed", "please",
			"plus", "possible", "presumably", "probably", "provides", "que",
			"quite", "qv", "rather", "rd", "re", "really", "reasonably",
			"regarding", "regardless", "regards", "relatively", "respectively",
			"right", "said", "same", "saw", "say", "saying", "says", "second",
			"secondly", "see", "seeing", "seem", "seemed", "seeming", "seems",
			"seen", "self", "selves", "sensible", "sent", "serious",
			"seriously", "seven", "several", "shall", "she", "should",
			"shouldnt", "since", "six", "so", "some", "somebody", "somehow",
			"someone", "something", "sometime", "sometimes", "somewhat",
			"somewhere", "soon", "sorry", "specified", "specify", "specifying",
			"still", "sub", "such", "sup", "sure", "ts", "take", "taken",
			"tell", "tends", "th", "than", "thank", "thanks", "thanx", "that",
			"thats", "thats", "the", "their", "theirs", "them", "themselves",
			"then", "thence", "there", "theres", "thereafter", "thereby",
			"therefore", "therein", "theres", "thereupon", "these", "they",
			"theyd", "theyll", "theyre", "theyve", "think", "third", "this",
			"thorough", "thoroughly", "those", "though", "three", "through",
			"throughout", "thru", "thus", "to", "together", "too", "took",
			"toward", "towards", "tried", "tries", "truly", "try", "trying",
			"twice", "two", "un", "under", "unfortunately", "unless",
			"unlikely", "until", "unto", "up", "upon", "us", "use", "used",
			"useful", "uses", "using", "usually", "value", "various", "very",
			"via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we",
			"wed", "well", "were", "weve", "welcome", "well", "went", "were",
			"werent", "what", "whats", "whatever", "when", "whence",
			"whenever", "where", "wheres", "whereafter", "whereas", "whereby",
			"wherein", "whereupon", "wherever", "whether", "which", "while",
			"whither", "who", "whos", "whoever", "whole", "whom", "whose",
			"why", "will", "willing", "wish", "with", "within", "without",
			"wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you",
			"youd", "youll", "youre", "youve", "your", "yours", "yourself",
			"yourselves", "zero" };	
	
	
	public static void indexer(String url){
		
		for(String stopword : stopwords) {
			stopwordset.add(stopword);
		}
		
//		try {
//		AutoDetectParser parser = new AutoDetectParser();
//		InputStream stream = TikaInputStream.get(new File(url));
//        ContentHandler handler = new BodyContentHandler();
//        Metadata metadata = new Metadata();
//       
//        LinkContentHandler linkhandler=new LinkContentHandler();
//        InputStream stream1 = TikaInputStream.get(new File(url));
//        parser.parse(stream1, linkhandler, metadata, new ParseContext());
//        List<Link> links=linkhandler.getLinks();
//        Set<String> uris = new HashSet<String>();
//        for(Link link: links){
//        	if(link.isAnchor()){
//        		String uri = link.getUri();
//            	uris.add(uri);
//        	}
//        }
//        parser.parse(stream, handler, metadata, new ParseContext());
//        String plainText = handler.toString();
//        String trimText = plainText.replaceAll("\\P{L}", " ");
//        stream.close();
//
//		for(String uri : uris){
//			getRealPath(url, uri);
//			System.out.println(uri);
//		}
        
        
//        String[] info = trimText.split("\\s+");
        
//        for(String s : info) {
//        	if(!stopwordset.contains(s.toLowerCase()) && !s.isEmpty() && s.length() > 1){
//        		IndexWords word = null;
//        		if(index.containsKey(s.toLowerCase())) {
//        			word = index.get(s.toLowerCase());
//        		}else{
//        			word = new IndexWords();
//        			index.put(s.toLowerCase(), word);
//        		}
//	        	word.setDocument(url);
//	        }
//        }
//		}catch(Exception e) {}
		
		try {
			Document document = Jsoup.parse(new File(url),"UTF-8");
			Elements links = document.select("a[href]");
			UrlLink link = UrlLink.getAllLinks().get(url);
			for(Element element: links){
				//System.out.println(element.baseUri());
				//System.out.println(element.);
				String href = element.attr("href");
				
				
				if(href == null || href != null &&(href.length() == 0 || href.startsWith("#") || href.contains("http://")||href.contains("https://"))){
					continue;
				}
				String u = getRealPath(url, href);
				if(UrlLink.getAllLinks().containsKey(u)){
					link.addLinkTo(u);
				}
				
				
			}
			
			String plainText = document.text();
			String[] infoArr = plainText.split("\\s+");
			int totalwords = 0;
			
	        for(String s : infoArr) {
	        	s = s.replaceAll("[^a-zA-Z]", "").toLowerCase();
        		if(!stopwordset.contains(s) && !s.isEmpty() && s.length() > 1){
        			IndexWords word = null;
        			if(index.containsKey(s)) {
        				word = index.get(s);
        			}else{
        				word = new IndexWords();
        				index.put(s, word);
        			}
	        		word.setDocument(url);
	        		totalwords++;
	        	}
	        }
	        UrlLink.getAllLinks().get(url).setTotalWordCount(totalwords);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getRealPath(String url, String uri) {
		if(url.charAt(url.length()-1) == '\\'){
			url = url.substring(0, url.length()-1);
		}
		url = url.substring(0, url.lastIndexOf('\\'));
		//System.out.println(url);
		while(uri.substring(0, 3).equals("../")){
			uri = uri.substring(3);
			url = url.substring(0, url.lastIndexOf('\\'));
		}
		return url+uri.replace('/', '\\');
	}

	public static HashMap<String, IndexWords> getALlTerms(){
		return index;
	}
	
}
