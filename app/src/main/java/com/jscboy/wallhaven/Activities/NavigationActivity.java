package com.jscboy.wallhaven.Activities;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jscboy.wallhaven.Adapters.RecyclerAdapter;
import com.jscboy.wallhaven.Database.DBManager;
import com.jscboy.wallhaven.Interfaces.CallbackInterface;
import com.jscboy.wallhaven.Libraries.EndlessRecyclerOnScrollListener;
import com.jscboy.wallhaven.Models.WallpaperModel;
import com.jscboy.wallhaven.R;
import com.jscboy.wallhaven.Singletons.VolleyRequests;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CallbackInterface {


    public static final String TAG = "MyRecyclerList";
    private static final String beginning = "https://api.desktoppr.co/1/wallpapers?page=";
    private List<WallpaperModel> httpWallpaperList = new ArrayList<>();
    private List<WallpaperModel> savedWallpapersList = new ArrayList<>();
    private RecyclerAdapter httpAdapter;
    private RecyclerAdapter savedAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefresh;

    private Context mContext;
    private String after_id;
    private VolleyRequests vr;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Wallpapers");

        mContext = NavigationActivity.this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        dbManager = new DBManager(mContext, null, null, 1);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        httpAdapter = new RecyclerAdapter(mContext, httpWallpaperList, this);
        savedAdapter = new RecyclerAdapter(mContext, dbManager.getSavedWallpapers(), this);

        mRecyclerView.setAdapter(httpAdapter);

        vr = VolleyRequests.getInstance(mContext, httpAdapter, httpWallpaperList);
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

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wallpapers) {

        } else if (id == R.id.nav_save) {
            mRecyclerView.swapAdapter(savedAdapter, true);
        } else if (id == R.id.rate_application) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
