package com.example.daman.testapplication;

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
    protected String backgroundColor;

    public ListViewRowHolder(View itemView) {
        super(itemView);

        thumbnail = (NetworkImageView) itemView.findViewById(R.id.network_image);
       // relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relLayout);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.linLayout);
        url = (TextView) itemView.findViewById(R.id.url);
        itemView.setClickable(true);
    }
}
