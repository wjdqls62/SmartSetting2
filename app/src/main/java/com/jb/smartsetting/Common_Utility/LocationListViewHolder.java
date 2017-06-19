package com.jb.smartsetting.Common_Utility;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jb.smartsetting.R;

/**
 * Created by wjdql on 2017-06-17.
 */

public class LocationListViewHolder extends RecyclerView.ViewHolder {

    public TextView tvLocationName;
    public ImageButton toggle;
    public CheckBox checkBox;

    public LocationListViewHolder(View itemView) {
        super(itemView);
        tvLocationName = (TextView) itemView.findViewById(R.id.location_name);
        toggle = (ImageButton) itemView.findViewById(R.id.toggle);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }



}
