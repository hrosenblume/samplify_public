
package com.mhacks.samplify;

import java.util.ArrayList;

public class Result {
    private String title;

    private ArrayList<String> artists;

    private String imageURL;

    private String nextURL;

    private String originalYoutubeLink;

    // add BufferedImage of Album Art
    // or whatever is best format for Android

    public Result() {
        title = "";
        artists = new ArrayList<String>();
        imageURL = "";
        nextURL = "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNextURL(String nextURL) {
        this.nextURL = nextURL;
    }

    public void setImageURL(String imageURL) {
        if (imageURL == null) {
            this.imageURL = "http://samplifymobile.com/img/noalbumartwork.png";
        } else {
            this.imageURL = imageURL;
        }
    }

    public void setOriginalYoutubeLink(String originalYoutubeLink) {
        this.originalYoutubeLink = originalYoutubeLink;
    }

    public void addArtist(String artist) {
        if (!artist.equals("")) {
            artists.add(artist);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalYoutubeLink() {
        return originalYoutubeLink;
    }

    public ArrayList<String> getArtists() {
        return artists;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getNextURL() {
        return nextURL;
    }

    public String toString() {
        String s = "";
        s += ("Title: " + getTitle());
        s += "\n";
        s += "Artists: " + getArtists();
        s += "\n";
        s += "Image URL: " + getImageURL();
        s += "\n";
        s += "Next URL: " + getNextURL();
        return s;
    }
}
