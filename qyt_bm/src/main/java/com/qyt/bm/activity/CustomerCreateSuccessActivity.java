package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangqu.lib.volley.ResponseCallBack;
import com.qyt.bm.R;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.ContactItem;
import com.qyt.bm.response.FarmerTaskData;
import com.qyt.bm.response.InstallTaskData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomerCreateSuccessActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.normal_back)
    Button normalBack;
    @BindView(R.id.sign_opera)
    LinearLayout signOpera;

    private ContactItem contactItem;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_customercreatesuccess);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("创建成功");
        contactItem = getIntent().getParcelableExtra(Constants.INTENT_OBJECT);
//        if ("s".equals(contactItem.pinyin)) {
//            showLoading();
//            signOpera.setVisibility(View.VISIBLE);
//            getFarmerId(contactItem.farmerId);
//        } else {
//            normalBack.setVisibility(View.VISIBLE);
//        }
        normalBack.setVisibility(View.VISIBLE);
    }

    private void getFarmerId(String farmerId) {
        getData(HttpConfig.TASK_INSTALL_DETAIL.replace("{taskid}", farmerId), new ResponseCallBack<FarmerTaskData>() {
            @Override
            public void onSuccessResponse(FarmerTaskData d, String msg) {
                dismissLoading();
                contactItem.farmerId = d.txtFarmerID;
            }

            @Override
            public void onFailResponse(String msg) {
                dismissLoading();
                showToast(msg);
            }

            @Override
            public void onVolleyError(int code, String msg) {
                dismissLoading();
                showToast(msg);
            }
        });
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();

    }

    @OnClick({R.id.back_confirm, R.id.normal_back, R.id.skip_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_confirm:
            case R.id.normal_back:
                onBackPressed();
                break;
            case R.id.skip_sign:
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.INTENT_OBJECT, contactItem);
                goToActivity(CustomerSignCreateActivity.class, bundle);
                onBackPressed();
                break;
        }

    }
}
