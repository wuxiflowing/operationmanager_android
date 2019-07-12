package com.qyt.om.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.utils.DateFormatUtil;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.UnScrollListView;
import com.bigkoo.pickerview.TimePickerView;
import com.qyt.om.R;
import com.qyt.om.adapter.InfoMapAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.InfoMap;
import com.qyt.om.response.AeratorControlItem;
import com.qyt.om.response.DeviceConfigInfo;
import com.qyt.om.response.DeviceRawData;
import com.qyt.om.response.RawDeviceInfo;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.widget.DeviceDataLine;
import com.qyt.om.widget.LineTypeDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.sensor_infos)
    UnScrollListView sensorInfos;
    @BindView(R.id.control1_infos)
    UnScrollListView control1Infos;
    @BindView(R.id.control2_infos)
    UnScrollListView control2Infos;
    @BindView(R.id.devicedetail_startdate)
    TextView devicedetailStartDate;
    @BindView(R.id.devicedetail_enddate)
    TextView devicedetailEndDate;
    @BindView(R.id.devicedetail_linetype)
    TextView devicedetailLinetype;
    @BindView(R.id.devicedetail_fishpond)
    TextView devicedetailFishpond;
    @BindView(R.id.devicedetail_id)
    TextView devicedetailId;
    @BindView(R.id.devicedetail_state)
    TextView devicedetailState;
    @BindView(R.id.device_data_view)
    DeviceDataLine deviceDataLine;
    @BindView(R.id.devicetime_rg)
    RadioGroup devicetimeRg;
    @BindView(R.id.devicedetail_today)
    RadioButton devicedetailToday;
    @BindView(R.id.devicedetail_five)
    RadioButton devicedetailFive;
    @BindView(R.id.control1_power)
    TextView control1Power;
    @BindView(R.id.control2_power)
    TextView control2Power;

    private List<InfoMap> sensorItems = new ArrayList<>();
    private InfoMapAdapter sensorAdapter;
    private List<InfoMap> control1Items = new ArrayList<>();
    private InfoMapAdapter control1Adapter;
    private List<InfoMap> control2Items = new ArrayList<>();
    private InfoMapAdapter control2Adapter;

    private String deviceId;
    private String[] lineTypes = new String[]{"time", "temperature", "ph", "dissolvedOxygen"};
    private ArrayList<String> timesValues = new ArrayList<>();
    private ArrayList<String> tmValues = new ArrayList<>();
    private ArrayList<String> phValues = new ArrayList<>();
    private ArrayList<String> oxValues = new ArrayList<>();

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_devicedetail);
        deviceId = getIntent().getStringExtra(Constants.INTENT_OBJECT);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("设备详情");
        devicedetailStartDate.setText(DateFormatUtil.getCurrentTimeFormat(DateFormatUtil.DATE_FORMAT));
        devicedetailEndDate.setText(DateFormatUtil.getCurrentTimeFormat(DateFormatUtil.DATE_FORMAT));
        sensorAdapter = new InfoMapAdapter(this, sensorItems);
        sensorInfos.setAdapter(sensorAdapter);
        control1Adapter = new InfoMapAdapter(this, control1Items);
        control1Infos.setAdapter(control1Adapter);
        control2Adapter = new InfoMapAdapter(this, control2Items);
        control2Infos.setAdapter(control2Adapter);
    }

    @Override
    protected void requestData() {
        super.requestData();
        getDeviceInfo();
        long start = DateFormatUtil.getString2Date(DateFormatUtil.getCurrentTimeFormat(DateFormatUtil.DATE_FORMAT),
                DateFormatUtil.DATE_FORMAT).getTime();
        getDeviceRawData(start, System.currentTimeMillis());
    }

    public void getDeviceInfo() {
        showLoading();
        getData(HttpConfig.DEVICE_TEST_CONFIG.replace("{id}", deviceId), new ResponseCallBack<DeviceConfigInfo>() {
            @Override
            public void onSuccessResponse(DeviceConfigInfo d, String msg) {
                dismissLoading();
                if (d != null) {
                    devicedetailId.setText(d.identifier + "");
                    if (d != null && d.workStatus == 0) {
                        devicedetailState.setTextColor(Color.GREEN);
                    } else {
                        devicedetailState.setTextColor(Color.RED);
                    }
                    devicedetailState.setText(Constants.DEVICE_STATE.get(d.workStatus));
                    sensorItems.clear();
                    sensorItems.add(new InfoMap("上限1", d.oxyLimitUp + "mg/L"));
                    sensorItems.add(new InfoMap("下限1", d.oxyLimitDownOne + "mg/L"));
                    sensorItems.add(new InfoMap("下限2", d.oxyLimitDownTwo + "mg/L"));
                    sensorItems.add(new InfoMap("报警线1", d.alertlineOne + "mg/L"));
                    sensorItems.add(new InfoMap("报警线2", d.alertlineTwo + "mg/L"));
                    sensorItems.add(new InfoMap("控制器状态", d.automatic ? "自动" : "手动"));
                    sensorAdapter.notifyDataSetChanged();
                    control1Items.clear();
                    if (d.aeratorControlList != null && d.aeratorControlList.size() > 0) {
                        AeratorControlItem item1 = d.aeratorControlList.get(0);
                        if (item1.open) {
                            control1Power.setText("开");
                            control1Power.setTextColor(Color.GREEN);
                        } else {
                            control1Power.setText("关");
                            control1Power.setTextColor(Color.RED);
                        }
                        control1Items.add(new InfoMap("设备类型", item1.ammeterType));
                        control1Items.add(new InfoMap("设备名称", item1.name));
                        if ("true".equals(item1.hasAmmeter)) {
                            control1Items.add(new InfoMap("是否有电流表", "有"));
                            control1Items.add(new InfoMap("电流表编号", item1.ammeterId));
                            control1Items.add(new InfoMap("功率", item1.power + "Kw"));
                            control1Items.add(new InfoMap("电压上限", item1.voltageUp + "V"));
                            control1Items.add(new InfoMap("电压下限", item1.voltageDown + "V"));
                            control1Items.add(new InfoMap("电流上限", item1.electricCurrentUp + "A"));
                            control1Items.add(new InfoMap("电流下限", item1.electricCurrentDown + "A"));
                        } else {
                            control1Items.add(new InfoMap("是否有电流表", "无"));
                        }
                        control1Adapter.notifyDataSetInvalidated();
                    }
                    control2Items.clear();
                    if (d.aeratorControlList != null && d.aeratorControlList.size() > 1) {
                        AeratorControlItem item2 = d.aeratorControlList.get(1);
                        if (item2.open) {
                            control2Power.setText("开");
                            control2Power.setTextColor(Color.GREEN);
                        } else {
                            control2Power.setText("关");
                            control2Power.setTextColor(Color.RED);
                        }
                        control2Items.add(new InfoMap("设备类型", item2.ammeterType));
                        control2Items.add(new InfoMap("设备名称", item2.name));
                        if ("true".equals(item2.hasAmmeter)) {
                            control2Items.add(new InfoMap("是否有电流表", "有"));
                            control2Items.add(new InfoMap("电流表编号", item2.ammeterId));
                            control2Items.add(new InfoMap("功率", item2.power + "Kw"));
                            control2Items.add(new InfoMap("电压上限", item2.voltageUp + "V"));
                            control2Items.add(new InfoMap("电压下限", item2.voltageDown + "V"));
                            control2Items.add(new InfoMap("电流上限", item2.electricCurrentUp + "A"));
                            control2Items.add(new InfoMap("电流下限", item2.electricCurrentDown + "A"));
                        } else {
                            control2Items.add(new InfoMap("是否有电流表", "无"));
                        }
                        control2Adapter.notifyDataSetInvalidated();
                    }
                } else {
                    showToast(msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                showToast(msg);
                dismissLoading();
            }

            @Override
            public void onVolleyError(int code, String msg) {
                showToast(msg);
                dismissLoading();
            }
        });
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        devicetimeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.devicedetail_today:
                        devicedetailStartDate.setText(DateFormatUtil.getCurrentTimeFormat(DateFormatUtil.DATE_FORMAT));
                        devicedetailEndDate.setText(DateFormatUtil.getCurrentTimeFormat(DateFormatUtil.DATE_FORMAT));
                        long start = DateFormatUtil.getString2Date(DateFormatUtil.getCurrentTimeFormat(DateFormatUtil.DATE_FORMAT),
                                DateFormatUtil.DATE_FORMAT).getTime();
                        getDeviceRawData(start, System.currentTimeMillis());
                        break;
                    case R.id.devicedetail_five:
                        devicedetailEndDate.setText(DateFormatUtil.getCurrentTimeFormat(DateFormatUtil.DATE_FORMAT));
                        long five = System.currentTimeMillis() - 5 * 24 * 3600 * 1000;
                        String startDate = DateFormatUtil.getDate2Str(new Date(five), DateFormatUtil.DATE_FORMAT);
                        devicedetailStartDate.setText(startDate);
                        long startlong = DateFormatUtil.getString2Date(startDate, DateFormatUtil.DATE_FORMAT).getTime();
                        getDeviceRawData(startlong, System.currentTimeMillis());
                        break;
                }
            }
        });
    }

    public void getDeviceRawData(long start, long end) {
        showLoading();
        String params = "?startTime=" + start + "&endTime=" + end;
        getData(HttpConfig.GET_DEVICE_RAWDATA.replace("{id}", deviceId) + params, new ResponseCallBack<DeviceRawData>() {
            @Override
            public void onSuccessResponse(DeviceRawData info, String msg) {
                dismissLoading();
                timesValues.clear();
                tmValues.clear();
                phValues.clear();
                oxValues.clear();
                if (info != null && info.columns != null && info.values != null && info.columns.size() > 0 && info.values.size() > 0) {
                    int tm = info.columns.indexOf(lineTypes[0]);
                    int it = info.columns.indexOf(lineTypes[1]);
                    int ip = info.columns.indexOf(lineTypes[2]);
                    int io = info.columns.indexOf(lineTypes[3]);
                    for (ArrayList<String> vs : info.values) {
                        timesValues.add(vs.get(tm));
                        tmValues.add(vs.get(it));
                        phValues.add(vs.get(ip));
                        oxValues.add(vs.get(io));
                    }
                    switch (devicedetailLinetype.getText().toString()) {
                        case "温度曲线":
                            deviceDataLine.refreshViewData(0, timesValues, tmValues);
                            break;
                        case "PH曲线":
                            deviceDataLine.refreshViewData(1, timesValues, phValues);
                            break;
                        case "溶氧曲线":
                            deviceDataLine.refreshViewData(2, timesValues, oxValues);
                            break;
                    }
                }
            }

            @Override
            public void onFailResponse(String msg) {
                showToast(msg);
                dismissLoading();
            }

            @Override
            public void onVolleyError(int code, String msg) {
                showToast(msg);
                dismissLoading();
            }
        });
    }

    @OnClick({R.id.devicedetail_linetype, R.id.devicedetail_enddate, R.id.devicedetail_startdate, R.id.devicedetail_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.devicedetail_linetype:
                new LineTypeDialog(this, new DialogConfirmListener() {
                    @Override
                    public void onDialogConfirm(boolean result, Object value) {
                        String type = String.valueOf(value);
                        if (type.equals(devicedetailLinetype.getText().toString())) {
                            return;
                        }
                        devicedetailLinetype.setText(type);
                        switch (type) {
                            case "温度曲线":
                                if (tmValues.size() > 0 && timesValues.size() == tmValues.size())
                                    deviceDataLine.refreshViewData(0, timesValues, tmValues);
                                break;
                            case "PH曲线":
                                if (phValues.size() > 0 && timesValues.size() == phValues.size())
                                    deviceDataLine.refreshViewData(1, timesValues, phValues);
                                break;
                            case "溶氧曲线":
                                if (oxValues.size() > 0 && timesValues.size() == oxValues.size())
                                    deviceDataLine.refreshViewData(2, timesValues, oxValues);
                                break;
                        }
                    }
                }).show();
                break;
            case R.id.devicedetail_startdate:
                showPickDate(false, devicedetailStartDate);
                break;
            case R.id.devicedetail_enddate:
                showPickDate(true, devicedetailEndDate);
                break;
            case R.id.devicedetail_search:
                long start = DateFormatUtil.getString2Date(devicedetailStartDate.getText().toString(),
                        DateFormatUtil.DATE_FORMAT).getTime();
                long end = DateFormatUtil.getString2Date(devicedetailEndDate.getText().toString(),
                        DateFormatUtil.DATE_FORMAT).getTime() + 24 * 3599 * 1000;
                devicedetailToday.setChecked(false);
                devicedetailFive.setChecked(false);
                getDeviceRawData(start, end);
                break;

        }
    }

    private void showPickDate(final boolean isEnd, final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(textView.getText())) {
            calendar.setTime(DateFormatUtil.getString2Date(textView.getText().toString(), DateFormatUtil.DATE_FORMAT));
        }
        //时间选择器
        TimePickerView mTimePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (isEnd && date.getTime() > System.currentTimeMillis()) {
                    showToast("结束时间大于当前时间");
                    textView.setText(DateFormatUtil.getCurrentTimeFormat(DateFormatUtil.DATE_FORMAT));
                } else {
                    String birth = DateFormatUtil.getDate2Str(date, DateFormatUtil.DATE_FORMAT);
                    textView.setText(birth);
                }
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
        mTimePickerView.setDate(calendar);
        mTimePickerView.show();
    }
}
