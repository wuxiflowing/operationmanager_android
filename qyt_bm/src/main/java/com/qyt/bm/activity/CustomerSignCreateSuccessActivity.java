package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.qyt.bm.R;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.model.ContactItem;

import butterknife.BindView;
import butterknife.OnClick;

public class CustomerSignCreateSuccessActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;

    private ContactItem contactItem;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_customersigncreatesuccess);
        contactItem = getIntent().getParcelableExtra(Constants.INTENT_OBJECT);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("创建成功");
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();

    }

    @OnClick({R.id.buy_feed, R.id.install_device})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buy_feed:
                break;
            case R.id.install_device:
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.INTENT_OBJECT, contactItem);
                goToActivity(InstallTaskCreateActivity.class, bundle);
                onBackPressed();
                break;
        }
    }

}
