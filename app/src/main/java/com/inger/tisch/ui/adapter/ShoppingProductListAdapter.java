package com.inger.tisch.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inger.tisch.R;
import com.inger.tisch.data.ShoppingEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingProductListAdapter extends RecyclerView.Adapter<ShoppingProductListAdapter.ViewHolder> {


    private Context context = null;
    private List<ShoppingEntity> entities = null;

    public ShoppingProductListAdapter(Context context, List<ShoppingEntity> entities) {
        this.context = context;
        this.entities = entities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shopping_list_product,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShoppingProductListAdapter.ViewHolder viewHolder, int i) {

        viewHolder.nameTxt.setText(entities.get(i).getName());
        viewHolder.quantityTxt.setText("x " + entities.get(i).getQuantity());
        viewHolder.priceTxt.setText("￥" + entities.get(i).getTotalPrice());
    }



    @Override
    public int getItemCount() {
        return entities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_name)
        TextView nameTxt;

        @BindView(R.id.txt_quantity)
        TextView quantityTxt;

        @BindView(R.id.txt_price)
        TextView priceTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
