package crawling;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Jsoup_example {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String connUrl = "https://news.naver.com/";
			
			Document doc = Jsoup.connect(connUrl).get();
			Elements text = doc.select("div[class=hdline_article_tit]");
			text = text.select("a");
			for(int i = 0; i < text.size(); i++) {
				System.out.println(text.get(i).text());
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
