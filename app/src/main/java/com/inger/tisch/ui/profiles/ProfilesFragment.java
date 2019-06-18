package com.inger.tisch.ui.profiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.inger.tisch.R;
import com.inger.tisch.base.BaseFragment;
import com.inger.tisch.utils.config.LCConfig;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfilesFragment extends BaseFragment  {

    private static final String TAG = "ProfilesFragment";

    @BindView(R.id.layout_user_info)
    RelativeLayout userInfoLayout;

    @BindView(R.id.me_image_avatar)
    CircleImageView avatar;

    @BindView(R.id.me_name)
    TextView profileUsername;

    @BindView(R.id.me_description)
    TextView profileDescription;

    @BindView(R.id.layout_user_trade)
    RelativeLayout userTradeLayout;

    @BindView(R.id.layout_user_like)
    RelativeLayout userLikeLayout;

    @Override
    protected void init(View mView, Bundle mSavedInstanceState) {

    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_profiles;
    }

    @OnClick(R.id.layout_user_trade)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.layout_user_trade:
                Intent intent = new Intent(getActivity(),TradeListActivity.class);
                startActivity(intent);
        }
    }

    @Override
    protected void logic() {
        userInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ModifyUserActivity.class));
            }
        });
        String url = (String) AVUser.getCurrentUser().get(LCConfig.getUSER_AVATAR());
        if (url != null) {
            Glide.with(this)
                    .load(url)
                    .into(avatar);
        }
        profileUsername.setText(AVUser.getCurrentUser().getUsername());
        profileDescription.setText(AVUser.getCurrentUser().get("description").toString());
    }

}
