package com.example.daman.testapplication;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by Daman on 2/15/2016.
 */
public class ListViewRowHolder extends RecyclerView.ViewHolder {

    protected NetworkImageView thumbnail;
   // protected RelativeLayout relativeLayout;
    protected LinearLayout linearLayout;
    protected TextView url;
    protected CardView cv;
    protected String backgroundColor;
    protected TextView resolution;

    public ListViewRowHolder(View itemView) {
        super(itemView);

        cv = (CardView) itemView.findViewById(R.id.cv);
        thumbnail = (NetworkImageView) itemView.findViewById(R.id.network_image);
        url = (TextView) itemView.findViewById(R.id.url);
        //relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relLayout);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.linLayout);
        resolution = (TextView) itemView.findViewById(R.id.resolution);

        itemView.setClickable(true);
    }
}
