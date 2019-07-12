package com.qyt.om.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.widget.ConfirmDialog;
import com.bangqu.lib.widget.RoundImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qyt.om.R;
import com.qyt.om.activity.AboutUsActivity;
import com.qyt.om.activity.AppCsmActivity;
import com.qyt.om.activity.ChangePassActivity;
import com.qyt.om.activity.LoginActivity;
import com.qyt.om.activity.UserInfoActivity;
import com.qyt.om.base.BaseFragment;
import com.qyt.om.comm.Constants;
import com.qyt.om.model.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhy on 15/4/26.
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.mine_header)
    RoundImageView mineHeader;
    @BindView(R.id.mine_name)
    TextView mineName;
    @BindView(R.id.mine_mobile)
    TextView mineMobile;
    @BindView(R.id.app_version)
    TextView appVersion;

    private UserInfo userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container,
                    false);
            unbinder = ButterKnife.bind(this, rootView);
            initView();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
            unbinder = ButterKnife.bind(this, rootView);
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initView() {
        title.setText("我的");
        userInfo = sharedPref.getJsonInfo(Constants.USER_INFO, UserInfo.class);
        mineName.setText(userInfo.userName);
        mineMobile.setText(userInfo.phone);
        appVersion.setText(AppUtils.getVersionName(getContext()));
        Glide.with(this).load(userInfo.photoUrl).apply(new RequestOptions().error(R.mipmap.default_header)).into(mineHeader);
    }

    @OnClick({R.id.lay_header, R.id.mine_change_pass, R.id.mine_about, R.id.mine_question, R.id.mine_report, R.id.mine_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lay_header:
                goToActivity(UserInfoActivity.class);
                break;
            case R.id.mine_change_pass:
                goToActivity(ChangePassActivity.class);
                break;
            case R.id.mine_about:
                goToActivity(AboutUsActivity.class);
                break;
            case R.id.mine_question:
                break;
            case R.id.mine_report:
                goToActivity(AppCsmActivity.class);
                break;
            case R.id.mine_exit:
                new ConfirmDialog(getContext(), "确认退出登录？", new DialogConfirmListener() {
                    @Override
                    public void onDialogConfirm(boolean result, Object value) {
                        if (result) {
//                            sharedPref.setString(Constants.LOGIN_ID, "");
                            sharedPref.setJsonInfo(Constants.USER_INFO, null);
                            goToActivity(LoginActivity.class);
                            getActivity().finish();
                        }
                    }
                }).show();
                break;
        }
    }
}
