package crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Article_parse {
	public ArrayList<Article1>parsing_articles(){
		String main_page = "http://www.koreaherald.com/list.php?ct=020200000000";
		ArrayList<Article1> arr = new ArrayList<Article1>();
		
		try {
			Document doc = Jsoup.connect(main_page).get();
			// Elements text = doc.select("div[class=list2_article_headline HD]");
			Elements text = doc.select("p[class=mb13]");
			text = text.select("a");
			for(int i = 0 ; i < text.size(); i++) {
				String url = "http://www.koreaherald.com" + text.get(i).attr("href");
				arr.add(new Article1(text.get(i).text(), url, parse_body(url)));
				// System.out.println(arr.get(i).title);
				// System.out.println(arr.get(i).body);
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		return arr;
	}
	public String parse_body(String input_url) {
		String url = input_url;
		String body = "";
		try {
			Document doc = Jsoup.connect(url).get();
			Elements text = doc.select("div[id=articleText]");
			body = text.text();
		}catch(IOException e){
			e.printStackTrace();
		}
		return body;
	}
	public static void main(String[] args) {
		Article_parse obj = new Article_parse();
		ArrayList<Article1> arr = obj.parsing_articles();
		Iterator<Article1> iter = arr.iterator();
		
		while(iter.hasNext()) {
			Article1 temp = iter.next();
			System.out.println("<Title : " + temp.title + ">");
			System.out.println("<url: "+ temp.url + ">");
			System.out.println("body : " + temp.body);
			System.out.println("-------------------------------------");
		}
	}
	
}

class Article1 {
	String url;
	String title;
	String body;
	
	public Article1() {
		title = "";
		url = "";
		body = "";
	}
	
	public Article1(String title, String url, String body){
		this.title = title;
		this.url = url;
		this.body = body;
	}
}
