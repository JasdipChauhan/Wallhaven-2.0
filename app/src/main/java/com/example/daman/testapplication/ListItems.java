package com.example.daman.testapplication;

public class ListItems {

    private String url;
    private String thumbnail;
    private String backgroundColor;
    private String resolution;
    private int colour;
    private int r;
    private int g;
    private int b;

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        int color = (int)Long.parseLong(backgroundColor, 16);
        r = (color >> 16) & 0xFF;
        g = (color >> 8) & 0xFF;
        b = (color) & 0xFF;
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

    public void setResolution (String resolution) {
        this.resolution = resolution;
    }

    public String getResolution () {
        return resolution;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }
    
    public int getB() {
        return b;
    }
}
