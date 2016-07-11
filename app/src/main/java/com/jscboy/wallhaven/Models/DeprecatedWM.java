package com.jscboy.wallhaven.Models;


import com.android.volley.toolbox.NetworkImageView;

public class DeprecatedWM {

    private int _id;
    private String _wallpaperURL;
    private NetworkImageView thumbnail;
    private String resolution;

    public DeprecatedWM(String _wallpaperURL) {
        this._wallpaperURL = _wallpaperURL;
       //this.resolution = resolution;
        //this.thumbnail = thumbnail;
    }

    public DeprecatedWM() {
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
