package com.jscboy.wallhaven.Activities;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.jscboy.wallhaven.Interfaces.CallbackInterface;
import com.jscboy.wallhaven.Database.DBManager;
import com.jscboy.wallhaven.Libraries.EndlessRecyclerOnScrollListener;
import com.jscboy.wallhaven.Models.ListItems;
import com.jscboy.wallhaven.R;
import com.jscboy.wallhaven.Adapters.RecyclerAdapter;
import com.jscboy.wallhaven.Singletons.VolleyRequests;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CallbackInterface {

    public static final String TAG = "MyRecyclerList";
    private static final String beginning = "https://api.desktoppr.co/1/wallpapers?page=";
    private List<ListItems> listItemsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;

    private Context mContext;
    private String after_id;
    private VolleyRequests vr;

    private DBManager dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Wallpapers");

        mContext = MainActivity.this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(MainActivity.this, listItemsList, this);
        mRecyclerView.setAdapter(adapter);

        vr = VolleyRequests.getInstance(MainActivity.this, adapter, listItemsList);
        vr.updateList();

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public synchronized void onLoadMore(int current_page) {
                vr.updateList();
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public synchronized void onRefresh() {
                vr.clearList();
                vr.updateList();
                swipeRefresh.setRefreshing(false);
            }
        });

        //porting over to a navigation drawer
        Intent i = new Intent(this, NavigationActivity.class);
        startActivity(i);
    }

    //perform the async task that helps with changing the device's wallpaper
    @Override
    public void changeWallpaper(final String url) {
        new SetWallpaper().execute(url);
    }

    private int fetchAccentColor() {
        return ContextCompat.getColor(mContext, R.color.colorAccent);
    }

    private class SetWallpaper extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                return Picasso.with(mContext).load(params[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Snackbar.make(findViewById(R.id.linLayout), "Changing your wallpaper...",
                    Snackbar.LENGTH_SHORT).show();
            vr.preLoad();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getBaseContext());
            try {
                wallpaperManager.setBitmap(bitmap);
                vr.postLoad();
                Snackbar.make(findViewById(R.id.linLayout), "Enjoy!",
                        Snackbar.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}