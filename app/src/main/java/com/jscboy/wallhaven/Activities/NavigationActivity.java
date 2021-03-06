package com.jscboy.wallhaven.Activities;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

    boolean isOnSavedWallpapers = false;

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

        ItemTouchHelper.SimpleCallback onSwipeCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (isOnSavedWallpapers) {
                    Log.i("D' Adapter Index: ", viewHolder.getAdapterPosition()+"");
                    savedAdapter.delete(viewHolder.getAdapterPosition());
                    savedAdapter.notifyDataSetChanged();
                } else {
                    httpAdapter.delete(viewHolder.getAdapterPosition());
                    httpAdapter.notifyDataSetChanged();
                }
            }
        };

        ItemTouchHelper onSwipeHelper = new ItemTouchHelper(onSwipeCallback);
        onSwipeHelper.attachToRecyclerView(mRecyclerView);

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
            httpAdapter.notifyDataSetChanged();
            mRecyclerView.swapAdapter(httpAdapter, true);
            vr.updateList();
            isOnSavedWallpapers = false;
        } else if (id == R.id.nav_save) {
            mRecyclerView.swapAdapter(new RecyclerAdapter(mContext, dbManager.getSavedWallpapers(), null), true); //hacky fix for now
            savedAdapter.notifyDataSetChanged();
            isOnSavedWallpapers = true;
        } else if (id == R.id.rate_application) {
            String url = "https://play.google.com/store/apps/details?id=com.jscboy.wallhaven&hl=en";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
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

    private void makeSnackbar(String message, int colour) {
        Snackbar snack = Snackbar.make(findViewById(R.id.content_rel_layout), message, Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        //tv.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        tv.setTextColor(colour);
        snack.show();
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
            makeSnackbar("Changing your wallpaper...", Color.WHITE);
            vr.preLoad();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getBaseContext());
            try {
                wallpaperManager.setBitmap(bitmap);
                vr.postLoad();
                makeSnackbar("Enjoy!", Color.WHITE);
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
