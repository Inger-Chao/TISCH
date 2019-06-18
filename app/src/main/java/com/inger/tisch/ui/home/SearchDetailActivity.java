package com.inger.tisch.ui.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.inger.tisch.R;
import com.inger.tisch.impl.OnLimitClickHelper;
import com.inger.tisch.impl.OnLimitClickListener;

public class SearchDetailActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);

        findViewById(R.id.search_cancel_layout).setOnClickListener(
                new OnLimitClickHelper(new OnLimitClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                })
        );
    }

}
