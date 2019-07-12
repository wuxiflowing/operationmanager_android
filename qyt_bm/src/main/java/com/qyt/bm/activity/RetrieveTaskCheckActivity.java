package com.qyt.bm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.qyt.bm.adapter.CallbackCheckAdapter;
import com.qyt.bm.adapter.CallbackFishpondAdapter;
import com.qyt.bm.adapter.OrderItemAdapter;
import com.qyt.bm.adapter.PhotosAdapter;
import com.qyt.bm.adapter.RecycleFinishAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.DeviceChoiceModel;
import com.qyt.bm.model.PondChoiceItem;
import com.qyt.bm.model.PondDeviceChoice;
import com.qyt.bm.response.RecycleTaskData;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class RetrieveTaskCheckActivity extends BaseActivity {

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
    @BindView(R.id.submit_confirm)
    Button submitConfirm;

    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private CallbackCheckAdapter fishpondAdapter;
    private ArrayList<RecycleTaskData.TabPondsBean> fishPondItems = new ArrayList<>();
    private String takID, status;
    private RecycleTaskData recycleTask;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_retrievetaskcheck);
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
        fishpondAdapter = new CallbackCheckAdapter(this, fishPondItems);
        installDeviceList.setAdapter(fishpondAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
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
        orderItems.add("发起时间：" + d.txtStartDate);
        String beginp = TextUtils.isEmpty(d.txtCSMembName) ? d.txtHK : d.txtCSMembName;
        String reason = TextUtils.isEmpty(d.tarReson) ? "" : d.tarReson;
        submitConfirm.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(status)) {
            switch (status) {
                case "check":
                    installTaskState.setText("待我审核");
                    orderItems.add("发起人：" + d.txtCSMembName);
                    orderItems.add("审核人：" + d.txtHK);
                    orderItems.add("回收原因：" + d.tarCustomerReson);
                    orderItems.add("备注：" + reason);
                    submitConfirm.setVisibility(View.VISIBLE);
                    break;
                case "sign":
                    installTaskState.setText("大区审核");
                    orderItems.add("发起人：" + beginp);
                    orderItems.add("审核人：" + d.txtCenMagName);
                    if (TextUtils.isEmpty(d.txtCSMembName)) {
                        orderItems.add("回收原因：" + d.txtResMulti);
                    } else {
                        orderItems.add("回收原因：" + d.tarCustomerReson);
                    }
                    orderItems.add("备注：" + reason);
                    break;
            }
        }
        orderItemAdapter.notifyDataSetChanged();
        if (d.tabPonds != null) {
            fishPondItems.addAll(d.tabPonds);
            fishpondAdapter.notifyDataSetChanged();
        }

    }

    @OnClick({R.id.install_customer_call, R.id.submit_confirm})
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
            case R.id.submit_confirm:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_OBJECT, takID);
                goToActivityForResult(CallbackApproveActivity.class, bundle, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            onBackPressed();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
