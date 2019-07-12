package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.ConfirmDialog;
import com.bangqu.lib.widget.RoundImageView;
import com.bangqu.lib.widget.UnScrollListView;
import com.bangqu.photos.GalleryActivity;
import com.bumptech.glide.Glide;
import com.qyt.bm.R;
import com.qyt.bm.adapter.OrderItemAdapter;
import com.qyt.bm.adapter.PhotosAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.response.RepairTaskData;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepairTaskDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.install_customer_name)
    TextView installCustomerName;
    @BindView(R.id.install_customer_header)
    RoundImageView installCustomerHeader;
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
    @BindView(R.id.task_create_pics)
    RecyclerView taskCreatePics;
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

    private String takID, status, mobile;
    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<String> repairPhotos = new ArrayList<>();
    private PhotosAdapter repairAdapter;
    private ArrayList<String> payPhotos = new ArrayList<>();
    private PhotosAdapter payAdapter;
    private ArrayList<String> createPhotos = new ArrayList<>();
    private PhotosAdapter createAdapter;

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
        getData(HttpConfig.TASK_INSTALL_DETAIL.replace("{taskid}", takID), new ResponseCallBack<RepairTaskData>() {
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
        mobile = d.txtFarmerPhone;
        installCustomerName.setText(d.txtFarmerName);
        installCustomerAdress.setText(d.txtFarmerAddr);
        Glide.with(this).load(d.picture).apply(Constants.REQUEST_OPTIONS).into(installCustomerHeader);
        orderItems.add("鱼塘名称：" + d.txtPondsName);
        orderItems.add("鱼塘地址：" + d.txtPondAddr);
        orderItems.add("运维管家：" + d.txtMatnerMembName);
        if (!TextUtils.isEmpty(d.txtMonMembName)) {
            orderItems.add("监控人员：" + d.txtMonMembName);
        }
        orderItems.add("设备型号：" + d.txtRepairEqpKind);
        orderItems.add("故障描述：" + d.txtMaintenDetail);
        orderItemAdapter.notifyDataSetChanged();
        if (!TextUtils.isEmpty(d.txtAppRepairImg)) {
            createPhotos.addAll(Arrays.asList(d.txtAppRepairImg.split(",")));
            createAdapter.notifyDataSetChanged();
        }
        switch (status) {
            case "prepare":
                installTaskState.setText("待接单");
                break;
            case "ing":
                installTaskState.setText("进行中");
                break;
            case "finish":
                installTaskState.setText("已完成");
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
                break;
        }
    }

    @OnClick({R.id.install_customer_call})
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
        }
    }

}
