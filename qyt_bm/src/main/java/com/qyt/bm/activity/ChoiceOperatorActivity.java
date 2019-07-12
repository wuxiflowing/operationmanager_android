package com.qyt.bm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.slipload.widget.SlipLoadLayout;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.DividerItemDecoration;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.bm.R;
import com.qyt.bm.adapter.ChoiceOperatorAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.ContactItem;
import com.qyt.bm.model.OperatorItem;
import com.qyt.bm.response.CustomerItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class ChoiceOperatorActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.slip_recycler_view)
    RecyclerView operatorList;
    @BindView(R.id.slip_loading_view)
    LoadingView loadingView;
    @BindView(R.id.slip_load_layout)
    SlipLoadLayout slipLoadLayout;

    private List<OperatorItem> operatorItems = new ArrayList<>();
    private ChoiceOperatorAdapter operatorAdapter;
    private String taskDesc;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_choiceoperator);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("运维人员");
        taskDesc = getIntent().getStringExtra(Constants.INTENT_OBJECT);
        operatorAdapter = new ChoiceOperatorAdapter(this, taskDesc, operatorItems);
        operatorList.setLayoutManager(new LinearLayoutManager(this));
        operatorList.setAdapter(operatorAdapter);
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
        operatorAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<OperatorItem>() {
            @Override
            public void onItemClick(int position, OperatorItem value) {
                Intent intent = new Intent();
                intent.putExtra(Constants.INTENT_OBJECT, value);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onItemOpera(String tag, int position, OperatorItem value) {

            }
        });
    }

    @Override
    protected void requestData() {
        super.requestData();
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        getData(HttpConfig.OPERATOR_LIST.replace("{loginid}", loginId), new ResponseCallBack<ArrayList<OperatorItem>>() {
            @Override
            public void onSuccessResponse(ArrayList<OperatorItem> d, String msg) {
                slipLoadLayout.onLoadingComplete(true);
                operatorItems.clear();
                if (d != null && d.size() > 0) {
                    operatorItems.addAll(d);
                    operatorAdapter.notifyDataSetChanged();
                    loadingView.setLoadingState(LoadingView.SHOW_DATA);
                } else {
                    operatorAdapter.notifyDataSetChanged();
                    loadingView.setLoadingState(LoadingView.NO_DATA);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                showToast(msg);
                slipLoadLayout.onLoadingComplete(false);
                loadingView.setLoadingState(LoadingView.NET_ERROR);
                operatorItems.clear();
                operatorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onVolleyError(int code, String msg) {
                showToast(msg);
                slipLoadLayout.onLoadingComplete(false);
                loadingView.setLoadingState(LoadingView.NET_ERROR);
                operatorItems.clear();
                operatorAdapter.notifyDataSetChanged();
            }
        });
    }

}
