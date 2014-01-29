package com.mhacks.samplify;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.io.IOException;
import java.util.ArrayList;

public class SampleCrawler {
	private ArrayList<Result> results;
	private String url;
	private Document doc;
	
	public SampleCrawler() {
		results = new ArrayList<Result>();
	}
	
	public void process() throws IOException {
		doc = Jsoup.connect(url)
				.userAgent(MainActivity.userAgent)
				.timeout(3000)
				.get();
		scrapeResult();
	}
	
	public void scrapeResult() throws IOException {
		if(containsSamples()) {
			Elements lists = doc.select("ul.list");
			Element tracksList = lists.get(0); //this gets only the tracks list
			Elements otherSongs = tracksList.select("li.listEntry");
			//Element songResult = otherSongs.get(0);
			for(Element songResult: otherSongs) {
				Result r = new Result();
				Elements otherSongElements = songResult.select("a");
				//System.out.println(songResult);
				
				Elements resultURLs = songResult.getElementsByTag("a");
				String resultURL = resultURLs.attr("href");
				
				Elements imageURLs = songResult.getElementsByAttribute("src");
				Element imageURL = imageURLs.get(0);
				
				int counter = 0;
				for(Element el: otherSongElements) {
					if(counter==1) {
						r.setTitle(el.text());
					} else {
						r.addArtist(el.text());
					}
					counter++;
				}
				r.setImageURL(urlPuller(imageURL));
				r.setNextURL(urlPuller(resultURL));
				this.addResult(r);
			} 
		} else {
			throw new IOException("there were no results");	
		}
	}
	
	public boolean containsSamples() {
		Elements lists = doc.select("div.sectionHeader > span");
		if (!lists.get(0).text().substring(0, 8).equals("Contains")) {
			return false;
		} else {
			return true;
		}
		
	}
	
	private void addResult(Result r) {
		results.add(r);
	}
	
	public String urlPuller(Element inURL) {
		int start = inURL.toString().indexOf("\"", 0);
		int end = inURL.toString().indexOf("\"",10);
		String finalURL = "http://whosampled.com" + inURL.toString().substring(start+1,end);
		return finalURL;
	}
	
	public String urlPuller(String inURL) {
		String finalURL = "http://whosampled.com" + inURL;
		return finalURL;
		
	}
	
	public ArrayList<Result> getResults() {
		return results;
	}
	
	public void set (String url) {
		this.url = url;
		try {
			process();
		} catch(Exception e) {System.out.println(e.getMessage());}
	}
}
