package com.qyt.bm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.LoadingView;
import com.qyt.bm.R;
import com.qyt.bm.adapter.ChoiceAreaAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.response.AreaItem;
import com.qyt.bm.utils.LogInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoiceAreaActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.choice_prov)
    TextView choiceProv;
    @BindView(R.id.choice_city)
    TextView choiceCity;
    @BindView(R.id.choice_area)
    TextView choiceArea;
    @BindView(R.id.choice_county)
    TextView choiceCounty;
    @BindView(R.id.slip_list_view)
    ListView slipListView;
    @BindView(R.id.slip_loading_view)
    LoadingView slipLoadingView;

    private List<AreaItem> areaItems = new ArrayList<>();
    private ChoiceAreaAdapter choiceAreaAdapter;
    private int index = 0;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_choicearea);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("行政区域");
        choiceAreaAdapter = new ChoiceAreaAdapter(this, areaItems);
        slipListView.setAdapter(choiceAreaAdapter);
        choiceProv.setText("请选择");
        choiceProv.setTextColor(Color.parseColor("#4375C7"));
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        slipListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AreaItem areaItem = areaItems.get(position);
                switch (index) {
                    case 0:
                        choiceProv.setText(areaItem.name);
                        choiceProv.setTag(areaItem.id);
                        getAreaList(areaItem.id);
                        index = 1;
                        break;
                    case 1:
                        choiceCity.setText(areaItem.name);
                        choiceCity.setTag(areaItem.id);
                        getAreaList(areaItem.id);
                        index = 2;
                        break;
                    case 2:
                        choiceArea.setText(areaItem.name);
                        choiceArea.setTag(areaItem.id);
                        getAreaList(areaItem.id);
                        index = 3;
                        break;
                    case 3:
                        choiceCounty.setText(areaItem.name);
                        index = 4;
                        StringBuffer sb = new StringBuffer();
                        sb.append(choiceProv.getText());
                        sb.append(choiceCity.getText());
                        sb.append(choiceArea.getText());
                        sb.append(areaItem.name);
                        AreaItem area = new AreaItem();
                        area.id = areaItem.id;
                        area.name = sb.toString();
                        Intent intent = new Intent();
                        intent.putExtra(Constants.INTENT_OBJECT, area);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
                changeTitle();
            }
        });
    }

    private void changeTitle() {
        switch (index) {
            case 1:
                choiceCity.setText("请选择");
                choiceProv.setTextColor(Color.parseColor("#212121"));
                choiceCity.setTextColor(Color.parseColor("#4375C7"));
                choiceArea.setText("");
                choiceCounty.setText("");
                break;
            case 2:
                choiceArea.setText("请选择");
                choiceCity.setTextColor(Color.parseColor("#212121"));
                choiceArea.setTextColor(Color.parseColor("#4375C7"));
                choiceCounty.setText("");
                break;
            case 3:
                choiceCounty.setText("请选择");
                choiceArea.setTextColor(Color.parseColor("#212121"));
                choiceCounty.setTextColor(Color.parseColor("#4375C7"));
                break;
        }
    }

    @Override
    protected void requestData() {
        super.requestData();
        getAreaList("PROV");
    }

    private void getAreaList(String areaId) {
        getData(HttpConfig.CITY_AREA_LIST.replace("{areaTypeID}", areaId), new ResponseCallBack<ArrayList<AreaItem>>() {
            @Override
            public void onSuccessResponse(ArrayList<AreaItem> d, String msg) {
                areaItems.clear();
                if (d != null && d.size() > 0) {
                    areaItems.addAll(d);
                    slipLoadingView.setLoadingState(LoadingView.SHOW_DATA);
                } else {
                    slipLoadingView.setLoadingState(LoadingView.NO_DATA);
                }
                choiceAreaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailResponse(String msg) {
                showToast(msg);
                areaItems.clear();
                choiceAreaAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
            }

            @Override
            public void onVolleyError(int code, String msg) {
                showToast(msg);
                areaItems.clear();
                choiceAreaAdapter.notifyDataSetChanged();
                slipLoadingView.setLoadingState(LoadingView.NET_ERROR);
            }
        });
    }

    @OnClick({R.id.choice_prov, R.id.choice_city, R.id.choice_area})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choice_prov:
                index = 0;
                choiceProv.setTextColor(Color.parseColor("#4375C7"));
                choiceCity.setTextColor(Color.parseColor("#212121"));
                choiceArea.setTextColor(Color.parseColor("#212121"));
                choiceCounty.setTextColor(Color.parseColor("#212121"));
                getAreaList("PROV");
                break;
            case R.id.choice_city:
                index = 1;
                choiceCity.setTextColor(Color.parseColor("#4375C7"));
                choiceProv.setTextColor(Color.parseColor("#212121"));
                choiceArea.setTextColor(Color.parseColor("#212121"));
                choiceCounty.setTextColor(Color.parseColor("#212121"));
                getAreaList(String.valueOf(choiceProv.getTag()));
                break;
            case R.id.choice_area:
                index = 2;
                choiceArea.setTextColor(Color.parseColor("#4375C7"));
                choiceCity.setTextColor(Color.parseColor("#212121"));
                choiceProv.setTextColor(Color.parseColor("#212121"));
                choiceCounty.setTextColor(Color.parseColor("#212121"));
                getAreaList(String.valueOf(choiceArea.getTag()));
                break;
        }
    }
}
