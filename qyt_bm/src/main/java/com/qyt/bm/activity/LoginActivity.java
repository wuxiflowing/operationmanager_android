package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.ClearableEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qyt.bm.R;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.UserInfo;
import com.qyt.bm.request.LoginModel;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.login_mobile)
    ClearableEditText loginMobile;
    @BindView(R.id.login_password)
    ClearableEditText loginPassword;
    @BindView(R.id.login_confirm)
    Button loginConfirm;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        title.setText("登录");
        setTextValue(loginMobile, sharedPref.getString(Constants.LOGIN_ID));
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        loginMobile.addTextChangedListener(textWatcher);
        loginPassword.addTextChangedListener(textWatcher);
    }

    private void refreshButton() {
        if (!TextUtils.isEmpty(loginMobile.getText()) && !TextUtils.isEmpty(loginPassword.getText())) {
            loginConfirm.setEnabled(true);
        } else {
            loginConfirm.setEnabled(false);
        }
    }

    @OnClick({R.id.login_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_confirm:
                login();
                break;
        }
    }

    private void login() {
        showLoading();
        final LoginModel loginModel = new LoginModel();
        loginModel.loginid = loginMobile.getText().toString();
        loginModel.password = loginPassword.getText().toString();
        loginModel.device = sharedPref.getString(Constants.GETUI_CLIENTID);
        LogInfo.error(loginModel.toString());
        postData(HttpConfig.LOGIN, loginModel.toString(), new ResponseCallBack<ArrayList<UserInfo>>() {
            @Override
            public void onSuccessResponse(ArrayList<UserInfo> d, String msg) {
                dismissLoading();
                if (d != null && d.size() == 1) {
                    sharedPref.setString(Constants.LOGIN_ID, loginModel.loginid);
                    sharedPref.setJsonInfo(Constants.USER_INFO, d.get(0));
                    goToActivity(MainActivity.class);
                    onBackPressed();
                } else {
                    showToast("用户信息异常");
                }
            }

            @Override
            public void onFailResponse(String msg) {
                showToast(msg);
                dismissLoading();
            }

            @Override
            public void onVolleyError(int code, String msg) {
                showToast(msg);
                dismissLoading();
            }
        });
    }

}
