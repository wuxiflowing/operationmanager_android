package com.qyt.bm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.qyt.bm.BmApplication;
import com.qyt.bm.R;
import com.qyt.bm.adapter.CallbackFishpondAdapter;
import com.qyt.bm.adapter.PhotosAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.ContactItem;
import com.qyt.bm.model.PondChoiceItem;
import com.qyt.bm.model.PondDeviceChoice;
import com.qyt.bm.request.RecyclePond;
import com.qyt.bm.request.RecycleTask;
import com.qyt.bm.response.FishPondInfo;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CallbackTaskCreateActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.install_customer_name)
    TextViewPlus installCustomerName;
    @BindView(R.id.install_customer_address)
    TextView installCustomerAddress;
    @BindView(R.id.install_customer_mobile)
    TextView installCustomerMobile;
    @BindView(R.id.callback_fishpond_count)
    TextView callbackFishpondCount;
    @BindView(R.id.install_pact_pics)
    RecyclerView installPactPics;
    @BindView(R.id.install_device_list)
    UnScrollListView installDeviceList;
    @BindView(R.id.recycle_device_count)
    TextView recycleDeviceCount;
    @BindView(R.id.callback_reason_list)
    GridLayout callbackReasonList;
    @BindView(R.id.back_bank_name)
    EditText backBankName;
    @BindView(R.id.back_bank_person)
    EditText backBankPerson;
    @BindView(R.id.back_bank_account)
    EditText backBankAccount;
    @BindView(R.id.callback_reason)
    EditText callbackReason;

    private PhotosAdapter photosAdapter;
    private ArrayList<String> photos = new ArrayList<>();
    private CallbackFishpondAdapter fishpondAdapter;
    private ArrayList<PondChoiceItem> fishPondItems = new ArrayList<>();
    private final int REQUEST_CUSTOMER = 100;
    private final int REQUEST_OPERATOR = 101;
    private final int REQUEST_DEVICES = 102;
    private RecycleTask recycleTask = new RecycleTask();
    private ContactItem contactItem;
    private String loginid;
    private ArrayList<CheckBox> contextChecks = new ArrayList<>();

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_callbacktaskcreate);
        contactItem = getIntent().getParcelableExtra(Constants.INTENT_OBJECT);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("设备回收");
        loginid = sharedPref.getString(Constants.LOGIN_ID);
        recycleTask.loginid = loginid;
        recycleTask.appData.txtHKID = loginid;
        photosAdapter = new PhotosAdapter(this, photos);
        installPactPics.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        installPactPics.setAdapter(photosAdapter);
        fishpondAdapter = new CallbackFishpondAdapter(this, fishPondItems);
        installDeviceList.setAdapter(fishpondAdapter);
        if (contactItem != null) {
            installCustomerName.setText(contactItem.name);
            installCustomerMobile.setText(contactItem.mobile);
            installCustomerAddress.setText(contactItem.farmerAdd);
            recycleTask.farmerId = contactItem.farmerId;
            recycleTask.appData.txtFarmerID = contactItem.farmerId;
            getFishPonds(contactItem.farmerId);
        }
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
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

    @OnClick({R.id.install_choice_customer, R.id.callback_add_fishpond, R.id.submit_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_choice_customer:
                goToActivityForResult(ChoiceCustomerActivity.class, REQUEST_CUSTOMER);
                break;
            case R.id.install_choice_operator:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_OBJECT, "回收设备数量：");
                goToActivityForResult(ChoiceOperatorActivity.class, bundle, REQUEST_OPERATOR);
                break;
            case R.id.callback_add_fishpond:
                if (TextUtils.isEmpty(recycleTask.farmerId)) {
                    showToast("请先选择养殖户");
                    return;
                }
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constants.INTENT_OBJECT, recycleTask.farmerId);
                goToActivityForResult(ChoiceDeviceActivity.class, bundle1, REQUEST_DEVICES);
                break;
            case R.id.submit_confirm:
                if (recycleTask.appData.fishPondList.size() > 0) {
                    if (TextUtils.isEmpty(backBankName.getText()) || TextUtils.isEmpty(backBankAccount.getText())
                            || TextUtils.isEmpty(backBankPerson.getText())) {
                        showToast("退款账号信息不能为空");
                        return;
                    }
                    recycleTask.appData.bankName = backBankName.getText().toString();
                    recycleTask.appData.bankPerson = backBankPerson.getText().toString();
                    recycleTask.appData.bankAccount = backBankAccount.getText().toString();
                    showLoading();
                    String issueTxt = "";
                    for (CheckBox issue : contextChecks) {
                        if (issue.isChecked()) {
                            issueTxt = issueTxt + "," + issue.getText().toString();
                        }
                    }
                    if (issueTxt.length() > 0)
                        issueTxt = issueTxt.substring(1);
                    recycleTask.appData.txtResMulti = issueTxt;
                    recycleTask.appData.tarReson = callbackReason.getText().toString();
                    createCustomer();
                } else {
                    showToast("请先选择回收设备");
                }
                break;
        }
    }

    private void createCustomer() {
        LogInfo.error(recycleTask.toString());
        postData(HttpConfig.CREATE_TASK_CUSTOMER.replace("{PROID}", Constants.RECYCLE_CREATE), recycleTask.toString(), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
                LogInfo.error(d);
                dismissLoading();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CUSTOMER:
                    ContactItem item = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    installCustomerName.setText(item.name);
                    installCustomerMobile.setText(item.mobile);
                    installCustomerAddress.setText(item.farmerAdd);
                    recycleTask.farmerId = item.farmerId;
                    recycleTask.appData.txtFarmerID = item.farmerId;
                    getFishPonds(item.farmerId);
                    fishPondItems.clear();
                    fishpondAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_DEVICES:
                    ArrayList<PondChoiceItem> operatorItems = data.getParcelableArrayListExtra(Constants.INTENT_OBJECT);
                    fishPondItems.clear();
                    fishPondItems.addAll(operatorItems);
                    fishpondAdapter.notifyDataSetChanged();
                    dealRecyclePond(operatorItems);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void dealRecyclePond(ArrayList<PondChoiceItem> operatorItems) {
        int count = 0;
        ArrayList<RecyclePond> recyclePonds = new ArrayList<>();
        for (PondChoiceItem choice : operatorItems) {
            RecyclePond recyclePond = new RecyclePond();
            recyclePond.pondId = choice.pondId;
            recyclePond.maintainKeeperID = choice.maintainKeeperID;
            for (PondDeviceChoice device : choice.deviceList) {
                RecyclePond.RecycleDevice recycleDevice = new RecyclePond.RecycleDevice();
                recycleDevice.deviceId = device.deviceId;
                recycleDevice.identifier = device.identifier;
                recyclePond.deviceList.add(recycleDevice);
                count++;
            }
            recyclePonds.add(recyclePond);
        }
        recycleTask.appData.fishPondList.clear();
        recycleTask.appData.fishPondList.addAll(recyclePonds);
        recycleDeviceCount.setText(count + "套");
    }

    private void getFishPonds(String farmerId) {
        /*获取鱼塘列表*/
        getData(HttpConfig.GET_CUSTOMER_PONDS.replace("{customerId}", farmerId), new ResponseCallBack<ArrayList<FishPondInfo>>() {
            @Override
            public void onSuccessResponse(ArrayList<FishPondInfo> d, String msg) {
                if (d != null && d.size() > 0) {
                    callbackFishpondCount.setText(d.size() + "个");
                } else {
                    callbackFishpondCount.setText("0个");
                }
            }

            @Override
            public void onFailResponse(String msg) {

            }

            @Override
            public void onVolleyError(int code, String msg) {

            }
        });
    }

}
