package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.qyt.bm.R;
import com.qyt.bm.adapter.CallbackPagerAdapter;
import com.qyt.bm.adapter.InstallPagerAdapter;
import com.qyt.bm.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CallbackTaskActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.install_tablayout)
    TabLayout installTablayout;
    @BindView(R.id.install_viewPager)
    ViewPager installViewPager;

    private String[] stateType = new String[]{"check", "sign", "prepare", "ing", "finish"};

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_installtask);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("设备回收");
        installViewPager.setAdapter(new CallbackPagerAdapter(getSupportFragmentManager()));
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

    @OnClick({R.id.install_create, R.id.install_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_create:
                goToActivity(CallbackTaskCreateActivity.class);
                break;
            case R.id.install_search:
                Bundle bundle = new Bundle();
                if (installViewPager.getCurrentItem() > 1) {
                    bundle.putString("queryType", "subRecycling");
                } else {
                    bundle.putString("queryType", "recycling");
                }
                bundle.putString("type", stateType[installViewPager.getCurrentItem()]);
                goToActivity(SearchTaskActivity.class, bundle);
                break;
        }
    }

}
