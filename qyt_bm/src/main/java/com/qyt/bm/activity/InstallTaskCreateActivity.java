package com.qyt.bm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.utils.DateFormatUtil;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.bangqu.photos.GalleryActivity;
import com.bigkoo.pickerview.TimePickerView;
import com.qyt.bm.R;
import com.qyt.bm.adapter.InstallDeviceChoiceAdapter;
import com.qyt.bm.adapter.PhotosAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.ContactItem;
import com.qyt.bm.model.LocPoi;
import com.qyt.bm.model.OperatorItem;
import com.qyt.bm.request.InstallTask;
import com.qyt.bm.response.PactDeviceInfo;
import com.qyt.bm.response.PactItem;
import com.qyt.bm.utils.BaiduLocManager;
import com.qyt.bm.utils.LogInfo;
import com.qyt.bm.widget.SentryListDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class InstallTaskCreateActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.install_customer_name)
    TextViewPlus installCustomerName;
    @BindView(R.id.install_customer_address)
    TextView installCustomerAddress;
    @BindView(R.id.install_customer_mobile)
    TextView installCustomerMobile;
    @BindView(R.id.install_pact_pics)
    RecyclerView installPactPics;
    @BindView(R.id.install_pact_pics_service)
    RecyclerView installPactPicsService;
    @BindView(R.id.install_device_list)
    UnScrollListView installDeviceList;
    @BindView(R.id.install_operator)
    TextViewPlus installOperator;
    @BindView(R.id.install_device_count)
    TextView installDeviceCount;
    @BindView(R.id.install_pond_address)
    EditText installPondAddress;
    @BindView(R.id.install_project_time)
    TextViewPlus installProjectTome;
    @BindView(R.id.install_pact_name)
    TextViewPlus installChoicePact;
    @BindView(R.id.install_pact_name_service)
    TextViewPlus installChoicePactService;
    @BindView(R.id.deposit_cost)
    EditText depositCost;
    @BindView(R.id.service_cost)
    EditText serviceCost;
    @BindView(R.id.depositNeed)
    CheckBox depositNeed;
    @BindView(R.id.serviceNeed)
    CheckBox serviceNeed;

    private PhotosAdapter photosAdapter;
    private ArrayList<String> photos = new ArrayList<>();
    private PhotosAdapter photosAdapter2;
    private ArrayList<String> photos2 = new ArrayList<>();
    private InstallDeviceChoiceAdapter deviceChoiceAdapter;
    private ArrayList<PactDeviceInfo> deviceChoiceModels = new ArrayList<>();
    private final int REQUEST_CUSTOMER = 100;
    private final int REQUEST_OPERATOR = 101;
    private final int REQUEST_LOCATION = 1001;
    private int deviceCount = 0;
    private TimePickerView mTimePickerView;
    private ArrayList<PactItem.ContractDataBean> pactItems = new ArrayList<>();
    private InstallTask installTask = new InstallTask();
    private ContactItem contactItem;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_installtaskcreate);
        contactItem = getIntent().getParcelableExtra(Constants.INTENT_OBJECT);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("新建安装任务");
        final String loginid = sharedPref.getString(Constants.LOGIN_ID);
        installTask.loginid = loginid;
        installTask.appData.loginid = loginid;
        photosAdapter = new PhotosAdapter(this, photos);
        installPactPics.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        installPactPics.setAdapter(photosAdapter);
        photosAdapter2 = new PhotosAdapter(this, photos2);
        installPactPicsService.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        installPactPicsService.setAdapter(photosAdapter2);
        deviceChoiceAdapter = new InstallDeviceChoiceAdapter(this, deviceChoiceModels);
        installDeviceList.setAdapter(deviceChoiceAdapter);
        BaiduLocManager.getInstance(getApplicationContext()).startLocation(new BaiduLocManager.OnLocationComplete() {
            @Override
            public void onLocationComplete(BDLocation location) {
                String province = location.getProvince();    //获取省份
                String city = location.getCity();    //获取城市
                String district = location.getDistrict();    //获取区县
                String street = location.getStreet();    //获取街道信息
                if (!TextUtils.isEmpty(location.getProvince()) && !"null".equals(location.getProvince())) {
                    installPondAddress.setText(province + city + district + street);
                    installTask.appData.latitude = location.getLatitude() + "";
                    installTask.appData.longitude = location.getLongitude() + "";
                }
            }
        });
        if (contactItem != null) {
            installCustomerName.setText(contactItem.name);
            installCustomerAddress.setText(contactItem.farmerAdd);
            installCustomerMobile.setText(contactItem.mobile);
            getPactList(contactItem.farmerId);
            installTask.farmerId = contactItem.farmerId;
            installTask.appData.farmerId = contactItem.farmerId;
        }
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        depositNeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    depositCost.setVisibility(View.VISIBLE);
                } else {
                    depositCost.setVisibility(View.INVISIBLE);
                    depositCost.setText("");
                }
            }
        });
        serviceNeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serviceCost.setVisibility(View.VISIBLE);
                } else {
                    serviceCost.setVisibility(View.INVISIBLE);
                    serviceCost.setText("");
                }
            }
        });
        deviceChoiceAdapter.setOnItemCountChange(new InstallDeviceChoiceAdapter.OnItemCountChange() {
            @Override
            public void onCountChange(int change) {
                deviceCount += change;
                installDeviceCount.setText("合计：" + deviceCount + "套");
            }
        });
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
        photosAdapter2.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("path", photos2);
                bundle.putBoolean("show", true);
                goToActivity(GalleryActivity.class, bundle);
            }

            @Override
            public void onItemOpera(String tag, int position, String value) {

            }
        });
    }

    @OnClick({R.id.install_choice_customer, R.id.install_project_time, R.id.install_choice_pact, R.id.install_choice_operator
            , R.id.submit_confirm, R.id.install_location, R.id.install_pond_address, R.id.install_choice_pact_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_location:
            case R.id.install_pond_address:
                goToActivityForResult(LocationActivity.class, REQUEST_LOCATION);
                break;
            case R.id.install_choice_customer:
                goToActivityForResult(ChoiceCustomerActivity.class, REQUEST_CUSTOMER);
                break;
            case R.id.install_choice_pact:
                if (TextUtils.isEmpty(installTask.farmerId)) {
                    showToast("请选择养殖户");
                    return;
                }
                if (pactItems.size() > 0) {
                    ArrayList<PactItem.ContractDataBean> pacts = new ArrayList<>();
                    for (PactItem.ContractDataBean bean : pactItems) {
                        if ("deposit".equals(bean.contractType)) {
                            pacts.add(bean);
                        }
                    }
                    if (pacts.size() == 0) {
                        showToast("未获取到押金合同信息");
                        return;
                    }
                    new SentryListDialog(mContext, pacts, new DialogConfirmListener() {
                        @Override
                        public void onDialogConfirm(boolean result, Object value) {
                            PactItem.ContractDataBean bean = (PactItem.ContractDataBean) value;
                            installChoicePact.setText(bean.contractName);
                            installTask.appData.contractId = bean.contractId;
                            showPactPhotos(bean.contractImage);
                            refreshDeviceList(bean.contractDeviceList);
                        }
                    }).show();
                } else {
                    showToast("未获取到押金合同信息");
                }
                break;
            case R.id.install_choice_pact_service:
                if (TextUtils.isEmpty(installTask.farmerId)) {
                    showToast("请选择养殖户");
                    return;
                }
                if (pactItems.size() > 0) {
                    ArrayList<PactItem.ContractDataBean> pacts = new ArrayList<>();
                    for (PactItem.ContractDataBean bean : pactItems) {
                        if ("service".equals(bean.contractType)) {
                            pacts.add(bean);
                        }
                    }
                    if (pacts.size() == 0) {
                        showToast("未获取到服务合同信息");
                        return;
                    }
                    new SentryListDialog(mContext, pacts, new DialogConfirmListener() {
                        @Override
                        public void onDialogConfirm(boolean result, Object value) {
                            PactItem.ContractDataBean bean = (PactItem.ContractDataBean) value;
                            installChoicePactService.setText(bean.contractName);
                            showServicePactPhotos(bean.contractImage);
                        }
                    }).show();
                } else {
                    showToast("未获取到服务合同信息");
                }
                break;
            case R.id.install_choice_operator:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_OBJECT, "安装设备数量：");
                goToActivityForResult(ChoiceOperatorActivity.class, bundle, REQUEST_OPERATOR);
                break;
            case R.id.install_project_time:
                AppUtils.hideSoftInput(this, getCurrentFocus());
                showPickBirthday();
                break;
            case R.id.submit_confirm:
                if (checkInstallData()) {
                    showLoading();
                    createCustomer();
                }
                break;
        }
    }

    private void refreshDeviceList(ArrayList<PactDeviceInfo> contractDeviceList) {
        deviceCount = 0;
        installDeviceCount.setText("合计：" + deviceCount + "套");
        deviceChoiceModels.clear();
        if (contractDeviceList != null) {
            deviceChoiceModels.addAll(contractDeviceList);
        }
        if (deviceChoiceModels.size() == 0) {
            showToast("无待安装设备清单");
        }
        deviceChoiceAdapter.notifyDataSetChanged();
    }

    private void createCustomer() {
        LogInfo.error(installTask.toString());
        postData(HttpConfig.CREATE_TASK_CUSTOMER.replace("{PROID}", Constants.INSTALL_CREATE), installTask.toString(), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
                dismissLoading();
                LogInfo.error(d);
                onBackPressed();
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

    private boolean checkInstallData() {
        if (TextUtils.isEmpty(installTask.farmerId)) {
            showToast("请选择养殖户");
            return false;
        }
        if (TextUtils.isEmpty(installTask.appData.memId)) {
            showToast("请选择运维人员");
            return false;
        }
        if (TextUtils.isEmpty(installPondAddress.getText())) {
            showToast("请输入安装地址");
            return false;
        }
        if (TextUtils.isEmpty(installProjectTome.getText())) {
            showToast("请选择预计安装时间");
            return false;
        }
        ArrayList<InstallTask.DeviceInfo> deviceInfos = new ArrayList<>();
        for (PactDeviceInfo deviceChoiceModel : deviceChoiceModels) {
            if (deviceChoiceModel.selected) {
                InstallTask.DeviceInfo info = installTask.new DeviceInfo();
                info.deviceTypeId = deviceChoiceModel.contractDeviceID;
                info.deviceTypeName = deviceChoiceModel.contractDeviceType;
                info.deviceCount = deviceChoiceModel.count + "";
                deviceInfos.add(info);
            }
        }
        if (deviceInfos.size() == 0) {
            showToast("请选择安装设备");
            return false;
        }
        installTask.appData.installAddress = installPondAddress.getText().toString();
        installTask.appData.reckonDate = installProjectTome.getText().toString();
        installTask.appData.deviceList = deviceInfos;
        if (depositNeed.isChecked()) {
            if (TextUtils.isEmpty(depositCost.getText())) {
                showToast("请输入代收金额");
                return false;
            }
            installTask.appData.depositAmount = depositCost.getText().toString();
        }
        if (serviceNeed.isChecked()) {
            if (TextUtils.isEmpty(serviceCost.getText())) {
                showToast("请输入代收金额");
                return false;
            }
            installTask.appData.serviceAmount = serviceCost.getText().toString();
        }
        return true;
    }

    private void showServicePactPhotos(String contractImage) {
        if (TextUtils.isEmpty(contractImage)) return;
        String[] images = contractImage.split(",");
        photos2.clear();
        photos2.addAll(Arrays.asList(images));
        photosAdapter2.notifyDataSetChanged();
    }

    private void showPactPhotos(String contractImage) {
        if (TextUtils.isEmpty(contractImage)) return;
        String[] images = contractImage.split(",");
        photos.clear();
        photos.addAll(Arrays.asList(images));
        photosAdapter.notifyDataSetChanged();
    }

    private void showPickBirthday() {
        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(installProjectTome.getText())) {
            calendar.setTime(DateFormatUtil.getString2Date(installProjectTome.getText().toString(), DateFormatUtil.DATE_FORMAT));
        }
        //时间选择器
        if (mTimePickerView == null) {
            mTimePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    String birth = DateFormatUtil.getDate2Str(date, DateFormatUtil.DATE_FORMAT);
                    installProjectTome.setText(birth);
                }
            }).setType(new boolean[]{true, true, true, false, false, false}).build();
        }
        mTimePickerView.setDate(calendar);
        mTimePickerView.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CUSTOMER:
                    ContactItem item = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    installCustomerName.setText(item.name);
                    installCustomerAddress.setText(item.farmerAdd);
                    installCustomerMobile.setText(item.mobile);
                    getPactList(item.farmerId);
                    installTask.farmerId = item.farmerId;
                    installTask.appData.farmerId = item.farmerId;
                    installChoicePact.setText("");
                    photos.clear();
                    photosAdapter.notifyDataSetChanged();
                    installChoicePactService.setText("");
                    photos2.clear();
                    photosAdapter2.notifyDataSetChanged();
                    break;
                case REQUEST_OPERATOR:
                    OperatorItem operator = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    installOperator.setText(operator.memName);
                    installTask.appData.memId = operator.memId;
                    break;
                case REQUEST_LOCATION:
                    LocPoi locPoi = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    installPondAddress.setText(locPoi.address);
                    installTask.appData.installAddress = locPoi.address;
                    installTask.appData.latitude = locPoi.lat + "";
                    installTask.appData.longitude = locPoi.lng + "";
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getPactList(String farmerId) {
        /*获取养殖户合同列表*/
        getData(HttpConfig.CONTRACT_LIST.replace("{farmerId}", farmerId), new ResponseCallBack<ArrayList<PactItem>>() {
            @Override
            public void onSuccessResponse(ArrayList<PactItem> d, String msg) {
                pactItems.clear();
                if (d != null && d.size() > 0 && d.get(0).contractData != null) {
                    pactItems.addAll(d.get(0).contractData);
                }
//                getPactAmount();
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
