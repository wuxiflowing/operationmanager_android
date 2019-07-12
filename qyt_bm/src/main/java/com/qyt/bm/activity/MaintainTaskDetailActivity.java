package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
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
import com.qyt.bm.response.MaintainTaskData;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MaintainTaskDetailActivity extends BaseActivity {

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
    RecyclerView maintainPictures;
    @BindView(R.id.install_customer_header)
    RoundImageView installCustomerHeader;
    @BindView(R.id.maintain_type)
    TextView maintainType;
    @BindView(R.id.maintain_next_time)
    TextView maintainNextTime;
    @BindView(R.id.task_confirm_time)
    TextView taskConfirmTime;
    @BindView(R.id.task_done_time)
    TextView taskDoneTime;
    @BindView(R.id.task_remark)
    TextView taskRemark;
    @BindView(R.id.maintain_result)
    LinearLayout maintainResult;

    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<String> maintainPhotos = new ArrayList<>();
    private PhotosAdapter maintainAdapter;
    private String takID;
    private MaintainTaskData taskData;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_maintaintaskdetail);
        takID = getIntent().getStringExtra(Constants.INTENT_OBJECT);
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
        maintainAdapter = new PhotosAdapter(this, maintainPhotos);
        maintainPictures.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        maintainPictures.setAdapter(maintainAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        maintainAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("path", maintainPhotos);
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
        getData(HttpConfig.TASK_INSTALL_DETAIL.replace("{taskid}", takID), new ResponseCallBack<MaintainTaskData>() {
            @Override
            public void onSuccessResponse(MaintainTaskData d, String msg) {
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

    private void showTaskData(MaintainTaskData d) {
        taskData = d;
        installCustomerName.setText(d.txtFarmerName);
        installCustomerAdress.setText(d.txtFarmerAddr);
        Glide.with(this).load(d.picture).apply(Constants.REQUEST_OPTIONS).into(installCustomerHeader);
        orderItems.add("工单号：" + d.txtFormNo);
        orderItems.add("鱼塘名称：" + d.txtPondsName);
        orderItems.add("鱼塘位置：" + d.txtPondAddr);
        orderItems.add("设备类型：" + d.txtRepairEqpKind);
        orderItems.add("运维管家：" + d.txtMatnerMembName);
        orderItemAdapter.notifyDataSetChanged();
        switch (d.taskState) {
            case "complete":
                installTaskState.setText("已完成");
                maintainResult.setVisibility(View.VISIBLE);
                maintainType.setText(d.tarMaintainCon);
                taskDoneTime.setText(d.txtEndDate);
                taskRemark.setText(d.tarRemarks);
                if (!TextUtils.isEmpty(d.txtMaintainImgSrc)) {
                    maintainPhotos.addAll(Arrays.asList(d.txtMaintainImgSrc.split(",")));
                    maintainAdapter.notifyDataSetChanged();
                }
                break;
            case "suspended":
                installTaskState.setText("进行中");
                maintainResult.setVisibility(View.GONE);
                break;
            default:
                installTaskState.setText("待接单");
                maintainResult.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick({R.id.install_customer_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_customer_call:
                new ConfirmDialog(this, "拨打电话", taskData.txtFarmerPhone, "拨号", "取消", new DialogConfirmListener() {
                    @Override
                    public void onDialogConfirm(boolean result, Object v) {
                        if (result) {
                            AppUtils.dialPhone(mContext, taskData.txtFarmerPhone);
                        }
                    }
                }).show();
                break;
        }
    }

}
