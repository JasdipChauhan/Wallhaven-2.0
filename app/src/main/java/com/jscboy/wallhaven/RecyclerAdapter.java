package com.jscboy.wallhaven;

import android.animation.ValueAnimator;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.jscboy.wallhaven.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<ListViewRowHolder> {

    private List<ListItems> listItemsList;
    private Context mContext;
    private ImageLoader mImageLoader;
    private int focusedItem = 0;
    private CallbackInterface event;
    private int colour;
    private int r;
    private int g;
    private int b;
    private int lastPosition = -1;
    private Button setWallpaperButton;
    private Button saveButton;

    public RecyclerAdapter(Context context, List<ListItems> listItemsList, CallbackInterface mCallback) {
        event = mCallback;
        mContext = context;
        this.listItemsList = listItemsList;
    }

    @Override
    public ListViewRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, null);
        ListViewRowHolder holder = new ListViewRowHolder(view);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView url = (TextView) v.findViewById(R.id.url);
                final String pictureURL = url.getText().toString();

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Would you like to set this picture as your wallpaper?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                event.changeWallpaper(pictureURL);
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create();
                builder.show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ListViewRowHolder holder, int position) {
        ListItems listItems = listItemsList.get(position);
        holder.itemView.setSelected(focusedItem == position);

        holder.getLayoutPosition();

        mImageLoader = MySingleton.getInstance(mContext).getImageLoader();

        holder.thumbnail.setImageUrl(listItems.getThumbnail(), mImageLoader);
        holder.thumbnail.setDefaultImageResId(R.drawable.placeholder);
        holder.url.setText(Html.fromHtml(listItems.getUrl()));
        holder.cv.setBackgroundColor(Color.rgb(listItems.getR(), listItems.getG(), listItems.getB()));
        holder.resolution.setText(listItems.getResolution());
        holder.resolution.setTextColor(Color.WHITE);

        setAnimation(holder.itemView, position);
    }

    public void clearAdapter () {
        listItemsList.clear();
        notifyDataSetChanged();
    }

    public void add(ListItems li) {
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
