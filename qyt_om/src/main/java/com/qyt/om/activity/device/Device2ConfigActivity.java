package com.qyt.om.activity.device;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.google.gson.JsonElement;
import com.google.zxing.activity.CaptureActivity;
import com.qyt.om.R;
import com.qyt.om.activity.LocationActivity;
import com.qyt.om.adapter.DeviceLinkManAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.BindDeviceInfo;
import com.qyt.om.model.FishPondInfo;
import com.qyt.om.model.InfoMap;
import com.qyt.om.model.LinkManMap;
import com.qyt.om.model.LocPoi;
import com.qyt.om.request.DeviceConfigPut;
import com.qyt.om.response.AeratorControlItem;
import com.qyt.om.response.DeviceConfigInfo;
import com.qyt.om.response.LinkManBean;
import com.qyt.om.utils.BaiduLocManager;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.widget.FishPondDialog;

import java.util.ArrayList;
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
    @BindView(R.id.lv_contacts)
    UnScrollListView lvContacts;
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
    TextView configControlState1;
    @BindView(R.id.config_voltage_up1)
    EditText configVoltageUp1;
    @BindView(R.id.config_voltage_down1)
    EditText configVoltageDown1;
    @BindView(R.id.current_up1)
    EditText currentUp1;
    @BindView(R.id.config_current_down1)
    EditText configCurrentDown1;
    @BindView(R.id.btn_control1_open)
    Button btnControl1Open;
    @BindView(R.id.btn_control1_close)
    Button btnControl1Close;
    @BindView(R.id.control1_ainfo)
    LinearLayout control1Ainfo;
    @BindView(R.id.config_control_state2)
    TextView configControlState2;
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

    private ArrayList<FishPondInfo> fishPondInfos = new ArrayList<>();
    private final int REQUEST_SCANNING = 100;
    private final int REQUEST_LOCATION = 1001;
    private String farmerID, farmerName;
    private boolean isRepair;
    private BindDeviceInfo bindDeviceInfo = new BindDeviceInfo();
    private String installMessage;
    private boolean isChange = false;

    private DeviceLinkManAdapter mLinkManAdapter;
    private List<LinkManMap> mContactList;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_deviceconfig2);
        farmerID = getIntent().getStringExtra(Constants.INTENT_OBJECT);
        farmerName = getIntent().getStringExtra(Constants.INTENT_FLAG);
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
            configFishpond.setText(pond);
            if (!TextUtils.isEmpty(id)) {
                title.setText("设备配置");
                configDeviceId.setText(id);
                getDeviceInfo();
            } else {
                title.setText("更换设备");
                isChange = true;
            }
        } else {
            title.setText("设备配置");
            bindDeviceInfo = getIntent().getParcelableExtra("info");
            if (bindDeviceInfo != null) {
                configDeviceId.setText(bindDeviceInfo.deviceIdentifier);
                configDeviceType.setText(bindDeviceInfo.deviceModel);
                configFishpond.setText(bindDeviceInfo.pondName);
                getDeviceInfo();
            } else {
                bindDeviceInfo = new BindDeviceInfo();
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
                    bindDeviceInfo.pondAddr = province + city + district + street;
                    bindDeviceInfo.latitude = location.getLatitude() + "";
                    bindDeviceInfo.longitude = location.getLongitude() + "";
                }
            }
        });

        //fixme 增加联系人
        mContactList = new ArrayList<>();
        mContactList.add(new LinkManMap(getString(R.string.text_day_contacts), null));
        mContactList.add(new LinkManMap(getString(R.string.text_night_contacts), null));
        mContactList.add(new LinkManMap(getString(R.string.text_day_contacts_2), null));
        mContactList.add(new LinkManMap(getString(R.string.text_night2_contacts_2), null));
        mLinkManAdapter = new DeviceLinkManAdapter(this, mContactList);
        lvContacts.setAdapter(mLinkManAdapter);

    }

    @Override
    protected void requestData() {
        super.requestData();
        getFishPonds(farmerID);
    }

    @OnClick({R.id.device_scanning, R.id.device_connect, R.id.device_reset, R.id.btn_device_pair,
            R.id.btn_control1_open, R.id.btn_control1_close,
            R.id.btn_control2_open, R.id.btn_control2_close,
            R.id.btn_control3_open, R.id.btn_control3_close,
            R.id.btn_control4_open, R.id.btn_control4_close,
            R.id.config_save, R.id.config_cancel, R.id.config_fishpond, R.id.install_location, R.id.install_pond_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_location:
            case R.id.install_pond_address:
                goToActivityForResult(LocationActivity.class, REQUEST_LOCATION);
                break;
            case R.id.device_scanning:
                goToActivityForResult(CaptureActivity.class, REQUEST_SCANNING);
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

                break;
            case R.id.btn_control1_open:
                deviceControlStatus(configDeviceId.getText().toString(), "1", "1");
                break;
            case R.id.btn_control1_close:
                deviceControlStatus(configDeviceId.getText().toString(), "1", "0");
                break;
            case R.id.btn_control2_open:
                deviceControlStatus(configDeviceId.getText().toString(), "2", "1");
                break;
            case R.id.btn_control2_close:
                deviceControlStatus(configDeviceId.getText().toString(), "2", "0");
                break;
            case R.id.btn_control3_open:
                deviceControlStatus(configDeviceId.getText().toString(), "3", "1");
                break;
            case R.id.btn_control3_close:
                deviceControlStatus(configDeviceId.getText().toString(), "3", "0");
                break;
            case R.id.btn_control4_open:
                deviceControlStatus(configDeviceId.getText().toString(), "4", "1");
                break;
            case R.id.btn_control4_close:
                deviceControlStatus(configDeviceId.getText().toString(), "4", "0");
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
                saveDeviceConfig();
                break;
            case R.id.config_fishpond:
                if (isRepair) return;
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
                            bindDeviceInfo.pondId = fishPondInfo.pondId;
                            bindDeviceInfo.pondName = fishPondInfo.name;
                        } else {
                            configFishpond.setText(String.valueOf(value));
                            bindDeviceInfo.pondId = "";
                            bindDeviceInfo.pondName = String.valueOf(value);
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
        if (!isSettingDeviceParam()) {
            return;
        }
        showLoading();
        DeviceConfigPut deviceConfigPut = new DeviceConfigPut();
//        deviceConfigPut.oxyLimitUp = configLimitUp.getText().toString();
//        deviceConfigPut.oxyLimitDownOne = configLimitDown1.getText().toString();
//        deviceConfigPut.oxyLimitDownTwo = configLimitDown2.getText().toString();
        deviceConfigPut.alertlineOne = configAlarmLine1.getText().toString();
        deviceConfigPut.alertlineTwo = configAlarmLine2.getText().toString();
//        deviceConfigPut.automatic = callbackApproveAgree.isChecked();
        AeratorControlItem item1 = new AeratorControlItem();
        AeratorControlItem item2 = new AeratorControlItem();
//        item1.name = configControlName1.getText().toString();
//        item2.name = configControlName2.getText().toString();
//        item1.hasAmmeter = configHasa1.isChecked();
//        item2.hasAmmeter = configHasa2.isChecked();
//        item1.ammeterId = configControlCode1.getText().toString();
//        item2.ammeterId = configControlCode2.getText().toString();
//        item1.power = configPower1.getText().toString();
//        item2.power = configPower2.getText().toString();
        item1.voltageUp = configVoltageUp1.getText().toString();
        item2.voltageUp = configVoltageUp2.getText().toString();
        item1.voltageDown = configVoltageDown1.getText().toString();
        item2.voltageDown = configVoltageDown2.getText().toString();
        item1.electricCurrentUp = currentUp1.getText().toString();
        item2.electricCurrentUp = currentUp2.getText().toString();
        item1.electricCurrentDown = configCurrentDown1.getText().toString();
        item2.electricCurrentDown = configCurrentDown2.getText().toString();
        deviceConfigPut.aeratorControlList.add(item1);
        deviceConfigPut.aeratorControlList.add(item2);
        String deviceId = configDeviceId.getText().toString();
        LogInfo.error(deviceConfigPut.toString());
        putData(HttpConfig.DEVICE_TEST_CONFIG.replace("{id}", deviceId), deviceConfigPut.toString(), new ResponseCallBack<JsonElement>() {
            @Override
            public void onSuccessResponse(JsonElement d, String msg) {
                dismissLoading();
                showToast("保存成功");
                Intent intent = new Intent();
//                bindDeviceInfo.automatic = callbackApproveAgree.isChecked() ? "自动" : "手动";
                intent.putExtra(Constants.INTENT_OBJECT, bindDeviceInfo);
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

    /**
     * 判断是否参数全部设置
     *
     * @return
     */
    private boolean isSettingDeviceParam() {
        if (!isSettingDeviceOxy(configVoltageUp1.getText().toString(), 1, 1)) {
            return false;
        }
        if (!isSettingDeviceOxy(configVoltageDown1.getText().toString(), 1, 0)) {
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
        if (!isSettingDeviceElectricity(configVoltageUp1.getText().toString(), 1, 1)) {
            return false;
        }
        if (!isSettingDeviceElectricity(configVoltageDown1.getText().toString(), 1, 0)) {
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
                            control1Ainfo.setVisibility(View.VISIBLE);
                        } else {
                            control1Ainfo.setVisibility(View.GONE);
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
//        configHasa1.setOnCheckedChangeListener(onCheckedChangeListener);
//        configHasa2.setOnCheckedChangeListener(onCheckedChangeListener);
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
                    bindDeviceInfo.pondAddr = locPoi.address;
                    bindDeviceInfo.latitude = locPoi.lat + "";
                    bindDeviceInfo.longitude = locPoi.lng + "";
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
                    testOpenAndClose(deviceId, isOpen);
                }
            } else {
                showToast("请输入6位设备ID");
            }
        }
    }

    public void testOpenAndClose(String deviceId, String isOpen) {
        putData(HttpConfig.DEVICE_TEST_SWITCH.replace("{id}", deviceId), "{\"powerControl\":" + isOpen + "}", new ResponseCallBack<Boolean>() {
            @Override
            public void onSuccessResponse(Boolean d, String msg) {
                if (d) {
                    showToast("发送成功");
                    delay5sGetInfo();
                } else {
                    showToast("发送失败");
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

    public void testReset() {
        String deviceId = configDeviceId.getText().toString();
        putData(HttpConfig.DEVICE_TEST_RESET.replace("{id}", deviceId), new ResponseCallBack<JsonElement>() {
            @Override
            public void onSuccessResponse(JsonElement d, String msg) {
                showToast("发送成功");
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

    private void delay5sGetInfo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDeviceInfo();
            }
        }, 5000);
    }

    public void getDeviceInfo() {
        showLoading();
        String deviceId = configDeviceId.getText().toString();
        getData(HttpConfig.DEVICE_TEST_CONFIG.replace("{id}", deviceId), new ResponseCallBack<DeviceConfigInfo>() {
            @Override
            public void onSuccessResponse(DeviceConfigInfo d, String msg) {
                dismissLoading();
                if (d != null) {
                    configDeviceType.setText(d.type);
//                    configLimitUp.setText(d.oxyLimitUp + "");
//                    configLimitDown1.setText(d.oxyLimitDownOne + "");
//                    configLimitDown2.setText(d.oxyLimitDownTwo + "");
                    configAlarmLine1.setText(d.alertlineOne + "");
                    configAlarmLine2.setText(d.alertlineTwo + "");
                    if (d.automatic) {
//                        callbackApproveAgree.setChecked(true);
                    } else {
//                        callbackApproveRefuse.setChecked(true);
                    }
                    bindDeviceInfo.control1 = "关";
                    bindDeviceInfo.control2 = "关";
                    if (d.aeratorControlList != null && d.aeratorControlList.size() > 0) {
                        AeratorControlItem item1 = d.aeratorControlList.get(0);
//                        configControlCode1.setText(item1.ammeterId);
//                        configControlName1.setText(item1.name);
//                        configHasa1.setChecked("true".equals(item1.hasAmmeter));
//                        configPower1.setText(item1.power);
                        configVoltageUp1.setText(item1.voltageUp);
                        configVoltageDown1.setText(item1.voltageDown);
                        currentUp1.setText(item1.electricCurrentUp);
                        configCurrentDown1.setText(item1.electricCurrentDown);
                        bindDeviceInfo.control1 = item1.open ? "开" : "关";
                        if (item1.open) {
                            configControlState1.setText("开");
                            configControlState1.setTextColor(Color.GREEN);
                        } else {
                            configControlState1.setText("关");
                            configControlState1.setTextColor(Color.RED);
                        }
                    }
                    if (d.aeratorControlList != null && d.aeratorControlList.size() > 1) {
                        AeratorControlItem item1 = d.aeratorControlList.get(1);
//                        configControlCode2.setText(item1.ammeterId);
//                        configControlName2.setText(item1.name);
//                        configHasa2.setChecked("true".equals(item1.hasAmmeter));
//                        configPower2.setText(item1.power);
                        configVoltageUp2.setText(item1.voltageUp);
                        configVoltageDown2.setText(item1.voltageDown);
                        currentUp2.setText(item1.electricCurrentUp);
                        configCurrentDown2.setText(item1.electricCurrentDown);
                        bindDeviceInfo.control2 = item1.open ? "开" : "关";
                        if (item1.open) {
                            configControlState2.setText("开");
                            configControlState2.setTextColor(Color.GREEN);
                        } else {
                            configControlState2.setText("关");
                            configControlState2.setTextColor(Color.RED);
                        }
                    }
                    bindDeviceInfo.deviceId = d.id + "";
                    bindDeviceInfo.deviceIdentifier = d.identifier;
                    bindDeviceInfo.deviceModel = d.type;
                    bindDeviceInfo.state = Constants.DEVICE_STATE.get(d.workStatus);
                    bindDeviceInfo.oxyValue = d.dissolvedOxygen + "mg/L";
                    bindDeviceInfo.temperature = d.temperature + "℃";
                    bindDeviceInfo.ph = d.ph + "";
                    bindDeviceInfo.automatic = d.automatic ? "自动" : "手动";
                    if ((!isRepair || isChange) && !TextUtils.isEmpty(configDeviceType.getText()) && !TextUtils.isEmpty(configFishpond.getText())) {
                        String type = configDeviceType.getText().toString();
//                        configControlName1.setText(configFishpond.getText().toString() + type + "1#");
//                        configControlName2.setText(configFishpond.getText().toString() + type + "2#");
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


}
