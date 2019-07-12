package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.slipload.widget.SlipLoadLayout;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.ConfirmDialog;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.bm.R;
import com.qyt.bm.adapter.CallbackTaskAdapter;
import com.qyt.bm.adapter.CheckbackTaskAdapter;
import com.qyt.bm.adapter.InstallTaskAdapter;
import com.qyt.bm.adapter.MaintainTaskAdapter;
import com.qyt.bm.adapter.RepairTaskAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.InstallTaskItem;
import com.qyt.bm.response.CheckRecycle;
import com.qyt.bm.response.RecycleItem;
import com.qyt.bm.response.RepairTaskItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchTaskActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.slip_recycler_view)
    RecyclerView slipRecyclerView;
    @BindView(R.id.slip_loading_view)
    LoadingView slipLoadingView;
    @BindView(R.id.slip_load_layout)
    SlipLoadLayout slipLoadLayout;
    @BindView(R.id.switchcity_input)
    EditText switchcityInput;
    @BindView(R.id.cancel_search)
    ImageView cancelSearch;

    private ArrayList<InstallTaskItem> installTaskModels = new ArrayList<>();
    private InstallTaskAdapter installTaskAdapter;
    private ArrayList<RepairTaskItem> repairTaskModels = new ArrayList<>();
    private RepairTaskAdapter repairTaskAdapter;
    private ArrayList<RepairTaskItem> maintainTaskModels = new ArrayList<>();
    private MaintainTaskAdapter maintainTaskAdapter;
    private ArrayList<RecycleItem> recycleTaskModels = new ArrayList<>();
    private CallbackTaskAdapter recycleTaskAdapter;
    private ArrayList<CheckRecycle> checkTaskModels = new ArrayList<>();
    private CheckbackTaskAdapter checkTaskAdapter;
    private String queryType;
    private String state;
    private int page = 0;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_searchtask);
        queryType = getIntent().getStringExtra("queryType");
        state = getIntent().getStringExtra("type");
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        switch (queryType) {
            case "install":
                title.setText("安装任务");
                installTaskAdapter = new InstallTaskAdapter(mContext, installTaskModels);
                slipRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                slipRecyclerView.setAdapter(installTaskAdapter);
                break;
            case "repair":
                title.setText("故障报修");
                repairTaskAdapter = new RepairTaskAdapter(mContext, repairTaskModels);
                slipRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                slipRecyclerView.setAdapter(repairTaskAdapter);
                break;
            case "maintain":
                title.setText("定期维护");
                maintainTaskAdapter = new MaintainTaskAdapter(mContext, maintainTaskModels);
                slipRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                slipRecyclerView.setAdapter(maintainTaskAdapter);
                break;
            case "subRecycling":
                title.setText("设备回收");
                recycleTaskAdapter = new CallbackTaskAdapter(mContext, recycleTaskModels);
                slipRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                slipRecyclerView.setAdapter(recycleTaskAdapter);
                break;
            case "recycling":
                title.setText("设备回收");
                checkTaskAdapter = new CheckbackTaskAdapter(mContext, checkTaskModels);
                slipRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                slipRecyclerView.setAdapter(checkTaskAdapter);
                break;
        }
        slipLoadingView.setLoadingState(LoadingView.NO_DATA);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        switchcityInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    AppUtils.hideSoftInput(mContext, getCurrentFocus());
                    searchData(v.getText().toString());
                }
                return false;
            }
        });
        switchcityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cancelSearch.setVisibility(View.VISIBLE);
                } else {
                    cancelSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        if (installTaskAdapter != null) {
            installTaskAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<InstallTaskItem>() {
                @Override
                public void onItemClick(int position, InstallTaskItem value) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.INTENT_OBJECT, value.tskID);
                    bundle.putString(Constants.INTENT_FLAG, state);
                    goToActivity(InstallTaskDetailActivity.class, bundle);
                }

                @Override
                public void onItemOpera(String tag, int position, final InstallTaskItem value) {
                    switch (tag) {
                        case "call":
                            new ConfirmDialog(mContext, "拨打电话", value.farmerPhone, "拨号", "取消", new DialogConfirmListener() {
                                @Override
                                public void onDialogConfirm(boolean result, Object v) {
                                    if (result) {
                                        AppUtils.dialPhone(mContext, value.farmerPhone);
                                    }
                                }
                            }).show();
                            break;
                    }
                }
            });
        }
        if (repairTaskAdapter != null) {
            repairTaskAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<RepairTaskItem>() {
                @Override
                public void onItemClick(int position, RepairTaskItem value) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.INTENT_OBJECT, value.tskID);
                    bundle.putString(Constants.INTENT_FLAG, state);
                    goToActivity(RepairTaskDetailActivity.class, bundle);
                }

                @Override
                public void onItemOpera(String tag, int position, final RepairTaskItem value) {
                    switch (tag) {
                        case "call":
                            new ConfirmDialog(mContext, "拨打电话", value.farmerPhone, "拨号", "取消", new DialogConfirmListener() {
                                @Override
                                public void onDialogConfirm(boolean result, Object v) {
                                    if (result) {
                                        AppUtils.dialPhone(mContext, value.farmerPhone);
                                    }
                                }
                            }).show();
                            break;
                    }
                }
            });
        }
        if (maintainTaskAdapter != null) {
            maintainTaskAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<RepairTaskItem>() {
                @Override
                public void onItemClick(int position, RepairTaskItem value) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.INTENT_OBJECT, value.tskID);
                    goToActivity(MaintainTaskDetailActivity.class, bundle);
                }

                @Override
                public void onItemOpera(String tag, int position, final RepairTaskItem value) {
                    switch (tag) {
                        case "call":
                            new ConfirmDialog(mContext, "拨打电话", value.farmerPhone, "拨号", "取消", new DialogConfirmListener() {
                                @Override
                                public void onDialogConfirm(boolean result, Object v) {
                                    if (result) {
                                        AppUtils.dialPhone(mContext, value.farmerPhone);
                                    }
                                }
                            }).show();
                            break;
                    }
                }
            });
        }
        if (recycleTaskAdapter != null) {
            recycleTaskAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<RecycleItem>() {
                @Override
                public void onItemClick(int position, RecycleItem value) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.INTENT_OBJECT, value.tskID);
                    bundle.putString(Constants.INTENT_FLAG, state);
                    if ("check".equals(value.tskType)) {
                        goToActivity(RetrieveTaskCheckActivity.class, bundle);
                    } else {
                        goToActivity(RetrieveTaskFinishActivity.class, bundle);
                    }
                }

                @Override
                public void onItemOpera(String tag, int position, final RecycleItem value) {
                    switch (tag) {
                        case "call":
                            new ConfirmDialog(mContext, "拨打电话", value.farmerPhone, "拨号", "取消", new DialogConfirmListener() {
                                @Override
                                public void onDialogConfirm(boolean result, Object v) {
                                    if (result) {
                                        AppUtils.dialPhone(mContext, value.farmerPhone);
                                    }
                                }
                            }).show();
                            break;
                    }
                }
            });
        }
        if (checkTaskAdapter != null) {
            checkTaskAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<CheckRecycle>() {
                @Override
                public void onItemClick(int position, CheckRecycle value) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.INTENT_OBJECT, value.tskID);
                    goToActivity(RetrieveTaskCheckActivity.class, bundle);
                }

                @Override
                public void onItemOpera(String tag, int position, final CheckRecycle value) {
                    switch (tag) {
                        case "approve":
                            goToActivity(CallbackApproveActivity.class);
                            break;
                        case "call":
                            new ConfirmDialog(mContext, "拨打电话", value.txtFarmerPhone, "拨号", "取消", new DialogConfirmListener() {
                                @Override
                                public void onDialogConfirm(boolean result, Object v) {
                                    if (result) {
                                        AppUtils.dialPhone(mContext, value.txtFarmerPhone);
                                    }
                                }
                            }).show();
                            break;
                    }
                }
            });
        }
    }

    protected void searchData(String name) {
        if (TextUtils.isEmpty(name)) {
            showToast("请输入任务关键字");
            return;
        }
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        switch (queryType) {
            case "install":
                getData(HttpConfig.GET_QUERY_TASK.replace("{loginid}", loginId) + queryType + "/" + state + "/" + name + "?page=" + page, installResponseCallBack);
                break;
            case "repair":
                getData(HttpConfig.GET_QUERY_TASK.replace("{loginid}", loginId) + queryType + "/" + state + "/" + name + "?page=" + page, repairResponseCallBack);
                break;
            case "maintain":
                getData(HttpConfig.GET_QUERY_TASK.replace("{loginid}", loginId) + queryType + "/" + state + "/" + name + "?page=" + page, maintainResponseCallBack);
                break;
            case "subRecycling":
                getData(HttpConfig.GET_QUERY_TASK.replace("{loginid}", loginId) + queryType + "/" + state + "/" + name + "?page=" + page, recycleResponseCallBack);
                break;
            case "recycling":
                getData(HttpConfig.GET_QUERY_TASK.replace("{loginid}", loginId) + queryType + "/" + state + "/" + name + "?page=" + page, checkResponseCallBack);
                break;
        }
    }

    ResponseCallBack<ArrayList<RepairTaskItem>> repairResponseCallBack = new ResponseCallBack<ArrayList<RepairTaskItem>>() {
        @Override
        public void onSuccessResponse(ArrayList<RepairTaskItem> d, String msg) {
            if (page == 0) {
                repairTaskModels.clear();
                slipLoadLayout.setNoMoreData(false);
            }
            slipLoadLayout.onLoadingComplete(true);
            if (d != null && d.size() > 0) {
                if (d.size() < 10) {
                    slipLoadLayout.setNoMoreData(true);
                } else {
                    slipLoadLayout.setLoadingMore(true);
                }
                repairTaskModels.addAll(d);
                repairTaskAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
            } else {
                repairTaskAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.NO_DATA);
            }
        }

        @Override
        public void onFailResponse(String msg) {
            showToast(msg);
            repairTaskModels.clear();
            repairTaskAdapter.notifyDataSetChanged();
            slipLoadingView.setLoadingState(LoadingView.NO_DATA);
            slipLoadLayout.onLoadingComplete(false);
        }

        @Override
        public void onVolleyError(int code, String msg) {
            showToast(msg);
            repairTaskModels.clear();
            repairTaskAdapter.notifyDataSetChanged();
            slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
            slipLoadLayout.onLoadingComplete(false);
        }
    };

    ResponseCallBack<ArrayList<InstallTaskItem>> installResponseCallBack = new ResponseCallBack<ArrayList<InstallTaskItem>>() {
        @Override
        public void onSuccessResponse(ArrayList<InstallTaskItem> d, String msg) {
            if (page == 0) {
                installTaskModels.clear();
                slipLoadLayout.setNoMoreData(false);
            }
            slipLoadLayout.onLoadingComplete(true);
            if (d != null && d.size() > 0) {
                if (d.size() < 10) {
                    slipLoadLayout.setNoMoreData(true);
                } else {
                    slipLoadLayout.setLoadingMore(true);
                }
                installTaskModels.addAll(d);
                installTaskAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
            } else {
                installTaskAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.NO_DATA);
            }
        }

        @Override
        public void onFailResponse(String msg) {
            showToast(msg);
            installTaskModels.clear();
            installTaskAdapter.notifyDataSetChanged();
            slipLoadingView.setLoadingState(LoadingView.NO_DATA);
            slipLoadLayout.onLoadingComplete(false);
        }

        @Override
        public void onVolleyError(int code, String msg) {
            showToast(msg);
            installTaskModels.clear();
            installTaskAdapter.notifyDataSetChanged();
            slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
            slipLoadLayout.onLoadingComplete(false);
        }
    };

    ResponseCallBack<ArrayList<RepairTaskItem>> maintainResponseCallBack = new ResponseCallBack<ArrayList<RepairTaskItem>>() {
        @Override
        public void onSuccessResponse(ArrayList<RepairTaskItem> d, String msg) {
            if (page == 0) {
                maintainTaskModels.clear();
                slipLoadLayout.setNoMoreData(false);
            }
            slipLoadLayout.onLoadingComplete(true);
            if (d != null && d.size() > 0) {
                if (d.size() < 10) {
                    slipLoadLayout.setNoMoreData(true);
                } else {
                    slipLoadLayout.setLoadingMore(true);
                }
                maintainTaskModels.addAll(d);
                maintainTaskAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
            } else {
                maintainTaskAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.NO_DATA);
            }
        }

        @Override
        public void onFailResponse(String msg) {
            showToast(msg);
            maintainTaskModels.clear();
            maintainTaskAdapter.notifyDataSetChanged();
            slipLoadingView.setLoadingState(LoadingView.NO_DATA);
            slipLoadLayout.onLoadingComplete(false);
        }

        @Override
        public void onVolleyError(int code, String msg) {
            showToast(msg);
            maintainTaskModels.clear();
            maintainTaskAdapter.notifyDataSetChanged();
            slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
            slipLoadLayout.onLoadingComplete(false);
        }
    };

    ResponseCallBack<ArrayList<RecycleItem>> recycleResponseCallBack = new ResponseCallBack<ArrayList<RecycleItem>>() {
        @Override
        public void onSuccessResponse(ArrayList<RecycleItem> d, String msg) {
            if (page == 0) {
                recycleTaskModels.clear();
                slipLoadLayout.setNoMoreData(false);
            }
            slipLoadLayout.onLoadingComplete(true);
            if (d != null && d.size() > 0) {
                if (d.size() < 10) {
                    slipLoadLayout.setNoMoreData(true);
                } else {
                    slipLoadLayout.setLoadingMore(true);
                }
                recycleTaskModels.addAll(d);
                recycleTaskAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
            } else {
                recycleTaskAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.NO_DATA);
            }
        }

        @Override
        public void onFailResponse(String msg) {
            showToast(msg);
            recycleTaskModels.clear();
            recycleTaskAdapter.notifyDataSetChanged();
            slipLoadingView.setLoadingState(LoadingView.NO_DATA);
            slipLoadLayout.onLoadingComplete(false);
        }

        @Override
        public void onVolleyError(int code, String msg) {
            showToast(msg);
            recycleTaskModels.clear();
            recycleTaskAdapter.notifyDataSetChanged();
            slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
            slipLoadLayout.onLoadingComplete(false);
        }
    };

    ResponseCallBack<ArrayList<CheckRecycle>> checkResponseCallBack = new ResponseCallBack<ArrayList<CheckRecycle>>() {
        @Override
        public void onSuccessResponse(ArrayList<CheckRecycle> d, String msg) {
            if (page == 0) {
                checkTaskModels.clear();
                slipLoadLayout.setNoMoreData(false);
            }
            slipLoadLayout.onLoadingComplete(true);
            if (d != null && d.size() > 0) {
                if (d.size() < 10) {
                    slipLoadLayout.setNoMoreData(true);
                } else {
                    slipLoadLayout.setLoadingMore(true);
                }
                checkTaskModels.addAll(d);
                checkTaskAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
            } else {
                checkTaskAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.NO_DATA);
            }
        }

        @Override
        public void onFailResponse(String msg) {
            showToast(msg);
            checkTaskModels.clear();
            checkTaskAdapter.notifyDataSetChanged();
            slipLoadingView.setLoadingState(LoadingView.NO_DATA);
            slipLoadLayout.onLoadingComplete(false);
        }

        @Override
        public void onVolleyError(int code, String msg) {
            showToast(msg);
            checkTaskModels.clear();
            checkTaskAdapter.notifyDataSetChanged();
            slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
            slipLoadLayout.onLoadingComplete(false);
        }
    };

    @OnClick({R.id.cancel_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_search:
                cancelSearch.setVisibility(View.GONE);
                switchcityInput.setText("");
                slipLoadingView.setLoadingState(LoadingView.NO_DATA);
                switch (queryType) {
                    case "install":
                        installTaskModels.clear();
                        installTaskAdapter.notifyDataSetChanged();
                        break;
                    case "repair":
                        repairTaskModels.clear();
                        repairTaskAdapter.notifyDataSetChanged();
                        break;
                    case "maintain":
                        maintainTaskModels.clear();
                        maintainTaskAdapter.notifyDataSetChanged();
                        break;
                    case "subRecycling":
                        recycleTaskModels.clear();
                        recycleTaskAdapter.notifyDataSetChanged();
                        break;
                    case "recycling":
                        checkTaskModels.clear();
                        checkTaskAdapter.notifyDataSetChanged();
                        break;
                }
                break;
        }
    }

}
