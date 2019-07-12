package com.qyt.bm.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bangqu.lib.volley.ResponseCallBack;
import com.google.gson.JsonElement;
import com.qyt.bm.R;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.request.ApproveTask;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class CallbackApproveActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.callback_approve_agree)
    RadioButton agree;
    @BindView(R.id.callback_approve_refuse)
    RadioButton refuse;
    @BindView(R.id.callback_approve_desc)
    EditText reason;
    @BindView(R.id.callback_reason_list)
    GridLayout callbackReasonList;

    private String takID;
    private ArrayList<CheckBox> contextChecks = new ArrayList<>();

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_callbackapprove);
        takID = getIntent().getStringExtra(Constants.INTENT_OBJECT);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("设备回收");
    }

    @Override
    protected void requestData() {
        super.requestData();
        getData(HttpConfig.GET_RECYCLING_DATA, new ResponseCallBack<ArrayList<String>>() {
            @Override
            public void onSuccessResponse(ArrayList<String> d, String msg) {
                addIssueItems(d);
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

    private void addIssueItems(ArrayList<String> d) {
        for (String issue : d) {
            CheckBox check = new CheckBox(this);
            check.setText(issue);
            check.setButtonDrawable(R.drawable.rc_checkbox);
            check.setPadding(20, 20, 20, 20);
            check.setTextColor(Color.BLACK);
            callbackReasonList.addView(check);
            contextChecks.add(check);
        }
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
    }

    @OnClick({R.id.callback_approve_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.callback_approve_confirm:
                ApproveTask approveTask = new ApproveTask();
                approveTask.loginid = sharedPref.getString(Constants.LOGIN_ID);
                approveTask.appData.isAgree = agree.isChecked() ? "1" : "0";
                approveTask.appData.remark = reason.getText().toString();
                if (refuse.isChecked() && TextUtils.isEmpty(reason.getText())) {
                    showToast("请输入撤销原因");
                    return;
                }
                String issueTxt = "";
                for (CheckBox issue : contextChecks) {
                    if (issue.isChecked()) {
                        issueTxt = issueTxt + "," + issue.getText().toString();
                    }
                }
                if (issueTxt.length() > 0)
                    issueTxt = issueTxt.substring(1);
                approveTask.appData.resMulti = issueTxt;
                if (agree.isChecked() && TextUtils.isEmpty(issueTxt)) {
                    showToast("请选择回收原因");
                    return;
                }
                showLoading();
                LogInfo.error(approveTask.toString());
                postData(HttpConfig.CONFIRM_TASK_FINISH.replace("{taskid}", takID), approveTask.toString(), new ResponseCallBack<JsonElement>() {
                    @Override
                    public void onSuccessResponse(JsonElement d, String msg) {
                        showToast(msg);
                        dismissLoading();
                        setResult(RESULT_OK);
                        onBackPressed();
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
                break;
        }
    }
}
