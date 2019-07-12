package com.qyt.om.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bangqu.lib.widget.UnScrollListView;
import com.qyt.om.R;
import com.qyt.om.adapter.OrderItemAdapter;
import com.qyt.om.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageAlarmDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.install_customer_name)
    TextView installCustomerName;
    @BindView(R.id.install_customer_adress)
    TextView installCustomerAdress;
    @BindView(R.id.install_order_info)
    UnScrollListView installOrderInfo;

    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_messagealarmdetail);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("告警消息");
        orderItemAdapter = new OrderItemAdapter(this, orderItems);
        installOrderInfo.setAdapter(orderItemAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();

    }

    @Override
    protected void requestData() {
        super.requestData();
        orderItems.add("安装工单：3个");
        orderItems.add("安装位置：无锡市滨湖区");
        orderItems.add("运维人员：小小");
        orderItems.add("代收服务费：2300元");
        orderItems.add("代收押金费：2300元");
        orderItemAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.install_customer_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_customer_call:
                break;
        }
    }

}
