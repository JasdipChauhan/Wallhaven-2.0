package com.example.daman.testapplication;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

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
    private int counter = 0;


    private Context mContext;
    //private ProgressDialog progressDialogWallpaper;
    //private ProgressDialog progressDialogLoadingMore;
    private String after_id;
    private VolleyRequests vr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Wallpapers");

        mContext = MainActivity.this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(MainActivity.this, listItemsList, this);
        mRecyclerView.setAdapter(adapter);


        vr = new VolleyRequests(MainActivity.this, adapter, listItemsList);
        vr.updateList();

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                vr.updateList();
            }
        });

    }/*

    public void updateList() {
        showPD();
        counter = generator.nextInt(400) + 1;
        String url = beginning + counter;
        adapter = new RecyclerAdapter(MainActivity.this, listItemsList, this);
        mRecyclerView.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(this);

        adapter.clearAdapter();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hidePD();
                try {
                    JSONObject pagination = response.getJSONObject("pagination");
                    after_id = pagination.getString("next");
                    JSONArray dataResponse = response.getJSONArray("response");

                    for (int i = 0; i < dataResponse.length(); i++) {

                        JSONObject post = dataResponse.getJSONObject(i);
                        JSONObject image = dataResponse.getJSONObject(i).getJSONObject("image");
                        JSONArray palette = dataResponse.getJSONObject(i).getJSONArray("palette");
                        JSONObject thumb = image.getJSONObject("thumb");

                        ListItems item = new ListItems();
                        item.setBackgroundColor(palette.get(0).toString());
                        item.setUrl(image.getString("url"));
                        item.setThumbnail(thumb.getString("url"));
                        item.setResolution(post.getString("width") + " x " + post.getString("height"));

                        listItemsList.add(item);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e(TAG, "Error" + volleyError.getMessage());
                hidePD();
            }
        });

        queue.add(jsonObjectRequest);

    }

    private void showPD() {
        if (progressDialogLoadingMore == null) {
            progressDialogLoadingMore = new ProgressDialog(this);
            progressDialogLoadingMore.setMessage("Loading more wallpapers...");
            progressDialogLoadingMore.setCancelable(false);
            progressDialogLoadingMore.show();
        }
    }

    private void hidePD() {
        if (progressDialogLoadingMore != null) {
            progressDialogLoadingMore.dismiss();
            progressDialogLoadingMore = null;
        }
    }*/

    //perform the async task that helps with changing the device's wallpaper
    @Override
    public void changeWallpaper(final String url) {
        new SetWallpaper().execute(url);
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
            vr.preLoad();
            /*progressDialogWallpaper = new ProgressDialog(MainActivity.this);
            progressDialogWallpaper.setMessage("Setting Wallpaper...");
            progressDialogWallpaper.setCancelable(false);
            progressDialogWallpaper.show();*/
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getBaseContext());
            try {
                wallpaperManager.setBitmap(bitmap);
                vr.postLoad();
                //progressDialogWallpaper.dismiss();
                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
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