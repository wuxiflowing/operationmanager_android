package com.qyt.om.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.listener.ListItemOperaListener;
import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.ConfirmDialog;
import com.bangqu.lib.widget.RoundImageView;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.bangqu.photos.GalleryActivity;
import com.bumptech.glide.Glide;
import com.qyt.om.R;
import com.qyt.om.adapter.DeviceListAdapter;
import com.qyt.om.adapter.InstallDeviceAdapter;
import com.qyt.om.adapter.OrderItemAdapter;
import com.qyt.om.adapter.PhotosAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.DeviceChoiceModel;
import com.qyt.om.response.EquipmentItem;
import com.qyt.om.response.InstallTaskData;
import com.qyt.om.utils.BaiduLocManager;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.utils.MapNaviUtil;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class InstallTaskFinishActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.install_customer_header)
    RoundImageView installCustomerHeader;
    @BindView(R.id.install_customer_name)
    TextView installCustomerName;
    @BindView(R.id.install_customer_adress)
    TextView installCustomerAdress;
    @BindView(R.id.install_customer_call)
    LinearLayout installCustomerCall;
    @BindView(R.id.install_task_state)
    TextView installTaskState;
    @BindView(R.id.install_order_info)
    UnScrollListView installOrderInfo;
    @BindView(R.id.install_order_project)
    TextView installOrderProject;
    @BindView(R.id.install_device_list)
    UnScrollListView installDeviceList;
    @BindView(R.id.task_confirm_time)
    TextView taskConfirmTime;
    @BindView(R.id.task_done_time)
    TextView taskDoneTime;
    @BindView(R.id.lab_service_cost)
    TextView labServiceCost;
    @BindView(R.id.task_service_cost)
    TextView taskServiceCost;
    @BindView(R.id.task_service_ispay)
    TextView taskServiceIspay;
    @BindView(R.id.task_service_paymethod)
    TextView taskServicePaymethod;
    @BindView(R.id.task_service_remark)
    TextView taskServiceRemark;
    @BindView(R.id.task_service_files)
    RecyclerView taskServiceFiles;
    @BindView(R.id.lab_deposit_cost)
    TextView labDepositCost;
    @BindView(R.id.task_deposit_cost)
    TextView taskDepositCost;
    @BindView(R.id.task_deposit_ispay)
    TextView taskDepositIspay;
    @BindView(R.id.task_deposit_paymethod)
    TextView taskDepositPaymethod;
    @BindView(R.id.task_deposit_remark)
    TextView taskDepositRemark;
    @BindView(R.id.task_deposit_files)
    RecyclerView taskDepositFiles;
    @BindView(R.id.lab_task_remark)
    TextView labTaskRemark;
