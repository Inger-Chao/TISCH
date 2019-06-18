package com.inger.tisch.ui.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inger.tisch.R;
import com.inger.tisch.base.BaseActivity;
import com.inger.tisch.data.Food;
import com.inger.tisch.data.Shop;
import com.inger.tisch.data.ShoppingCart;
import com.inger.tisch.data.ShoppingEntity;
import com.inger.tisch.ui.adapter.ShopFoodAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopActivity extends BaseActivity {

    private static final String TAG = "ShopActivity";

    @BindView(R.id.shop_food_recycle_view)
    RecyclerView shopFoodRecyclerView;

    @BindView(R.id.layout_shopping_info)
    LinearLayout mShoppingInfoLayout;

    @BindView(R.id.txt_selected_count)
    TextView mSelectedCountTxt;

    @BindView(R.id.txt_total_price)
    TextView mTotalPriceTxt;

    @BindView(R.id.btn_settle)
    Button mSettleBtn;

    @BindView(R.id.img_cart_logo)
    ImageView shopCartLogo;


    private final Integer FLUSH_FOOD_LIST_UI = 7894;

    private Shop curShop = null;
    private ShopFoodAdapter adapter = null;
    private ArrayList<Food> foods = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLUSH_FOOD_LIST_UI){
                initAdapter();
            }
        }
    };

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        Intent intent = getIntent();
        curShop = (Shop) intent.getSerializableExtra("shop");
        Log.i(TAG, "logicActivity: " + curShop.getName());
        refreshBottomUi();
        findAllFoodsInShop();
        initAdapter();
    }

    private void initAdapter() {
        adapter = new ShopFoodAdapter(this,foods);
        adapter.setAnimTargetView(shopCartLogo);
        adapter.setOnAddNum(new ShopFoodAdapter.OnAddNum() {
            @Override
            public void add(int position) {
                refreshBottomUi();
            }
        });
        shopFoodRecyclerView.setAdapter(adapter);
        shopFoodRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.notifyDataSetChanged();
    }


    private void findAllFoodsInShop() {
        AVQuery<AVObject> query = new AVQuery<>("Food");
        Log.i(TAG, "findAllFoodsInShop: ");
        query.whereStartsWith("shop",curShop.getObjectId());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if (avException == null){
                    for (AVObject object : avObjects){
                        Food food = new Food(object.getObjectId(), object.get("name").toString(),
                                object.get("price").toString(),object.get("imgUrl").toString()
                                ,object.get("like").toString(), curShop.getObjectId(),
                                object.get("description").toString()
                                );
                        foods.add(food);
                        handler.sendEmptyMessage(FLUSH_FOOD_LIST_UI);
                    }
                }
            }
        });
    }

    /**
     * 更新底部面板
     */
    private void refreshBottomUi() {
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        int totalCount = shoppingCart.getTotalQuantity();
        double totalPrice = shoppingCart.getTotalPrice();
        Log.i(TAG, "refreshBottomUi: total count, price :" + totalCount + " " + totalPrice);
        List<ShoppingEntity> list = ShoppingCart.getInstance().getShoppingList();
        for (ShoppingEntity entity : list){
            Log.i(TAG, "refreshBottomUi: shopping cart item: " + entity.getName() + " " + entity.getQuantity());
        }
        shopCartLogo.setSelected(totalCount > 0);
        mSelectedCountTxt.setText(totalCount + "份");
        mTotalPriceTxt.setText("￥" + totalPrice);
        mSettleBtn.setEnabled(totalCount>0);
    }

    @OnClick(R.id.btn_settle)
    public void createTrade(){
        Intent intent = new Intent(this, TradePredetermineActivity.class);
        intent.putExtra("shop",curShop);
        startActivity(intent);
        finish();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop;
    }
}
