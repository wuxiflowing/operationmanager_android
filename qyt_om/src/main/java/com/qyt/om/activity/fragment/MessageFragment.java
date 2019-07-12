package com.qyt.om.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.slipload.widget.SlipLoadLayout;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.DividerItemDecoration;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.om.R;
import com.qyt.om.activity.MessageAlarmListActivity;
import com.qyt.om.activity.MessageTaskListActivity;
import com.qyt.om.adapter.MessageAdapter;
import com.qyt.om.base.BaseFragment;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.MessageModel;
import com.qyt.om.response.MessageItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhy on 15/4/26.
 */
public class MessageFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.slip_recycler_view)
    RecyclerView slipRecyclerView;
    @BindView(R.id.slip_load_layout)
    SlipLoadLayout slipLoadLayout;
    @BindView(R.id.slip_loading_view)
    LoadingView slipLoadingView;

    private ArrayList<MessageItem> messageModels = new ArrayList<>();
    private MessageAdapter messageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_message, container,
                    false);
            unbinder = ButterKnife.bind(this, rootView);
            initView();
            addViewListener();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
            unbinder = ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    private void requestData() {
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        getData(HttpConfig.MESSAGE_LIST.replace("{loginid}", loginId), new ResponseCallBack<ArrayList<MessageItem>>() {
            @Override
            public void onSuccessResponse(ArrayList<MessageItem> d, String msg) {
                try {
                    slipLoadLayout.onLoadingComplete(true);
                    messageModels.clear();
                    if (d != null && d.size() > 0) {
                        messageModels.addAll(d);
                        messageAdapter.notifyDataSetChanged();
                        slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
                    } else {
                        messageAdapter.notifyDataSetChanged();
                        slipLoadingView.setLoadingState(LoadingView.NO_DATA);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailResponse(String msg) {
                try {
                    messageModels.clear();
                    messageAdapter.notifyDataSetChanged();
                    slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
                    slipLoadLayout.onLoadingComplete(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyError(int code, String msg) {
                try {
                    messageModels.clear();
                    messageAdapter.notifyDataSetChanged();
                    slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
                    slipLoadLayout.onLoadingComplete(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addViewListener() {
        slipLoadLayout.setOnRefreshListener(new SlipLoadLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                requestData();
            }

            @Override
            public void onLoadingMore() {

            }
        });
        messageAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<MessageItem>() {
            @Override
            public void onItemClick(int position, MessageItem value) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_OBJECT, value.messageType);
                if ("warning".equals(value.messageType) || "sys".equals(value.messageType)) {
                    goToActivity(MessageAlarmListActivity.class, bundle);
                } else {
                    goToActivity(MessageTaskListActivity.class, bundle);
                }
            }

            @Override
            public void onItemOpera(String tag, int position, MessageItem value) {

            }
        });
    }

    private void initView() {
        title.setText("消息");
        messageAdapter = new MessageAdapter(getContext(), messageModels);
        slipRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        slipRecyclerView.setAdapter(messageAdapter);
        slipRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST, 1f, Color.parseColor("#DDDDDD")));
    }

}
