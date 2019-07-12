package com.qyt.bm.activity.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.slipload.widget.SlipLoadLayout;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.ConfirmDialog;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.bm.R;
import com.qyt.bm.activity.CallbackApproveActivity;
import com.qyt.bm.activity.RetrieveTaskCheckActivity;
import com.qyt.bm.activity.RetrieveTaskFinishActivity;
import com.qyt.bm.adapter.CallbackTaskAdapter;
import com.qyt.bm.base.BaseFragment;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.response.RecycleItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhy on 15/4/26.
 */
public class CallbackTaskFragment extends BaseFragment {

    @BindView(R.id.slip_recycler_view)
    RecyclerView slipRecyclerView;
    @BindView(R.id.slip_load_layout)
    SlipLoadLayout slipLoadLayout;
    @BindView(R.id.slip_loading_view)
    LoadingView slipLoadingView;

    private ArrayList<RecycleItem> installTaskModels = new ArrayList<>();
    private CallbackTaskAdapter installTaskAdapter;
    private int index;
    //    check(待審核)、prepare(待接單)、ing(進行中)、finish(已完成)
    private String[] stateType = new String[]{"check", "sign", "prepare", "ing", "finish"};
    private int page = 0;

    public static CallbackTaskFragment newInstance(int position) {
        CallbackTaskFragment f = new CallbackTaskFragment();
        Bundle args = new Bundle();
        args.putInt("index", position);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_installtask, container,
                    false);
            unbinder = ButterKnife.bind(this, rootView);
            initView();
            addViewListener();
            requestData();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
            unbinder = ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    private void requestData() {
        if (index == 0 || index == 1) {
            getCheck();
        } else {
            getOther();
        }
    }

    private void getOther() {
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        getData(HttpConfig.TASK_INSTALL_LIST.replace("{loginid}", loginId) + "/subRecycling/" + stateType[index] + "?page=" + page, new ResponseCallBack<ArrayList<RecycleItem>>() {
            @Override
            public void onSuccessResponse(ArrayList<RecycleItem> d, String msg) {
                if (page == 0) {
                    installTaskModels.clear();
                    slipLoadLayout.setNoMoreData(false);
                }
                slipLoadLayout.onLoadingComplete(true);
                if (d != null && d.size() > 0) {
                    installTaskModels.addAll(d);
                    if (d.size() < 10) {
                        slipLoadLayout.setNoMoreData(true);
                    } else {
                        slipLoadLayout.setLoadingMore(true);
                    }
                }
                installTaskAdapter.notifyDataSetChanged();
                if (installTaskModels.size() > 0) {
                    slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
                } else {
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
        });
    }

    private void getCheck() {
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        getData(HttpConfig.TASK_INSTALL_LIST.replace("{loginid}", loginId) + "/recycling/" + stateType[index], new ResponseCallBack<ArrayList<RecycleItem>>() {
            @Override
            public void onSuccessResponse(ArrayList<RecycleItem> d, String msg) {
                installTaskModels.clear();
                slipLoadLayout.onLoadingComplete(true);
                if (d != null && d.size() > 0) {
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
        });
    }

    private void addViewListener() {
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
        installTaskAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<RecycleItem>() {
            @Override
            public void onItemClick(int position, RecycleItem value) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_OBJECT, value.tskID);
                bundle.putString(Constants.INTENT_FLAG, stateType[index]);
                if ("check".equals(value.tskType) || "sign".equals(value.tskType)) {
                    goToActivity(RetrieveTaskCheckActivity.class, bundle);
                } else {
                    goToActivity(RetrieveTaskFinishActivity.class, bundle);
                }
            }

            @Override
            public void onItemOpera(String tag, int position, final RecycleItem value) {
                switch (tag) {
                    case "approve":
                        goToActivity(CallbackApproveActivity.class);
                        break;
                    case "call":
                        new ConfirmDialog(getContext(), "拨打电话", value.farmerPhone, "拨号", "取消", new DialogConfirmListener() {
                            @Override
                            public void onDialogConfirm(boolean result, Object v) {
                                if (result) {
                                    AppUtils.dialPhone(getContext(), value.farmerPhone);
                                }
                            }
                        }).show();
                        break;
                }
            }
        });
    }

    private void initView() {
        index = getArguments().getInt("index");
        installTaskAdapter = new CallbackTaskAdapter(getContext(), installTaskModels);
        slipRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        slipRecyclerView.setAdapter(installTaskAdapter);
    }

}
