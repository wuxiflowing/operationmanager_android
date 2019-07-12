package com.qyt.bm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.slipload.widget.SlipLoadLayout;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.DividerItemDecoration;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.bm.R;
import com.qyt.bm.adapter.ChoiceDeviceAdapter;
import com.qyt.bm.adapter.RecycleDeviceAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.OperatorItem;
import com.qyt.bm.model.PondChoiceItem;
import com.qyt.bm.model.PondDeviceChoice;
import com.qyt.bm.response.DeviceConfigInfo;
import com.qyt.bm.response.FishPondInfo;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ChoiceDeviceActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.slip_recycler_view)
    RecyclerView operatorList;
    @BindView(R.id.slip_loading_view)
    LoadingView loadingView;
    @BindView(R.id.slip_load_layout)
    SlipLoadLayout slipLoadLayout;

    private ArrayList<PondChoiceItem> pondChoiceItems = new ArrayList<>();
    private RecycleDeviceAdapter recycleDeviceAdapter;
    private String farmerId;
    private final int REQUEST_OPERATOR = 101;
    private int pos = -1;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_choiceoperator);
        farmerId = getIntent().getStringExtra(Constants.INTENT_OBJECT);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("选择回收设备");
        rightTv.setText(R.string.ok);
        rightTv.setVisibility(View.VISIBLE);
        recycleDeviceAdapter = new RecycleDeviceAdapter(this, pondChoiceItems);
        operatorList.setLayoutManager(new LinearLayoutManager(this));
        operatorList.setAdapter(recycleDeviceAdapter);
        operatorList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, 1f, Color.TRANSPARENT));
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        slipLoadLayout.setOnRefreshListener(new SlipLoadLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                requestData();
            }

            @Override
            public void onLoadingMore() {

            }
        });
        recycleDeviceAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<PondChoiceItem>() {
            @Override
            public void onItemClick(int position, PondChoiceItem value) {

            }

            @Override
            public void onItemOpera(String tag, int position, PondChoiceItem value) {
                pos = position;
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_OBJECT, "回收设备数量：");
                goToActivityForResult(ChoiceOperatorActivity.class, bundle, REQUEST_OPERATOR);
            }
        });
    }

    @Override
    protected void requestData() {
        super.requestData();
        /*获取鱼塘列表*/
        getData(HttpConfig.GET_CUSTOMER_PONDS.replace("{customerId}", farmerId), new ResponseCallBack<ArrayList<FishPondInfo>>() {
            @Override
            public void onSuccessResponse(ArrayList<FishPondInfo> d, String msg) {
                slipLoadLayout.onLoadingComplete(true);
                pondChoiceItems.clear();
                if (d != null && d.size() > 0) {
                    for (FishPondInfo pond : d) {
                        PondChoiceItem pondChoiceItem = new PondChoiceItem();
                        pondChoiceItem.pondId = pond.pondId;
                        pondChoiceItem.pondName = pond.name;
                        pondChoiceItem.pondAddress = pond.pondAddress;
                        pondChoiceItem.maintainKeeperID = pond.maintainKeeperID;
                        pondChoiceItem.maintainKeeper = pond.maintainKeeper;
                        if (pond.childDeviceList != null && pond.childDeviceList.size() > 0) {
                            for (DeviceConfigInfo device : pond.childDeviceList) {
                                PondDeviceChoice pondDeviceChoice = new PondDeviceChoice();
                                pondDeviceChoice.deviceId = device.id;
                                pondDeviceChoice.identifier = device.identifier;
                                pondDeviceChoice.kind = device.type;
                                pondChoiceItem.deviceList.add(pondDeviceChoice);
                            }
                            pondChoiceItems.add(pondChoiceItem);
                        }
                    }
                }
                if (pondChoiceItems.size() > 0) {
                    loadingView.setLoadingState(LoadingView.SHOW_DATA);
                } else {
                    loadingView.setLoadingState(LoadingView.NO_DATA);
                }
                recycleDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailResponse(String msg) {
                slipLoadLayout.onLoadingComplete(false);
                loadingView.setLoadingState(LoadingView.NET_ERROR);
                pondChoiceItems.clear();
                recycleDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onVolleyError(int code, String msg) {
                slipLoadLayout.onLoadingComplete(false);
                loadingView.setLoadingState(LoadingView.NET_ERROR);
                pondChoiceItems.clear();
                recycleDeviceAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.right_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_tv:
                ArrayList<PondChoiceItem> ponds = new ArrayList<>();
                for (PondChoiceItem choice : pondChoiceItems) {
                    PondChoiceItem item = new PondChoiceItem();
                    item.pondId = choice.pondId;
                    item.pondName = choice.pondName;
                    item.pondAddress = choice.pondAddress;
                    item.maintainKeeper = choice.maintainKeeper;
                    item.maintainKeeperID = choice.maintainKeeperID;
                    if (choice.deviceList != null && choice.deviceList.size() > 0) {
                        ArrayList<PondDeviceChoice> deviceList = new ArrayList<>();
                        for (PondDeviceChoice device : choice.deviceList) {
                            if (device.isChoice) {
                                deviceList.add(device);
                            }
                        }
                        if (deviceList.size() > 0) {
                            item.deviceList.addAll(deviceList);
                            ponds.add(item);
                        }
                    }
                }
                if (ponds.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.INTENT_OBJECT, ponds);
                    setResult(RESULT_OK, intent);
                    onBackPressed();
                } else {
                    showToast("请选择回收设备");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_OPERATOR:
                    OperatorItem operator = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    pondChoiceItems.get(pos).maintainKeeperID = operator.memId;
                    pondChoiceItems.get(pos).maintainKeeper = operator.memName;
                    recycleDeviceAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

}
