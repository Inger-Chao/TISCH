package com.inger.tisch.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inger.tisch.R;
import com.inger.tisch.base.BaseRvViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchListViewHolder extends BaseRvViewHolder {

    @BindView(R.id.food_item_image)
    public ImageView foodImage;

    @BindView(R.id.food_item_name)
    public TextView foodName;

    @BindView(R.id.food_item_description)
    public TextView foodDescription;

    @BindView(R.id.food_item_price)
    public TextView foodPrice;
    public SearchListViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
