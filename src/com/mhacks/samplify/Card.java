
package com.mhacks.samplify;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Card extends Result implements Parcelable {

    private String sampleYoutubeLink;

    public Card() {
        super();
    }

    public Card(Result r) {
        this.setTitle(r.getTitle());
        this.setArtists(r.getArtists());
        this.setImageURL(r.getImageURL());
        this.setNextURL(r.getNextURL());
        this.setOriginalYoutubeLink("");
        this.setSampleYoutubeLink("");
    }

    public Card(Parcel parcel) {
        this.setTitle(parcel.readString());
        ArrayList<String> artists = new ArrayList<String>();
        parcel.readList(artists, null);
        for (String artist : artists) {
            this.addArtist(artist);
        }
        this.setImageURL(parcel.readString());
        this.setNextURL(parcel.readString());
        this.setOriginalYoutubeLink(parcel.readString());
        this.setSampleYoutubeLink(parcel.readString());
    }

    public void setArtists(ArrayList<String> artists) {
        for (String a : artists) {
            this.addArtist(a);
        }
    }

    public void setSampleYoutubeLink(String sampleYoutubeLink) {
        this.sampleYoutubeLink = sampleYoutubeLink;
    }

    public String getSampleYoutubeLink() {
        return sampleYoutubeLink;
    }

    public String getArtistNames() {
        String result = "";
        for (String artist : this.getArtists()) {
            result += artist + ", ";
        }
        result = result.substring(0, result.length() - 2);
        return result;
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
        s += "\n";
        s += "Original Youtube Link: " + getOriginalYoutubeLink();
        s += "\n";
        s += "Sample Youtube Link: " + sampleYoutubeLink;
        return s;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getTitle());
        out.writeList(getArtists());
        out.writeString(getImageURL());
        out.writeString(getNextURL());
        out.writeString(getOriginalYoutubeLink());
        out.writeString(sampleYoutubeLink);
    }

    public static Creator<Card> CREATOR = new Creator<Card>() {
        public Card createFromParcel(Parcel parcel) {
            return new Card(parcel);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}
