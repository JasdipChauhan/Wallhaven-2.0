package com.example.daman.testapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MyRecyclerList";
    private List<ListItems> listItemsList = new ArrayList<ListItems>();

    private RecyclerView mRecyclerView;
    private RecyclerAdapter adapter;

    private int counter = 0;
    private String count;
    private String jsonSubReddit;
    private String after_id;
    private static final String earthPorn = "EarthPorn";
    private static final String subRedditUrl = "http://www.reddit.com/r/";
    private static final String jsonEnd = "/.json";
    private static final String qCount = "?count=";
    private static final String after = "&after=";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        updateList("EarthPorn");
    }

    public void updateList(String subreddit) {
        counter = 0;
        subreddit = subRedditUrl + earthPorn + jsonEnd;
        adapter = new RecyclerAdapter(MainActivity.this, listItemsList);
        mRecyclerView.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(this);

        adapter.clearAdapter();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, subreddit, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONObject data = response.getJSONObject("data");
                    after_id = data.getString("after");
                    JSONArray children = data.getJSONArray("children");

                    for (int i = 0; i < children.length(); i++) {
                        JSONObject post = children.getJSONObject(i).getJSONObject("data");

                        ListItems item = new ListItems();

                        item.setThumbnail(post.getString("thumbnail"));
                        item.setUrl(post.getString("url"));

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

                VolleyLog.d(TAG, "Error" + volleyError.getMessage());
            }
        });

        queue.add(jsonObjectRequest);

    }
}
