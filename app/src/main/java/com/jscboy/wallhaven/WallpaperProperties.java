package com.jscboy.wallhaven;


public class WallpaperProperties {

    private int _id;
    private String _wallpaperURL;

    public WallpaperProperties(String wallpaperURL) {
        this._wallpaperURL = wallpaperURL;
    }

    public WallpaperProperties() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_wallpaperURL() {
        return _wallpaperURL;
    }

    public void set_wallpaperURL(String _wallpaperURL) {
        this._wallpaperURL = _wallpaperURL;
    }
}
