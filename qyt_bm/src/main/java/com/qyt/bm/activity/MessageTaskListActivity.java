package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.slipload.widget.SlipLoadLayout;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.bm.R;
import com.qyt.bm.adapter.MessageAlarmAdapter;
import com.qyt.bm.adapter.MessageTaskAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.response.CheckRecycle;
import com.qyt.bm.response.RecycleTaskData;
import com.qyt.bm.response.TaskMessageItem;

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
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_OBJECT, value.taskId);
                bundle.putString(Constants.INTENT_FLAG, value.taskStatus);
                switch (taskType) {
                    case "install":
                        isReadMessage(value.msgId + "");
                        goToActivity(InstallTaskDetailActivity.class, bundle);
                        break;
                    case "repair":
                        isReadMessage(value.msgId + "");
                        goToActivity(RepairTaskDetailActivity.class, bundle);
                        break;
                    case "maintain":
                        isReadMessage(value.msgId + "");
                        goToActivity(MaintainTaskDetailActivity.class, bundle);
                        break;
                    case "recycling":
                        isReadMessage(value.msgId + "");
                        getTaskState(value.taskId);
                        break;
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

    private void getTaskState(final String taskId) {
        showLoading();
        getData(HttpConfig.TASK_INSTALL_DETAIL.replace("{taskid}", taskId), new ResponseCallBack<RecycleTaskData>() {
            @Override
            public void onSuccessResponse(RecycleTaskData d, String msg) {
                dismissLoading();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_OBJECT, taskId);
                if (!TextUtils.isEmpty(d.txtMessageState)) {
                    switch (d.txtMessageState) {
                        case "toAudited":
                        case "auditPass":
                        case "auditReject":
                        case "magAuditPass":
                        case "magAuditReject":
                            goToActivity(MsgRetrieveCheckActivity.class, bundle);
                            break;
                        default:
                            goToActivity(RetrieveTaskFinishActivity.class, bundle);
                            break;
                    }
                } else {
                    goToActivity(RetrieveTaskFinishActivity.class, bundle);
                }
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
