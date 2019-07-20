package com.qyt.om.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.TextViewPlus;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.activity.CaptureActivity;
import com.qyt.om.R;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.BindDeviceInfo;
import com.qyt.om.model.FishPondInfo;
import com.qyt.om.model.LocPoi;
import com.qyt.om.request.DeviceConfigPut;
import com.qyt.om.response.AeratorControlItem;
import com.qyt.om.response.DeviceConfigInfo;
import com.qyt.om.response.LinkManBean;
import com.qyt.om.response.PondLinkMan;
import com.qyt.om.utils.BaiduLocManager;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.widget.EditContactsDialog;
import com.qyt.om.widget.FishPondDialog;
import com.qyt.om.widget.ListLinkManPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceConfigActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.config_device_id)
    EditText configDeviceId;
    @BindView(R.id.config_device_type)
    TextViewPlus configDeviceType;
    @BindView(R.id.config_fishpond)
    TextViewPlus configFishpond;
    @BindView(R.id.callback_approve_agree)
    RadioButton callbackApproveAgree;
    @BindView(R.id.callback_approve_refuse)
    RadioButton callbackApproveRefuse;
    @BindView(R.id.config_control_name1)
    TextViewPlus configControlName1;
    @BindView(R.id.config_hasa1)
    CheckBox configHasa1;
    @BindView(R.id.config_control_code1)
    EditText configControlCode1;
    @BindView(R.id.config_power1)
    EditText configPower1;
    @BindView(R.id.config_voltage_up1)
    EditText configVoltageUp1;
    @BindView(R.id.config_voltage_down1)
    EditText configVoltageDown1;
    @BindView(R.id.current_up1)
    EditText currentUp1;
    @BindView(R.id.config_current_down1)
    EditText configCurrentDown1;
    @BindView(R.id.control1_ainfo)
    LinearLayout control1Ainfo;
    @BindView(R.id.config_control_name2)
    TextViewPlus configControlName2;
    @BindView(R.id.config_hasa2)
    CheckBox configHasa2;
    @BindView(R.id.config_control_code2)
    EditText configControlCode2;
    @BindView(R.id.config_power2)
    EditText configPower2;
    @BindView(R.id.config_voltage_up2)
    EditText configVoltageUp2;
    @BindView(R.id.config_voltage_down2)
    EditText configVoltageDown2;
    @BindView(R.id.current_up2)
    EditText currentUp2;
    @BindView(R.id.config_current_down2)
    EditText configCurrentDown2;
    @BindView(R.id.control2_ainfo)
    LinearLayout control2Ainfo;
    @BindView(R.id.config_limit_up)
    EditText configLimitUp;
    @BindView(R.id.config_limit_down1)
    EditText configLimitDown1;
    @BindView(R.id.config_limit_down2)
    EditText configLimitDown2;
    @BindView(R.id.config_alarm_line1)
    EditText configAlarmLine1;
    @BindView(R.id.config_alarm_line2)
    EditText configAlarmLine2;
    @BindView(R.id.config_control_state1)
    TextView configControlState1;
    @BindView(R.id.config_control_state2)
    TextView configControlState2;
    @BindView(R.id.install_pond_address)
    EditText installPondAddress;
    @BindView(R.id.tv_add_contacts)
    TextView tvAddContacts;
    @BindView(R.id.tv_linkman_day)
    TextView tvLinkmanDay;
    @BindView(R.id.tv_linkman_day2)
    TextView tvLinkmanDay2;
    @BindView(R.id.tv_linkman_night)
    TextView tvLinkmanNight;
    @BindView(R.id.tv_linkman_night2)
    TextView tvLinkmanNight2;

    private ArrayList<FishPondInfo> fishPondInfos = new ArrayList<>();
    private final int REQUEST_SCANNING = 100;
    private final int REQUEST_LOCATION = 1001;
    private String farmerID, farmerName;
    private boolean isRepair;
    private BindDeviceInfo bindDeviceInfo = new BindDeviceInfo();
    private String installMessage;
    private boolean isChange = false;

    //联系人列表
    private List<LinkManBean> mLinkManList;
    private ListLinkManPopupWindow mListLinkManPopupWindow;
    private Handler mHander;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_deviceconfig);
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
            bindDeviceInfo = getIntent().getParcelableExtra("info");
            if (bindDeviceInfo != null) {
                configDeviceId.setText(bindDeviceInfo.deviceIdentifier);
                configDeviceType.setText(bindDeviceInfo.deviceModel);
                configFishpond.setText(bindDeviceInfo.pondName);
                getDeviceInfo();
                // getPondLinkManSetting(bindDeviceInfo.pondId);
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

    @OnClick({R.id.device_scanning, R.id.device_connect, R.id.device_open, R.id.device_close, R.id.device_reset
            , R.id.config_save, R.id.config_cancel, R.id.config_fishpond, R.id.install_location,
            R.id.install_pond_address, R.id.tv_add_contacts,
            R.id.tv_linkman_day, R.id.tv_linkman_day2, R.id.tv_linkman_night, R.id.tv_linkman_night2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_location:
            case R.id.install_pond_address:
                goToActivityForResult(LocationActivity.class, REQUEST_LOCATION);
                break;
            case R.id.tv_add_contacts:
                //添加联系人接口
                new EditContactsDialog(DeviceConfigActivity.this, new EditContactsDialog.OnEditContactListener() {
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
            case R.id.device_open:
                if (!TextUtils.isEmpty(configDeviceId.getText())) {
                    if (configDeviceId.getText().toString().length() == 6) {
                        if (!isRepair && !TextUtils.isEmpty(installMessage)) {
                            showToast(installMessage);
                        } else if (isChange && !TextUtils.isEmpty(installMessage)) {
                            showToast(installMessage);
                        } else {
                            testOpenAndClose("1");
                        }
                    } else {
                        showToast("请输入6位设备ID");
                    }
                }
                break;
            case R.id.device_close:
                if (!TextUtils.isEmpty(configDeviceId.getText())) {
                    if (configDeviceId.getText().toString().length() == 6) {
                        if (!isRepair && !TextUtils.isEmpty(installMessage)) {
                            showToast(installMessage);
                        } else if (isChange && !TextUtils.isEmpty(installMessage)) {
                            showToast(installMessage);
                        } else {
                            testOpenAndClose("0");
                        }
                    } else {
                        showToast("请输入6位设备ID");
                    }
                }
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
                            bindDeviceInfo.pondId = fishPondInfo.pondId;
                            bindDeviceInfo.pondName = fishPondInfo.name;

                            getPondLinkManSetting(fishPondInfo.pondId);
                        } else {
                            configFishpond.setText(String.valueOf(value));
                            bindDeviceInfo.pondId = "";
                            bindDeviceInfo.pondName = String.valueOf(value);
                        }
                        if (!TextUtils.isEmpty(configDeviceType.getText())) {
                            String type = configDeviceType.getText().toString();
                            configControlName1.setText(configFishpond.getText().toString() + type + "1#");
                            configControlName2.setText(configFishpond.getText().toString() + type + "2#");
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
        if (TextUtils.isEmpty(configLimitUp.getText())) {
            showToast("请输入传感器上限");
            return;
        }
        if (TextUtils.isEmpty(configLimitDown1.getText())) {
            showToast("请输入传感器下限1");
            return;
        }
        if (TextUtils.isEmpty(configLimitDown2.getText())) {
            showToast("请输入传感器下限2");
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
        showLoading();
        DeviceConfigPut deviceConfigPut = new DeviceConfigPut();
        deviceConfigPut.oxyLimitUp = configLimitUp.getText().toString();
        deviceConfigPut.oxyLimitDownOne = configLimitDown1.getText().toString();
        deviceConfigPut.oxyLimitDownTwo = configLimitDown2.getText().toString();
        deviceConfigPut.alertlineOne = configAlarmLine1.getText().toString();
        deviceConfigPut.alertlineTwo = configAlarmLine2.getText().toString();
        deviceConfigPut.automatic = callbackApproveAgree.isChecked();
        AeratorControlItem item1 = new AeratorControlItem();
        AeratorControlItem item2 = new AeratorControlItem();
        item1.name = configControlName1.getText().toString();
        item2.name = configControlName2.getText().toString();
        item1.hasAmmeter = configHasa1.isChecked();
        item2.hasAmmeter = configHasa2.isChecked();
        item1.ammeterId = configControlCode1.getText().toString();
        item2.ammeterId = configControlCode2.getText().toString();
        item1.power = configPower1.getText().toString();
        item2.power = configPower2.getText().toString();
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
                bindDeviceInfo.automatic = callbackApproveAgree.isChecked() ? "1" : "0";
                intent.putExtra(Constants.INTENT_OBJECT, bindDeviceInfo);
                //设置联系人
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
                intent.putExtra("contacters", contacters[1]);
                intent.putExtra("contactPhone", contacters[0]);
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
        configHasa1.setOnCheckedChangeListener(onCheckedChangeListener);
        configHasa2.setOnCheckedChangeListener(onCheckedChangeListener);
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

    public void testOpenAndClose(String isOpen) {
        String deviceId = configDeviceId.getText().toString();
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
        showLoading();
        String deviceId = configDeviceId.getText().toString();
        putData(HttpConfig.DEVICE_TEST_RESET.replace("{id}", deviceId), new ResponseCallBack<JsonElement>() {
            @Override
            public void onSuccessResponse(JsonElement d, String msg) {
                dismissLoading();
                showToast("发送成功");
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
        getData(HttpConfig.DEVICE_TEST_CONFIG.replace("{id}", deviceId), new ResponseCallBack<DeviceConfigInfo>() {
            @Override
            public void onSuccessResponse(DeviceConfigInfo d, String msg) {
                dismissLoading();
                if (d != null) {

                    configDeviceType.setText(d.type);
                    configLimitUp.setText(d.oxyLimitUp + "");
                    configLimitDown1.setText(d.oxyLimitDownOne + "");
                    configLimitDown2.setText(d.oxyLimitDownTwo + "");
                    configAlarmLine1.setText(d.alertlineOne + "");
                    configAlarmLine2.setText(d.alertlineTwo + "");
                    if (d.automatic) {
                        callbackApproveAgree.setChecked(true);
                    } else {
                        callbackApproveRefuse.setChecked(true);
                    }
                    bindDeviceInfo.control1 = "关";
                    bindDeviceInfo.control2 = "关";
                    if (d.aeratorControlList != null && d.aeratorControlList.size() > 0) {
                        AeratorControlItem item1 = d.aeratorControlList.get(0);
                        configControlCode1.setText(item1.ammeterId);
                        configControlName1.setText(item1.name);
                        configHasa1.setChecked("true".equals(item1.hasAmmeter));
                        configPower1.setText(item1.power);
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
                        configControlCode2.setText(item1.ammeterId);
                        configControlName2.setText(item1.name);
                        configHasa2.setChecked("true".equals(item1.hasAmmeter));
                        configPower2.setText(item1.power);
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
                    bindDeviceInfo.oxyValue = d.dissolvedOxygen;
                    bindDeviceInfo.temperature = d.temperature;
                    bindDeviceInfo.ph = d.ph + "";
                    bindDeviceInfo.automatic = d.automatic ? "自动" : "手动";
                    if ((!isRepair || isChange) && !TextUtils.isEmpty(configDeviceType.getText()) && !TextUtils.isEmpty(configFishpond.getText())) {
                        String type = configDeviceType.getText().toString();
                        configControlName1.setText(configFishpond.getText().toString() + type + "1#");
                        configControlName2.setText(configFishpond.getText().toString() + type + "2#");
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
     * @param pondId 鱼塘id
     */
    private void getPondLinkManSetting(String pondId) {
        if (TextUtils.isEmpty(pondId)) {
            return;
        }
        getData(HttpConfig.LINKMAN_ADDED_POND.replace("{deviceId}", pondId),
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

    @Override
    protected void onDestroy() {
        if (mHander != null) {
            mHander.removeCallbacksAndMessages(null);
        }
        super.onDestroy();

    }
}
