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
import com.qyt.om.adapter.OrderItemAdapter;
import com.qyt.om.adapter.PhotosAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.response.RepairTaskData;
import com.qyt.om.utils.BaiduLocManager;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.utils.MapNaviUtil;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepairTaskFinshActivity extends BaseActivity {

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
    @BindView(R.id.task_files_pics)
    RecyclerView taskFilesPics;
    @BindView(R.id.task_pay_pics)
    RecyclerView taskPayPics;
    @BindView(R.id.repair_finish_info)
    LinearLayout repairFinishInfo;
    @BindView(R.id.install_customer_call)
    LinearLayout installCustomerCall;
    @BindView(R.id.task_confirm_time)
    TextView taskConfirmTime;
    @BindView(R.id.task_done_time)
    TextView taskDoneTime;
    @BindView(R.id.task_deal_type)
    TextView taskDealType;
    @BindView(R.id.task_change_device)
    TextView taskChangeDevice;
    @BindView(R.id.task_remark)
    TextView taskRemark;
    @BindView(R.id.task_reason)
    TextView taskReason;
    @BindView(R.id.task_context)
    TextView taskContext;
    @BindView(R.id.task_create_pics)
    RecyclerView taskCreatePics;
    @BindView(R.id.install_customer_header)
    RoundImageView installCustomerHeader;
    @BindView(R.id.repair_pond_address)
    TextViewPlus repairPondAddress;

    private ArrayList<String> createPhotos = new ArrayList<>();
    private PhotosAdapter createAdapter;
    private String takID, status, mobile;
    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<String> repairPhotos = new ArrayList<>();
    private PhotosAdapter repairAdapter;
    private ArrayList<String> payPhotos = new ArrayList<>();
    private PhotosAdapter payAdapter;
    private RepairTaskData taskData;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_repairtaskfinish);
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
        repairAdapter = new PhotosAdapter(this, repairPhotos);
        taskFilesPics.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        taskFilesPics.setAdapter(repairAdapter);
        payAdapter = new PhotosAdapter(this, payPhotos);
        taskPayPics.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        taskPayPics.setAdapter(payAdapter);
        createAdapter = new PhotosAdapter(mContext, createPhotos);
        taskCreatePics.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        taskCreatePics.setAdapter(createAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        repairAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("path", repairPhotos);
                bundle.putBoolean("show", true);
                goToActivity(GalleryActivity.class, bundle);
            }

            @Override
            public void onItemOpera(String tag, int position, String value) {

            }
        });
        payAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("path", payPhotos);
                bundle.putBoolean("show", true);
                goToActivity(GalleryActivity.class, bundle);
            }

            @Override
            public void onItemOpera(String tag, int position, String value) {

            }
        });
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
        installTaskState.setText("已完成");
        if (!TextUtils.isEmpty(d.txtAppRepairImg)) {
            createPhotos.addAll(Arrays.asList(d.txtAppRepairImg.split(",")));
            createAdapter.notifyDataSetChanged();
        }
        repairFinishInfo.setVisibility(View.VISIBLE);
        setTextValue(taskConfirmTime, d.txtStartDate);
        setTextValue(taskDoneTime, d.txtEndDate);
        if ("true".equals(d.rdoSelfYes)) {
            taskDealType.setText("养殖户自行解决");
        } else {
            taskDealType.setText("现场解决");
        }
        taskChangeDevice.setText(TextUtils.isEmpty(d.txtNewID) ? "否" : "是");
        taskRemark.setText(d.tarRemarks);
        taskReason.setText(d.txtResMulti + "\n" + d.tarResOth);
        taskContext.setText(d.txtConMulti + "\n" + d.tarConOth);
        if (!TextUtils.isEmpty(d.txtRepairFormImg)) {
            repairPhotos.addAll(Arrays.asList(d.txtRepairFormImg.split(",")));
            repairAdapter.notifyDataSetChanged();
        }
        if (!TextUtils.isEmpty(d.txtReceiptImg)) {
            payPhotos.addAll(Arrays.asList(d.txtReceiptImg.split(",")));
            payAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.install_customer_call, R.id.repair_pond_address})
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

}
