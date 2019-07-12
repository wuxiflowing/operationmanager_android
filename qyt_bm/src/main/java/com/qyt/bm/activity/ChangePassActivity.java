package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.ClearableEditText;
import com.google.gson.Gson;
import com.qyt.bm.R;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.UserInfo;
import com.qyt.bm.request.LoginModel;
import com.qyt.bm.request.UpdatePwModel;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePassActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.changepass_old)
    ClearableEditText changepassOld;
    @BindView(R.id.changepass_new)
    ClearableEditText changepassNew;
    @BindView(R.id.changepass_again)
    ClearableEditText changepassAgain;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_changepass);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("修改密码");
    }

    @OnClick({R.id.changepass_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.changepass_confirm:
                if (checkParams()) {
                    updatePw();
                }
                break;
        }
    }

    private void updatePw() {
        UpdatePwModel updatePwModel = new UpdatePwModel();
        updatePwModel.loginid = sharedPref.getString(Constants.LOGIN_ID);
        updatePwModel.oldPW = changepassOld.getText().toString();
        updatePwModel.newPW = changepassNew.getText().toString();
        String req = new Gson().toJson(updatePwModel);
        postData(HttpConfig.UPDATE_PW, req, new ResponseCallBack<ArrayList<UserInfo>>() {
            @Override
            public void onSuccessResponse(ArrayList<UserInfo> d, String msg) {
                showToast(msg);
                onBackPressed();
            }

            @Override
            public void onFailResponse(String msg) {
                showToast(msg);
            }

            @Override
            public void onVolleyError(int code, String msg) {
                showToast(msg);
            }
        });
    }

    private boolean checkParams() {
        if (TextUtils.isEmpty(changepassOld.getText())) {
            showToast("请输入原密码");
            return false;
        }
        if (TextUtils.isEmpty(changepassNew.getText())) {
            showToast("请输入新密码");
            return false;
        }
        if (TextUtils.isEmpty(changepassAgain.getText())) {
            showToast("");
            return false;
        }
        if (!changepassNew.getText().toString()
                .equals(changepassAgain.getText().toString())) {

            return false;
        }
        return true;
    }
}
