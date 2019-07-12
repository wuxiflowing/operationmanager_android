package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.qyt.bm.R;
import com.qyt.bm.adapter.OrderPagerAdapter;
import com.qyt.bm.adapter.PactPagerAdapter;
import com.qyt.bm.base.BaseActivity;

import butterknife.BindView;

public class CustomerOrderActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.install_tablayout)
    TabLayout installTablayout;
    @BindView(R.id.install_viewPager)
    ViewPager installViewPager;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_customerpact);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("我的订单");
        installTablayout.addTab(installTablayout.newTab().setText("服务订单"));
        installTablayout.addTab(installTablayout.newTab().setText("押金订单"));
        installTablayout.addTab(installTablayout.newTab().setText("预付款订单"));
        installTablayout.addTab(installTablayout.newTab().setText("饲料订单"));
        installViewPager.setAdapter(new OrderPagerAdapter(getSupportFragmentManager()));
        installTablayout.setupWithViewPager(installViewPager);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();

    }

    @Override
    protected void requestData() {
        super.requestData();

    }

}
