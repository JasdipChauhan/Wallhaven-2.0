package com.example.daman.testapplication;

/**
 * Created by Daman on 2/15/2016.
 */
public class ListItems {

    private String url;
    private String thumbnail;
    private String backgroundColor;

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBackgroundColor() {

        return backgroundColor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {

        return thumbnail;
    }
}
