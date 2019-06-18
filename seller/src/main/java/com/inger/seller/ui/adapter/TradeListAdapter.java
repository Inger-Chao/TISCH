package com.inger.seller.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.inger.seller.R;
import com.inger.seller.data.TradeItemInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TradeListAdapter extends RecyclerView.Adapter<TradeListAdapter.ViewHolder> {

    private Context context = null;
    private ArrayList<TradeItemInfo> tradeItemInfos = new ArrayList<>();

    public TradeListAdapter(Context context, ArrayList<TradeItemInfo> tradeItemInfos) {
        this.context = context;
        this.tradeItemInfos = tradeItemInfos;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trade,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int i) {
        final TradeItemInfo info = tradeItemInfos.get(i);
        Glide.with(context)
                .load(info.getAvatar()).into(viewHolder.foodImage);
        viewHolder.tradeId.setText(info.getTradeId());
        viewHolder.tradeTotalPrice.setText("￥" + info.getPrice());
        viewHolder.tradeCookedTime.setText(info.getCookTime());
        viewHolder.tradePayway.setText(info.getPayway());
        switch (info.getStatus()){
            case 1:
                viewHolder.tradeStatus.setText("已付款");
                break;
            case 2:
                viewHolder.tradeStatus.setText("请求制作");
                viewHolder.startMakeFood.setVisibility(View.VISIBLE);
                break;
            case 3:
                viewHolder.tradeStatus.setText("制作中");
                viewHolder.startMakeFood.setVisibility(View.GONE);
                break;
            case 4:
                viewHolder.tradeStatus.setText("已完成");
                break;
        }
        viewHolder.startMakeFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVQuery<AVObject> query = new AVQuery<>("Trade");
                query.whereStartsWith("tradeId",info.getTradeId());
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> avObjects, AVException avException) {
                        for (AVObject o: avObjects){
                            o.put("status", "3");
                            o.saveInBackground();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return tradeItemInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.txt_trade_id)
        TextView tradeId;

        @BindView(R.id.txt_order_status)
        TextView tradeStatus;

        @BindView(R.id.trade_food_image)
        ImageView foodImage;

        @BindView(R.id.txt_total_price)
        TextView tradeTotalPrice;

        @BindView(R.id.txt_cook_time)
        TextView tradeCookedTime;

        @BindView(R.id.txt_trade_payway)
        TextView tradePayway;

        @BindView(R.id.btn_start_make_food)
        TextView startMakeFood;


        public ViewHolder( View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
