package com.inger.tisch.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.inger.tisch.R;
import com.inger.tisch.base.BaseRvAdapter;
import com.inger.tisch.data.Food;
import com.inger.tisch.ui.FoodDetailsActivity;
import com.inger.tisch.ui.viewholder.SearchBannerViewHolder;
import com.inger.tisch.ui.viewholder.SearchListViewHolder;

import java.util.ArrayList;

public class SearchTitleBarAdapter extends BaseRvAdapter {

    private static final String TAG = "SearchTitleBarAdapter";

    private final int TYPE_BANNER = 1;
    private final int TYPE_NORMAL = 2;

    private Context context = null;
    private ArrayList<Food> foodList = null;


    public SearchTitleBarAdapter(Context context, ArrayList<Food> foodList) {
        this.context = context;
        Log.i(TAG, "SearchTitleBarAdapter: size of foodList" + foodList.size());
        this.foodList = foodList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: data list size: " + foodList.size());
        RecyclerView.ViewHolder holder = null;
        if(viewType == TYPE_BANNER){
            View view = LayoutInflater.from(context).inflate(R.layout.search_banner,
                    parent, false);
            holder = new SearchBannerViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_food_list,
                    parent, false);
            holder = new SearchListViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: ");
        super.onBindViewHolder(holder, position);
        if (position != 0){
            SearchListViewHolder viewHolder = (SearchListViewHolder) holder;
            Log.i(TAG, "onBindViewHolder: position is " + holder.getPosition());
            viewHolder.foodPrice.setText("￥" + foodList.get(position-1).getPrice());
            viewHolder.foodDescription.setText(foodList.get(position-1).getDescription());
            viewHolder.foodName.setText(foodList.get(position-1).getName());

            Glide.with(this.context)
                    .load(foodList.get(position-1).getImgUrl())
                    .into(viewHolder.foodImage);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FoodDetailsActivity.class);
                    intent.putExtra("food",foodList.get(position-1));
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return foodList == null ? 0 : foodList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_BANNER;
        } else {
            return TYPE_NORMAL;
        }
    }



}