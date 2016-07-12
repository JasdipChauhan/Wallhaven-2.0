package com.jscboy.wallhaven.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.facebook.login.widget.LoginButton;
import com.jscboy.wallhaven.Models.WallpaperModel;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6; //increment for version control
    private static final String DATABASE_NAME = "wallpapers.db"; //file name stored locally on device
    public static final String TABLE_SAVEDWALLPAPERS = "savedwallpapers"; //table name
    public static final String COLUMN_ID = "_id"; //column #1
    public static final String COLUMN_WALLPAPERURL = "wallpaperURL"; //columns in the table
    public static final String COLUMN_THUMBNAILURL = "thumbnailURL";
    public static final String COLUMN_RESOLUTION = "resolution";
    public static final String COLUMN_R = "R";
    public static final String COLUMN_B = "B";
    public static final String COLUMN_G = "G";


    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_SAVEDWALLPAPERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WALLPAPERURL + " TEXT," +
                COLUMN_THUMBNAILURL + " TEXT," +
                COLUMN_RESOLUTION + " TEXT," +
                COLUMN_R + " INTEGER," +
                COLUMN_B + " INTEGER," +
                COLUMN_G + " INTEGER"
                + ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVEDWALLPAPERS);
        Log.i("old", oldVersion+"");
        Log.i("new", newVersion+"");
        onCreate(db); // execute new table code when table is upgraded
    }

    //add wallpaper to db
    public void addWallpaper(WallpaperModel wallpaper) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLPAPERURL, wallpaper.getUrl());
        values.put(COLUMN_THUMBNAILURL, wallpaper.getThumbnail());
        values.put(COLUMN_RESOLUTION, wallpaper.getResolution());
        values.put(COLUMN_R, wallpaper.getR());
        values.put(COLUMN_B, wallpaper.getB());
        values.put(COLUMN_G, wallpaper.getG());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_SAVEDWALLPAPERS, null, values);
        db.close();
    }

    //delete wallpaper from db
    public void deleteWallpaper(String wallpaperURl) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SAVEDWALLPAPERS + " WHERE " + COLUMN_WALLPAPERURL + "=\""
                + wallpaperURl + "\";");
    }

    //retrieving the saved wallpapers in the database so the list can be inputted into the adapter with ease
    public ArrayList<WallpaperModel> getSavedWallpapers() {

        try {


            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_SAVEDWALLPAPERS;

            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            ArrayList<WallpaperModel> savedWallpapers = new ArrayList<>();

            while (!cursor.isAfterLast()) {
                String url = cursor.getString(cursor.getColumnIndex(COLUMN_WALLPAPERURL));
                String thumbnail = cursor.getString(cursor.getColumnIndex(COLUMN_THUMBNAILURL));
                String resolution = cursor.getString(cursor.getColumnIndex(COLUMN_RESOLUTION));
                int r = cursor.getInt(cursor.getColumnIndex(COLUMN_R));
                int g = cursor.getInt(cursor.getColumnIndex(COLUMN_G));
                int b = cursor.getInt(cursor.getColumnIndex(COLUMN_B));

                if (url != null && thumbnail != null && resolution != null) {
                    savedWallpapers.add(new WallpaperModel(url, thumbnail, resolution, r, g, b));
                }
                cursor.moveToNext();
            }

            db.close();


            return savedWallpapers;
        }catch (Exception e) {};

        return null;
    }

}
