package com.qyt.bm.activity.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.slipload.widget.SlipLoadLayout;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.bm.R;
import com.qyt.bm.activity.CustomerPactDetailActivity;
import com.qyt.bm.activity.InstallTaskDetailActivity;
import com.qyt.bm.adapter.CustomerPactAdapter;
import com.qyt.bm.adapter.InstallTaskAdapter;
import com.qyt.bm.base.BaseFragment;
import com.qyt.bm.model.InstallTaskModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhy on 15/4/26.
 */
public class CustomerPactFragment extends BaseFragment {

    @BindView(R.id.slip_recycler_view)
    RecyclerView slipRecyclerView;
    @BindView(R.id.slip_load_layout)
    SlipLoadLayout slipLoadLayout;
    @BindView(R.id.slip_loading_view)
    LoadingView slipLoadingView;

    private ArrayList<InstallTaskModel> installTaskModels = new ArrayList<>();
    private CustomerPactAdapter installTaskAdapter;

    public static CustomerPactFragment newInstance(int position) {
        CustomerPactFragment f = new CustomerPactFragment();
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
        getMessageList();
    }

    private void addViewListener() {
        slipLoadLayout.setOnRefreshListener(new SlipLoadLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                slipLoadLayout.onLoadingComplete(true);
            }

            @Override
            public void onLoadingMore() {

            }
        });
        installTaskAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<InstallTaskModel>() {
            @Override
            public void onItemClick(int position, InstallTaskModel value) {
                goToActivity(CustomerPactDetailActivity.class);
            }

            @Override
            public void onItemOpera(String tag, int position, InstallTaskModel value) {

            }
        });
    }

    private void initView() {
        int index = getArguments().getInt("index");
        installTaskAdapter = new CustomerPactAdapter(getContext(), installTaskModels);
        slipRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        slipRecyclerView.setAdapter(installTaskAdapter);
//        slipRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
//                DividerItemDecoration.VERTICAL_LIST, 1f, Color.parseColor("#DDDDDD")));
    }

    private void getMessageList() {
        for (int i = 0; i < 8; i++) {
            installTaskModels.add(new InstallTaskModel());
        }
        installTaskAdapter.notifyDataSetChanged();
        slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
    }

}
