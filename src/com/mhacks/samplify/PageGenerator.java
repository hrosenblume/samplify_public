package com.mhacks.samplify;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.os.Environment;

public class PageGenerator {

	public static void generatePage(ArrayList<Card> cards) throws IOException {

		if (isExternalStorageWritable()) {
			//File externalDir = Context.getApplicationContext();
			File textFile = new File(Environment.getExternalStorageDirectory().getPath()
					+ File.separator + "text.html");

			PrintWriter writer = new PrintWriter(textFile);
			writer.println("<!DOCTYPE html>");
			writer.println("<html lang=\"en\">");
			writer.println("<head>");
			writer.println("<meta charset=\"utf-8\">");
			writer.println("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
			writer.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
			writer.println("<meta name=\"description\" content=\"\">");
			writer.println("<meta name=\"author\" content=\"\">");
			writer.println("<link href=\"" + "file:///android_asset/css.css"
					+ "\" rel=\"stylesheet\" type=\"text/css\">");
			writer.println("</head>");
			writer.println("<body>");

			int counter = 0;

			for (Card c : cards) {

				if (counter == 0) {
					writer.println("<div class=\"yourSong\">");
					writer.println("<h1>Your Song:</h1>");
				} else {
					writer.println("<div class=\"otherSongs\">");
					if (counter == 1) {
						writer.println("<h1>Contains Samples Of:</h1>");
					}
				}

				writer.println("<div class=\"card\">");
				writer.println("<div class=\"img\">");
				writer.println("<img src=\"" + c.getImageURL() + "\">");
				writer.println("</div>");
				writer.println("<p>" + c.getTitle() + "</p>");
				writer.println("<p>" + c.getArtistNames() + "</p>");

				if (counter == 0) {
					if (hasValidLink(c, counter)) {
						writer.println("<p><a href=\""
								+ c.getOriginalYoutubeLink()
								+ "\" class=\"btn btn-danger\">Play Original Song</a></p>");
					} else {
						writer.println("<p><a href=\"#\" class=\"btn btn-danger btn-no\">No YouTube Link Available</a></p>");
					}
				} else if (hasValidLink(c, counter)) {
					writer.println("<p><a href=\"" + c.getSampleYoutubeLink()
							+ "\" class=\"btn btn-danger\">Play Sample</a></p>");
				} else {
					writer.println("<p><a href=\"#\" class=\"btn btn-danger btn-no\">No Songs Available</a></p>");
				}

				writer.println("</div>");
				writer.println("</div>");
				counter++;
			}
			writer.println("</body>");
			writer.println("</html>");
			writer.close();
		} else {
			throw new IOException();
		}
	}

	private static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	private static boolean hasValidLink(Card c, int i) {
		if (i == 0) {
			return c.getOriginalYoutubeLink().contains("youtube");
		}
		return c.getSampleYoutubeLink().contains("youtube");
	}
}
