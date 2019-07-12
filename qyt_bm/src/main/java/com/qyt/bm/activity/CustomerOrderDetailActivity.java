package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bangqu.lib.widget.UnScrollListView;
import com.qyt.bm.R;
import com.qyt.bm.adapter.OrderItemAdapter;
import com.qyt.bm.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class CustomerOrderDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.pact_orderid)
    TextView pactOrderid;
    @BindView(R.id.pact_infolist)
    UnScrollListView pactInfolist;
    @BindView(R.id.pact_detaillist)
    UnScrollListView pactDetaillist;

    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<String> detailItems = new ArrayList<>();
    private OrderItemAdapter detailItemAdapter;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_customerpactdetail);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("订单详情");
        pactOrderid.setText("订单编号：124198");
        orderItemAdapter = new OrderItemAdapter(this, orderItems);
        pactInfolist.setAdapter(orderItemAdapter);
        detailItemAdapter = new OrderItemAdapter(this, detailItems);
        pactDetaillist.setAdapter(detailItemAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();

    }

    @Override
    protected void requestData() {
        super.requestData();
        orderItems.add("合同名称：2018押金合同");
        orderItems.add("合同金额：¥2333");
        orderItems.add("签约时间：2018.7.1");
        orderItemAdapter.notifyDataSetChanged();
        for (int i = 0; i < 8; i++) {
            detailItems.add("合同详情" + i);
        }
        detailItemAdapter.notifyDataSetChanged();
    }

}
