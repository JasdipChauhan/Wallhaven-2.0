package com.jscboy.wallhaven.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jscboy.wallhaven.Models.ListItems;
import com.jscboy.wallhaven.Models.WallpaperModel;

public class DBManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1; //increment for version control
    private static final String DATABASE_NAME = "wallpapers.db"; //file name stored locally on device
    public static final String TABLE_PROPERTIES = "properties"; //table name
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
        String query = "CREATE TABLE " + TABLE_PROPERTIES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WALLPAPERURL + " TEXT " +
                COLUMN_THUMBNAILURL + " TEXT " +
                COLUMN_RESOLUTION + " TEXT " +
                COLUMN_R + " TEXT " +
                COLUMN_B + " TEXT " +
                COLUMN_G + " TEXT "
                + ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROPERTIES);
        onCreate(db); // execute new table code when table is upgraded
    }

    //add wallpaper to db
    public void addWallpaper(ListItems wallpaper) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLPAPERURL, wallpaper.getUrl());
        values.put(COLUMN_THUMBNAILURL, wallpaper.getThumbnail());
        values.put(COLUMN_RESOLUTION, wallpaper.getResolution());
        values.put(COLUMN_R, wallpaper.getR());
        values.put(COLUMN_B, wallpaper.getB());
        values.put(COLUMN_G, wallpaper.getG());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PROPERTIES, null, values);
        db.close();
    }

    //delete wallpaper from db
    public void deleteWallpaper(String wallpaperURl) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PROPERTIES + " WHERE " + COLUMN_WALLPAPERURL + "=\""
                + wallpaperURl + "\";");
    }

    //helper function to output db results
    public String dbToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PROPERTIES + " WHERE 1";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            String cursorString = cursor.getString(cursor.getColumnIndex("wallpaperURL"));

            if (cursorString != null) {
                dbString += cursorString;
                dbString += "\n";
            }
            cursor.moveToNext();
        }

        db.close();

        return dbString;
    }

}
