package com.qyt.bm.activity.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.slipload.widget.SlipLoadLayout;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.bm.R;
import com.qyt.bm.activity.InstallTaskDetailActivity;
import com.qyt.bm.activity.MaintainTaskDetailActivity;
import com.qyt.bm.adapter.InstallTaskAdapter;
import com.qyt.bm.adapter.MaintainTaskAdapter;
import com.qyt.bm.adapter.RepairTaskAdapter;
import com.qyt.bm.base.BaseFragment;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.InstallTaskModel;
import com.qyt.bm.response.RepairTaskItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhy on 15/4/26.
 */
public class MaintainTaskFragment extends BaseFragment {

    @BindView(R.id.slip_recycler_view)
    RecyclerView slipRecyclerView;
    @BindView(R.id.slip_load_layout)
    SlipLoadLayout slipLoadLayout;
    @BindView(R.id.slip_loading_view)
    LoadingView slipLoadingView;

    private ArrayList<RepairTaskItem> installTaskModels = new ArrayList<>();
    private MaintainTaskAdapter installTaskAdapter;
    private int index;
    //    check(待審核)、prepare(待接單)、ing(進行中)、finish(已完成)
    private String[] stateType = new String[]{"prepare", "ing", "finish"};
    private int page = 0;

    public static MaintainTaskFragment newInstance(int position) {
        MaintainTaskFragment f = new MaintainTaskFragment();
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
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        getData(HttpConfig.TASK_INSTALL_LIST.replace("{loginid}", loginId) + "/maintain/" + stateType[index] + "?page=" + page, new ResponseCallBack<ArrayList<RepairTaskItem>>() {
            @Override
            public void onSuccessResponse(ArrayList<RepairTaskItem> d, String msg) {
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
        installTaskAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<RepairTaskItem>() {
            @Override
            public void onItemClick(int position, RepairTaskItem value) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_OBJECT, value.tskID);
                goToActivity(MaintainTaskDetailActivity.class, bundle);
            }

            @Override
            public void onItemOpera(String tag, int position, RepairTaskItem value) {

            }
        });
    }

    private void initView() {
        index = getArguments().getInt("index");
        installTaskAdapter = new MaintainTaskAdapter(getContext(), installTaskModels);
        slipRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        slipRecyclerView.setAdapter(installTaskAdapter);
//        slipRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
//                DividerItemDecoration.VERTICAL_LIST, 1f, Color.parseColor("#DDDDDD")));
    }

}
