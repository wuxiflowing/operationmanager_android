package com.qyt.om.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.lib.listener.ListItemOperaListener;
import com.bangqu.lib.slipload.widget.SlipLoadLayout;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.DividerItemDecoration;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.om.R;
import com.qyt.om.activity.DeviceDetailActivity;
import com.qyt.om.activity.device.DeviceNewDetailActivity;
import com.qyt.om.adapter.DeviceInfoAdapter;
import com.qyt.om.adapter.DeviceInfoAdapter2;
import com.qyt.om.base.BaseFragment;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.response.ChildDeviceListBean;
import com.qyt.om.response.PondDeviceInfo2;
import com.qyt.om.response.PondDeviceInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhy on 15/4/26.
 */
public class DeviceFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.slip_recycler_view)
    RecyclerView slipRecyclerView;
    @BindView(R.id.slip_load_layout)
    SlipLoadLayout slipLoadLayout;
    @BindView(R.id.slip_loading_view)
    LoadingView slipLoadingView;
    @BindView(R.id.phone_search)
    EditText switchcityInput;
    @BindView(R.id.phone_search_cancel)
    ImageView cancelSearch;

    private ArrayList<PondDeviceInfo2> deviceModels = new ArrayList<>();
    //private DeviceInfoAdapter deviceAdapter;
    private ArrayList<ChildDeviceListBean> mListData;
    private DeviceInfoAdapter2 deviceInfoAdapter2;
    private int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_device, container,
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

    private void initView() {
        title.setText("设备");
        mListData = new ArrayList<>();
        //deviceAdapter = new DeviceInfoAdapter(getContext(), deviceModels);
        deviceInfoAdapter2 = new DeviceInfoAdapter2(getContext(), mListData);

        slipRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        slipRecyclerView.setAdapter(deviceInfoAdapter2);
        slipRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST, 8f, Color.TRANSPARENT));
    }

    private void requestData() {
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        getData(HttpConfig.PONDS_INFO_LIST.replace("{maintainKeeperID}", loginId) + "?page=" + page,
                new ResponseCallBack<ArrayList<PondDeviceInfo2>>() {
                    @Override
                    public void onSuccessResponse(ArrayList<PondDeviceInfo2> d, String msg) {
                        if (page == 0) {
                            deviceModels.clear();
                            mListData.clear();
                            slipLoadLayout.setNoMoreData(false);
                        }
                        slipLoadLayout.onLoadingComplete(true);
                        if (d != null && d.size() > 0) {
                            deviceModels.addAll(d);
                            if (d.size() < 10) {
                                slipLoadLayout.setNoMoreData(true);
                            } else {
                                slipLoadLayout.setLoadingMore(true);
                            }
                        }
                        for (PondDeviceInfo2 info : deviceModels) {
//                            if (info.childDeviceList != null && info.childDeviceList.size() != 0) {
                            for (ChildDeviceListBean device : info.childDeviceList) {
                                device.pondId = info.pondId;
                                device.pondName = info.name;
                                mListData.add(device);
                            }
//                            } else {
//                                mListData.add(new ChildDeviceListBean());
//                            }
                        }
                        deviceInfoAdapter2.notifyDataSetChanged();
                        if (deviceModels.size() > 0) {
                            slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
                        } else {
                            slipLoadingView.setLoadingState(LoadingView.NO_DATA);
                        }
                    }

                    @Override
                    public void onFailResponse(String msg) {
                        deviceModels.clear();
                        mListData.clear();
                        deviceInfoAdapter2.notifyDataSetChanged();
                        slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
                        slipLoadLayout.onLoadingComplete(false);
                    }

                    @Override
                    public void onVolleyError(int code, String msg) {
                        deviceModels.clear();
                        mListData.clear();
                        deviceInfoAdapter2.notifyDataSetChanged();
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
                if (TextUtils.isEmpty(switchcityInput.getText())) {
                    requestData();
                } else {
                    searchData(switchcityInput.getText().toString());
                }
            }

            @Override
            public void onLoadingMore() {
                page++;
                if (TextUtils.isEmpty(switchcityInput.getText())) {
                    requestData();
                } else {
                    searchData(switchcityInput.getText().toString());
                }
            }
        });
        deviceInfoAdapter2.setOperaListener(new ListItemOperaListener() {
            @Override
            public void onItemOpera(String tag, int position, Object value) {
                switch (tag) {
                    case "fishpond":
                        Bundle bundle = new Bundle();
                        PondDeviceInfo2 fishPondInfo = null;
                        for (PondDeviceInfo2 info : deviceModels) {
                            if (info.pondId.equals((String) value)) {
                                fishPondInfo = info;
                                break;
                            }
                        }
                        bundle.putParcelable(Constants.INTENT_OBJECT, fishPondInfo);
                        goToActivity(getActivity().getClass(), bundle);
                        break;
                    case "device":
                        String typeAndId = (String) value;
                        int index = typeAndId.indexOf("-");
                        if (Constants.DEVICE_TYPE_KD326.equals(typeAndId.substring(0, index))) {
                            Bundle bundle1 = new Bundle();
                            bundle1.putString(Constants.INTENT_OBJECT, typeAndId.substring(index + 1));
                            goToActivity(DeviceDetailActivity.class, bundle1);
                        } else if (Constants.DEVICE_TYPE_QY601.equals(typeAndId.substring(0, index))) {
                            Bundle bundle1 = new Bundle();
                            bundle1.putString(Constants.INTENT_OBJECT, typeAndId.substring(index + 1));
                            goToActivity(DeviceNewDetailActivity.class, bundle1);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        switchcityInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    AppUtils.hideSoftInput(getContext(), switchcityInput);
                    page = 0;
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
    }

    protected void searchData(String name) {
        if (TextUtils.isEmpty(name)) {
            showToast("请输入关键字");
            return;
        }
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        getData(HttpConfig.PONDS_INFO_LIST_FILTER
                        .replace("{maintainKeeperID}", loginId)
                        .replace("{queryValue}", name) + "?page=" + page,
                new ResponseCallBack<ArrayList<PondDeviceInfo2>>() {
                    @Override
                    public void onSuccessResponse(ArrayList<PondDeviceInfo2> d, String msg) {
                        if (page == 0) {
                            deviceModels.clear();
                            mListData.clear();
                            slipLoadLayout.setNoMoreData(false);
                        }
                        slipLoadLayout.onLoadingComplete(true);
                        if (d != null && d.size() > 0) {
                            deviceModels.addAll(d);
                            if (d.size() < 10) {
                                slipLoadLayout.setNoMoreData(true);
                            } else {
                                slipLoadLayout.setLoadingMore(true);
                            }
                        }
                        for (PondDeviceInfo2 info : deviceModels) {
//                            if (info.childDeviceList != null && info.childDeviceList.size() != 0) {
                            for (ChildDeviceListBean device : info.childDeviceList) {
                                device.pondId = info.pondId;
                                device.pondName = info.name;
                                mListData.add(device);
                            }
//                            } else {
//                                mListData.add(new ChildDeviceListBean());
//                            }
                        }
                        deviceInfoAdapter2.notifyDataSetChanged();
                        if (deviceModels.size() > 0) {
                            slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
                        } else {
                            slipLoadingView.setLoadingState(LoadingView.NO_DATA);
                        }
                    }

                    @Override
                    public void onFailResponse(String msg) {
                        deviceModels.clear();
                        mListData.clear();
                        deviceInfoAdapter2.notifyDataSetChanged();
                        slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
                        slipLoadLayout.onLoadingComplete(false);
                    }

                    @Override
                    public void onVolleyError(int code, String msg) {
                        deviceModels.clear();
                        mListData.clear();
                        deviceInfoAdapter2.notifyDataSetChanged();
                        slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
                        slipLoadLayout.onLoadingComplete(false);
                    }
                });
    }


    @OnClick({R.id.phone_search_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.phone_search_cancel:
                cancelSearch.setVisibility(View.GONE);
                switchcityInput.setText("");
                requestData();
                break;
            default:
                break;
        }
    }

}
