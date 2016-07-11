package com.jscboy.wallhaven.Models;

public class WallpaperModel {

    private int _id;
    private String url;
    private String thumbnail;
    private String backgroundColor;
    private String resolution;
    private int r;
    private int g;
    private int b;

    public WallpaperModel(String url, String thumbnail, String resolution, int r, int g, int b) {
        this.url = url;
        this.thumbnail = thumbnail;
        this.resolution = resolution;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public WallpaperModel() {
    }

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


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setB(int b) {
        this.b = b;
    }
}
