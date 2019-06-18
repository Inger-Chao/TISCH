package com.inger.tisch.ui.home;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inger.tisch.R;
import com.inger.tisch.data.Food;
import com.inger.tisch.ui.adapter.SearchTitleBarAdapter;
import com.inger.tisch.utils.Utils;
import com.inger.tisch.widgets.SearchTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchTitleBar.OnSearchTitleBarListener {

    private static final String TAG = "SearchFragment";

    @BindView(R.id.food_recycle_view)
    RecyclerView foodRecycleView;
    @BindView(R.id.search_title_bar)
    SearchTitleBar searchTitleBar;

    private SearchTitleBarAdapter adapter = null;
    private volatile ArrayList<Food> dataList = new ArrayList<>();

    private final int FLUSH_FOOD_ITEM = 4396;

    private int targetHeight = 0;
    private int resetTargetHeight = 0;
    private View firstVisibleView = null;
    private int searchBarState = 0;// 0初始状态  1展开状态

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLUSH_FOOD_ITEM)
                initAdapter();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_title_bar, container, false);
        ButterKnife.bind(this, view);
        targetHeight = (int) (Utils.getResourcesDimension(R.dimen.search_target_height) * 0.45f);
        resetTargetHeight = (int) (Utils.getResourcesDimension(R.dimen.search_target_height) * 0.15f);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        foodRecycleView.setLayoutManager(layoutManager);

        initData();
        initAdapter();
        registerListener();

        return view;
    }

    private void initAdapter() {
        Log.i(TAG, "initAdapter: size of data list :" + dataList.size());
        adapter = new SearchTitleBarAdapter(this.getActivity(), dataList);
        foodRecycleView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void initData(){
        AVQuery<AVObject> query = new AVQuery<>("Food");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null){
                    for (AVObject o:list) {
                        String name = o.getString("name");
                        String price = o.getString("price");
                        String like = o.getString("like");
                        String img = o.getString("imgUrl");
                        String description = o.getString("description");
                        String shopId = o.get("shop").toString();
                        Food food = new Food(name,price,img,like,shopId,description);
                        Log.i(TAG, "done: food search successfully" + food.getShopId());
                        dataList.add(food);
                        handler.sendEmptyMessage(FLUSH_FOOD_ITEM);
                        Log.i(TAG, "done: size of dataList: " + dataList.size());
                    }
                }
                else {
                    e.printStackTrace();
                }
            }
        });
    }


    private void registerListener() {
        searchTitleBar.setOnSearchTitleBarListener(this);
        foodRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                setupTitleBar();
            }
        });
    }

    public void setupTitleBar() {
        if (foodRecycleView == null || searchTitleBar == null) {
            return;
        }

        if (firstVisibleView == null) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) foodRecycleView.getLayoutManager();
            firstVisibleView = layoutManager.getChildAt(0);
        }

        if(firstVisibleView == null){
            return;
        }

        int top = firstVisibleView.getTop();

        if (top > (-resetTargetHeight)
                && searchBarState == 1) {
            searchBarState = 0;
            searchTitleBar.performAnimate(false);
        } else if (top <= (-resetTargetHeight)
                && top >= (-targetHeight)) {
            searchTitleBar.hideDivide();
        } else if (top < (-targetHeight)
                && searchBarState == 0) {
            searchBarState = 1;
            searchTitleBar.performAnimate(true);
        }

        if (Math.abs(top) <= targetHeight) {
            float percent = (Math.abs(top) * 1.0f / targetHeight);
            int alpha = (int) (percent * 255);
            searchTitleBar.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
        } else {
            searchTitleBar.setBackgroundColor(Color.argb(255, 255, 255, 255));
        }
    }

    @Override
    public void onClickSearch() {
        Intent intent = new Intent(getContext(), SearchDetailActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                    Pair.create(searchTitleBar.getSearchLayout()
                            , Utils.getResourcesString(R.string.trans_anim_search_layout)),
                    Pair.create(searchTitleBar.getSearchTv()
                            , Utils.getResourcesString(R.string.trans_anim_search_tv)),
                    Pair.create(searchTitleBar.getSearchIv()
                            , Utils.getResourcesString(R.string.trans_anim_search_iv)),
                    Pair.create(searchTitleBar.getSearchEt()
                            , Utils.getResourcesString(R.string.trans_anim_search_edit))).toBundle());
        } else {
            startActivity(intent);
        }
    }
}
