package com.inger.tisch.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.inger.tisch.R;
import com.inger.tisch.data.TradeItemInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TradeListAdapter extends RecyclerView.Adapter<TradeListAdapter.ViewHolder> {
    private static final String TAG = "TradeListAdapter";
    private Context context = null;
    private ArrayList<TradeItemInfo> tradeItemInfos = null;

    private FlushTradeList flushTradeList;
    public interface FlushTradeList{
        void flush();
    }

    public FlushTradeList getFlushTradeList() {
        return flushTradeList;
    }

    public void setFlushTradeList(FlushTradeList flushTradeList) {
        this.flushTradeList = flushTradeList;
    }

    public TradeListAdapter(Context context, ArrayList<TradeItemInfo> tradeItemInfos) {
        Log.i(TAG, "TradeListAdapter: " + tradeItemInfos.size());
        this.context = context;
        this.tradeItemInfos = tradeItemInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_trade,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TradeItemInfo info = tradeItemInfos.get(i);
        viewHolder.shopName.setText(info.getShop().getName());
        Glide.with(context).load(info.getShop().getAvatar()).into(viewHolder.shopImage);
        viewHolder.tradeCookedTime.setText(info.getTrade().getCookTime());
        double price = 0.0d;
        price = info.getPrice();
        viewHolder.tradeTotalPrice.setText("￥" + price);
        viewHolder.tradePayway.setText(info.getTrade().getPayWay());
        switch (info.getTrade().getStatus()){
            case 1:
                viewHolder.tradeStatus.setText("已付款");
                viewHolder.informShop.setVisibility(View.VISIBLE);
                break;
            case 2:
                viewHolder.tradeStatus.setText("已通知店家");
                viewHolder.informShop.setVisibility(View.GONE);
                break;
            case 3:
                viewHolder.tradeStatus.setText("制作中");
                viewHolder.confirmReceived.setVisibility(View.VISIBLE);
                break;
            case 4:
                viewHolder.tradeStatus.setText("已完成");
                viewHolder.confirmReceived.setVisibility(View.GONE);
                viewHolder.againTrade.setVisibility(View.VISIBLE);
                break;
        }

        viewHolder.informShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVQuery<AVObject> tradeAVQuery = new AVQuery<>("Trade");
                tradeAVQuery.whereStartsWith("tradeId",info.getTrade().getTradeId());
                tradeAVQuery.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> avObjects, AVException avException) {
                        for(AVObject o : avObjects){
                            o.put("status","2");
                            o.saveInBackground();
                        }
                        flushTradeList.flush();
                    }
                });
            }
        });
        viewHolder.confirmReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVQuery<AVObject> tradeAVQuery = new AVQuery<>("Trade");
                tradeAVQuery.whereStartsWith("tradeId",info.getTrade().getTradeId());
                tradeAVQuery.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> avObjects, AVException avException) {
                        for(AVObject o : avObjects){
                            o.put("status","4");
                            o.saveInBackground();
                        }
                        Toast.makeText(context, "完成后钱会进入卖家账户", Toast.LENGTH_SHORT).show();
                        flushTradeList.flush();

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return tradeItemInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_business_name)
        TextView shopName;

        @BindView(R.id.txt_order_status)
        TextView tradeStatus;

        @BindView(R.id.trade_image)
        ImageView shopImage;

        @BindView(R.id.txt_total_price)
        TextView tradeTotalPrice;

        @BindView(R.id.txt_created_at)
        TextView tradeCookedTime;

        @BindView(R.id.txt_trade_payway)
        TextView tradePayway;

        @BindView(R.id.btn_inform_shop)
        TextView informShop;

        @BindView(R.id.btn_order_again)
        TextView againTrade;

        @BindView(R.id.btn_confirm_received)
        TextView confirmReceived;


        public ViewHolder( View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
