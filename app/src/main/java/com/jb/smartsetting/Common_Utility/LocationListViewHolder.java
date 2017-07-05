package com.jb.smartsetting.Common_Utility;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.jb.smartsetting.R;

/**
 * Created by wjdql on 2017-06-17.
 */

public class LocationListViewHolder extends RecyclerView.ViewHolder {

    public TextView locationName;
    public Switch toggleButton;
    public CheckBox checkBox;
    public LinearLayout itemLayout;
    public TextView indentification;

    public LocationListViewHolder(View itemView) {
        super(itemView);
        locationName = (TextView) itemView.findViewById(R.id.location_name);
        toggleButton = (Switch) itemView.findViewById(R.id.toggle);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
        indentification = (TextView) itemView.findViewById(R.id.indentification);
        }
}
