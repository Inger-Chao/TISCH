package com.inger.seller.ui.trade;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inger.seller.R;
import com.inger.seller.base.BaseFragment;
import com.inger.seller.data.TradeItemInfo;
import com.inger.seller.ui.adapter.TradeListAdapter;
import com.inger.seller.utils.config.CurrentShopInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TradeFragment extends BaseFragment {

    @BindView(R.id.trade_recycler_view)
    RecyclerView tradeRecyclerView;

    private TradeListAdapter adapter = null;
    private ArrayList<TradeItemInfo> infos = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 9){
                initAdapter();
            }
        }
    };

    @Override
    protected void init(View mView, Bundle mSavedInstanceState) {

    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_trade;
    }

    @Override
    protected void logic() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tradeRecyclerView.setLayoutManager(layoutManager);

        initData();
        initAdapter();
    }

    private void initAdapter() {

        adapter = new TradeListAdapter(getContext(),infos);
        tradeRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initData() {

        AVQuery<AVObject> query = new AVQuery<>("Trade");
        query.whereStartsWith("shopId",CurrentShopInfo.id);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                String lastTradeId = "init";
                double price = 0.0d;
                for (AVObject o : avObjects){
                    if (o.get("tradeId").toString().equals(lastTradeId)){
                        price += Double.parseDouble(o.get("price").toString());
                        infos.get(infos.size()-1).setPrice(price);
                    }
                    else {
                        lastTradeId = o.get("tradeId").toString();
                        TradeItemInfo info = new TradeItemInfo();
                        info.setTradeId(lastTradeId);
                        price = Double.parseDouble(o.get("price").toString());
                        info.setPrice(price);
                        info.setCookTime(o.get("cookTime").toString());
                        info.setPayway(o.get("payway").toString());
                        info.setStatus(Integer.parseInt(o.get("status").toString()));
                        info.setAvatar(o.get("shopAvatar").toString());
                        infos.add(info);
                    }
                    handler.sendEmptyMessage(9);
                }
            }
        });
    }
}
