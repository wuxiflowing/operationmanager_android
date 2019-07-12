package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangqu.lib.listener.ListItemOperaListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.RoundImageView;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qyt.bm.BmApplication;
import com.qyt.bm.R;
import com.qyt.bm.activity.device.FishpondInfoAdapter2;
import com.qyt.bm.adapter.FishpondInfoAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.ContactItem;
import com.qyt.bm.response.CustomerData;
import com.qyt.bm.response.DeviceConfigInfo;
import com.qyt.bm.response.FishPondInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class CustomerHomeActivity extends BaseActivity {

    /**
     * ScrollView上半部分
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /**
     * 头像
     */
    @BindView(R.id.head_iv)
    RoundImageView headIv;
    /**
     * CollapsingToolbarLayout内部显示内容部分
     */
    @BindView(R.id.head_layout)
    RelativeLayout headLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    /**
     * 折叠部分
     */
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    /**
     * ViewPager
     */
    @BindView(R.id.fishpond_list)
    UnScrollListView fishpondList;
    /**
     * 整个布局
     */
    @BindView(R.id.coordinator_Layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fm_credit_score)
    TextView fmCreditScore;
    @BindView(R.id.fm_integral)
    TextView fmIntegral;
    @BindView(R.id.fm_name)
    TextView fmName;
    @BindView(R.id.fm_level)
    TextView fmLevel;
    @BindView(R.id.fm_tel)
    TextViewPlus fmTel;
    @BindView(R.id.fm_address)
    TextViewPlus fmAddress;
    @BindView(R.id.fm_device_count)
    TextView fmDeviceCount;
    @BindView(R.id.fm_fishpond_size)
    TextView fmFishpondSize;
    @BindView(R.id.fm_prepay_sum)
    TextView fmPrepaySum;
    @BindView(R.id.fm_own_sum)
    TextView fmOwnSum;
    @BindView(R.id.right_iv)
    ImageButton setting;
    @BindView(R.id.customer_general)
    LinearLayout customerGeneral;

    private ArrayList<FishPondInfo> fishPondInfos = new ArrayList<>();
    private ArrayList<DeviceConfigInfo> mListData;
    private FishpondInfoAdapter fishpondInfoAdapter;
    private FishpondInfoAdapter2 fishpondInfoAdapter2;
    private ContactItem farmer;
    private CustomerData customerData;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_customerhome);
        farmer = getIntent().getParcelableExtra(Constants.INTENT_OBJECT);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //把title设置到CollapsingToolbarLayout上
        setTitleToCollapsingToolbarLayout();
        fmName.setText(farmer.name);
        fmTel.setText(farmer.mobile);
        fmAddress.setText(farmer.farmerAdd);

        mListData = new ArrayList<>();
        fishpondInfoAdapter2 = new FishpondInfoAdapter2(this, mListData);
        fishpondList.setAdapter(fishpondInfoAdapter2);
    }

    /**
     * 使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，
     * 设置到Toolbar上则不会显示
     */
    private void setTitleToCollapsingToolbarLayout() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -headLayout.getHeight() / 2) {
                    collapsingToolbarLayout.setTitle(farmer.name);
                    //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
                    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.color_white));
                } else {
                    collapsingToolbarLayout.setTitle("");
                }
            }
        });
    }


    @Override
    protected void addViewListener() {
        super.addViewListener();
        fishpondInfoAdapter2.setOperaListener(new ListItemOperaListener() {
            @Override
            public void onItemOpera(String tag, int position, Object value) {
                switch (tag) {
                    case "fishpond":
                        Bundle bundle = new Bundle();
                        FishPondInfo fishPondInfo = null;
                        for (FishPondInfo info : fishPondInfos) {
                            if (info.pondId.equals((String) value)) {
                                fishPondInfo = info;
                                break;
                            }
                        }
                        bundle.putParcelable(Constants.INTENT_OBJECT, fishPondInfo);
                        goToActivity(FishpondInfoActivity.class, bundle);
                        break;
                    case "device":
                        Bundle bundle1 = new Bundle();
                        bundle1.putString(Constants.INTENT_OBJECT, (String) value);
                        goToActivity(DeviceDetailActivity.class, bundle1);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void requestData() {
        super.requestData();
        getData(HttpConfig.GET_CUSTOMER_DATA.replace("{customerId}", farmer.farmerId), new ResponseCallBack<CustomerData>() {
            @Override
            public void onSuccessResponse(CustomerData d, String msg) {
                try {
                    customerData = d;
                    if (!TextUtils.isEmpty(d.picture)) {
                        Glide.with(mContext).load(d.picture).apply(new RequestOptions().error(R.mipmap.default_header)).into(headIv);
                    }
                    fmAddress.setText(d.region + d.homeAddress);
                    fmLevel.setText("Lv." + (TextUtils.isEmpty(d.customerLevel) ? "1" : d.customerLevel));
                    fmCreditScore.setText(TextUtils.isEmpty(d.credit) ? "0" : d.credit);
                    fmIntegral.setText(TextUtils.isEmpty(d.integral) ? "0" : d.integral);
                    fmDeviceCount.setText(TextUtils.isEmpty(d.equipment) ? "0" : AppUtils.removeZeroAndDot(d.equipment));
                    fmFishpondSize.setText(TextUtils.isEmpty(d.totalArea) ? "0" : d.totalArea);
                    fmPrepaySum.setText(TextUtils.isEmpty(d.advanceCapital) ? "0" : d.advanceCapital);
                    fmOwnSum.setText(TextUtils.isEmpty(d.arrears) ? "0" : d.arrears);
                    if ("contract".equals(d.farmerType)) {
                        getFishPonds();
                    } else {
                        customerGeneral.setVisibility(View.VISIBLE);
                        setting.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailResponse(String msg) {
                showToast(msg);
            }

            @Override
            public void onVolleyError(int code, String msg) {
                showToast(msg);
            }
        });
    }

    @OnClick({R.id.right_iv, R.id.head_iv, R.id.customer_base, R.id.customer_score, R.id.customer_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_iv:
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable(Constants.INTENT_OBJECT, farmer);
                goToActivity(CustomerMenuActivity.class, bundle1);
                break;
            case R.id.head_iv:
            case R.id.customer_base:
            case R.id.customer_score:
                if (setting.getVisibility() == View.VISIBLE) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.INTENT_OBJECT, farmer.farmerId);
                    goToActivity(CustomerBaseInfoActivity.class, bundle);
                }
                break;
            case R.id.customer_update:
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.INTENT_OBJECT, customerData);
                goToActivity(CustomerCreateActivity.class, bundle);
                BmApplication.getInstance().addToSteamActivity(this);
                break;
        }
    }

    private void getFishPonds() {
        /*获取鱼塘列表*/
        getData(HttpConfig.GET_CUSTOMER_PONDS.replace("{customerId}", farmer.farmerId), new ResponseCallBack<ArrayList<FishPondInfo>>() {
            @Override
            public void onSuccessResponse(ArrayList<FishPondInfo> d, String msg) {
                fishPondInfos.clear();
                if (d != null && d.size() > 0) {
                    fishPondInfos.addAll(d);

                    mListData.clear();
                    for (FishPondInfo info : d) {
                        for (DeviceConfigInfo device : info.childDeviceList) {
                            device.pondId = info.pondId;
                            device.pondName = info.name;
                            mListData.add(device);
                        }
                    }
                    fishpondInfoAdapter2.notifyDataSetChanged();
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
}
