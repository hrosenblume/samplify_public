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

public class SongCrawler {
	private String url;
	private Document doc;
	private Card result;
	private String originalYoutubeLink;
	private String sampleYoutubeLink;
	private String line;
	private int startPoint;
	private int colonPoint;
	private String minString;
	private String secString;
	
	public SongCrawler() {
		result = new Card();
	}
	
	public void set(Result r) throws IOException {
		url = r.getNextURL();
		result = new Card();
		result.setArtists(r.getArtists());
		result.setTitle(r.getTitle());
		result.setImageURL(r.getImageURL());
		result.setNextURL("");
		result.setOriginalYoutubeLink("");
		result.setSampleYoutubeLink("");
		process();
	}
	
	public void process() throws IOException {
		doc = Jsoup.connect(url)
				.userAgent(MainActivity.userAgent)
				.timeout(3000)
				.get();
		scrapeResult();
	}
	
	public void scrapeResult() throws IOException {
		int minutes, seconds;
		
		Elements lines = doc.select("div.sampleVideoRight > iframe"); // sampled
		sampleYoutubeLink = lines.get(0).attr("src");
		lines = doc.select("div.sampleTimingRight"); // sampled time
		line = lines.get(0).text();
		startPoint = line.indexOf("at");
		colonPoint = line.indexOf(":");
		minString = line.substring(startPoint+3,colonPoint);
		secString = line.substring(colonPoint+1,colonPoint+3);
		minutes = Integer.parseInt(minString);
		seconds = Integer.parseInt(secString);
		sampleYoutubeLink = convertYoutubeLink(sampleYoutubeLink, minutes, seconds);
		result.setSampleYoutubeLink(sampleYoutubeLink);
		
		lines = doc.select("div.sampleVideoLeft > iframe"); // original
		originalYoutubeLink = lines.get(0).attr("src");
		lines = doc.select("div.sampleTimingLeft"); // original time
		line = lines.get(0).text();
		startPoint = line.indexOf("at");
		colonPoint = line.indexOf(":");
		minString = line.substring(startPoint+3,colonPoint);
		secString = line.substring(colonPoint+1,colonPoint+3);
		minutes = Integer.parseInt(minString);
		seconds = Integer.parseInt(secString);
		originalYoutubeLink = convertYoutubeLink(originalYoutubeLink, minutes, seconds);
		result.setOriginalYoutubeLink(originalYoutubeLink);
	}
	
	public String convertYoutubeLink(String inURL, int minutes, int seconds) {
		return inURL.substring(0, inURL.lastIndexOf("?")).replace("embed/", "watch?v=")
				+ "&t=" + minutes + "m" + seconds + "s";
	}

	public Card getCard() {
		return result;
	}
}
