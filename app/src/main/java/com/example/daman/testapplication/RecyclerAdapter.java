package com.example.daman.testapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by Daman on 2/15/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<ListViewRowHolder> {

    private List<ListItems> listItemsList;
    private Context mContext;
    private ImageLoader mImageLoader;
    private int focusedItem;

    public RecyclerAdapter (Context context, List<ListItems> listItemsList) {
        mContext = context;
        this.listItemsList = listItemsList;
    }

    @Override
    public ListViewRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from((parent.getContext())).inflate(R.layout.list_row, null);
        ListViewRowHolder holder = new ListViewRowHolder(v);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView redditUrl = (TextView) v.findViewById(R.id.url);
                String postUrl = redditUrl.getText().toString();
                //Intent i = new Intent(mContext, WebActivity.class);
                //i.putExtra("url", postUrl);
                //mContext.startActivity(i);
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
        holder.thumbnail.setDefaultImageResId(R.drawable.reddit_placeholder);
        holder.url.setText(Html.fromHtml(listItems.getUrl()));
    }

    public void classAdapter() {
        listItemsList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != listItemsList ? listItemsList.size() : 0);
    }
}
