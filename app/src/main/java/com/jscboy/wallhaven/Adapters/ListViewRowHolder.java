package com.jscboy.wallhaven.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.jscboy.wallhaven.R;

public class ListViewRowHolder extends RecyclerView.ViewHolder {

    protected NetworkImageView thumbnail;
    protected LinearLayout linearLayout;
    protected TextView url;
    protected CardView cv;
    protected TextView resolution;

    public ListViewRowHolder(View itemView) {
        super(itemView);

        cv = (CardView) itemView.findViewById(R.id.cv);
        thumbnail = (NetworkImageView) itemView.findViewById(R.id.network_image);
        url = (TextView) itemView.findViewById(R.id.url);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.linLayout);
        resolution = (TextView) itemView.findViewById(R.id.resolution);

        itemView.setClickable(true);
    }
}
