package com.qyt.om.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.model.UserInfo;

public class SplashActivity extends BaseActivity {

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkLogin()) {
                    goToActivity(MainActivity.class);
                } else {
                    goToActivity(LoginActivity.class);
                }
                finish();
            }
        }, Constants.SPLASH_TIME);
    }

    private boolean checkLogin() {
        if (!TextUtils.isEmpty(sharedPref.getString(Constants.LOGIN_ID))
                && sharedPref.getJsonInfo(Constants.USER_INFO, UserInfo.class) != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
