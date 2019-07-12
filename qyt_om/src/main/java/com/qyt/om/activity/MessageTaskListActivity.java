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
import com.qyt.om.adapter.MessageTaskAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.response.RepairTaskData;
import com.qyt.om.response.TaskMessageItem;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class MessageTaskListActivity extends BaseActivity {

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
    private MessageTaskAdapter messageAdapter;
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
        messageAdapter = new MessageTaskAdapter(this, messageModels);
        slipRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        slipRecyclerView.setAdapter(messageAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        slipLoadLayout.setOnRefreshListener(new SlipLoadLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                page = 0;
                requestData();
            }

            @Override
            public void onLoadingMore() {
                page++;
                requestData();
            }
        });
        messageAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<TaskMessageItem>() {
            @Override
            public void onItemClick(int position, TaskMessageItem value) {
                isReadMessage(value.msgId + "");
                showLoading();
                getTaskState(value.messageType, value.taskId);
            }

            @Override
            public void onItemOpera(String tag, int position, TaskMessageItem value) {

            }
        });
    }

    private void getTaskState(final String taskType, final String taskId) {
        getData(HttpConfig.TASK_DETAIL.replace("{taskid}", taskId), new ResponseCallBack<RepairTaskData>() {
            @Override
            public void onSuccessResponse(RepairTaskData d, String msg) {
                String taskState = d.taskState;
                skipToTaskDetial(taskType, taskState, taskId);
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

    private void skipToTaskDetial(String taskType, String state, String taskId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_OBJECT, taskId);
        bundle.putString(Constants.INTENT_FLAG, state);
        switch (taskType) {
            case "install":
                switch (state) {
                    case "ready":
                        goToActivity(InstallTaskDetailActivity.class, bundle);
                        break;
                    case "suspended":
                        goToActivity(InstallTaskDealActivity.class, bundle);
                        break;
                    case "complete":
                        goToActivity(InstallTaskFinishActivity.class, bundle);
                        break;
                }
                break;
            case "repair":
                switch (state) {
                    case "ready":
                        goToActivity(RepairTaskDetailActivity.class, bundle);
                        break;
                    case "suspended":
                        goToActivity(RepairTaskDealActivity.class, bundle);
                        break;
                    case "complete":
                        goToActivity(RepairTaskFinshActivity.class, bundle);
                        break;
                }
                break;
            case "maintain":
                switch (state) {
                    case "ready":
                        bundle.putString(Constants.INTENT_FLAG, "ing");
                        goToActivity(MaintainTaskDetailActivity.class, bundle);
                        finish();
                        break;
                    case "suspended":
                        bundle.putString(Constants.INTENT_FLAG, "ing");
                        goToActivity(MaintainTaskDealActivity.class, bundle);
                        finish();
                        break;
                    case "complete":
                        bundle.putString(Constants.INTENT_FLAG, "finish");
                        goToActivity(MaintainTaskFinishActivity.class, bundle);
                        finish();
                        break;
                }
                break;
            case "recycling":
                switch (state) {
                    case "ready":
                        bundle.putString(Constants.INTENT_FLAG, "ing");
                        goToActivity(RetrieveTaskDetailActivity.class, bundle);
                        finish();
                        break;
                    case "suspended":
                        bundle.putString(Constants.INTENT_FLAG, "ing");
                        goToActivity(RetrieveTaskDealActivity.class, bundle);
                        finish();
                        break;
                    case "complete":
                        bundle.putString(Constants.INTENT_FLAG, "finish");
                        goToActivity(RetrieveTaskFinishActivity.class, bundle);
                        finish();
                        break;
                }
                break;
        }
    }

    private void isReadMessage(String msgId) {
        putData(HttpConfig.ISREAD_MESSAGE.replace("{msgId}", msgId), new ResponseCallBack() {
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
                if (page == 0) {
                    messageModels.clear();
                    slipLoadLayout.setNoMoreData(false);
                }
                slipLoadLayout.onLoadingComplete(true);
                if (d != null && d.size() > 0) {
                    messageModels.addAll(d);
                    if (d.size() < 10) {
                        slipLoadLayout.setNoMoreData(true);
                    } else {
                        slipLoadLayout.setLoadingMore(true);
                    }
                }
                messageAdapter.notifyDataSetChanged();
                if (messageModels.size() > 0) {
                    slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
                } else {
                    slipLoadingView.setLoadingState(LoadingView.NO_DATA);
                }
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
    }

}
