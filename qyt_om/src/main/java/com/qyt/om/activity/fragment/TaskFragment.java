package com.qyt.om.activity.fragment;

import android.app.Activity;
import android.content.Intent;
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
import com.google.zxing.activity.CaptureActivity;
import com.qyt.om.R;
import com.qyt.om.activity.DeviceDetailActivity;
import com.qyt.om.activity.InstallTaskActivity;
import com.qyt.om.activity.MaintainTaskActivity;
import com.qyt.om.activity.RepairTaskActivity;
import com.qyt.om.activity.RetrieveTaskActivity;
import com.qyt.om.adapter.WorkMenuAdapter;
import com.qyt.om.base.BaseFragment;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.WorkMenuModel;
import com.qyt.om.response.WorkCount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhy on 15/4/26.
 */
public class TaskFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content_banner)
    BannerView contentBanner;
    //待办事项菜单
    @BindView(R.id.task_deals)
    UnScrollGridView taskDeals;
    private List<WorkMenuModel> dealModels = new ArrayList<>();
    private WorkMenuAdapter workMenuAdapter;
    @BindArray(R.array.work_deal_array)
    String[] workDealsName;
    int[] workDealsRes = new int[]{R.mipmap.device_install, R.mipmap.device_stick, R.mipmap.device_repair, R.mipmap.device_retrieve
            , R.mipmap.icon_task_scanning};

    private List<View> bannerList = new ArrayList<>();
    private final int REQUEST_QRCODE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_task, container,
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
                        goToActivity(RetrieveTaskActivity.class);
                        break;
                    case "扫一扫":
                        goToActivityForResult(CaptureActivity.class, REQUEST_QRCODE);
                        break;
                }
            }
        };
        taskDeals.setOnItemClickListener(onItemClickListener);
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
            dealModels.add(model);
        }
        workMenuAdapter = new WorkMenuAdapter(getContext(), dealModels);
        taskDeals.setAdapter(workMenuAdapter);
    }

    private void refreshCount() {
        dealModels.clear();
        for (int i = 0; i < workDealsName.length && i < workDealsRes.length; i++) {
            WorkMenuModel model = new WorkMenuModel();
            model.name = workDealsName[i];
            model.resource = workDealsRes[i];
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
                                break;
                            case "维护任务":
                                if (hashMap.keySet().contains("maintain")) {
                                    model.count = hashMap.get("maintain");
                                }
                                break;
                            case "报修任务":
                                if (hashMap.keySet().contains("repair")) {
                                    model.count = hashMap.get("repair");
                                }
                                break;
                            case "回收任务":
                                if (hashMap.keySet().contains("subRecycling")) {
                                    model.count = hashMap.get("subRecycling");
                                }
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
                for (String banner : d) {
                    ImageView image = new ImageView(getActivity());
                    image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    //设置显示格式
                    image.setScaleType(ImageView.ScaleType.FIT_XY);
                    Glide.with(TaskFragment.this).load(banner).into(image);
                    bannerList.add(image);
                }
                contentBanner.setViewList(bannerList);
                contentBanner.startLoop(true);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_QRCODE) {
            String qrcode = data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.INTENT_OBJECT, qrcode);
            goToActivity(DeviceDetailActivity.class, bundle);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
