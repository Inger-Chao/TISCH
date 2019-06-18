package com.inger.tisch.ui.shop;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.avos.avoscloud.utils.StringUtils;
import com.inger.tisch.R;
import com.inger.tisch.base.BaseActivity;
import com.inger.tisch.controller.TradeController;
import com.inger.tisch.data.Food;
import com.inger.tisch.data.Shop;
import com.inger.tisch.data.ShoppingCart;
import com.inger.tisch.data.ShoppingEntity;
import com.inger.tisch.data.Trade;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class TradePredetermineActivity extends BaseActivity {
    private static final String TAG = "TradePredetermineActivi";
    public static final int REQUEST_CODE_REMARK = 1000;

    @BindView(R.id.radio_group_pay_way)
    RadioGroup payWayRadioGroup;

    @BindView(R.id.txt_booked_time)
    TextView bookedTime;

    @BindView(R.id.txt_remark)
    TextView remarkTxt;

    private String payWay = null;
    private Shop curShop = null;

    private Calendar calendar;
    ArrayList<Trade> tradeArrayList = new ArrayList<>();


    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        Intent intent = getIntent();
        curShop = (Shop) intent.getSerializableExtra("shop");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trade_predetermine;
    }

    @OnClick(R.id.layout_send_time)
    public void setPredetermineTime(){
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        final int  mYear = calendar.get(Calendar.YEAR);
        final int  mMonth = calendar.get(Calendar.MONTH);
        final int  mDay = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Log.i(TAG, "onTimeSet: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                bookedTime.setText(mYear + "-" + mMonth + "-" + mDay+
                        " " + " "+ calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
            }
        },hour,minute,true
        ).show();
    }

    @OnClick(R.id.layout_remark)
    public void addRemark() {

        LinearLayout writeRemark = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.dialog_remark, null);

        final EditText remarkEdt = writeRemark.findViewById(R.id.dialog_edt_remark);
        new AlertDialog.Builder(this)
                .setTitle("填写店铺信息")
                .setView(writeRemark)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remarkTxt.setText(remarkEdt.getText());
                        Toast.makeText(TradePredetermineActivity.this, "备注填写完成", Toast.LENGTH_SHORT).show();
                    }
                })
                //由于“取消”的button我们没有设置点击效果，直接设为null就可以了
                .setNegativeButton("取消", null)
                .create()
                .show();
    }


    @OnClick(R.id.btn_submit_order)
    public void submitTrade(){
        RadioButton radioButton = findViewById(payWayRadioGroup.getCheckedRadioButtonId());
        payWay = radioButton.getText().toString();
        initTrade();
        new TradeController().addTrades(tradeArrayList);
        Toast.makeText(this,"正在模拟支付",Toast.LENGTH_LONG).show();
        Toast.makeText(this,"支付成功",Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initTrade(){
        String id = StringUtils.getRandomString(10);
        for (int i = 0; i < ShoppingCart.getInstance().getShoppingItemCount(); i++)
        {
            ShoppingEntity entity = ShoppingCart.getInstance().getShoppingList().get(i);
            Food food = entity.getFood();
            tradeArrayList.add(new Trade("Trade",id,curShop.getObjectId(),mAVUserFinal.getObjectId(),
                    food.getObjectId(),1,entity.getQuantity(),entity.getTotalPrice(),bookedTime.getText().toString(),
                    payWay, remarkTxt.getText().toString()));
        }
    }

}
