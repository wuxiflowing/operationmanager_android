package com.qyt.bm.activity.device;

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
import com.qyt.bm.R;
import com.qyt.bm.adapter.InfoMapDeviceDetailAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.InfoMap;
import com.qyt.bm.response.AeratorControlItem;
import com.qyt.bm.response.DeviceConfigInfo2;
import com.qyt.bm.response.DeviceControlInfoBean;
import com.qyt.bm.response.DeviceRawData;
import com.qyt.bm.widget.DeviceDataLine;
import com.qyt.bm.widget.LineTypeDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * QY601的设备详情界面
 */
public class DeviceNewDetailActivity extends BaseActivity {

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
    @BindView(R.id.control3_infos)
    UnScrollListView control3Infos;
    @BindView(R.id.control4_infos)
    UnScrollListView control4Infos;
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
    @BindView(R.id.devicedetail_startdate)
    TextView devicedetailStartDate;
    @BindView(R.id.devicedetail_enddate)
    TextView devicedetailEndDate;
    @BindView(R.id.control1_power)
    TextView control1Power;
    @BindView(R.id.control2_power)
    TextView control2Power;
    @BindView(R.id.control3_power)
    TextView control3Power;
    @BindView(R.id.control4_power)
    TextView control4Power;

    private List<InfoMap> sensorItems;
    private List<InfoMap> control1Items;
    private List<InfoMap> control2Items;
    private List<InfoMap> control3Items;
    private List<InfoMap> control4Items;

    private InfoMapDeviceDetailAdapter sensorAdapter;
    private InfoMapDeviceDetailAdapter control1Adapter;
    private InfoMapDeviceDetailAdapter control2Adapter;
    private InfoMapDeviceDetailAdapter control3Adapter;
    private InfoMapDeviceDetailAdapter control4Adapter;

