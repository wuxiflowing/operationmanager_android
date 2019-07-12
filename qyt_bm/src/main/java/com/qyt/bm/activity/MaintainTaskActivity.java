package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.qyt.bm.R;
import com.qyt.bm.adapter.InstallPagerAdapter;
import com.qyt.bm.adapter.MaintainPagerAdapter;
import com.qyt.bm.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MaintainTaskActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.install_tablayout)
    TabLayout installTablayout;
    @BindView(R.id.install_viewPager)
    ViewPager installViewPager;

    private String[] stateType = new String[]{"prepare", "ing", "finish"};

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_maintaintask);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("定期维护");
        installViewPager.setAdapter(new MaintainPagerAdapter(getSupportFragmentManager()));
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

    @OnClick({R.id.install_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_search:
                Bundle bundle = new Bundle();
                bundle.putString("queryType", "maintain");
                bundle.putString("type", stateType[installViewPager.getCurrentItem()]);
                goToActivity(SearchTaskActivity.class, bundle);
                break;
        }
    }

}
