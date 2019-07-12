package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.UnScrollListView;
import com.bangqu.photos.GalleryActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qyt.bm.R;
import com.qyt.bm.adapter.InfoMapAdapter;
import com.qyt.bm.adapter.OrderItemAdapter;
import com.qyt.bm.adapter.PhotosAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.InfoMap;
import com.qyt.bm.response.CustomerData;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindArray;
import butterknife.BindView;

public class CustomerBaseInfoActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.customer_baselist)
    UnScrollListView customerBaselist;
    @BindView(R.id.base_form_pictures)
    RecyclerView baseFormPictures;
    @BindArray(R.array.customer_base_array)
    String[] baseLabels;

    private ArrayList<InfoMap> mapItems = new ArrayList<>();
    private InfoMapAdapter infoMapAdapter;
    private PhotosAdapter photosAdapter;
    private ArrayList<String> photos = new ArrayList<>();

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_customerbaseinfo);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("基础信息");
        infoMapAdapter = new InfoMapAdapter(this, mapItems);
        customerBaselist.setAdapter(infoMapAdapter);
        photosAdapter = new PhotosAdapter(this, photos);
        baseFormPictures.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        baseFormPictures.setAdapter(photosAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        photosAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("path", photos);
                bundle.putBoolean("show", true);
                goToActivity(GalleryActivity.class, bundle);
            }

            @Override
            public void onItemOpera(String tag, int position, String value) {

            }
        });
    }

    @Override
    protected void requestData() {
        super.requestData();
        String farmerId = getIntent().getStringExtra(Constants.INTENT_OBJECT);
        getData(HttpConfig.GET_CUSTOMER_DATA.replace("{customerId}", farmerId), new ResponseCallBack<CustomerData>() {
            @Override
            public void onSuccessResponse(CustomerData d, String msg) {
                mapItems.add(new InfoMap("农户姓名", d.name));
                mapItems.add(new InfoMap("信用等级", d.customerLevel));
                mapItems.add(new InfoMap("积分", d.integral));
                mapItems.add(new InfoMap("预付款", d.advanceCapital));
                mapItems.add(new InfoMap("欠款", d.arrears));
                mapItems.add(new InfoMap("欠款额度", d.arrearsQuota));
                mapItems.add(new InfoMap("鱼塘数量", d.pondNumber));
                mapItems.add(new InfoMap("总设备", d.equipment));
                mapItems.add(new InfoMap("面积", d.totalArea));
                mapItems.add(new InfoMap("联系方式", d.contactInfo));
                mapItems.add(new InfoMap("出生日期", d.birthday));
                mapItems.add(new InfoMap("性别", d.sex));
                mapItems.add(new InfoMap("身份证号", d.idNumber));
                mapItems.add(new InfoMap("开户时间", d.openAccountDate));
                infoMapAdapter.notifyDataSetChanged();
                if (!TextUtils.isEmpty(d.picture)) {
                    photos.addAll(Arrays.asList(d.picture.split(",")));
                }
                if (!TextUtils.isEmpty(d.registerPicture)) {
                    photos.addAll(Arrays.asList(d.registerPicture.split(",")));
                }
                if (!TextUtils.isEmpty(d.idPicture)) {
                    photos.addAll(Arrays.asList(d.idPicture.split(",")));
                }
                photosAdapter.notifyDataSetChanged();
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

}
