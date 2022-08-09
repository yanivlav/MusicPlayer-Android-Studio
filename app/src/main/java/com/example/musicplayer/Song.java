package com.example.musicplayer;


import java.io.Serializable;

/**
 * Created by erankatsav on 19/03/2018.
 */

public class Song implements Serializable {

    private String name;
    private String link;
    private String photoPath;
    private int photoPathInt;

    public Song(String name, String link, int photoPathInt) {
        this.name = name;
        this.link = link;
        this.photoPathInt = photoPathInt;
    }

    public Song(String name, String link, String photoPath) {
        this.name = name;
        this.link = link;
        this.photoPath = photoPath;
    }



    public String getPhotoPath() { return photoPath; }

    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String name) {
        this.link = link;
    }

    public int getPhotoPathInt() {
        return photoPathInt;
    }

    public void setPhotoPathInt(int photoPathInt) {
        this.photoPathInt = photoPathInt;
    }

}
