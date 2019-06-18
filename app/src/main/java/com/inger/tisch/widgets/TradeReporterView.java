package com.inger.tisch.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.inger.tisch.R;
import com.inger.tisch.controller.ShopController;
import com.inger.tisch.data.ShoppingCart;
import com.inger.tisch.ui.adapter.DiscountInfoListAdapter;
import com.inger.tisch.ui.adapter.ShoppingProductListAdapter;
import com.inger.tisch.utils.FixedLinearLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TradeReporterView extends FrameLayout {

    private static final String TAG = "TradeReporterView";

    @BindView(R.id.txt_shop_name)
    TextView mBusinessNameTxt;

    @BindView(R.id.img_arrow)
    ImageView mArrowImg;

    @BindView(R.id.divider)
    View mShoppingProductDivider;

    @BindView(R.id.recycler_view)
    RecyclerView mShoppingProductRecyclerView;

    @BindView(R.id.divider2)
    View mExtraFeeDivider;

    @BindView(R.id.recycler_view3)
    RecyclerView mDiscountInfoRecyclerView;

    @BindView(R.id.txt_origin_price)
    TextView mOriginPriceTxt;

    @BindView(R.id.txt_discount_price)
    TextView mDiscountPriceTxt;

    @BindView(R.id.txt_total_price)
    TextView mTotalPriceTxt;

    private ShoppingProductListAdapter mShoppingProductAdapter;

    private DiscountInfoListAdapter mDiscountInfoAdapter;


    public TradeReporterView(Context context) {
        super(context);
    }

    public TradeReporterView(  Context context,    AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_trade_report_view, this);
        ButterKnife.bind(view);
        initViews();
    }


    private void initViews() {

        ShopController controller = new ShopController();
        Log.i(TAG, "initViews: set shop text");
        mBusinessNameTxt.setText(ShoppingCart.getInstance().getShop().getName());
        mOriginPriceTxt.setText("￥"+ShoppingCart.getInstance().getTotalPrice());
        mDiscountPriceTxt.setText("￥10");
        mTotalPriceTxt.setText("￥" + (ShoppingCart.getInstance().getTotalPrice()-10));

        mShoppingProductAdapter = new ShoppingProductListAdapter(getContext(),ShoppingCart.getInstance().getShoppingList());
        mShoppingProductRecyclerView.setAdapter(mShoppingProductAdapter);
        mShoppingProductRecyclerView.setLayoutManager(new FixedLinearLayoutManager(getContext()));

        mDiscountInfoAdapter = new DiscountInfoListAdapter(getContext());
        mDiscountInfoRecyclerView.setAdapter(mDiscountInfoAdapter);
        mDiscountInfoRecyclerView.setLayoutManager(new FixedLinearLayoutManager(getContext()));
    }




}
