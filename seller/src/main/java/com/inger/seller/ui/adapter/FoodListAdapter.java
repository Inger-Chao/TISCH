package com.inger.seller.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.bumptech.glide.Glide;
import com.inger.seller.R;
import com.inger.seller.data.Food;
import com.inger.seller.ui.food.FoodUpdateActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private static final String TAG = "FoodListAdapter";

    private Context context = null;
    private ArrayList<Food> foods = null;

    public FoodListAdapter(Context context, ArrayList<Food> foods) {
        this.context = context;
        this.foods = foods;
    }

    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =LayoutInflater.from(context).inflate(R.layout.item_food_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int i) {
        ViewHolder holder =  viewHolder;
        final Food food = foods.get(i);
        Glide.with(context)
                .load(food.getImgUrl())
                .into(holder.foodImage);
        holder.foodPrice.setText("￥" + food.getPrice());
        holder.foodName.setText(food.getName());
        holder.foodDescription.setText(food.getDescription());
        holder.editFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FoodUpdateActivity.class);
                intent.putExtra("food",food);
                context.startActivity(intent);
            }
        });

        holder.deleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: going to create dialog");
                new AlertDialog.Builder(context)
                        .setMessage("你确定要删除该商品吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AVQuery.doCloudQueryInBackground("delete from Food where objectId='" + food.getObjectId()+"'", new CloudQueryCallback<AVCloudQueryResult>() {
                                    @Override
                                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                        if (e == null){
                                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create()
                        .show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return foods.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.food_item_image)
        ImageView foodImage;

        @BindView(R.id.food_item_name)
        TextView foodName;

        @BindView(R.id.food_item_description)
        TextView foodDescription;

        @BindView(R.id.food_item_price)
        TextView foodPrice;

        @BindView(R.id.edit_food_item_info)
        ImageView editFood;
        
        @BindView(R.id.delete_food_item)
        ImageView deleteFood;

        public ViewHolder( View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