    private String deviceId;
    private String[] lineTypes = new String[]{"time", "temperature", "ph", "dissolvedOxygen"};
    private ArrayList<String> timesValues = new ArrayList<>();
    private ArrayList<String> tmValues = new ArrayList<>();
    private ArrayList<String> phValues = new ArrayList<>();
    private ArrayList<String> oxValues = new ArrayList<>();

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_devicedetail2);
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

        sensorItems = new ArrayList<>();
        control1Items = new ArrayList<>();
        control2Items = new ArrayList<>();
        control3Items = new ArrayList<>();
        control4Items = new ArrayList<>();

        sensorAdapter = new InfoMapDeviceDetailAdapter(this, sensorItems);
        sensorInfos.setAdapter(sensorAdapter);
        control1Adapter = new InfoMapDeviceDetailAdapter(this, control1Items);
        control1Infos.setAdapter(control1Adapter);
        control2Adapter = new InfoMapDeviceDetailAdapter(this, control2Items);
        control2Infos.setAdapter(control2Adapter);

        control3Adapter = new InfoMapDeviceDetailAdapter(this, control3Items);
        control3Infos.setAdapter(control3Adapter);
        control4Adapter = new InfoMapDeviceDetailAdapter(this, control4Items);
        control4Infos.setAdapter(control4Adapter);
    }

    @Override
    protected void requestData() {
        super.requestData();
        getDeviceInfo();
        long start = DateFormatUtil.getString2Date(DateFormatUtil.getCurrentTimeFormat(DateFormatUtil.DATE_FORMAT),
                DateFormatUtil.DATE_FORMAT).getTime();
        getDeviceRawData(start, System.currentTimeMillis());
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
                    default:
                        break;
                }
            }
        });
    }

    public void getDeviceInfo() {
        showLoading();
        getData(HttpConfig.DEVICE_TEST_CONFIG_NEW.replace("{id}", deviceId), new ResponseCallBack<DeviceConfigInfo2>() {
            @Override
            public void onSuccessResponse(DeviceConfigInfo2 d, String msg) {
                dismissLoading();
                if (d != null) {
                    devicedetailId.setText(d.identifier);
                    if (d.workStatus == 0) {
                        devicedetailState.setTextColor(Color.GREEN);
                    } else {
                        devicedetailState.setTextColor(Color.RED);
                    }
                    devicedetailState.setText(Constants.DEVICE_STATE.get(d.workStatus));
                    sensorItems.clear();
                    //选择连接方式
                    sensorItems.add(new InfoMap(getString(R.string.connection_mode_select), d.connectionType == 0 ? getString(R.string.connection_mode_line) : getString(R.string.connection_mode_wireless)));
                    sensorItems.add(new InfoMap("报警线1", d.alertline1 + "mg/L"));
                    sensorItems.add(new InfoMap("报警线2", d.alertline2 + "mg/L"));
                    sensorAdapter.notifyDataSetChanged();
                    control1Items.clear();
                    if (d.deviceControlInfoBeanList != null && d.deviceControlInfoBeanList.size() > 0) {
                        DeviceControlInfoBean item1 = d.deviceControlInfoBeanList.get(0);
                        if (item1.open == 1) {
                            control1Power.setText("开");
                            control1Power.setTextColor(Color.GREEN);
                        } else {
                            control1Power.setText("关");
                            control1Power.setTextColor(Color.RED);
                        }
                        control1Items.add(new InfoMap("溶氧上限", TextUtils.isEmpty(item1.oxyLimitUp) ? "--mg/L" : item1.oxyLimitUp + "mg/L"));
                        control1Items.add(new InfoMap("溶氧下限", TextUtils.isEmpty(item1.oxyLimitDown) ? "--mg/L" : item1.oxyLimitDown + "mg/L"));
                        control1Items.add(new InfoMap("电流上限", TextUtils.isEmpty(item1.electricityUp) ? "--A" : item1.electricityUp + "A"));
                        control1Items.add(new InfoMap("电流下限", TextUtils.isEmpty(item1.electricityDown) ? "--A" : item1.electricityDown + "A"));
                        control1Items.add(new InfoMap("设备状态", item1.auto == 1 ? getString(R.string.text_auto) : getString(R.string.text_manual)));

                        control1Adapter.notifyDataSetInvalidated();
                    }
                    control2Items.clear();
                    if (d.deviceControlInfoBeanList != null && d.deviceControlInfoBeanList.size() > 1) {
                        DeviceControlInfoBean item1 = d.deviceControlInfoBeanList.get(1);
                        if (item1.open == 1) {
                            control1Power.setText("开");
                            control1Power.setTextColor(Color.GREEN);
                        } else {
                            control1Power.setText("关");
                            control1Power.setTextColor(Color.RED);
                        }
                        control2Items.add(new InfoMap("溶氧上限", TextUtils.isEmpty(item1.oxyLimitUp) ? "--mg/L" : item1.oxyLimitUp + "mg/L"));
                        control2Items.add(new InfoMap("溶氧下限", TextUtils.isEmpty(item1.oxyLimitDown) ? "--mg/L" : item1.oxyLimitDown + "mg/L"));
                        control2Items.add(new InfoMap("电流上限", TextUtils.isEmpty(item1.electricityUp) ? "--A" : item1.electricityUp + "A"));
                        control2Items.add(new InfoMap("电流下限", TextUtils.isEmpty(item1.electricityDown) ? "--A" : item1.electricityDown + "A"));
                        control2Items.add(new InfoMap("设备状态", item1.auto == 1 ? getString(R.string.text_auto) : getString(R.string.text_manual)));

                        control1Adapter.notifyDataSetInvalidated();
                    }
                    control3Items.clear();
                    if (d.deviceControlInfoBeanList != null && d.deviceControlInfoBeanList.size() > 2) {
                        DeviceControlInfoBean item1 = d.deviceControlInfoBeanList.get(2);
                        if (item1.open == 1) {
                            control1Power.setText("开");
                            control1Power.setTextColor(Color.GREEN);
                        } else {
                            control1Power.setText("关");
                            control1Power.setTextColor(Color.RED);
                        }
                        control3Items.add(new InfoMap("溶氧上限", TextUtils.isEmpty(item1.oxyLimitUp) ? "--mg/L" : item1.oxyLimitUp + "mg/L"));
                        control3Items.add(new InfoMap("溶氧下限", TextUtils.isEmpty(item1.oxyLimitDown) ? "--mg/L" : item1.oxyLimitDown + "mg/L"));
                        control3Items.add(new InfoMap("电流上限", TextUtils.isEmpty(item1.electricityUp) ? "--A" : item1.electricityUp + "A"));
                        control3Items.add(new InfoMap("电流下限", TextUtils.isEmpty(item1.electricityDown) ? "--A" : item1.electricityDown + "A"));
                        control3Items.add(new InfoMap("设备状态", item1.auto == 1 ? getString(R.string.text_auto) : getString(R.string.text_manual)));

                        control1Adapter.notifyDataSetInvalidated();
                    }
                    control4Items.clear();
                    if (d.deviceControlInfoBeanList != null && d.deviceControlInfoBeanList.size() > 3) {
                        DeviceControlInfoBean item1 = d.deviceControlInfoBeanList.get(3);
                        if (item1.open == 1) {
                            control1Power.setText("开");
                            control1Power.setTextColor(Color.GREEN);
                        } else {
                            control1Power.setText("关");
                            control1Power.setTextColor(Color.RED);
                        }
                        control4Items.add(new InfoMap("溶氧上限", TextUtils.isEmpty(item1.oxyLimitUp) ? "--mg/L" : item1.oxyLimitUp + "mg/L"));
                        control4Items.add(new InfoMap("溶氧下限", TextUtils.isEmpty(item1.oxyLimitDown) ? "--mg/L" : item1.oxyLimitDown + "mg/L"));
                        control4Items.add(new InfoMap("电流上限", TextUtils.isEmpty(item1.electricityUp) ? "--A" : item1.electricityUp + "A"));
                        control4Items.add(new InfoMap("电流下限", TextUtils.isEmpty(item1.electricityDown) ? "--A" : item1.electricityDown + "A"));
                        control4Items.add(new InfoMap("设备状态", item1.auto == 1 ? getString(R.string.text_auto) : getString(R.string.text_manual)));

                        control1Adapter.notifyDataSetInvalidated();
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
                                if (tmValues.size() > 0 && timesValues.size() == tmValues.size()) {
                                    deviceDataLine.refreshViewData(0, timesValues, tmValues);
                                }
                                break;
                            case "PH曲线":
                                if (phValues.size() > 0 && timesValues.size() == phValues.size()) {
                                    deviceDataLine.refreshViewData(1, timesValues, phValues);
                                }
                                break;
                            case "溶氧曲线":
                                if (oxValues.size() > 0 && timesValues.size() == oxValues.size()) {
                                    deviceDataLine.refreshViewData(2, timesValues, oxValues);
                                }
                                break;
                            default:
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
            default:
                break;
        }
    }

    public void getDeviceRawData(long start, long end) {
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
            }

            @Override
            public void onVolleyError(int code, String msg) {
                showToast(msg);
            }
        });
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
