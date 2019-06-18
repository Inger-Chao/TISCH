package com.inger.seller.ui.food;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inger.seller.R;
import com.inger.seller.base.BaseFragment;
import com.inger.seller.data.Food;
import com.inger.seller.ui.adapter.FoodListAdapter;
import com.inger.seller.utils.config.CurrentShopInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FoodFragment extends BaseFragment {

    private static final String TAG = "FoodFragment";
    private static final Integer FLUSH_LIST = 9874;
    private static final int REQUEST_CODE_CHOOSE = 23;


    @BindView(R.id.food_recycler_view)
    RecyclerView foodRecyclerView;

    @BindView(R.id.add_food_item_btn)
    FloatingActionButton addFoodButton;
    private FoodListAdapter adapter = null;
    private ArrayList<Food> foods = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLUSH_LIST){
                initAdapter();
            }
        }
    };

    @Override
    protected void init(View mView, Bundle mSavedInstanceState) {

    }

    @OnClick(R.id.add_food_item_btn)
    public void onClick(){

        LinearLayout writeFoodInfo = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.dialog_update_food_info, null);

        final EditText foodName = writeFoodInfo.findViewById(R.id.add_food_name);
        final EditText foodDescription = writeFoodInfo.findViewById(R.id.add_food_description);
        final EditText foodPrice = writeFoodInfo.findViewById(R.id.add_food_price);

        new AlertDialog.Builder(getContext())
                .setTitle("添加菜品")
                .setView(writeFoodInfo)
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Food food = new Food();
                        food.setName(foodName.getText().toString());
                        food.setDescription(foodDescription.getText().toString());
                        food.setPrice(foodPrice.getText().toString());
                        food.setShopId(CurrentShopInfo.id);
                        AVObject avFood = new AVObject("Food");
                        avFood.put("name",food.getName());
                        avFood.put("description",food.getDescription());
                        avFood.put("price",food.getPrice());
                        avFood.put("shop",food.getShopId());
                        avFood.saveInBackground();
                        Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消",null)
                .create()
                .show();
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_food;
    }

    @Override
    protected void logic() {
        Log.i(TAG, "logic: `");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        foodRecyclerView.setLayoutManager(layoutManager);

        initDatas();
        initAdapter();
    }

    private void initAdapter() {
        adapter = new FoodListAdapter(getContext(),foods);
        foodRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initDatas() {
        AVQuery<AVObject> query = new AVQuery<>("Food");
        query.whereContains("shop",CurrentShopInfo.id);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                for (AVObject o: avObjects) {
                    Food food = new Food(o.getObjectId(),o.get("name").toString(),
                            o.get("price").toString(),o.get("imgUrl").toString(),
                            o.get("like").toString(),o.get("shop").toString(),
                            o.get("description").toString());
                    foods.add(food);
                }
                handler.sendEmptyMessage(FLUSH_LIST);
            }
        });
    }
}