//    @BindView(R.id.task_remark)
//    TextView taskRemark;
    @BindView(R.id.task_files_pics)
    RecyclerView taskFilesPics;
    @BindView(R.id.finish_info)
    LinearLayout finishInfo;
    @BindView(R.id.service_ispay_info)
    LinearLayout serviceIspayInfo;
    @BindView(R.id.deposit_ispay_info)
    LinearLayout depositIspayInfo;
    @BindView(R.id.install_pond_address)
    TextViewPlus installPondAddress;

    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<DeviceChoiceModel> deviceItems = new ArrayList<>();
    private DeviceListAdapter deviceListAdapter;
    private InstallTaskData taskData;
    private String takID, status, mobile;
    private ArrayList<String> servicePhotos = new ArrayList<>();
    private ArrayList<String> depositPhotos = new ArrayList<>();
    private ArrayList<String> installPhotos = new ArrayList<>();
    private PhotosAdapter serviceAdapter, depositAdapter, installAdapter;
    private ArrayList<EquipmentItem> equipmentItems = new ArrayList<>();
    private InstallDeviceAdapter installDeviceAdapter;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_installtaskfinish);
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
        installAdapter = new PhotosAdapter(this, installPhotos);
        taskFilesPics.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        taskFilesPics.setAdapter(installAdapter);
        serviceAdapter = new PhotosAdapter(this, servicePhotos);
        taskServiceFiles.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        taskServiceFiles.setAdapter(serviceAdapter);
        depositAdapter = new PhotosAdapter(this, depositPhotos);
        taskDepositFiles.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        taskDepositFiles.setAdapter(depositAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        installAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("path", installPhotos);
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
        if (TextUtils.isEmpty(takID)) return;
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
        installOrderProject.setText("计划完成时间：" + d.calExpectedTime);
        installPondAddress.setText("安装地址：" + d.txtInstallAddress);
        int count = 0;
        if (d.tabEquipmentBindPond != null) {
            count = d.tabEquipmentBindPond.size();
        }
        orderItems.add("设备安装数量：" + count + "套");
//        orderItems.add("安装地址：" + d.txtInstallAddress);
        orderItems.add("养殖管家：" + d.txtFarmerManager);
        orderItems.add("代收押金费：¥" + d.txtDepositAmount);
        orderItems.add("代收服务费：¥" + d.txtServiceAmount);
        orderItemAdapter.notifyDataSetChanged();
        switch (status) {
            case "prepare":
                installTaskState.setText("待接单");
                break;
            case "ing":
                installTaskState.setText("进行中");
                break;
            case "finish":
                installTaskState.setText("已完成");
                finishInfo.setVisibility(View.VISIBLE);
                setTextValue(taskConfirmTime, d.txtReciptTime);
                setTextValue(taskDoneTime, d.calInstallationTime);
                if (TextUtils.isEmpty(d.txtRelaceAmoutS) || "0".equals(d.txtRelaceAmoutS)) {
                    taskServiceIspay.setText("否");
                    serviceIspayInfo.setVisibility(View.GONE);
                } else {
                    taskServiceIspay.setText("是");
                    taskServiceCost.setText("¥" + d.txtRelaceAmoutS);
                    serviceIspayInfo.setVisibility(View.VISIBLE);
                    taskServicePaymethod.setText("付款方式：" + d.txtPaymentMethodS);
                    taskServiceRemark.setText("备注：" + d.txtServiceRemark);
                    if (!TextUtils.isEmpty(d.txtPaymentUrlS)) {
                        servicePhotos.addAll(Arrays.asList(d.txtPaymentUrlS.split(",")));
                        serviceAdapter.notifyDataSetChanged();
                    }
                }
                if (TextUtils.isEmpty(d.txtRelaceAmoutD) || "0".equals(d.txtRelaceAmoutD)) {
                    taskDepositIspay.setText("否");
                    depositIspayInfo.setVisibility(View.GONE);
                } else {
                    taskDepositIspay.setText("是");
                    depositIspayInfo.setVisibility(View.VISIBLE);
                    taskDepositCost.setText("¥" + d.txtRelaceAmoutD);
                    taskDepositPaymethod.setText("付款方式：" + d.txtPaymentMethodD);
                    taskDepositRemark.setText("备注：" + d.txtDepositRemark);
                    if (!TextUtils.isEmpty(d.txtPaymentUrlD)) {
                        depositPhotos.addAll(Arrays.asList(d.txtPaymentUrlD.split(",")));
                        depositAdapter.notifyDataSetChanged();
                    }
                }
                if (!TextUtils.isEmpty(d.txtUrls)) {
                    installPhotos.addAll(Arrays.asList(d.txtUrls.split(",")));
                    installAdapter.notifyDataSetChanged();
                }
                if (d.tabEquipmentBindPond != null && d.tabEquipmentBindPond.size() > 0) {
                    equipmentItems.clear();
                    equipmentItems.addAll(d.tabEquipmentBindPond);
                    installDeviceAdapter = new InstallDeviceAdapter(mContext, equipmentItems);
                    installDeviceList.setAdapter(installDeviceAdapter);
                    installDeviceAdapter.setListItemOperaListener(new ListItemOperaListener() {
                        @Override
                        public void onItemOpera(String tag, int position, Object value) {
                            Bundle bundle1 = new Bundle();
                            bundle1.putString(Constants.INTENT_OBJECT, (String) value);
                            goToActivity(DeviceDetailActivity.class, bundle1);
                        }
                    });
                }
                break;
        }
    }

    @OnClick({R.id.install_customer_call, R.id.install_pond_address})
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

}
