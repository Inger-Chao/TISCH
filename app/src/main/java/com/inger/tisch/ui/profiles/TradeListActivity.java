package com.inger.tisch.ui.profiles;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inger.tisch.R;
import com.inger.tisch.base.BaseActivity;
import com.inger.tisch.data.Shop;
import com.inger.tisch.data.Trade;
import com.inger.tisch.data.TradeItemInfo;
import com.inger.tisch.ui.adapter.TradeListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TradeListActivity extends BaseActivity {

    private static final String TAG = "TradeListActivity";


    private static final Integer FLUSH_TRADE_LIST = 7984;


    @BindView(R.id.profiles_trade_recycler_view)
    RecyclerView tradeRecyclerView;

    ArrayList<TradeItemInfo> tradeItemInfos = new ArrayList<>();
    TradeListAdapter adapter = null;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLUSH_TRADE_LIST){
                initAdapter();
            }
        }
    };

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        initTradesList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tradeRecyclerView.setLayoutManager(layoutManager);
        initAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trade_list;
    }

    private void initAdapter(){
        adapter= new TradeListAdapter(this,tradeItemInfos);

        tradeRecyclerView.setAdapter(adapter);
        adapter.setFlushTradeList(new TradeListAdapter.FlushTradeList() {
            @Override
            public void flush() {
                initTradesList();
                initAdapter();
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void initTradesList(){
        AVQuery<AVObject> tradeAVQuery = new AVQuery<>("Trade");
        tradeAVQuery.whereContains("buyerId",mAVUserFinal.getObjectId());
        tradeAVQuery.orderByDescending("createdAt");
        tradeAVQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                String lastTradeId = "init";
                double price = 0.0d;
                for (int i = 0 ; i < avObjects.size(); i++){
                    if (avObjects.get(i).get("tradeId").toString().equals(lastTradeId)){
                        price +=Double.parseDouble( avObjects.get(i).get("price").toString());
                        tradeItemInfos.get(tradeItemInfos.size()-1).setPrice(price);
                    } else
                    {
                        TradeItemInfo info = new TradeItemInfo();
                        Trade trade = new Trade();
                        lastTradeId = avObjects.get(i).get("tradeId").toString();
                        trade.setObjectId(avObjects.get(i).getObjectId());
                        trade.setTradeId(avObjects.get(i).get("tradeId").toString());
                        trade.setCookTime(avObjects.get(i).get("cookTime").toString());
                        trade.setShopId(avObjects.get(i).get("shopId").toString());
                        trade.setPayWay(avObjects.get(i).get("payway").toString());
                        trade.setCreateAt(avObjects.get(i).getCreatedAt().toString());
                        trade.setStatus(Integer.parseInt(avObjects.get(i).get("status").toString()));
                        info.setTrade(trade);
                        Shop shop = new Shop();
                        shop.setName(avObjects.get(i).get("shopName").toString());
                        shop.setAvatar(avObjects.get(i).get("shopAvatar").toString());
                        info.setShop(shop);
                        price = Double.parseDouble(avObjects.get(i).get("price").toString());
                        info.setPrice(price);
                        tradeItemInfos.add(info);
                    }
                    handler.sendEmptyMessage(FLUSH_TRADE_LIST);
                }
            }
        });
    }

}
