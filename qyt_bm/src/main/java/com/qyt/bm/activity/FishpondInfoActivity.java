package com.qyt.bm.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bangqu.lib.widget.UnScrollListView;
import com.qyt.bm.R;
import com.qyt.bm.adapter.InfoMapAdapter;
import com.qyt.bm.adapter.PhotosAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.model.InfoMap;
import com.qyt.bm.response.FishPondInfo;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;

public class FishpondInfoActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.fishpond_infolist)
    UnScrollListView infolist;
    @BindArray(R.array.customer_fishpond_array)
    String[] baseLabels;

    private ArrayList<InfoMap> mapItems = new ArrayList<>();
    private InfoMapAdapter infoMapAdapter;
    private FishPondInfo fishPondInfo;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_fishpondinfo);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("鱼塘信息");
        fishPondInfo = getIntent().getParcelableExtra(Constants.INTENT_OBJECT);
        mapItems.add(new InfoMap("鱼塘名称", fishPondInfo.name));
        mapItems.add(new InfoMap("鱼塘面积", fishPondInfo.area + ""));
        mapItems.add(new InfoMap("鱼种", fishPondInfo.fishVariety));
        if (fishPondInfo.childDeviceList != null && fishPondInfo.childDeviceList.size() > 0) {
            mapItems.add(new InfoMap("鱼塘设备", fishPondInfo.childDeviceList.get(0).name));
        }
        mapItems.add(new InfoMap("鱼塘地址", fishPondInfo.pondAddress));
        mapItems.add(new InfoMap("投放鱼苗时间", fishPondInfo.putInDate));
        mapItems.add(new InfoMap("预计卖鱼时间", fishPondInfo.reckonSaleDate));
        infoMapAdapter = new InfoMapAdapter(this, mapItems);
        infolist.setAdapter(infoMapAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();

    }

    @Override
    protected void requestData() {
        super.requestData();
    }

}
