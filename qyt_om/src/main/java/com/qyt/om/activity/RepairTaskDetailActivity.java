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
import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.ConfirmDialog;
import com.bangqu.lib.widget.RoundImageView;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.bangqu.photos.GalleryActivity;
import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.qyt.om.R;
import com.qyt.om.adapter.OrderItemAdapter;
import com.qyt.om.adapter.PhotosAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.DeviceChoiceModel;
import com.qyt.om.response.InstallTaskData;
import com.qyt.om.response.RepairTaskData;
import com.qyt.om.response.RepairTaskItem;
import com.qyt.om.utils.BaiduLocManager;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.utils.MapNaviUtil;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class RepairTaskDetailActivity extends BaseActivity {

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
    @BindView(R.id.task_create_pics)
    RecyclerView taskCreatePics;
    @BindView(R.id.install_customer_header)
    RoundImageView installCustomerHeader;
    @BindView(R.id.repair_pond_address)
    TextViewPlus repairPondAddress;

    private ArrayList<String> createPhotos = new ArrayList<>();
    private PhotosAdapter createAdapter;
    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private String takID, status, mobile;
    private RepairTaskData taskData;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_repairtaskdetail);
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
        createAdapter = new PhotosAdapter(mContext, createPhotos);
        taskCreatePics.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        taskCreatePics.setAdapter(createAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        createAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("path", createPhotos);
                bundle.putBoolean("show", true);
                goToActivity(GalleryActivity.class, bundle);
            }

            @Override
            public void onItemOpera(String tag, int position, String value) {

            }
        });
    }

    @Override
    protected void requestData() {
        super.requestData();
        showLoading();
        getData(HttpConfig.TASK_DETAIL.replace("{taskid}", takID), new ResponseCallBack<RepairTaskData>() {
            @Override
            public void onSuccessResponse(RepairTaskData d, String msg) {
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

    private void showTaskData(RepairTaskData d) {
        taskData = d;
        mobile = d.txtFarmerPhone;
        installCustomerName.setText(d.txtFarmerName);
        installCustomerAdress.setText(d.txtFarmerAddr);
        Glide.with(this).load(d.picture).apply(Constants.REQUEST_OPTIONS).into(installCustomerHeader);
        installTaskState.setText("待接单");
        repairPondAddress.setText("鱼塘位置：" + d.txtPondAddr);
        orderItems.add("鱼塘名称：" + d.txtPondsName);
        if (!TextUtils.isEmpty(d.txtHKName)) {
            orderItems.add("养殖管家：" + d.txtHKName);
        }
        if (!TextUtils.isEmpty(d.txtMonMembName)) {
            orderItems.add("监控人员：" + d.txtMonMembName);
        }
        orderItems.add("设备型号：" + d.txtRepairEqpKind);
        orderItems.add("故障描述：" + d.txtMaintenDetail);
        if (!TextUtils.isEmpty(d.txtMonMembName)) {
            orderItems.add("备注：" + (TextUtils.isEmpty(d.txtRemarks) ? "-" : d.txtRemarks));
        }
        orderItemAdapter.notifyDataSetChanged();
        if (!TextUtils.isEmpty(d.txtAppRepairImg)) {
            createPhotos.addAll(Arrays.asList(d.txtAppRepairImg.split(",")));
            createAdapter.notifyDataSetChanged();
        }
        if (!TextUtils.isEmpty(d.taskState)) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.INTENT_OBJECT, takID);
            switch (d.taskState) {
                case "suspended":
                    bundle.putString(Constants.INTENT_FLAG, "ing");
                    goToActivity(RepairTaskDealActivity.class, bundle);
                    finish();
                    break;
                case "complete":
                    bundle.putString(Constants.INTENT_FLAG, "finish");
                    goToActivity(RepairTaskFinshActivity.class, bundle);
                    finish();
                    break;
            }
        }
    }

    @OnClick({R.id.install_customer_call, R.id.task_opera, R.id.repair_pond_address})
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
            case R.id.repair_pond_address:
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
                                                    taskData.txtPondAddr);
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
                goToActivity(RepairTaskDealActivity.class, bundle);
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
