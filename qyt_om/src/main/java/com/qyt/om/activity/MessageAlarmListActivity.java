package com.qyt.om.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.slipload.widget.SlipLoadLayout;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.om.R;
import com.qyt.om.adapter.MessageAlarmAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.response.TaskMessageItem;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class MessageAlarmListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.slip_recycler_view)
    RecyclerView slipRecyclerView;
    @BindView(R.id.slip_load_layout)
    SlipLoadLayout slipLoadLayout;
    @BindView(R.id.slip_loading_view)
    LoadingView slipLoadingView;

    private ArrayList<TaskMessageItem> messageModels = new ArrayList<>();
    private MessageAlarmAdapter messageAdapter;
    private String taskType;
    private int page = 0;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_messagealarmlist);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        taskType = getIntent().getStringExtra(Constants.INTENT_OBJECT);
        title.setText(Constants.TASK_TITLES.get(taskType));
        messageAdapter = new MessageAlarmAdapter(this, messageModels);
        slipRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        slipRecyclerView.setAdapter(messageAdapter);
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
        messageAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<TaskMessageItem>() {
            @Override
            public void onItemClick(int position, TaskMessageItem value) {
                switch (taskType) {
//                    case "install":
//                        goToActivity(InstallTaskDetailActivity.class);
//                        break;
//                    default:
//                        goToActivity(MessageAlarmDetailActivity.class);
//                        break;
                }
            }

            @Override
            public void onItemOpera(String tag, int position, TaskMessageItem value) {

            }
        });
    }

    @Override
    protected void requestData() {
        /**/
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        HashMap<String, String> params = new HashMap<>();
        params.put("messageType", taskType);
        params.put("page", page + "");
        getData(HttpConfig.TASK_MESSAGE_LIST.replace("{loginid}", loginId) + "?" + getParamsToUrl(params), new ResponseCallBack<ArrayList<TaskMessageItem>>() {
            @Override
            public void onSuccessResponse(ArrayList<TaskMessageItem> d, String msg) {
                messageModels.clear();
                messageModels.addAll(d);
                messageAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
                slipLoadLayout.onLoadingComplete(true);
            }

            @Override
            public void onFailResponse(String msg) {
                messageModels.clear();
                messageAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.NO_DATA);
                slipLoadLayout.onLoadingComplete(false);
            }

            @Override
            public void onVolleyError(int code, String msg) {
                messageModels.clear();
                messageAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
                slipLoadLayout.onLoadingComplete(false);
            }
        });
        putData(HttpConfig.ISREAD_SYS_MESSAGE.replace("{loginid}", loginId).replace("{type}", taskType), new ResponseCallBack() {
            @Override
            public void onSuccessResponse(Object d, String msg) {

            }

            @Override
            public void onFailResponse(String msg) {

            }

            @Override
            public void onVolleyError(int code, String msg) {

            }
        });
    }

}
