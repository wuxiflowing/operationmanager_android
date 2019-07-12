package com.qyt.om.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.ConfirmDialog;
import com.bangqu.lib.widget.LoadingView;
import com.bangqu.lib.widget.RoundImageView;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.qyt.om.R;
import com.qyt.om.adapter.DeviceListAdapter;
import com.qyt.om.adapter.OrderItemAdapter;
import com.qyt.om.adapter.PhotosAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.DeviceChoiceModel;
import com.qyt.om.response.EquipmentItem;
import com.qyt.om.response.InstallTaskData;
import com.qyt.om.response.InstallTaskItem;
import com.qyt.om.response.MessageItem;
import com.qyt.om.utils.BaiduLocManager;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.utils.MapNaviUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class InstallTaskDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.install_customer_name)
    TextView installCustomerName;
    @BindView(R.id.install_customer_adress)
    TextView installCustomerAdress;
    @BindView(R.id.install_task_state)
    TextView installTaskState;
    @BindView(R.id.install_order_info)
    UnScrollListView installOrderInfo;
    @BindView(R.id.install_order_project)
    TextView installOrderProject;
    @BindView(R.id.install_device_list)
    UnScrollListView installDeviceList;
    @BindView(R.id.install_customer_header)
    RoundImageView installCustomerHeader;
    @BindView(R.id.install_pond_address)
    TextViewPlus installPondAddress;

    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<DeviceChoiceModel> deviceItems = new ArrayList<>();
    private DeviceListAdapter deviceListAdapter;
    private InstallTaskData taskData;
    private String takID, status, mobile;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_installtaskdetail);
        takID = getIntent().getStringExtra(Constants.INTENT_OBJECT);
        status = getIntent().getStringExtra(Constants.INTENT_FLAG);
        LogInfo.error(takID);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("任务详情");
        orderItemAdapter = new OrderItemAdapter(this, orderItems);
        installOrderInfo.setAdapter(orderItemAdapter);
        deviceListAdapter = new DeviceListAdapter(this, deviceItems);
        installDeviceList.setAdapter(deviceListAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
    }

    @Override
    protected void requestData() {
        super.requestData();
        showLoading();
        getData(HttpConfig.TASK_DETAIL.replace("{taskid}", takID), new ResponseCallBack<InstallTaskData>() {
            @Override
            public void onSuccessResponse(InstallTaskData d, String msg) {
                showTaskData(d);
                dismissLoading();
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

    private void showTaskData(InstallTaskData d) {
        taskData = d;
        mobile = d.txtPhone;
        installCustomerName.setText(d.txtFarmer);
        installCustomerAdress.setText(d.txtFarmerAddress);
        Glide.with(this).load(d.picture).apply(Constants.REQUEST_OPTIONS).into(installCustomerHeader);
        installTaskState.setText("待接单");
        installOrderProject.setText("计划完成时间：" + d.calExpectedTime);
        installPondAddress.setText("安装地址：" + d.txtInstallAddress);
        int count = 0;
        if (d.tabEquipmentList != null && d.tabEquipmentList.size() > 0) {
            for (EquipmentItem item : d.tabEquipmentList) {
                DeviceChoiceModel model = new DeviceChoiceModel();
                model.device_model = item.ITEM2;
                model.device_id = item.ITEM3;
                deviceItems.add(model);
                try {
                    count += Integer.parseInt(item.ITEM3);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            deviceListAdapter.notifyDataSetChanged();
        }
        orderItems.add("设备安装数量：" + count + "套");
//        orderItems.add("安装地址：" + d.txtInstallAddress);
        orderItems.add("养殖管家：" + d.txtFarmerManager);
        orderItems.add("代收押金费：¥" + d.txtDepositAmount);
        orderItems.add("代收服务费：¥" + d.txtServiceAmount);
        orderItemAdapter.notifyDataSetChanged();
        if (!TextUtils.isEmpty(d.taskState)) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.INTENT_OBJECT, takID);
            switch (d.taskState) {
                case "suspended":
                    bundle.putString(Constants.INTENT_FLAG, "ing");
                    goToActivity(InstallTaskDealActivity.class, bundle);
                    finish();
                    break;
                case "complete":
                    bundle.putString(Constants.INTENT_FLAG, "finish");
                    goToActivity(InstallTaskFinishActivity.class, bundle);
                    finish();
                    break;
            }
        }
    }

    @OnClick({R.id.install_customer_call, R.id.task_opera, R.id.install_pond_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_customer_call:
                new ConfirmDialog(this, "拨打电话", mobile, "拨号", "取消", new DialogConfirmListener() {
                    @Override
                    public void onDialogConfirm(boolean result, Object v) {
                        if (result) {
                            AppUtils.dialPhone(mContext, mobile);
                        }
                    }
                }).show();
                break;
            case R.id.task_opera:
                showAcceptDialog();
                break;
            case R.id.install_pond_address:
                if (taskData != null && !TextUtils.isEmpty(taskData.latitude) && !TextUtils.isEmpty(taskData.longitude)) {
                    try {
                        final double lat = Double.parseDouble(taskData.latitude);
                        final double lon = Double.parseDouble(taskData.longitude);
                        BaiduLocManager.getInstance(getApplicationContext()).startLocation(new BaiduLocManager.OnLocationComplete() {
                            @Override
                            public void onLocationComplete(BDLocation location) {
                                int code = location.getLocType();
                                if (code == 61 || code == 161) {
                                    MapNaviUtil.getInstance(mContext).
                                            openBaiduMap(new LatLng(location.getLatitude(), location.getLongitude()),
                                                    new LatLng(lat, lon),
                                                    taskData.txtInstallAddress);
                                } else {
                                    showToast("未定位到您的当前位置，无法导航");
                                }
                            }
                        });
                    } catch (NumberFormatException e) {
                        showToast("经纬度信息错误");
                    }
                } else {
                    showToast("未获取到经纬度信息");
                }
                break;
        }
    }

    private void showAcceptDialog() {
        new ConfirmDialog(this, "确认接单", "是否确认接单？", new DialogConfirmListener() {
            @Override
            public void onDialogConfirm(boolean result, Object value) {
                if (result) {
                    confirmTask();
                }
            }
        }).show();
    }

    public void confirmTask() {
        showLoading();
        getData(HttpConfig.TASK_CONFIRM.replace("{taskid}", takID), new ResponseCallBack<JsonElement>() {
            @Override
            public void onSuccessResponse(JsonElement d, String msg) {
                dismissLoading();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_OBJECT, takID);
                bundle.putString(Constants.INTENT_FLAG, status);
                goToActivity(InstallTaskDealActivity.class, bundle);
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

}
