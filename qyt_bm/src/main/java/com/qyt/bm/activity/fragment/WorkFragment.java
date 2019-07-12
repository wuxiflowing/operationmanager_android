package com.qyt.bm.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangqu.lib.banner.BannerView;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.UnScrollGridView;
import com.bumptech.glide.Glide;
import com.qyt.bm.R;
import com.qyt.bm.activity.CallbackTaskActivity;
import com.qyt.bm.activity.CallbackTaskCreateActivity;
import com.qyt.bm.activity.ChoiceCustomerActivity;
import com.qyt.bm.activity.CustomerCreateActivity;
import com.qyt.bm.activity.CustomerSignCreateActivity;
import com.qyt.bm.activity.InstallTaskActivity;
import com.qyt.bm.activity.InstallTaskCreateActivity;
import com.qyt.bm.activity.MaintainTaskActivity;
import com.qyt.bm.activity.RepairTaskActivity;
import com.qyt.bm.activity.RepairTaskCreateActivity;
import com.qyt.bm.adapter.WorkMenuAdapter;
import com.qyt.bm.base.BaseFragment;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.WorkMenuModel;
import com.qyt.bm.response.MessageItem;
import com.qyt.bm.response.WorkCount;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhy on 15/4/26.
 */
public class WorkFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content_banner)
    BannerView contentBanner;
    //待办事项菜单
    @BindView(R.id.work_deals)
    UnScrollGridView workDeals;
    private List<WorkMenuModel> dealModels = new ArrayList<>();
    @BindArray(R.array.work_deal_array)
    String[] workDealsName;
    int[] workDealsRes = new int[]{R.mipmap.device_install, R.mipmap.device_stick, R.mipmap.device_repair, R.mipmap.device_retrieve};
    int[] workDealsResOld = new int[]{R.mipmap.device_install, R.mipmap.device_stick, R.mipmap.device_repair, R.mipmap.device_retrieve
            , R.mipmap.customer_tackpay, R.mipmap.customer_bill, R.mipmap.customer_prepay, R.mipmap.employee_routing, R.mipmap.device_alarm};
    //传感器业务菜单
    @BindView(R.id.work_devices)
    UnScrollGridView workDevices;
    private List<WorkMenuModel> sensorModels = new ArrayList<>();
    @BindArray(R.array.work_sensor_array)
    String[] workSensorName;
    int[] workSensorRes = new int[]{R.mipmap.customer_create, R.mipmap.customer_agreement, R.mipmap.device_install, R.mipmap.device_repair
            , R.mipmap.device_retrieve};
    int[] workSensorResOld = new int[]{R.mipmap.customer_create, R.mipmap.device_install, R.mipmap.device_repair, R.mipmap.customer_tackpay
            , R.mipmap.device_retrieve, R.mipmap.customer_agreement};
    //饲料业务菜单
    @BindView(R.id.work_feed)
    UnScrollGridView workFeed;
    private List<WorkMenuModel> feedModels = new ArrayList<>();
    @BindArray(R.array.work_feed_array)
    String[] workFeedName;
    int[] workFeedRes = new int[]{R.mipmap.customer_feed, R.mipmap.customer_bill, R.mipmap.customer_simple, R.mipmap.employee_routing};
    private WorkMenuAdapter workMenuAdapter;

    private List<View> bannerList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_work, container,
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

    private void addViewListener() {
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkMenuAdapter adapter = (WorkMenuAdapter) parent.getAdapter();
                String name = adapter.getItem(position).name;
                switch (name) {
                    case "安装任务":
                        goToActivity(InstallTaskActivity.class);
                        break;
                    case "维护任务":
                        goToActivity(MaintainTaskActivity.class);
                        break;
                    case "报修任务":
                        goToActivity(RepairTaskActivity.class);
                        break;
                    case "回收任务":
                        goToActivity(CallbackTaskActivity.class);
                        break;
                    case "设备安装":
                        goToActivity(InstallTaskCreateActivity.class);
                        break;
                    case "故障报修":
                        goToActivity(RepairTaskCreateActivity.class);
                        break;
                    case "设备回收":
                        goToActivity(CallbackTaskCreateActivity.class);
                        break;
                    case "开户申请":
                        goToActivity(CustomerCreateActivity.class);
                        break;
                    case "签/续约":
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.INTENT_OBJECT, "sign");
                        goToActivity(ChoiceCustomerActivity.class, bundle);
                        break;
                }
            }
        };
        workDeals.setOnItemClickListener(onItemClickListener);
        workDevices.setOnItemClickListener(onItemClickListener);
        workFeed.setOnItemClickListener(onItemClickListener);
    }

    private void initView() {
        title.setText("庆渔堂");
        int w = AppUtils.getDisplayMetrics(getContext()).widthPixels;
        int h = w * 10 / 30;
        contentBanner.setLayoutParams(new LinearLayout.LayoutParams(w, h));
        getBannerList();
        for (int i = 0; i < workDealsName.length && i < workDealsRes.length; i++) {
            WorkMenuModel model = new WorkMenuModel();
            model.name = workDealsName[i];
            model.resource = workDealsRes[i];
            model.isDone = true;
            dealModels.add(model);
        }
        workMenuAdapter = new WorkMenuAdapter(getContext(), dealModels);
        workDeals.setAdapter(workMenuAdapter);
        for (int i = 0; i < workSensorName.length && i < workSensorRes.length; i++) {
            WorkMenuModel model = new WorkMenuModel();
            model.name = workSensorName[i];
            model.resource = workSensorRes[i];
            model.isDone = true;
            sensorModels.add(model);
        }
        workDevices.setAdapter(new WorkMenuAdapter(getContext(), sensorModels));
        for (int i = 0; i < workFeedName.length && i < workFeedRes.length; i++) {
            WorkMenuModel model = new WorkMenuModel();
            model.name = workFeedName[i];
            model.resource = workFeedRes[i];
            feedModels.add(model);
        }
        workFeed.setAdapter(new WorkMenuAdapter(getContext(), feedModels));
    }

    private void refreshCount() {
        dealModels.clear();
        for (int i = 0; i < workDealsName.length && i < workDealsRes.length; i++) {
            WorkMenuModel model = new WorkMenuModel();
            model.name = workDealsName[i];
            model.resource = workDealsRes[i];
            if (i == 0 || i == 1 || i == 2 || i == 3) {
                model.isDone = true;
            }
            dealModels.add(model);
        }
        workMenuAdapter.notifyDataSetChanged();
    }

    public void getTaskCount() {
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        getData(HttpConfig.TASK_COUNT_LIST.replace("{loginid}", loginId), new ResponseCallBack<ArrayList<WorkCount>>() {
            @Override
            public void onSuccessResponse(ArrayList<WorkCount> d, String msg) {
                if (d != null && d.size() > 0) {
                    dealModels.clear();
                    HashMap<String, String> hashMap = new HashMap<>();
                    for (WorkCount workCount : d) {
                        hashMap.put(workCount.queryType, workCount.total);
                    }
                    for (int i = 0; i < workDealsName.length && i < workDealsRes.length; i++) {
                        WorkMenuModel model = new WorkMenuModel();
                        model.name = workDealsName[i];
                        model.resource = workDealsRes[i];
                        switch (model.name) {
                            case "安装任务":
                                if (hashMap.keySet().contains("install")) {
                                    model.count = hashMap.get("install");
                                }
                                model.isDone = true;
                                break;
                            case "维护任务":
                                if (hashMap.keySet().contains("maintain")) {
                                    model.count = hashMap.get("maintain");
                                }
                                model.isDone = true;
                                break;
                            case "报修任务":
                                if (hashMap.keySet().contains("repair")) {
                                    model.count = hashMap.get("repair");
                                }
                                model.isDone = true;
                                break;
                            case "回收任务":
                                if (hashMap.keySet().contains("recycling")) {
                                    model.count = hashMap.get("recycling");
                                }
                                model.isDone = true;
                                break;
                        }
                        dealModels.add(model);
                    }
                    workMenuAdapter.notifyDataSetChanged();
                } else {
                    refreshCount();
                }
            }

            @Override
            public void onFailResponse(String msg) {
                refreshCount();
            }

            @Override
            public void onVolleyError(int code, String msg) {
                refreshCount();
            }
        });
    }

    public void getBannerList() {
        getData(HttpConfig.BANNER_LIST, new ResponseCallBack<ArrayList<String>>() {
            @Override
            public void onSuccessResponse(ArrayList<String> d, String msg) {
                bannerList.clear();
                if (d != null && d.size() > 0) {
                    for (String banner : d) {
                        ImageView image = new ImageView(getActivity());
                        image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        //设置显示格式
                        image.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(WorkFragment.this).load(banner).into(image);
                        bannerList.add(image);
                    }
                    contentBanner.setViewList(bannerList);
                    contentBanner.startLoop(true);
                }
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
    public void onResume() {
        super.onResume();
        if (contentBanner != null)
            contentBanner.startLoop(true);
        getTaskCount();
    }

    @Override
    public void onDestroyView() {
        if (contentBanner != null)
            contentBanner.startLoop(false);
        super.onDestroyView();
    }

}
