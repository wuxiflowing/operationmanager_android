package com.qyt.om.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.bangqu.lib.utils.AppUtils;
import com.qyt.om.R;
import com.qyt.om.adapter.PoiResultAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.model.LocPoi;
import com.qyt.om.utils.BaiduLocManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LocationActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.location_mapview)
    MapView mMapView;
    @BindView(R.id.map_location)
    ImageView mapLocation;
    @BindView(R.id.location_points)
    ListView locationPoints;
    @BindView(R.id.search_key)
    EditText searchKey;
    @BindView(R.id.cancel_search)
    ImageView cancelSearch;
    @BindView(R.id.auto_sug)
    ListView autoSug;

    private BaiduMap mBaiduMap;
    private GeoCoder mSearch;
    private PoiResultAdapter poiResultAdapter;
    private List<PoiInfo> poiInfos = new ArrayList<>();
    private SuggestionSearch mSuggestionSearch;
    private ArrayAdapter<String> autoAdapter;
    private ArrayList<String> autoString = new ArrayList<>();
    private ArrayList<LatLng> autoLatlngs = new ArrayList<>();
    private String poiCity = "湖州";

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_location);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("安装地址");
        rightTv.setText("确定");
        rightTv.setVisibility(View.VISIBLE);
        autoAdapter = new ArrayAdapter(this, R.layout.item_autokey, autoString);
        autoSug.setAdapter(autoAdapter);
        poiResultAdapter = new PoiResultAdapter(this, poiInfos);
        locationPoints.setAdapter(poiResultAdapter);
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(listener);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(suglistener);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        refreshLocation();
    }

    OnGetSuggestionResultListener suglistener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null) {
                showToast("没有检索到结果");
                return;
                //未找到相关结果
            }
            autoString.clear();
            //获取在线建议检索结果
            for (SuggestionResult.SuggestionInfo sug : res.getAllSuggestions()) {
                autoLatlngs.add(sug.pt);
                autoString.add(sug.key);
            }
            if (autoString.size() > 0) {
                autoAdapter.notifyDataSetChanged();
                autoSug.setVisibility(View.VISIBLE);
            } else {
                autoSug.setVisibility(View.GONE);
            }
        }
    };

    private void refreshLocation() {
        BaiduLocManager.getInstance(getApplicationContext()).startLocation(new BaiduLocManager.OnLocationComplete() {
            @Override
            public void onLocationComplete(BDLocation location) {
                if (location != null) {
                    poiCity = location.getCity();
                    //移动地图中心到定位点
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory
                            .newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(new LatLng(location.getLatitude(), location.getLongitude())));
                }
            }
        });
    }

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
            }
            //获取地理编码结果
        }

        @Override

        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                showToast("没有检索到结果");
                return;
            }
            poiInfos.clear();
            //获取反向地理编码结果
            List<PoiInfo> list = result.getPoiList();
            if (list != null) {
                poiInfos.addAll(list);
            }
            poiResultAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void addViewListener() {
        super.addViewListener();
        searchKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    AppUtils.hideSoftInput(mContext, searchKey);
                    if (!TextUtils.isEmpty(v.getText())) {
                        if (TextUtils.isEmpty(poiCity))
                            poiCity = "湖州";
                        autoSug.setVisibility(View.GONE);
                        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(v.getText().toString()).city(poiCity));
                    }
                }
                return false;
            }
        });
        autoSug.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppUtils.hideSoftInput(mContext, searchKey);
                searchKey.setText(autoString.get(position));
                autoSug.setVisibility(View.GONE);
                mBaiduMap.setMapStatus(MapStatusUpdateFactory
                        .newLatLng(autoLatlngs.get(position)));
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(autoLatlngs.get(position)));
            }
        });
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(mapStatus.target));
            }
        });
    }

    @OnClick({R.id.right_tv, R.id.map_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_tv:
                if (poiInfos.size() <= 0) {
                    showToast("未获取到位置信息");
                    onBackPressed();
                    return;
                }
                PoiInfo poiInfo = poiResultAdapter.getCheckPoi();
                LocPoi locPoi = new LocPoi();
                locPoi.address = poiInfo.address + poiInfo.name;
                locPoi.lat = poiInfo.location.latitude;
                locPoi.lng = poiInfo.location.longitude;
                Intent intent = new Intent();
                intent.putExtra(Constants.INTENT_OBJECT, locPoi);
                setResult(RESULT_OK, intent);
                onBackPressed();
                break;
            case R.id.map_location:
                refreshLocation();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mSuggestionSearch.destroy();
        mSearch.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}

