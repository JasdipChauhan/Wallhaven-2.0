package com.jscboy.wallhaven.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jscboy.wallhaven.Database.DBManager;
import com.jscboy.wallhaven.Interfaces.CallbackInterface;
import com.jscboy.wallhaven.Models.WallpaperModel;
import com.jscboy.wallhaven.Singletons.MySingleton;
import com.jscboy.wallhaven.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<ListViewRowHolder> {

    private List<WallpaperModel> listItemsList;
    private Context mContext;
    private ImageLoader mImageLoader;
    private int focusedItem = 0;
    private CallbackInterface event;
    private int colour;
    private int r;
    private int g;
    private int b;
    private int lastPosition = -1;
    private DBManager dbManager;

    public RecyclerAdapter(Context context, List<WallpaperModel> listItemsList, CallbackInterface mCallback) {
        event = mCallback;
        mContext = context;
        this.listItemsList = listItemsList;
        dbManager = new DBManager(context, null, null, 1);
    }

    @Override
    public ListViewRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, null);
        ListViewRowHolder holder = new ListViewRowHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ListViewRowHolder holder, int position) {
        final WallpaperModel wallpaperItem = listItemsList.get(position);
        holder.itemView.setSelected(focusedItem == position);

        holder.getLayoutPosition();


        mImageLoader = MySingleton.getInstance(mContext).getImageLoader();

        holder.thumbnail.setImageUrl(wallpaperItem.getThumbnail(), mImageLoader);
        holder.thumbnail.setDefaultImageResId(R.drawable.placeholder);
        holder.url.setText(Html.fromHtml(wallpaperItem.getUrl()));
        holder.cv.setBackgroundColor(Color.rgb(wallpaperItem.getR(), wallpaperItem.getG(), wallpaperItem.getB()));
        holder.resolution.setText(wallpaperItem.getResolution());
        holder.resolution.setTextColor(Color.WHITE);

        setAnimation(holder.itemView, position);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView urlTV = (TextView) v.findViewById(R.id.url);
                TextView resolutionTV = (TextView) v.findViewById(R.id.resolution);
                NetworkImageView thumbnail = (NetworkImageView) v.findViewById(R.id.network_image);

                final String url = urlTV.getText().toString();
                final String resolution = resolutionTV.getText().toString();

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Would you like to set this picture as your wallpaper?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbManager.addWallpaper(wallpaperItem);
                                event.changeWallpaper(url);
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create();
                builder.show();
            }
        });
    }

    public void updateAdapter() {
        List<WallpaperModel> tempWallpaperList = new ArrayList<>();
        for (int i = 0; i < listItemsList.size(); i++) {
            tempWallpaperList.add(i, listItemsList.get(i));
        }
        listItemsList.clear();
        listItemsList.addAll(tempWallpaperList);
        this.notifyDataSetChanged();
    }

    public void clearAdapter () {
        listItemsList.clear();
        notifyDataSetChanged();
    }

    public void add(WallpaperModel li) {
        listItemsList.add(li);
        this.notifyItemInserted(listItemsList.size() - 1);
    }

    @Override
    public int getItemCount() {
        return (null != listItemsList ? listItemsList.size() : 0);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
