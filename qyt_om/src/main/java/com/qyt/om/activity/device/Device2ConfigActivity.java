package com.qyt.om.activity.device;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.volley.ResponseCallBackRaw;
import com.bangqu.lib.widget.TextViewPlus;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.activity.CaptureActivity;
import com.qyt.om.R;
import com.qyt.om.activity.LocationActivity;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.FishPondInfo;
import com.qyt.om.model.LocPoi;
import com.qyt.om.request.DeviceConfigPut2;
import com.qyt.om.request.DeviceControl;
import com.qyt.om.response.ChildDeviceListBean;
import com.qyt.om.response.DeviceControlInfoBean;
import com.qyt.om.response.LinkManBean;
import com.qyt.om.response.PondLinkMan;
import com.qyt.om.utils.BaiduLocManager;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.utils.PointLengthFilter;
import com.qyt.om.widget.EditContactsDialog;
import com.qyt.om.widget.FishPondDialog;
import com.qyt.om.widget.ListLinkManPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class Device2ConfigActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.right_iv)
    ImageButton rightIv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.device_scanning)
    ImageButton deviceScanning;
    @BindView(R.id.config_device_id)
    EditText configDeviceId;
    @BindView(R.id.config_device_type)
    TextViewPlus configDeviceType;
    @BindView(R.id.config_fishpond)
    TextViewPlus configFishpond;
    @BindView(R.id.pal)
    TextView pal;
    @BindView(R.id.install_location)
    ImageView installLocation;
    @BindView(R.id.install_pond_address)
    EditText installPondAddress;
    @BindView(R.id.tv_add_contacts)
    TextView tvAddContacts;
    @BindView(R.id.device_connect)
    Button deviceConnect;
    @BindView(R.id.device_reset)
    Button deviceReset;
    @BindView(R.id.tv_connect)
    TextView tvConnect;
    @BindView(R.id.rb_connect_line)
    RadioButton rbConnectLine;
    @BindView(R.id.rb_connect_wiredless)
    RadioButton rbConnectWiredless;
    @BindView(R.id.rg_connect)
    RadioGroup rgConnect;
    @BindView(R.id.btn_device_pair)
    Button btnDevicePair;
    @BindView(R.id.config_alarm_line1)
    EditText configAlarmLine1;
    @BindView(R.id.config_alarm_line2)
    EditText configAlarmLine2;
    @BindView(R.id.config_control_state1)
    TextView tvControl1State;
    @BindView(R.id.config_voltage_up1)
    EditText etOxygenUp1;
    @BindView(R.id.config_voltage_down1)
    EditText etOxygenUpDown1;
    @BindView(R.id.current_up1)
    EditText etCurrentUp1;
    @BindView(R.id.config_current_down1)
    EditText etCurrentDown1;
    @BindView(R.id.btn_control1_open)
    Button btnControl1Open;
    @BindView(R.id.btn_control1_close)
    Button btnControl1Close;
    @BindView(R.id.control1_ainfo)
    LinearLayout control1Info;
    @BindView(R.id.config_control_state2)
    TextView tvControl2State;
    @BindView(R.id.config_voltage_up2)
    EditText configVoltageUp2;
    @BindView(R.id.config_voltage_down2)
    EditText configVoltageDown2;
    @BindView(R.id.current_up2)
    EditText currentUp2;
    @BindView(R.id.config_current_down2)
    EditText configCurrentDown2;
    @BindView(R.id.btn_control2_open)
    Button btnControl2Open;
    @BindView(R.id.btn_control2_close)
    Button btnControl2Close;
    @BindView(R.id.control2_ainfo)
    LinearLayout control2Ainfo;
    @BindView(R.id.config_control_state3)
    TextView configControlState3;
    @BindView(R.id.config_voltage_up3)
    EditText configVoltageUp3;
    @BindView(R.id.config_voltage_down3)
    EditText configVoltageDown3;
    @BindView(R.id.current_up3)
    EditText currentUp3;
    @BindView(R.id.config_current_down3)
    EditText configCurrentDown3;
    @BindView(R.id.btn_control3_open)
    Button btnControl3Open;
    @BindView(R.id.btn_control3_close)
    Button btnControl3Close;
    @BindView(R.id.control3_ainfo)
    LinearLayout control3Ainfo;
    @BindView(R.id.config_control_state4)
    TextView configControlState4;
    @BindView(R.id.config_voltage_up4)
    EditText configVoltageUp4;
    @BindView(R.id.config_voltage_down4)
    EditText configVoltageDown4;
    @BindView(R.id.current_up4)
    EditText currentUp4;
    @BindView(R.id.config_current_down4)
    EditText configCurrentDown4;
    @BindView(R.id.btn_control4_open)
    Button btnControl4Open;
    @BindView(R.id.btn_control4_close)
    Button btnControl4Close;
    @BindView(R.id.control4_ainfo)
    LinearLayout control4Ainfo;
    @BindView(R.id.config_cancel)
    Button configCancel;
    @BindView(R.id.config_save)
    Button configSave;
    @BindView(R.id.tv_linkman_day)
    TextView tvLinkmanDay;
    @BindView(R.id.tv_linkman_day2)
    TextView tvLinkmanDay2;
    @BindView(R.id.tv_linkman_night)
    TextView tvLinkmanNight;
    @BindView(R.id.tv_linkman_night2)
    TextView tvLinkmanNight2;

    @BindView(R.id.rl_control1)
    RelativeLayout rlControl1;
    @BindView(R.id.rl_control2)
    RelativeLayout rlControl2;
    @BindView(R.id.rl_control3)
    RelativeLayout rlControl3;
    @BindView(R.id.rl_control4)
    RelativeLayout rlControl4;

    private ArrayList<FishPondInfo> fishPondInfos = new ArrayList<>();
    private final int REQUEST_SCANNING = 100;
    private final int REQUEST_LOCATION = 1001;
    private String farmerID, farmerName;
    private boolean isRepair;
    private ChildDeviceListBean mChildDeviceListBean = new ChildDeviceListBean();
    private String installMessage;
    private boolean isChange = false;

    //联系人列表
    private List<LinkManBean> mLinkManList;
    private ListLinkManPopupWindow mListLinkManPopupWindow;
    private Handler mHander;
    //controlId 控制器0、控制器1、控制器2、控制器3
    private HashMap<String, String> mControlOpenMap = new HashMap<>(4);
    private HashMap<String, Integer> mControlAutoMap = new HashMap<>(4);

    //设备配对
    private boolean isDevicePair;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_deviceconfig2);
        farmerID = getIntent().getStringExtra(Constants.INTENT_OBJECT);
        farmerName = getIntent().getStringExtra(Constants.INTENT_FLAG);

        mControlAutoMap.put("0", 1);
        mControlAutoMap.put("1", 1);
        mControlAutoMap.put("2", 1);
        mControlAutoMap.put("3", 1);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        isRepair = getIntent().getBooleanExtra("isRepair", false);
        String id = getIntent().getStringExtra("deviceId");
        if (isRepair) {
            String pond = getIntent().getStringExtra("pondsName");
            String pondsId = getIntent().getStringExtra("pondsId");
            configFishpond.setText(pond);
            if (!TextUtils.isEmpty(id)) {
                title.setText("设备配置");
                configDeviceId.setText(id);
                getDeviceInfo();
            } else {
                title.setText("更换设备");
                isChange = true;
            }
            getPondLinkManSetting(pondsId);
        } else {
            title.setText("设备配置");
            mChildDeviceListBean = getIntent().getParcelableExtra("info");
            if (mChildDeviceListBean != null) {
                //设置ui
                initUI(true);
                getDeviceInfo();
                //getPondLinkManSetting(mChildDeviceListBean.pondId);
            } else {
                mChildDeviceListBean = new ChildDeviceListBean();
            }
        }
        BaiduLocManager.getInstance(getApplicationContext()).startLocation(new BaiduLocManager.OnLocationComplete() {
            @Override
            public void onLocationComplete(BDLocation location) {
                String province = location.getProvince();    //获取省份
                String city = location.getCity();    //获取城市
                String district = location.getDistrict();    //获取区县
                String street = location.getStreet();    //获取街道信息
                if (!TextUtils.isEmpty(location.getProvince()) && !"null".equals(location.getProvince())) {
                    installPondAddress.setText(province + city + district + street);
                    mChildDeviceListBean.pondAddr = province + city + district + street;
                    mChildDeviceListBean.latitude = location.getLatitude() + "";
                    mChildDeviceListBean.longitude = location.getLongitude() + "";
                }
            }
        });

        mLinkManList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        String contacters = bundle.getString("contacters");
        String contactPhone = bundle.getString("contactPhone");
        String nightContacters = bundle.getString("nightContacters");
        String nightContactPhone = bundle.getString("nightContactPhone");

        String standbyContact = bundle.getString("standbyContact");
        String standbyContactPhone = bundle.getString("standbyContactPhone");
        String standbyNightContact = bundle.getString("standbyNightContact");
        String standbyNightContactPhone = bundle.getString("standbyNightContactPhone");

        setDeviceLinkMan(contacters, contactPhone, nightContacters, nightContactPhone,
                standbyContact, standbyContactPhone, standbyNightContact, standbyNightContactPhone);

        //setEditTextInputFilter();
        rbConnectLine.toggle();
        btnDevicePair.setEnabled(false);
        //设备控制器默认只有两路
        rlControl3.setVisibility(View.GONE);
        control3Ainfo.setVisibility(View.GONE);
        rlControl4.setVisibility(View.GONE);
        control4Ainfo.setVisibility(View.GONE);
    }

    private void initUI(boolean isSetIdentifier) {
        if (isSetIdentifier) {
            configDeviceId.setText(mChildDeviceListBean.identifier);
            configFishpond.setText(mChildDeviceListBean.pondName);
        }
        configDeviceType.setText(mChildDeviceListBean.type);
        configAlarmLine1.setText(mChildDeviceListBean.alertline1);
        configAlarmLine2.setText(mChildDeviceListBean.alertline2);
        if (mChildDeviceListBean.deviceControlInfoBeanList != null
                && mChildDeviceListBean.deviceControlInfoBeanList.size() > 0) {
            for (DeviceControlInfoBean item1 : mChildDeviceListBean.deviceControlInfoBeanList) {
                if (item1.controlId == 0) {
                    control1Setting(item1);
                    continue;
                }
                if (item1.controlId == 1) {
                    control2Setting(item1);
                    continue;
                }

                if (item1.controlId == 2) {
                    control3Setting(item1);
                    continue;
                }
                if (item1.controlId == 3) {
                    control4Setting(item1);
                    continue;
                }
            }
        }
    }

    private void setDeviceLinkMan(
            String contacters,
            String contactPhone,
            String nightContacters,
            String nightContactPhone,
            String standbyContact,
            String standbyContactPhone,
            String standbyNightContact,
            String standbyNightContactPhone) {

        if (!TextUtils.isEmpty(contacters) && !TextUtils.isEmpty(contactPhone)) {
            tvLinkmanDay.setText(contacters + "(" + contactPhone + ")");
        }
        if (!TextUtils.isEmpty(nightContacters) && !TextUtils.isEmpty(nightContactPhone)) {
            tvLinkmanNight.setText(nightContacters + "(" + nightContactPhone + ")");
        }
        if (!TextUtils.isEmpty(standbyContact) && !TextUtils.isEmpty(standbyContactPhone)) {
            tvLinkmanDay2.setText(standbyContact + "(" + standbyContactPhone + ")");
        }
        if (!TextUtils.isEmpty(standbyNightContact) && !TextUtils.isEmpty(standbyNightContactPhone)) {
            tvLinkmanNight2.setText(standbyNightContact + "(" + standbyNightContactPhone + ")");
        }
    }

    @Override
    protected void requestData() {
        super.requestData();
        getFishPonds(farmerID);
        queryLinkManList(farmerID);
    }

    @OnClick({R.id.device_scanning, R.id.device_connect, R.id.device_reset, R.id.btn_device_pair,
            R.id.btn_control1_open, R.id.btn_control1_close,
            R.id.btn_control2_open, R.id.btn_control2_close,
            R.id.btn_control3_open, R.id.btn_control3_close,
            R.id.btn_control4_open, R.id.btn_control4_close,
            R.id.tv_add_contacts, R.id.config_save, R.id.config_cancel, R.id.config_fishpond,
            R.id.install_location, R.id.install_pond_address, R.id.rb_connect_line, R.id.rb_connect_wiredless,
            R.id.tv_linkman_day, R.id.tv_linkman_day2, R.id.tv_linkman_night, R.id.tv_linkman_night2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_location:
            case R.id.install_pond_address:
                goToActivityForResult(LocationActivity.class, REQUEST_LOCATION);
                break;
            case R.id.device_scanning:
                goToActivityForResult(CaptureActivity.class, REQUEST_SCANNING);
                break;
            case R.id.rb_connect_line:
                btnDevicePair.setEnabled(false);
                connectTypeChange(1);
                break;
            case R.id.rb_connect_wiredless:
                btnDevicePair.setEnabled(true);
                connectTypeChange(0);
                break;
            case R.id.tv_add_contacts:
                //添加联系人接口
                new EditContactsDialog(Device2ConfigActivity.this, new EditContactsDialog.OnEditContactListener() {
                    @Override
                    public void onEditContact(String tag, String people, String phoneNum) {
                        //fixme 添加到联系人列表
                        addLinkMan(people, phoneNum);
                    }
                }).show();
                break;
            case R.id.tv_linkman_day:
                showLinkManList(tvLinkmanDay);
                break;
            case R.id.tv_linkman_day2:
                showLinkManList(tvLinkmanDay2);
                break;
            case R.id.tv_linkman_night:
                showLinkManList(tvLinkmanNight);
                break;
            case R.id.tv_linkman_night2:
                showLinkManList(tvLinkmanNight2);
                break;
            case R.id.device_connect:
                if (!TextUtils.isEmpty(configDeviceId.getText())) {
                    if (configDeviceId.getText().toString().length() == 6) {
                        if (!isRepair && !TextUtils.isEmpty(installMessage)) {
                            showToast(installMessage);
                        } else if (isChange && !TextUtils.isEmpty(installMessage)) {
                            showToast(installMessage);
                        } else {
                            testConnect();
                        }
                    } else {
                        showToast("请输入6位设备ID");
                    }
                }
                break;
            case R.id.btn_device_pair:
                //TODO 配对
                deviceLinkPair(configDeviceId.getText().toString(), rbConnectLine.isChecked() ? "1" : "0");
                break;
            case R.id.btn_control1_open:
                deviceControlStatus(configDeviceId.getText().toString(), "0", "1");
                break;
            case R.id.btn_control1_close:
                deviceControlStatus(configDeviceId.getText().toString(), "0", "0");
                break;
            case R.id.btn_control2_open:
                deviceControlStatus(configDeviceId.getText().toString(), "1", "1");
                break;
            case R.id.btn_control2_close:
                deviceControlStatus(configDeviceId.getText().toString(), "1", "0");
                break;
            case R.id.btn_control3_open:
                deviceControlStatus(configDeviceId.getText().toString(), "2", "1");
                break;
            case R.id.btn_control3_close:
                deviceControlStatus(configDeviceId.getText().toString(), "2", "0");
                break;
            case R.id.btn_control4_open:
                deviceControlStatus(configDeviceId.getText().toString(), "3", "1");
                break;
            case R.id.btn_control4_close:
                deviceControlStatus(configDeviceId.getText().toString(), "3", "0");
                break;
            case R.id.device_reset:
                if (!TextUtils.isEmpty(configDeviceId.getText())) {
                    if (configDeviceId.getText().toString().length() == 6) {
                        if (!isRepair && !TextUtils.isEmpty(installMessage)) {
                            showToast(installMessage);
                        } else if (isChange && !TextUtils.isEmpty(installMessage)) {
                            showToast(installMessage);
                        } else {
                            testReset();
                        }
                    } else {
                        showToast("请输入6位设备ID");
                    }
                }
                break;
            case R.id.config_cancel:
                onBackPressed();
                break;
            case R.id.config_save:
                if (!isDevicePair && rbConnectWiredless.isChecked()) {
                    showToast(getString(R.string.toast_device_pair_no));
                    return;
                }
                saveDeviceConfig();
                break;
            case R.id.config_fishpond:
                if (isRepair) {
                    return;
                }
                String notice = farmerName;
                if (!TextUtils.isEmpty(configFishpond.getText())) {
                    notice = configFishpond.getText().toString();
                }
                new FishPondDialog(mContext, notice, fishPondInfos, new DialogConfirmListener() {
                    @Override
                    public void onDialogConfirm(boolean result, Object value) {
                        if (result) {
                            FishPondInfo fishPondInfo = (FishPondInfo) value;
                            configFishpond.setText(fishPondInfo.name);
                            mChildDeviceListBean.pondId = fishPondInfo.pondId;
                            mChildDeviceListBean.pondName = fishPondInfo.name;
                            getPondLinkManSetting(fishPondInfo.pondId);
                        } else {
                            configFishpond.setText(String.valueOf(value));
                            mChildDeviceListBean.pondId = "";
                            mChildDeviceListBean.pondName = String.valueOf(value);
                        }
                        if (!TextUtils.isEmpty(configDeviceType.getText())) {
                            String type = configDeviceType.getText().toString();
                            //fixme 控制器名字
//                            configControlName1.setText(configFishpond.getText().toString() + type + "1#");
//                            configControlName2.setText(configFishpond.getText().toString() + type + "2#");
                        }
                    }
                }).show();
                break;
            default:
                break;
        }
    }

    private void saveDeviceConfig() {
        if (!isRepair && !TextUtils.isEmpty(installMessage)) {
            showToast(installMessage);
            return;
        }
        if (isChange && !TextUtils.isEmpty(installMessage)) {
            showToast(installMessage);
            return;
        }
        if (TextUtils.isEmpty(configDeviceId.getText())) {
            showToast("请输入设备ID");
            return;
        }
        if (TextUtils.isEmpty(configFishpond.getText())) {
            showToast("请选择鱼塘");
            return;
        }

        if (TextUtils.isEmpty(configAlarmLine1.getText())) {
            showToast("请输入传感器报警线1");
            return;
        }
        if (TextUtils.isEmpty(configAlarmLine2.getText())) {
            showToast("请输入传感器报警线2");
            return;
        }
//        if (!isSettingDeviceParam()) {
//            return;
//        }
        showLoading();
        final String deviceId = configDeviceId.getText().toString();
        final DeviceConfigPut2 deviceConfigPut = new DeviceConfigPut2();
        String alarmLin1 = configAlarmLine1.getText().toString();
        String alarmLin2 = configAlarmLine2.getText().toString();
        deviceConfigPut.alertline1 = alarmLin1;
        deviceConfigPut.alertline2 = alarmLin2;
        //deviceConfigPut.identifierID = deviceId;

        deviceConfigPut.deviceControlInfoBeanList = settingDeviceControl();
        LogInfo.error(deviceConfigPut.toString());
        putData(HttpConfig.DEVICE_TEST_CONFIG2.replace("{identifierID}", deviceId), deviceConfigPut.toString(), new ResponseCallBack<JsonElement>() {
            @Override
            public void onSuccessResponse(JsonElement d, String msg) {
                dismissLoading();
                showToast("保存成功");
                Intent intent = new Intent();
                //fixme 设备返回数据
                setChildDeviceListBean(deviceId, deviceConfigPut.deviceControlInfoBeanList);
                intent.putExtra(Constants.INTENT_OBJECT, mChildDeviceListBean);
                setResultLinkMan(intent);
                setResult(RESULT_OK, intent);
                onBackPressed();
            }

            @Override
            public void onFailResponse(String msg) {
                dismissLoading();
                showToast(msg);
            }

            @Override
            public void onVolleyError(int code, String msg) {
                dismissLoading();
                showToast(msg);
            }
        });
    }

    private void setResultLinkMan(Intent intent) {
        String contacter = tvLinkmanDay.getText().toString();
        if (!TextUtils.isEmpty(contacter)) {
            try {
                contacter = contacter.replace("(", ",").replace(")", "");
                String[] contacters = contacter.split(",");
                intent.putExtra("contacters", contacters[0]);
                intent.putExtra("contactPhone", contacters[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        contacter = tvLinkmanDay2.getText().toString();
        if (!TextUtils.isEmpty(contacter)) {
            try {
                contacter = contacter.replace("(", ",").replace(")", "");
                String[] contacters = contacter.split(",");
                intent.putExtra("standbyContact", contacters[0]);
                intent.putExtra("standbyContactPhone", contacters[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        contacter = tvLinkmanNight.getText().toString();
        if (!TextUtils.isEmpty(contacter)) {
            try {
                contacter = contacter.replace("(", ",").replace(")", "");
                String[] contacters = contacter.split(",");
                intent.putExtra("nightContacters", contacters[0]);
                intent.putExtra("nightContactPhone", contacters[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        contacter = tvLinkmanNight2.getText().toString();
        if (!TextUtils.isEmpty(contacter)) {
            try {
                contacter = contacter.replace("(", ",").replace(")", "");
                String[] contacters = contacter.split(",");
                intent.putExtra("standbyNightContact", contacters[0]);
                intent.putExtra("standbyNightContactPhone", contacters[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void setChildDeviceListBean(String identifierID, List<DeviceControl> controlList) {
        if (mChildDeviceListBean == null) {
            mChildDeviceListBean = new ChildDeviceListBean();
        }

        mChildDeviceListBean.identifier = identifierID;

        mChildDeviceListBean.connectionType = rbConnectLine.isChecked() ? 1 : 0;
        mChildDeviceListBean.alertline1 = configAlarmLine1.getText().toString();
        mChildDeviceListBean.alertline2 = configAlarmLine2.getText().toString();

        List<DeviceControlInfoBean> deviceControlList = new ArrayList<>();
        for (DeviceControl deviceControl : controlList) {
            DeviceControlInfoBean infoBean = new DeviceControlInfoBean();
            infoBean.controlId = deviceControl.controlId;          //控制器编号
            infoBean.oxyLimitUp = deviceControl.oxyLimitUp;         //溶氧上限
            infoBean.oxyLimitDown = deviceControl.oxyLimitDown;       //溶氧上限
            infoBean.electricityUp = deviceControl.electricityUp;   //电流上限
            infoBean.electricityDown = deviceControl.electricityDown; //电流下限
//            if (mChildDeviceListBean.deviceControlInfoBeanList != null) {
//                for (DeviceControlInfoBean bean : mChildDeviceListBean.deviceControlInfoBeanList) {
//                    if (bean == null) {
//                        continue;
//                    }
//                    if (bean.controlId == infoBean.controlId) {
//                        infoBean.open = bean.open;
//                        infoBean.auto = bean.auto;
//                        break;
//                    }
//
//                }
//            }
            infoBean.open = deviceControl.open;               //开关状态（1开、0关）
            infoBean.auto = deviceControl.auto;               //控制状态 （1 自动、0 手动）
            deviceControlList.add(infoBean);
        }
        mChildDeviceListBean.deviceControlInfoBeanList = deviceControlList;

    }

    /**
     * 设置设备控制器
     *
     * @return
     */
    private List<DeviceControl> settingDeviceControl() {
        List<DeviceControl> deviceControls = new ArrayList<>();
        DeviceControl deviceControl = new DeviceControl();
        deviceControl.controlId = 0;
        deviceControl.oxyLimitUp = etOxygenUp1.getText().toString();
        deviceControl.oxyLimitDown = etOxygenUpDown1.getText().toString();
        deviceControl.electricityUp = etCurrentUp1.getText().toString();
        deviceControl.electricityDown = etCurrentDown1.getText().toString();
        String open = mControlOpenMap.get("0");
        deviceControl.open = TextUtils.isEmpty(open) || "1".equals(open) ? "1" : "0";
        deviceControl.auto = mControlAutoMap.get("0");

        DeviceControl deviceControl1 = new DeviceControl();
        deviceControl1.controlId = 1;
        deviceControl1.oxyLimitUp = configVoltageUp2.getText().toString();
        deviceControl1.oxyLimitDown = configVoltageDown2.getText().toString();
        deviceControl1.electricityUp = currentUp2.getText().toString();
        deviceControl1.electricityDown = configCurrentDown2.getText().toString();
        String open1 = mControlOpenMap.get("1");
        deviceControl1.open = TextUtils.isEmpty(open1) || "1".equals(open1) ? "1" : "0";
        deviceControl1.auto = mControlAutoMap.get("1");

        DeviceControl deviceControl2 = new DeviceControl();
        deviceControl2.controlId = 2;
        deviceControl2.oxyLimitUp = configVoltageUp3.getText().toString();
        deviceControl2.oxyLimitDown = configVoltageDown3.getText().toString();
        deviceControl2.electricityUp = currentUp3.getText().toString();
        deviceControl2.electricityDown = configCurrentDown3.getText().toString();
        String open2 = mControlOpenMap.get("2");
        deviceControl2.open = TextUtils.isEmpty(open2) || "1".equals(open2) ? "1" : "0";
        deviceControl2.auto = mControlAutoMap.get("2");

        DeviceControl deviceControl3 = new DeviceControl();
        deviceControl3.controlId = 3;
        deviceControl3.oxyLimitUp = configVoltageUp4.getText().toString();
        deviceControl3.oxyLimitDown = configVoltageDown4.getText().toString();
        deviceControl3.electricityUp = currentUp4.getText().toString();
        deviceControl3.electricityDown = configCurrentDown4.getText().toString();
        String open3 = mControlOpenMap.get("3");
        deviceControl3.open = TextUtils.isEmpty(open3) || "1".equals(open3) ? "1" : "0";
        deviceControl3.auto = mControlAutoMap.get("3");

        if (!TextUtils.isEmpty(deviceControl.oxyLimitUp)
                && !TextUtils.isEmpty(deviceControl.oxyLimitDown)
                && !TextUtils.isEmpty(deviceControl.electricityUp)
                && !TextUtils.isEmpty(deviceControl.electricityDown)) {
            deviceControls.add(deviceControl);

        }
        if (!TextUtils.isEmpty(deviceControl1.oxyLimitUp)
                && !TextUtils.isEmpty(deviceControl1.oxyLimitDown)
                && !TextUtils.isEmpty(deviceControl1.electricityUp)
                && !TextUtils.isEmpty(deviceControl1.electricityDown)) {
            deviceControls.add(deviceControl1);

        }
        if (!TextUtils.isEmpty(deviceControl2.oxyLimitUp)
                && !TextUtils.isEmpty(deviceControl2.oxyLimitDown)
                && !TextUtils.isEmpty(deviceControl2.electricityUp)
                && !TextUtils.isEmpty(deviceControl2.electricityDown)) {
            deviceControls.add(deviceControl2);

        }

        if (!TextUtils.isEmpty(deviceControl3.oxyLimitUp)
                && !TextUtils.isEmpty(deviceControl3.oxyLimitDown)
                && !TextUtils.isEmpty(deviceControl3.electricityUp)
                && !TextUtils.isEmpty(deviceControl3.electricityDown)) {
            deviceControls.add(deviceControl3);
        }

        return deviceControls;

    }

    /**
     * 判断是否参数全部设置
     *
     * @return
     */
    private boolean isSettingDeviceParam() {
        if (!isSettingDeviceOxy(etOxygenUp1.getText().toString(), 1, 1)) {
            return false;
        }
        if (!isSettingDeviceOxy(etOxygenUpDown1.getText().toString(), 1, 0)) {
            return false;
        }

        if (!isSettingDeviceOxy(configVoltageUp2.getText().toString(), 2, 1)) {
            return false;
        }
        if (!isSettingDeviceOxy(configVoltageDown2.getText().toString(), 2, 0)) {
            return false;
        }
        if (!isSettingDeviceOxy(configVoltageUp3.getText().toString(), 3, 1)) {
            return false;
        }
        if (!isSettingDeviceOxy(configVoltageDown3.getText().toString(), 3, 0)) {
            return false;
        }
        if (!isSettingDeviceOxy(configVoltageUp4.getText().toString(), 4, 1)) {
            return false;
        }
        if (!isSettingDeviceOxy(configVoltageDown4.getText().toString(), 4, 0)) {
            return false;
        }
        //电流
        if (!isSettingDeviceElectricity(etOxygenUp1.getText().toString(), 1, 1)) {
            return false;
        }
        if (!isSettingDeviceElectricity(etOxygenUpDown1.getText().toString(), 1, 0)) {
            return false;
        }

        if (!isSettingDeviceElectricity(configVoltageUp2.getText().toString(), 2, 1)) {
            return false;
        }
        if (!isSettingDeviceElectricity(configVoltageDown2.getText().toString(), 2, 0)) {
            return false;
        }
        if (!isSettingDeviceElectricity(configVoltageUp3.getText().toString(), 3, 1)) {
            return false;
        }
        if (!isSettingDeviceElectricity(configVoltageDown3.getText().toString(), 3, 0)) {
            return false;
        }
        if (!isSettingDeviceElectricity(configVoltageUp4.getText().toString(), 4, 1)) {
            return false;
        }
        if (!isSettingDeviceElectricity(configVoltageDown4.getText().toString(), 4, 0)) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否输入溶解氧
     *
     * @param oxy        输入溶解氧
     * @param type       1 上限 1 下限
     * @param controlNum 控制器序号 1234
     * @return true 通过 false 未通过，提示
     */
    private boolean isSettingDeviceOxy(String oxy, int controlNum, int type) {
        if (TextUtils.isEmpty(oxy)) {
            showToast("请输入控制器" + controlNum + "溶解氧" + (type == 1 ? "上限" : "下限"));
            return false;
        }
        return true;
    }

    private boolean isSettingDeviceElectricity(String electricity, int controlNum, int type) {
        if (TextUtils.isEmpty(electricity)) {
            showToast("请输入控制器" + controlNum + "电流" + (type == 1 ? "上限" : "下限"));
            return false;
        }
        return true;
    }

    private void getFishPonds(String farmerId) {
        /*获取鱼塘列表*/
        getData(HttpConfig.GET_CUSTOMER_PONDS.replace("{customerId}", farmerId), new ResponseCallBack<ArrayList<FishPondInfo>>() {
            @Override
            public void onSuccessResponse(ArrayList<FishPondInfo> d, String msg) {
                fishPondInfos.clear();
                if (d != null && d.size() > 0) {
                    fishPondInfos.addAll(d);
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
    protected void addViewListener() {
        super.addViewListener();

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.config_hasa1:
                        if (isChecked) {
                            control1Info.setVisibility(View.VISIBLE);
                        } else {
                            control1Info.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.config_hasa2:
                        if (isChecked) {
                            control2Ainfo.setVisibility(View.VISIBLE);
                        } else {
                            control2Ainfo.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        configDeviceId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && s.length() >= 6) {
                    if (isRepair) {
                        if (isChange) {
                            getDeviceInstallState();
                        } else {
                            getDeviceInfo();
                        }
                    } else {
                        getDeviceInstallState();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SCANNING:
                    String qrcode = data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
                    configDeviceId.setText(qrcode);
                    if (isRepair) {
                        getDeviceInfo();
                    } else {
                        getDeviceInstallState();
                    }
                    break;
                case REQUEST_LOCATION:
                    LocPoi locPoi = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    installPondAddress.setText(locPoi.address);
                    mChildDeviceListBean.pondAddr = locPoi.address;
                    mChildDeviceListBean.latitude = locPoi.lat + "";
                    mChildDeviceListBean.longitude = locPoi.lng + "";
                    break;
                default:
                    break;
            }
        }
    }

    public void testConnect() {
        showLoading();
        String deviceId = configDeviceId.getText().toString();
        getData(HttpConfig.DEVICE_TEST_CONNECT.replace("{id}", deviceId), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
                dismissLoading();
                if ("ON".equals(d)) {
                    showToast("设备在线");
                } else {
                    showToast("设备不在线");
                }
            }

            @Override
            public void onFailResponse(String msg) {
                dismissLoading();
                showToast(msg);
            }

            @Override
            public void onVolleyError(int code, String msg) {
                showToast(msg);
                dismissLoading();
            }
        });
    }

    /**
     * @param deviceId  设备Id
     * @param controlId 控制器Id
     */
    public void deviceControlStatus(String deviceId, String controlId, String isOpen) {
        if (!TextUtils.isEmpty(configDeviceId.getText())) {
            if (configDeviceId.getText().toString().length() == 6) {
                if (!isRepair && !TextUtils.isEmpty(installMessage)) {
                    showToast(installMessage);
                } else if (isChange && !TextUtils.isEmpty(installMessage)) {
                    showToast(installMessage);
                } else {
                    testOpenAndClose(deviceId, controlId, isOpen);
                }
            } else {
                showToast("请输入6位设备ID");
            }
        }
    }

    public void testOpenAndClose(String deviceIdentifier, final String controlId, final String isOpen) {
        showLoading();
        putData(HttpConfig.DEVICE_CONTROL_SWITCH.replace("{identifier}", deviceIdentifier)
                        .replace("{cch}", controlId)
                        .replace("{state}", "1".equals(isOpen) ? "on" : "off"),
                new ResponseCallBackRaw<String>() {
                    @Override
                    public void onSuccessResponse(String response) {
                        dismissLoading();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("err");
                            if (code == 0) {
                                showToast("1".equals(isOpen) ? "打开成功" : "关闭成功");
                                mControlOpenMap.put(controlId, isOpen);
                                updateUIOpenStatus(controlId, isOpen);
                               // delay5sGetInfo();
                            } else {
                                showToast("1".equals(isOpen) ? "打开失败" : "关闭失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailResponse(String msg) {
                        dismissLoading();
                        showToast(msg);
                    }

                    @Override
                    public void onVolleyError(int code, String msg) {
                        dismissLoading();
                        showToast(msg);
                    }
                });
    }


    public void testReset() {
        showLoading();
        String identifierID = configDeviceId.getText().toString();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ch", "0");
        jsonObject.addProperty("type", "do");
        jsonObject.addProperty("stage", "1");
        String json = jsonObject.toString();
        putData(HttpConfig.DEVICE_NEW_RESET.replace("{identifierID}", identifierID), json,
                new ResponseCallBack<JsonElement>() {
                    @Override
                    public void onSuccessResponse(JsonElement d, String msg) {
                        dismissLoading();
                        showToast(msg);
                    }

                    @Override
                    public void onFailResponse(String msg) {
                        dismissLoading();
                        showToast(msg);
                    }

                    @Override
                    public void onVolleyError(int code, String msg) {
                        dismissLoading();
                        showToast(msg);
                    }
                });
    }

    /**
     * 连接方式切换
     */
    public void connectTypeChange(int type) {
        showLoading();
        String identifierID = configDeviceId.getText().toString();
        if (TextUtils.isEmpty(identifierID)) {
            return;
        }
        putData(HttpConfig.DEVICE_CONNECT_TYPE_CHANGE.replace("{identifierID}", identifierID)
                        .replace("{ch}", "0")
                        .replace("{pairType}", type + ""),
                new ResponseCallBack<JsonElement>() {
                    @Override
                    public void onSuccessResponse(JsonElement d, String msg) {
                        dismissLoading();
                        showToast(msg);
                    }

                    @Override
                    public void onFailResponse(String msg) {
                        dismissLoading();
                        showToast(msg);
                    }

                    @Override
                    public void onVolleyError(int code, String msg) {
                        dismissLoading();
                        showToast(msg);
                    }
                });
    }

    private void delay5sGetInfo() {
        if (mHander == null) {
            mHander = new Handler();
        }
        mHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDeviceInfo();
            }
        }, 5000);
    }

    public void getDeviceInfo() {
        showLoading();
        String deviceId = configDeviceId.getText().toString();
        getData(HttpConfig.DEVICE_TEST_CONFIG_NEW.replace("{id}", deviceId),
                new ResponseCallBack<ChildDeviceListBean>() {
                    @Override
                    public void onSuccessResponse(ChildDeviceListBean d, String msg) {
                        dismissLoading();
                        if (d == null) {
                            showToast(msg);
                            return;
                        }
                        mChildDeviceListBean.identifier = d.identifier;
                        mChildDeviceListBean.id = d.id;
                        mChildDeviceListBean.name = d.name;
                        mChildDeviceListBean.type = d.type;
                        mChildDeviceListBean.workStatus = d.workStatus;
                        mChildDeviceListBean.oxy = d.oxy;
                        mChildDeviceListBean.temp = d.temp;
                        mChildDeviceListBean.ph = d.ph;
                        mChildDeviceListBean.alertline1 = d.alertline1;
                        mChildDeviceListBean.alertline2 = d.alertline2;
                        mChildDeviceListBean.connectionType = d.connectionType;
                        mChildDeviceListBean.deviceControlInfoBeanList = d.deviceControlInfoBeanList;
                        initUI(false);

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
    private void updateUIOpenStatus(final String controlId, final String isOpen) {
        switch (controlId) {
            case "0":
                if ("1".equals(isOpen)) {
                    tvControl1State.setText("开");
                    tvControl1State.setTextColor(Color.GREEN);
                } else {
                    tvControl1State.setText("关");
                    tvControl1State.setTextColor(Color.RED);
                }
                break;
            case "1":
                if ("1".equals(isOpen)) {
                    tvControl2State.setText("开");
                    tvControl2State.setTextColor(Color.GREEN);
                } else {
                    tvControl2State.setText("关");
                    tvControl2State.setTextColor(Color.RED);
                }
                break;
            case "2":
                if ("1".equals(isOpen)) {
                    configControlState3.setText("开");
                    configControlState3.setTextColor(Color.GREEN);
                } else {
                    configControlState3.setText("关");
                    configControlState3.setTextColor(Color.RED);
                }
                break;
            case "3":
                if ("1".equals(isOpen)) {
                    configControlState4.setText("开");
                    configControlState4.setTextColor(Color.GREEN);
                } else {
                    configControlState4.setText("关");
                    configControlState4.setTextColor(Color.RED);
                }
                break;
            default:
                break;
        }

    }

    private void control1Setting(DeviceControlInfoBean item1) {
        if (!TextUtils.isEmpty(item1.open)) {
            rlControl1.setVisibility(View.VISIBLE);
            control1Info.setVisibility(View.VISIBLE);
            //fixme
            if ("1".equals(item1.open)) {
                tvControl1State.setText("开");
                tvControl1State.setTextColor(Color.GREEN);
            } else {
                tvControl1State.setText("关");
                tvControl1State.setTextColor(Color.RED);
            }
            mControlOpenMap.put("0", item1.open);
            mControlAutoMap.put("0", item1.auto);

            etOxygenUp1.setText(TextUtils.isEmpty(item1.oxyLimitUp) ? "" : item1.oxyLimitUp);
            etOxygenUpDown1.setText(TextUtils.isEmpty(item1.oxyLimitDown) ? "" : item1.oxyLimitDown);
            etCurrentUp1.setText(TextUtils.isEmpty(item1.electricityUp) ? "" : item1.electricityUp);
            etCurrentDown1.setText(TextUtils.isEmpty(item1.electricityDown) ? "" : item1.electricityDown);

        } else {
            tvControl1State.setText("");
            etOxygenUp1.setText("");
            etOxygenUpDown1.setText("");
            etCurrentUp1.setText("");
            etCurrentDown1.setText("");
            rlControl1.setVisibility(View.GONE);
            control1Info.setVisibility(View.GONE);
        }
    }

    private void control2Setting(DeviceControlInfoBean item1) {
        if (!TextUtils.isEmpty(item1.open)) {
            rlControl2.setVisibility(View.VISIBLE);
            control2Ainfo.setVisibility(View.VISIBLE);

            if ("1".equals(item1.open)) {
                tvControl2State.setText("开");
                tvControl2State.setTextColor(Color.GREEN);
            } else {
                tvControl2State.setText("关");
                tvControl2State.setTextColor(Color.RED);
            }
            mControlOpenMap.put("1", item1.open);
            mControlAutoMap.put("1", item1.auto);

            configVoltageUp2.setText(TextUtils.isEmpty(item1.oxyLimitUp) ? "" : item1.oxyLimitUp);
            configVoltageDown2.setText(TextUtils.isEmpty(item1.oxyLimitDown) ? "" : item1.oxyLimitDown);
            currentUp2.setText(TextUtils.isEmpty(item1.electricityUp) ? "" : item1.electricityUp);
            configCurrentDown2.setText(TextUtils.isEmpty(item1.electricityDown) ? "" : item1.electricityDown);

        } else {
            tvControl2State.setText("");
            configVoltageUp2.setText("");
            configVoltageDown2.setText("");
            currentUp2.setText("");
            configCurrentDown2.setText("");
            rlControl2.setVisibility(View.GONE);
            control2Ainfo.setVisibility(View.GONE);

        }
    }

    private void control3Setting(DeviceControlInfoBean item1) {
        if (!TextUtils.isEmpty(item1.open)) {
            rlControl3.setVisibility(View.VISIBLE);
            control3Ainfo.setVisibility(View.VISIBLE);

            if ("1".equals(item1.open)) {
                configControlState3.setText("开");
                configControlState3.setTextColor(Color.GREEN);
            } else {
                configControlState3.setText("关");
                configControlState3.setTextColor(Color.RED);
            }
            mControlOpenMap.put("2", item1.open);
            mControlAutoMap.put("2", item1.auto);

            configVoltageUp3.setText(TextUtils.isEmpty(item1.oxyLimitUp) ? "" : item1.oxyLimitUp);
            configVoltageDown3.setText(TextUtils.isEmpty(item1.oxyLimitDown) ? "" : item1.oxyLimitDown);
            currentUp3.setText(TextUtils.isEmpty(item1.electricityUp) ? "" : item1.electricityUp);
            configCurrentDown3.setText(TextUtils.isEmpty(item1.electricityDown) ? "" : item1.electricityDown);

        } else {
            configControlState3.setText("");
            configVoltageUp3.setText("");
            configVoltageDown3.setText("");
            currentUp3.setText("");
            configCurrentDown3.setText("");

            rlControl3.setVisibility(View.GONE);
            control3Ainfo.setVisibility(View.GONE);

        }
    }

    private void control4Setting(DeviceControlInfoBean item1) {
        if (!TextUtils.isEmpty(item1.open)) {
            rlControl4.setVisibility(View.VISIBLE);
            control4Ainfo.setVisibility(View.VISIBLE);

            //fixme
            if ("1".equals(item1.open)) {
                configControlState4.setText("开");
                configControlState4.setTextColor(Color.GREEN);
            } else {
                configControlState4.setText("关");
                configControlState4.setTextColor(Color.RED);
            }
            mControlOpenMap.put("3", item1.open);
            mControlAutoMap.put("3", item1.auto);

            configVoltageUp4.setText(TextUtils.isEmpty(item1.oxyLimitUp) ? "" : item1.oxyLimitUp);
            configVoltageDown4.setText(TextUtils.isEmpty(item1.oxyLimitDown) ? "" : item1.oxyLimitDown);
            currentUp4.setText(TextUtils.isEmpty(item1.electricityUp) ? "" : item1.electricityUp);
            configCurrentDown4.setText(TextUtils.isEmpty(item1.electricityDown) ? "" : item1.electricityDown);

        } else {
            configControlState4.setText("");
            configVoltageUp4.setText("");
            configVoltageDown4.setText("");
            currentUp4.setText("");
            configCurrentDown4.setText("");
            rlControl4.setVisibility(View.GONE);
            control4Ainfo.setVisibility(View.GONE);
        }
    }

    public void getDeviceInstallState() {
        showLoading();
        String deviceId = configDeviceId.getText().toString();
        getData(HttpConfig.DEVICE_INSTALL_CHECK.replace("{id}", deviceId), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
                dismissLoading();
                if ("OK".equals(d)) {
                    installMessage = null;
                    getDeviceInfo();
                } else {
                    installMessage = msg;
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

    /**
     * 设备配对
     *
     * @param identifierID
     * @param pairType     (有线: 1; 无线: 0;)
     */
    private void deviceLinkPair(String identifierID, String pairType) {
        if (TextUtils.isEmpty(identifierID)) {
            showToast(getString(R.string.toast_input_device_id));
            return;
        }
        showLoading();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pairType", pairType);
        String json = jsonObject.toString();
        putData(HttpConfig.DEVICE_LINK_PAIR.replace("{identifierID}", identifierID), json,
                new ResponseCallBack<Boolean>() {
                    @Override
                    public void onSuccessResponse(Boolean d, String msg) {
                        dismissLoading();
                        showToast(d ? getString(R.string.text_device_pair_success) : getString(R.string.text_device_pair_fail));
                        isDevicePair = d;
//                        if (isDevicePair) {
//                            btnDevicePair.setBackgroundResource(R.drawable.btn_r5_solid_primary);
//                            btnDevicePair.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_white));
//                        }
                    }

                    @Override
                    public void onFailResponse(String msg) {
                        dismissLoading();
                        showToast(msg);
                    }

                    @Override
                    public void onVolleyError(int code, String msg) {
                        dismissLoading();
                        showToast(msg);
                    }
                });
    }

    /**
     * 添加联系人
     */
    private void addLinkMan(String name, String phoneNumber) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("phoneNumber", phoneNumber);
        String json = jsonObject.toString();
        showLoading();
        putData(HttpConfig.LINKMAN_ADD.replace("{farmerId}", farmerID), json, new ResponseCallBack<LinkManBean>() {
            @Override
            public void onSuccessResponse(LinkManBean d, String msg) {
                dismissLoading();
                if (d != null) {
                    showToast("联系人添加成功");
                    mLinkManList.add(d);
                } else {
                    showToast(msg);
                }
            }

            @Override
            public void onFailResponse(String msg) {
                dismissLoading();
                showToast(msg);
            }

            @Override
            public void onVolleyError(int code, String msg) {
                dismissLoading();
                showToast(msg);
            }
        });

    }

    /**
     * 查询联系人列表
     */
    private void queryLinkManList(String farmerID) {
        getData(HttpConfig.LINKMAN_GET.replace("{farmerId}", farmerID), new ResponseCallBack<List<LinkManBean>>() {
            @Override
            public void onSuccessResponse(List<LinkManBean> d, String msg) {
                mLinkManList.clear();
                mLinkManList.addAll(d);
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

    /**
     * 选择联系人列表
     *
     * @param textView
     */
    private void showLinkManList(final TextView textView) {
        //设置联系人
        mListLinkManPopupWindow = new ListLinkManPopupWindow(this,
                mLinkManList, new ListLinkManPopupWindow.OnItemClickListener() {
            @Override
            public void itemClick(LinkManBean linkManBean) {
                //
                if (mListLinkManPopupWindow != null && mListLinkManPopupWindow.isShowing()) {
                    mListLinkManPopupWindow.dismiss();
                }
                textView.setText(new StringBuilder(linkManBean.name).append("(").append(linkManBean.phoneNumber).append(")"));
                textView.setTag(linkManBean);
            }
        });
        mListLinkManPopupWindow.showAsDropDown(textView, 0, AppUtils.dp2px(mContext, 16), Gravity.END);

    }

    /**
     * 根据设备id,查询鱼塘联系人配置
     *
     * @param pondsId 设备id,int值
     */
    private void getPondLinkManSetting(String pondsId) {
        if (TextUtils.isEmpty(pondsId)) {
            return;
        }
        getData(HttpConfig.LINKMAN_ADDED_POND.replace("{deviceId}", pondsId),
                new ResponseCallBack<PondLinkMan>() {
                    @Override
                    public void onSuccessResponse(PondLinkMan d, String msg) {
                        //
                        setDeviceLinkMan(d.contacters, d.contactPhone, d.nightContacters, d.nightContactPhone,
                                d.standbyContact, d.standbyContactPhone, d.standbyNightContact, d.standbyNightContactPhone);
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

    /**
     * 设置EditText小数点一位
     */
    private void setEditTextInputFilter() {
        etOxygenUp1.setFilters(new InputFilter[]{new PointLengthFilter()});
        etOxygenUpDown1.setFilters(new InputFilter[]{new PointLengthFilter()});
        configVoltageUp3.setFilters(new InputFilter[]{new PointLengthFilter()});
        configVoltageDown2.setFilters(new InputFilter[]{new PointLengthFilter()});
        configVoltageUp3.setFilters(new InputFilter[]{new PointLengthFilter()});
        configVoltageDown3.setFilters(new InputFilter[]{new PointLengthFilter()});
        configVoltageUp4.setFilters(new InputFilter[]{new PointLengthFilter()});
        configVoltageDown4.setFilters(new InputFilter[]{new PointLengthFilter()});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHander != null) {
            mHander.removeCallbacksAndMessages(null);
        }
    }

}
