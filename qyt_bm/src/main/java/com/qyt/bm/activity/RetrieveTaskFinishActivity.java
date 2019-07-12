package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
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
import com.qyt.bm.R;
import com.qyt.bm.adapter.OrderItemAdapter;
import com.qyt.bm.adapter.PhotosAdapter;
import com.qyt.bm.adapter.RecycleDeviceAdapter;
import com.qyt.bm.adapter.RecycleFinishAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.DeviceChoiceModel;
import com.qyt.bm.response.RecycleTaskData;
import com.qyt.bm.utils.BaiduLocManager;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class RetrieveTaskFinishActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.install_customer_name)
    TextView installCustomerName;
    @BindView(R.id.install_customer_adress)
    TextView installCustomerAdress;
    @BindView(R.id.install_customer_header)
    RoundImageView installCustomerHeader;
    @BindView(R.id.install_task_state)
    TextView installTaskState;
    @BindView(R.id.install_order_info)
    UnScrollListView installOrderInfo;
    @BindView(R.id.install_device_list)
    UnScrollListView installDeviceList;
    @BindView(R.id.device_broken)
    TextView deviceBroken;
    @BindView(R.id.recycle_explain)
    TextView recycleExplain;
    @BindView(R.id.recycle_remark)
    TextView recycleRemark;
    @BindView(R.id.broken_pictures)
    RecyclerView brokenPictures;
    @BindView(R.id.retrieve_forms)
    RecyclerView retrieveForms;
    @BindView(R.id.recycle_finish_info)
    LinearLayout finishInfo;

    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<DeviceChoiceModel> deviceItems = new ArrayList<>();
    private RecycleFinishAdapter deviceListAdapter;
    private String takID, status;
    private RecycleTaskData recycleTask;
    private ArrayList<String> repairPhotos = new ArrayList<>();
    private PhotosAdapter repairAdapter;
    private ArrayList<String> payPhotos = new ArrayList<>();
    private PhotosAdapter payAdapter;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_retrievetaskfinish);
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
        deviceListAdapter = new RecycleFinishAdapter(this, deviceItems);
        installDeviceList.setAdapter(deviceListAdapter);
        repairAdapter = new PhotosAdapter(this, repairPhotos);
        brokenPictures.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        brokenPictures.setAdapter(repairAdapter);
        payAdapter = new PhotosAdapter(this, payPhotos);
        retrieveForms.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        retrieveForms.setAdapter(payAdapter);
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
    }

    @Override
    protected void requestData() {
        super.requestData();
        showLoading();
        getData(HttpConfig.TASK_INSTALL_DETAIL.replace("{taskid}", takID), new ResponseCallBack<RecycleTaskData>() {
            @Override
            public void onSuccessResponse(RecycleTaskData d, String msg) {
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

    private void showTaskData(RecycleTaskData d) {
        recycleTask = d;
        installCustomerName.setText(d.txtFarmerName);
        installCustomerAdress.setText(d.txtFarmerAddr);
        Glide.with(this).load(d.picture).apply(Constants.REQUEST_OPTIONS).into(installCustomerHeader);
        installTaskState.setText("待接单");
        orderItems.add("鱼塘名称：" + d.txtPondName);
        orderItems.add("鱼塘地址：" + d.txtPondAddr);
        orderItems.add("联系方式：" + d.txtPondPhone);
        orderItems.add("回收原因：" + d.txtResMulti);
        orderItems.add("备注信息：" + d.tarReson);
        orderItems.add("运维管家：" + d.txtMatnerMembName);
        orderItemAdapter.notifyDataSetChanged();
        if (d.tarEqps != null) {
            for (RecycleTaskData.TarEqpsBean eqpsBean : d.tarEqps) {
                DeviceChoiceModel model = new DeviceChoiceModel();
                model.deviceTypeName = eqpsBean.ITEM5;
                model.deviceTypeId = eqpsBean.ITEM2;
                deviceItems.add(model);
            }
            deviceListAdapter.notifyDataSetChanged();
        }
        switch (d.taskState) {
            case "ready":
                installTaskState.setText("待接单");
                break;
            case "suspended":
                installTaskState.setText("进行中");
                break;
            case "complete":
                installTaskState.setText("已完成");
                finishInfo.setVisibility(View.VISIBLE);
                setTextValue(deviceBroken, "true".equals(d.rdoRCYesSec) ? "是" : "否");
                setTextValue(recycleExplain, d.tarExplain);
                setTextValue(recycleRemark, d.tarRemarks);
                if (!TextUtils.isEmpty(d.txtDamageImgSrc)) {
                    repairPhotos.addAll(Arrays.asList(d.txtDamageImgSrc.split(",")));
                    repairAdapter.notifyDataSetChanged();
                }
                if (!TextUtils.isEmpty(d.txtFormImgSrc)) {
                    payPhotos.addAll(Arrays.asList(d.txtFormImgSrc.split(",")));
                    payAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @OnClick({R.id.install_customer_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_customer_call:
                new ConfirmDialog(this, "拨打电话", recycleTask.txtFarmerPhone, "拨号", "取消", new DialogConfirmListener() {
                    @Override
                    public void onDialogConfirm(boolean result, Object v) {
                        if (result) {
                            AppUtils.dialPhone(mContext, recycleTask.txtFarmerPhone);
                        }
                    }
                }).show();
                break;
        }
    }

}
