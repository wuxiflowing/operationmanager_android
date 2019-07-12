package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.qyt.bm.R;
import com.qyt.bm.adapter.InstallPagerAdapter;
import com.qyt.bm.adapter.PactPagerAdapter;
import com.qyt.bm.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CustomerPactActivity extends BaseActivity {

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
        title.setText("合同管理");
        installTablayout.addTab(installTablayout.newTab().setText("服务合同"));
        installTablayout.addTab(installTablayout.newTab().setText("押金合同"));
        installTablayout.addTab(installTablayout.newTab().setText("预付款"));
        installViewPager.setAdapter(new PactPagerAdapter(getSupportFragmentManager()));
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
