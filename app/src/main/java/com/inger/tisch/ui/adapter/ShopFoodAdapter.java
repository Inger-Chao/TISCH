package com.inger.tisch.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inger.tisch.R;
import com.inger.tisch.base.BaseRvViewHolder;
import com.inger.tisch.data.Food;
import com.inger.tisch.data.ShoppingCart;
import com.inger.tisch.widgets.ShoppingCountView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopFoodAdapter extends RecyclerView.Adapter<ShopFoodAdapter.ViewHolder> {

    private static final String TAG = "ShopFoodAdapter";

    private Context context = null;

    private ArrayList<Food> foodList = null;

    private View mAnimTargetView;
    private OnAddNum onAddNum;
    public interface OnAddNum{
        void add(int position);
    }

    public void setOnAddNum(OnAddNum onAddNum) {
        this.onAddNum = onAddNum;
    }

    public void setAnimTargetView(View animTargetView) {
        mAnimTargetView = animTargetView;
    }

    public ShopFoodAdapter(Context context, ArrayList<Food> foodList) {
        this.context = context;
        Log.i(TAG, "ShopFoodAdapter: food list size: " + foodList.size());
        this.foodList = foodList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop_food,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopFoodAdapter.ViewHolder viewHolder, int i) {
        ViewHolder holder =  viewHolder;
        Food food = foodList.get(i);
        Glide.with(context)
                .load(food.getImgUrl())
                .into(holder.foodImage);
        holder.foodPrice.setText("￥" + food.getPrice());
        holder.foodName.setText(food.getName());
        holder.foodDescription.setText(food.getDescription());

        int quantity = ShoppingCart.getInstance().getQuantityForProduct(food);
        holder.shoppingCountView.setShoppingCount(quantity);
        holder.shoppingCountView.setAnimTargetView(mAnimTargetView);
        holder.shoppingCountView.setOnShoppingClickListener(new ShoppingCountView.ShoppingClickListener() {
            @Override
            public void onAddClick(int num) {
                Log.i(TAG, "onAddClick: ");
                ShoppingCart.getInstance().push(food);

                onAddNum.add(holder.getLayoutPosition());
            }

            @Override
            public void onMinusClick(int num) {
                Log.i(TAG, "onMinusClick: ");
                ShoppingCart.getInstance().pop(food);
                    onAddNum.add(holder.getLayoutPosition());


            }
        });
        holder.shoppingCountView.setVisibility(View.VISIBLE);

    }

    private void showClearDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_shopping_cart_business_conflict_title);
        builder.setMessage(R.string.dialog_shopping_cart_business_conflict_message);
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.setPositiveButton(R.string.dialog_clear, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShoppingCart.getInstance().clearAll();
            }
        });
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class ViewHolder extends BaseRvViewHolder {

        @BindView(R.id.item_image_shop_food)
        ImageView foodImage;

        @BindView(R.id.txt_product_name)
        TextView foodName;

        @BindView(R.id.txt_product_description)
        TextView foodDescription;

        @BindView(R.id.txt_product_price)
        TextView foodPrice;

        @BindView(R.id.shopping_count_view)
        ShoppingCountView shoppingCountView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
