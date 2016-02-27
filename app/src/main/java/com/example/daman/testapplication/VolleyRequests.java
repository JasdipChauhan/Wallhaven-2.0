package com.example.daman.testapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

public class VolleyRequests {

    public static final String TAG = "MyRecyclerList";
    private static final String beginning = "https://api.desktoppr.co/1/wallpapers?page=";
    private static String after_id;
    private Context context;
    private ProgressDialog progressDialogWallpaper;
    private ProgressDialog progressDialogLoadingMore;
    private List<ListItems> listItemsList;
    private RecyclerAdapter adapter;
    private int counter = 0;

    private Random generator = new Random();

    public VolleyRequests(Context context, RecyclerAdapter adapter, List<ListItems> listItemsList) {
        this.context = context;
        this.adapter = adapter;
        this.listItemsList = listItemsList;
    }

    public void updateList() {
        showPD();
        counter = generator.nextInt(1000) + 1;
        final String url = beginning + counter;

        RequestQueue queue = Volley.newRequestQueue(context);

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

    public void showPD() {
        if (progressDialogLoadingMore == null) {
            progressDialogLoadingMore = new ProgressDialog(context);
            progressDialogLoadingMore.setMessage("Loading more wallpapers...");
            progressDialogLoadingMore.setCancelable(false);
            progressDialogLoadingMore.show();
        }
    }

    public void hidePD() {
        if (progressDialogLoadingMore != null) {
            progressDialogLoadingMore.dismiss();
            progressDialogLoadingMore = null;
        }
    }

    public void preLoad() {
        progressDialogWallpaper = new ProgressDialog(context);
        progressDialogWallpaper.setMessage("Setting Wallpaper...");
        progressDialogWallpaper.setCancelable(false);
        progressDialogWallpaper.show();
    }

    public void postLoad() {
        progressDialogWallpaper.dismiss();
    }

}