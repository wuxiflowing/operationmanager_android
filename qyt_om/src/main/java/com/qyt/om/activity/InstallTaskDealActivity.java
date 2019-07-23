package com.qyt.om.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.listener.ListItemOperaListener;
import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.utils.CameraUtil;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.ConfirmDialog;
import com.bangqu.lib.widget.MenuDialog;
import com.bangqu.lib.widget.RoundImageView;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.bangqu.photos.GalleryActivity;
import com.bangqu.photos.PhotosActivity;
import com.bangqu.photos.util.ImageSelect;
import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.qyt.om.R;
import com.qyt.om.activity.device.Device2ConfigActivity;
import com.qyt.om.activity.device.FishpondInfoAdapter2;
import com.qyt.om.adapter.DeviceListAdapter;
import com.qyt.om.adapter.OrderItemAdapter;
import com.qyt.om.adapter.PhotosChoiceAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.BindDeviceInfo;
import com.qyt.om.model.DeviceChoiceModel;
import com.qyt.om.request.DeviceBind;
import com.qyt.om.request.InstallFinish;
import com.qyt.om.request.InstallSubmit;
import com.qyt.om.request.UploadModel;
import com.qyt.om.response.ChildDeviceListBean;
import com.qyt.om.response.DeviceControlInfoBean;
import com.qyt.om.response.EquipmentItem;
import com.qyt.om.response.InstallTaskData;
import com.qyt.om.utils.BaiduLocManager;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.utils.MapNaviUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class InstallTaskDealActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.install_customer_name)
    TextView installCustomerName;
    @BindView(R.id.install_customer_adress)
    TextView installCustomerAdress;
    @BindView(R.id.install_task_state)
    TextView installTaskState;
    @BindView(R.id.install_order_info)
    UnScrollListView installOrderInfo;
    @BindView(R.id.install_order_project)
    TextView installOrderProject;
    @BindView(R.id.install_device_list)
    UnScrollListView installDeviceList;
    @BindView(R.id.install_fishpond_list)
    UnScrollListView installFishpondList;
    @BindView(R.id.take_service_files)
    RecyclerView takeServiceFiles;
    @BindView(R.id.take_deposit_files)
    RecyclerView takeDepositFiles;
    @BindView(R.id.take_confirm_files)
    RecyclerView takeConfirmFiles;
    @BindView(R.id.install_deal_service)
    CheckBox installDealService;
    @BindView(R.id.install_service_info)
    LinearLayout installServiceInfo;
    @BindView(R.id.install_deal_deposit)
    CheckBox installDealDeposit;
    @BindView(R.id.install_deposit_info)
    LinearLayout installDepositInfo;
    @BindView(R.id.deposit_pay_crash)
    RadioButton depositPayCrash;
    @BindView(R.id.service_pay_crash)
    RadioButton servicePayCrash;
    @BindView(R.id.install_service_amount)
    TextView installServiceAmount;
    @BindView(R.id.install_service_real)
    EditText installServiceReal;
    @BindView(R.id.install_service_remark)
    EditText installServiceRemark;
    @BindView(R.id.install_deposit_amount)
    TextView installDepositAmount;
    @BindView(R.id.install_deposit_real)
    EditText installDepositReal;
    @BindView(R.id.install_deposit_remark)
    EditText installDepositRemark;
    @BindView(R.id.install_customer_header)
    RoundImageView installCustomerHeader;
    @BindView(R.id.install_pond_address)
    TextViewPlus installPondAddress;

    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<DeviceChoiceModel> deviceItems = new ArrayList<>();
    private DeviceListAdapter deviceListAdapter;
    //private ArrayList<BindDeviceInfo> deviceModels = new ArrayList<>();
    //private BindDeviceAdapter deviceAdapter;

    private PhotosChoiceAdapter serviceAdapter;
    private ArrayList<String> servicePhotos = new ArrayList<>();
    private PhotosChoiceAdapter depositAdapter;
    private ArrayList<String> depositPhotos = new ArrayList<>();
    private PhotosChoiceAdapter confirmAdapter;
    private ArrayList<String> confirmPhotos = new ArrayList<>();

    private File cameraFile;
    private final int REQUEST_DEVICE_CONFIG1 = 1000;
    private final int REQUEST_DEVICE_CONFIG2 = 1001;
    private final int REQUEST_DEVICE_CONFIG3 = 1002;
    private final int REQUEST_DEVICE_CONFIG4 = 1003;
    private String farmerID;
    private InstallSubmit installSubmit = new InstallSubmit();
    final int REQUEST_FORM_PHOTO = 30001;
    final int REQUEST_FORM_CAMERA = 30002;
    final int REQUEST_FARMER_PHOTO = 40001;
    final int REQUEST_FARMER_CAMERA = 40002;
    final int REQUEST_POND_PHOTO = 50001;
    final int REQUEST_POND_CAMERA = 50002;
    final String PHOTO_FORM = "photo_form";
    final String PHOTO_FARMER = "photo_farmer";
    final String PHOTO_POND = "photo_pond";
    final int PERMISSIONS_REQUEST_STORAGE = 1001;
    final int PERMISSIONS_REQUEST_CAMERA = 1002;
    private MenuDialog menuDialog;
    private int pos = -1;
    private String takID, mobile;
    private InstallTaskData taskData;
    private int count = -1;

    private FishpondInfoAdapter2 fishpondInfoAdapter2;
    private List<ChildDeviceListBean> mChildDeviceList;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_installtaskdeal);
        takID = getIntent().getStringExtra(Constants.INTENT_OBJECT);
        LogInfo.error(takID);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("任务详情");
        orderItemAdapter = new OrderItemAdapter(this, orderItems);
        installOrderInfo.setAdapter(orderItemAdapter);
        deviceListAdapter = new DeviceListAdapter(this, deviceItems);
        installDeviceList.setAdapter(deviceListAdapter);

        mChildDeviceList = new ArrayList<>();
        ArrayList<ChildDeviceListBean> deviceInfos = sharedPref.getJsonInfo(takID, new TypeToken<ArrayList<ChildDeviceListBean>>() {
        }.getType());
        if (deviceInfos != null && deviceInfos.size() > 0) {
            //deviceModels.addAll(deviceInfos);
            mChildDeviceList.addAll(deviceInfos);
        }
        //fixme
        //deviceAdapter = new BindDeviceAdapter(this, deviceModels);
        //installFishpondList.setAdapter(deviceAdapter);

        fishpondInfoAdapter2 = new FishpondInfoAdapter2(this, mChildDeviceList);
        fishpondInfoAdapter2.setType(1);
        installFishpondList.setVisibility(mChildDeviceList.size() == 0 ? View.GONE : View.VISIBLE);
        installFishpondList.setAdapter(fishpondInfoAdapter2);

        serviceAdapter = new PhotosChoiceAdapter(this, servicePhotos);
        takeServiceFiles.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        takeServiceFiles.setAdapter(serviceAdapter);
        depositAdapter = new PhotosChoiceAdapter(this, depositPhotos);
        takeDepositFiles.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        takeDepositFiles.setAdapter(depositAdapter);
        confirmAdapter = new PhotosChoiceAdapter(this, confirmPhotos);
        takeConfirmFiles.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        takeConfirmFiles.setAdapter(confirmAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        confirmAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                ImageSelect.mSelectedImage.clear();
                ImageSelect.mSelectedImage.addAll(confirmPhotos);
                if (position == confirmPhotos.size()) {
                    showPickHeaderMenu(PHOTO_FORM);
                } else {
                    Intent intent = new Intent(mContext, GalleryActivity.class);
                    intent.putStringArrayListExtra("path", new ArrayList<>(ImageSelect.mSelectedImage));
                    intent.putExtra("index", position);
                    startActivityForResult(intent, REQUEST_FORM_PHOTO);
                }
            }

            @Override
            public void onItemOpera(String tag, int position, String value) {

            }
        });
        serviceAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                ImageSelect.mSelectedImage.clear();
                ImageSelect.mSelectedImage.addAll(servicePhotos);
                if (position == servicePhotos.size()) {
                    showPickHeaderMenu(PHOTO_FARMER);
                } else {
                    Intent intent = new Intent(mContext, GalleryActivity.class);
                    intent.putStringArrayListExtra("path", new ArrayList<>(ImageSelect.mSelectedImage));
                    intent.putExtra("index", position);
                    startActivityForResult(intent, REQUEST_FARMER_PHOTO);
                }
            }

            @Override
            public void onItemOpera(String tag, int position, String value) {

            }
        });
        depositAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                ImageSelect.mSelectedImage.clear();
                ImageSelect.mSelectedImage.addAll(depositPhotos);
                if (position == depositPhotos.size()) {
                    showPickHeaderMenu(PHOTO_POND);
                } else {
                    Intent intent = new Intent(mContext, GalleryActivity.class);
                    intent.putStringArrayListExtra("path", new ArrayList<>(ImageSelect.mSelectedImage));
                    intent.putExtra("index", position);
                    startActivityForResult(intent, REQUEST_POND_PHOTO);
                }
            }

            @Override
            public void onItemOpera(String tag, int position, String value) {

            }
        });
        fishpondInfoAdapter2.setOperaListener(new ListItemOperaListener<String>() {
            @Override
            public void onItemOpera(String tag, final int position, String value) {
                switch (tag) {
                    case "fishpond":
                        new ConfirmDialog(mContext, "是否删除该设备？", new DialogConfirmListener() {
                            @Override
                            public void onDialogConfirm(boolean result, Object value) {
                                if (result) {
                                    mChildDeviceList.remove(position);
                                    fishpondInfoAdapter2.notifyDataSetChanged();
                                    installFishpondList.setVisibility(mChildDeviceList.size() == 0 ? View.GONE : View.VISIBLE);

                                }
                            }
                        }).show();
                        break;
                    case "device":
                        pos = position;
                        Bundle bundle1 = new Bundle();
                        bundle1.putString(Constants.INTENT_OBJECT, farmerID);
                        ChildDeviceListBean childDevice = mChildDeviceList.get(position);

                        if (Constants.DEVICE_TYPE_KD326.equals(childDevice.type)) {
                            BindDeviceInfo bindDeviceInfo = childDeviceListBean2BindDeviceInfo(childDevice);

                            bundle1.putParcelable("info", bindDeviceInfo);
                            bundle1.putString("contacters", childDevice.contacters);
                            bundle1.putString("contactPhone", childDevice.contactPhone);
                            bundle1.putString("standbyContact", childDevice.standbyContact);
                            bundle1.putString("standbyContactPhone", childDevice.standbyContactPhone);

                            bundle1.putString("nightContacters", childDevice.nightContacters);
                            bundle1.putString("nightContactPhone", childDevice.nightContactPhone);
                            bundle1.putString("standbyNightContact", childDevice.standbyNightContact);
                            bundle1.putString("standbyNightContactPhone", childDevice.standbyNightContactPhone);

                            goToActivityForResult(DeviceConfigActivity.class, bundle1, REQUEST_DEVICE_CONFIG2);
                        } else if (Constants.DEVICE_TYPE_QY601.equals(childDevice.type)) {

                            bundle1.putParcelable("info", childDevice);
                            bundle1.putString("contacters", childDevice.contacters);
                            bundle1.putString("contactPhone", childDevice.contactPhone);
                            bundle1.putString("standbyContact", childDevice.standbyContact);
                            bundle1.putString("standbyContactPhone", childDevice.standbyContactPhone);

                            bundle1.putString("nightContacters", childDevice.nightContacters);
                            bundle1.putString("nightContactPhone", childDevice.nightContactPhone);
                            bundle1.putString("standbyNightContact", childDevice.standbyNightContact);
                            bundle1.putString("standbyNightContactPhone", childDevice.standbyNightContactPhone);


                            goToActivityForResult(Device2ConfigActivity.class, bundle1, REQUEST_DEVICE_CONFIG4);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.install_deal_deposit:
                        if (isChecked) {
                            installDepositInfo.setVisibility(View.VISIBLE);
                        } else {
                            installDepositInfo.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.install_deal_service:
                        if (isChecked) {
                            installServiceInfo.setVisibility(View.VISIBLE);
                        } else {
                            installServiceInfo.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        installDealDeposit.setOnCheckedChangeListener(onCheckedChangeListener);
        installDealService.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    @Override
    protected void requestData() {
        super.requestData();
        showLoading();
        getData(HttpConfig.TASK_DETAIL.replace("{taskid}", takID), new ResponseCallBack<InstallTaskData>() {
            @Override
            public void onSuccessResponse(InstallTaskData d, String msg) {
                showTaskData(d);
                dismissLoading();
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

    private void showTaskData(InstallTaskData d) {
        taskData = d;
        mobile = d.txtPhone;
        farmerID = d.txtFarmerID;
        installCustomerName.setText(d.txtFarmer);
        installCustomerAdress.setText(d.txtFarmerAddress);
        Glide.with(this).load(d.picture).apply(Constants.REQUEST_OPTIONS).into(installCustomerHeader);
        installTaskState.setText("进行中");
        installOrderProject.setText("计划完成时间：" + d.calExpectedTime);
        installPondAddress.setText("安装地址：" + d.txtInstallAddress);
        count = 0;
        if (d.tabEquipmentList != null && d.tabEquipmentList.size() > 0) {
            for (EquipmentItem item : d.tabEquipmentList) {
                DeviceChoiceModel model = new DeviceChoiceModel();
                model.device_model = item.ITEM2;
                model.device_id = item.ITEM3;
                deviceItems.add(model);
                try {
                    count += Integer.parseInt(item.ITEM3);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            deviceListAdapter.notifyDataSetChanged();
        }
        orderItems.add("设备安装数量：" + count + "套");
//        orderItems.add("安装地址：" + d.txtInstallAddress);
        orderItems.add("养殖管家：" + d.txtFarmerManager);
        orderItems.add("代收押金费：¥" + d.txtDepositAmount);
        orderItems.add("代收服务费：¥" + d.txtServiceAmount);
        installDepositAmount.setText("押金费用¥" + d.txtDepositAmount);
        installServiceAmount.setText("服务费用¥" + d.txtServiceAmount);
        orderItemAdapter.notifyDataSetChanged();
        if (TextUtils.isEmpty(d.txtServiceAmount) || Float.valueOf(d.txtServiceAmount) <= 0) {
            installDealService.setClickable(false);
        }
        if (TextUtils.isEmpty(d.txtDepositAmount) || Float.valueOf(d.txtDepositAmount) <= 0) {
            installDealDeposit.setClickable(false);
        }
    }

    @OnClick({R.id.install_customer_call, R.id.install_add_fishpond, R.id.install_add_fishpond2,
            R.id.install_save, R.id.install_finish, R.id.install_pond_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_save:
                if (mChildDeviceList.size() > 0) {
                    sharedPref.setJsonInfo(takID, mChildDeviceList);
                    showToast("保存成功");
                }
                break;
            case R.id.install_finish:
                submitInstallTask();
                break;
            case R.id.install_customer_call:
                new ConfirmDialog(this, "拨打电话", mobile, "拨号", "取消", new DialogConfirmListener() {
                    @Override
                    public void onDialogConfirm(boolean result, Object v) {
                        if (result) {
                            AppUtils.dialPhone(mContext, mobile);
                        }
                    }
                }).show();
                break;
            case R.id.install_add_fishpond:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_OBJECT, farmerID);
                bundle.putString(Constants.INTENT_FLAG, taskData.txtFarmer);
                goToActivityForResult(DeviceConfigActivity.class, bundle, REQUEST_DEVICE_CONFIG1);
                break;
            case R.id.install_add_fishpond2:
                Bundle bundle2 = new Bundle();
                bundle2.putString(Constants.INTENT_OBJECT, farmerID);
                bundle2.putString(Constants.INTENT_FLAG, taskData.txtFarmer);
                goToActivityForResult(Device2ConfigActivity.class, bundle2, REQUEST_DEVICE_CONFIG3);
                break;
            case R.id.install_pond_address:
                if (taskData != null && !TextUtils.isEmpty(taskData.latitude) && !TextUtils.isEmpty(taskData.longitude)) {
                    try {
                        final double lat = Double.parseDouble(taskData.latitude);
                        final double lon = Double.parseDouble(taskData.longitude);
                        BaiduLocManager.getInstance(getApplicationContext()).startLocation(new BaiduLocManager.OnLocationComplete() {
                            @Override
                            public void onLocationComplete(BDLocation location) {
                                int code = location.getLocType();
                                if (code == 61 || code == 161) {
                                    MapNaviUtil.getInstance(mContext).
                                            openBaiduMap(new LatLng(location.getLatitude(), location.getLongitude()),
                                                    new LatLng(lat, lon),
                                                    taskData.txtInstallAddress);
                                } else {
                                    showToast("未定位到您的当前位置，无法导航");
                                }
                            }
                        });
                    } catch (NumberFormatException e) {
                        showToast("经纬度信息错误");
                    }
                } else {
                    showToast("未获取到经纬度信息");
                }
                break;
            default:
                break;
        }
    }

    private void submitInstallTask() {
        if (mChildDeviceList.size() == 0) {
            showToast("未安装设备");
            return;
        }
        if (mChildDeviceList.size() != count) {
            showToast("安装设备数量不符");
            return;
        }
        String loginid = sharedPref.getString(Constants.LOGIN_ID);
        installSubmit.loginid = loginid;
        installSubmit.appData.loginid = loginid;
        installSubmit.appData.farmerId = farmerID;
        if (TextUtils.isEmpty(installDepositReal.getText())) {
            installSubmit.appData.realDepositAmount = "0";
        } else {
            installSubmit.appData.realDepositAmount = installDepositReal.getText().toString();
        }
        if (TextUtils.isEmpty(installServiceReal.getText())) {
            installSubmit.appData.realServiceAmount = "0";
        } else {
            installSubmit.appData.realServiceAmount = installServiceReal.getText().toString();
        }
        installSubmit.appData.payServiceMethod = servicePayCrash.isChecked() ? "现金" : "银行汇款";
        installSubmit.appData.payDepositMethod = depositPayCrash.isChecked() ? "现金" : "银行汇款";
        installSubmit.appData.serviceNote = installServiceRemark.getText().toString();
        installSubmit.appData.depositNote = installDepositRemark.getText().toString();

        //设备列表
        if (!submitDeviceList()) {
            return;
        }
        showLoading();
        uploadPhotoList();
    }

    private int photoSize = 0;

    private void uploadPhotoList() {
        photoSize = confirmPhotos.size() + servicePhotos.size() + depositPhotos.size();
        if (photoSize == 0) {
            createCustomer();
            return;
        }
        for (String path : confirmPhotos) {
            uploadPhoto("installForm", path);
        }
        for (String path : servicePhotos) {
            uploadPhoto(" installService", path);
        }
        for (String path : depositPhotos) {
            uploadPhoto("installDeposit", path);
        }
    }

    private void uploadPhoto(String type, String path) {
        UploadModel uploadModel = new UploadModel();
        uploadModel.type = type;
        uploadModel.imageData = AppUtils.fileToBase64(path);
        uploadModel.imageName = System.currentTimeMillis() + path.substring(path.lastIndexOf("."));
        String loginId = sharedPref.getString(Constants.LOGIN_ID);
        postData(HttpConfig.UPLOAD_IMAGE.replace("{loginid}", loginId), uploadModel.toString(), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
                if (d.contains("installForm")) {
                    installSubmit.appData.confirmUrls.add(d);
                } else if (d.contains("installDeposit")) {
                    installSubmit.appData.depositPayUrls.add(d);
                } else if (d.contains("installService")) {
                    installSubmit.appData.servicePayUrls.add(d);
                }
                if (--photoSize == 0) {
                    createCustomer();
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

    private void createCustomer() {
        LogInfo.error(installSubmit.toString());
        postData(HttpConfig.CONFIRM_TASK_FINISH.replace("{taskid}", takID),
                installSubmit.toString(), new ResponseCallBack<JsonElement>() {
                    @Override
                    public void onSuccessResponse(JsonElement d, String msg) {
                        LogInfo.error(d);
                        sharedPref.removeKey(takID);
                        sharedPref.removeKey(taskData.txtFarmer);
                        onBackPressed();
                        dismissLoading();
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

    private void showPickHeaderMenu(final String tag) {
        if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_STORAGE)) {
            return;
        }
        if (menuDialog == null) {
            menuDialog = new MenuDialog(this);
        }
        menuDialog.setMenuItems(new String[]{"拍照", "图库选择"}, new MenuDialog.OnMenuItemClickedListener() {
            @Override
            public void MenuItemClicked(String value, int position) {
                switch (position) {
                    case 0:
                        switch (tag) {
                            case PHOTO_FORM:
                                takePhoto(REQUEST_FORM_CAMERA);
                                break;
                            case PHOTO_FARMER:
                                takePhoto(REQUEST_FARMER_CAMERA);
                                break;
                            case PHOTO_POND:
                                takePhoto(REQUEST_POND_CAMERA);
                                break;
                        }
                        break;
                    case 1:
                        switch (tag) {
                            case PHOTO_FORM:
                                pickPhotos(REQUEST_FORM_PHOTO);
                                break;
                            case PHOTO_FARMER:
                                pickPhotos(REQUEST_FARMER_PHOTO);
                                break;
                            case PHOTO_POND:
                                pickPhotos(REQUEST_POND_PHOTO);
                                break;
                        }
                        break;
                }
            }
        }).show();
    }

    private void takePhoto(int requestCode) {
        if (checkPermission(Manifest.permission.CAMERA, PERMISSIONS_REQUEST_CAMERA)) {
            cameraFile = new File(AppUtils.getDiskCacheDir(this), CameraUtil.getPhotoFileName());
            Uri fileUri = FileProvider.getUriForFile(this, "com.qyt.om.fileprovider", cameraFile);
            startActivityForResult(CameraUtil.getTakePickIntent(fileUri), requestCode);
        }
    }

    private void pickPhotos(int requestCode) {
        goToActivityForResult(PhotosActivity.class, requestCode);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_DEVICE_CONFIG1:
                    BindDeviceInfo info = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    ChildDeviceListBean bean = bindDeviceInfo2ChildDeviceListBean(info);
                    if (mChildDeviceList.contains(bean)) {
                        showToast("设备重复安装");
                        return;
                    }
                    mChildDeviceList.add(bean);
                    fishpondInfoAdapter2.notifyDataSetChanged();
                    installFishpondList.setVisibility(mChildDeviceList.size() == 0 ? View.GONE : View.VISIBLE);

                    handleLinkMan(data, bean);
                    break;
                case REQUEST_DEVICE_CONFIG2:
                    BindDeviceInfo info1 = data.getParcelableExtra(Constants.INTENT_OBJECT);

                    ChildDeviceListBean bean1 = bindDeviceInfo2ChildDeviceListBean(info1);
                    mChildDeviceList.set(pos, bean1);
                    fishpondInfoAdapter2.notifyDataSetChanged();
                    installFishpondList.setVisibility(mChildDeviceList.size() == 0 ? View.GONE : View.VISIBLE);

                    handleLinkMan(data, bean1);
                    break;
                case REQUEST_DEVICE_CONFIG3:
                    ChildDeviceListBean bean2 = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    if (mChildDeviceList.contains(bean2)) {
                        showToast("设备重复安装");
                        return;
                    }
                    mChildDeviceList.add(bean2);
                    fishpondInfoAdapter2.notifyDataSetChanged();
                    installFishpondList.setVisibility(mChildDeviceList.size() == 0 ? View.GONE : View.VISIBLE);

                    handleLinkMan(data, bean2);
                    break;
                case REQUEST_DEVICE_CONFIG4:
                    ChildDeviceListBean bean3 = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    mChildDeviceList.set(pos, bean3);
                    fishpondInfoAdapter2.notifyDataSetChanged();
                    installFishpondList.setVisibility(mChildDeviceList.size() == 0 ? View.GONE : View.VISIBLE);

                    handleLinkMan(data, bean3);
                    break;
                case REQUEST_FORM_CAMERA:
                    lubanCompress(REQUEST_FORM_CAMERA, cameraFile);
//                    confirmPhotos.clear();
//                    ImageSelect.mSelectedImage.add(cameraFile.getPath());
//                    confirmPhotos.addAll(ImageSelect.mSelectedImage);
//                    confirmAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_FORM_PHOTO:
                    confirmPhotos.clear();
                    confirmPhotos.addAll(ImageSelect.mSelectedImage);
                    confirmAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_FARMER_CAMERA:
                    lubanCompress(REQUEST_FARMER_CAMERA, cameraFile);
//                    servicePhotos.clear();
//                    ImageSelect.mSelectedImage.add(cameraFile.getPath());
//                    servicePhotos.addAll(ImageSelect.mSelectedImage);
//                    serviceAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_FARMER_PHOTO:
                    servicePhotos.clear();
                    servicePhotos.addAll(ImageSelect.mSelectedImage);
                    serviceAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_POND_CAMERA:
                    lubanCompress(REQUEST_POND_CAMERA, cameraFile);
//                    depositPhotos.clear();
//                    ImageSelect.mSelectedImage.add(cameraFile.getPath());
//                    depositPhotos.addAll(ImageSelect.mSelectedImage);
//                    depositAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_POND_PHOTO:
                    depositPhotos.clear();
                    depositPhotos.addAll(ImageSelect.mSelectedImage);
                    depositAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    private void handleLinkMan(Intent data, ChildDeviceListBean bean) {
        String contacters = data.getStringExtra("contacters");
        String contactPhone = data.getStringExtra("contactPhone");
        String nightContacters = data.getStringExtra("nightContacters");
        String nightContactPhone = data.getStringExtra("nightContactPhone");

        String standbyContact = data.getStringExtra("standbyContact");
        String standbyContactPhone = data.getStringExtra("standbyContactPhone");
        String standbyNightContact = data.getStringExtra("standbyNightContact");
        String standbyNightContactPhone = data.getStringExtra("standbyNightContactPhone");

        bean.contacters = contacters;
        bean.contactPhone = contactPhone;
        bean.nightContacters = nightContacters;
        bean.nightContactPhone = nightContactPhone;

        bean.standbyContact = standbyContact;
        bean.standbyContactPhone = standbyContactPhone;
        bean.standbyNightContact = standbyNightContact;
        bean.standbyNightContactPhone = standbyNightContactPhone;


    }

    private void lubanCompress(final int code, File file) {
        LogInfo.error(file.getPath());
        Luban.with(this)
                .load(file)
                .ignoreBy(200)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        switch (code) {
                            case REQUEST_FORM_CAMERA:
                                confirmPhotos.clear();
                                ImageSelect.mSelectedImage.add(file.getPath());
                                confirmPhotos.addAll(ImageSelect.mSelectedImage);
                                confirmAdapter.notifyDataSetChanged();
                                break;
                            case REQUEST_FARMER_CAMERA:
                                servicePhotos.clear();
                                ImageSelect.mSelectedImage.add(file.getPath());
                                servicePhotos.addAll(ImageSelect.mSelectedImage);
                                serviceAdapter.notifyDataSetChanged();
                                break;
                            case REQUEST_POND_CAMERA:
                                depositPhotos.clear();
                                ImageSelect.mSelectedImage.add(file.getPath());
                                depositPhotos.addAll(ImageSelect.mSelectedImage);
                                depositAdapter.notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        switch (code) {
                            case REQUEST_FORM_CAMERA:
                                confirmPhotos.clear();
                                ImageSelect.mSelectedImage.add(cameraFile.getPath());
                                confirmPhotos.addAll(ImageSelect.mSelectedImage);
                                confirmAdapter.notifyDataSetChanged();
                                break;
                            case REQUEST_FARMER_CAMERA:
                                servicePhotos.clear();
                                ImageSelect.mSelectedImage.add(cameraFile.getPath());
                                servicePhotos.addAll(ImageSelect.mSelectedImage);
                                serviceAdapter.notifyDataSetChanged();
                                break;
                            case REQUEST_POND_CAMERA:
                                depositPhotos.clear();
                                ImageSelect.mSelectedImage.add(cameraFile.getPath());
                                depositPhotos.addAll(ImageSelect.mSelectedImage);
                                depositAdapter.notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }
                    }
                }).launch();
    }

    /**
     * bean 转换
     *
     * @param info
     * @return
     */
    private ChildDeviceListBean bindDeviceInfo2ChildDeviceListBean(BindDeviceInfo info) {
        ChildDeviceListBean bean = new ChildDeviceListBean();
        bean.pondId = info.pondId;//鱼塘Id
        bean.pondName = info.pondName;//鱼塘名称
        bean.pondAddr = info.pondAddr;
        bean.longitude = info.longitude;
        bean.latitude = info.latitude;
        bean.id = info.deviceId;//设备Id
        bean.identifier = info.deviceIdentifier;//设备identifier
        bean.type = info.deviceModel;//设备型号
        bean.workStatus = info.state;//设备状态
        bean.oxy = !TextUtils.isEmpty(info.oxyValue) ? info.oxyValue.replace("mg/L", "") : "";//溶氧值
        bean.temp = !TextUtils.isEmpty(info.temperature) ? info.temperature.replace("℃", "") : "";//温度
        bean.ph = info.ph;//PH
        List<DeviceControlInfoBean> deviceControls = new ArrayList<>();

        DeviceControlInfoBean controlsBean = new DeviceControlInfoBean();
        controlsBean.controlId = 0;
        controlsBean.open = "开".equals(info.control1) ? "1" : "0";
        controlsBean.auto = (TextUtils.isEmpty(info.automatic) || "1".equals(info.automatic)) ? 1 : 0;
        deviceControls.add(controlsBean);

        DeviceControlInfoBean controlsBean1 = new DeviceControlInfoBean();
        controlsBean1.controlId = 1;
        controlsBean1.open = "开".equals(info.control2) ? "1" : "0";
        controlsBean1.auto = (TextUtils.isEmpty(info.automatic) || "1".equals(info.automatic)) ? 1 : 0;
        deviceControls.add(controlsBean1);

        DeviceControlInfoBean controlsBean2 = new DeviceControlInfoBean();
        controlsBean2.controlId = 2;
        controlsBean2.open = null;
        deviceControls.add(controlsBean2);

        DeviceControlInfoBean controlsBean3 = new DeviceControlInfoBean();
        controlsBean3.controlId = 3;
        controlsBean3.open = null;
        deviceControls.add(controlsBean3);

        bean.deviceControlInfoBeanList = deviceControls;
        return bean;
    }

    private BindDeviceInfo childDeviceListBean2BindDeviceInfo(ChildDeviceListBean info) {
        BindDeviceInfo bean = new BindDeviceInfo();
        bean.pondId = info.pondId;//鱼塘Id
        bean.pondName = info.pondName;//鱼塘名称
        bean.pondAddr = info.pondAddr;
        bean.longitude = info.longitude;
        bean.latitude = info.latitude;
        bean.pondId = info.pondId;//鱼塘Id
        bean.pondName = info.pondName;//鱼塘名称
        bean.pondAddr = info.pondAddr;
        bean.longitude = info.longitude;
        bean.latitude = info.latitude;
        bean.deviceId = info.id;//设备Id
        bean.deviceIdentifier = info.identifier;//设备identifier
        bean.deviceModel = info.type;//设备型号
        bean.state = info.workStatus;//设备状态      workStatus
        bean.oxyValue = info.oxy;//溶氧值
        bean.temperature = info.temp;//温度
        bean.ph = info.ph;//PH
        DeviceControlInfoBean deviceControl = info.deviceControlInfoBeanList.get(0);

        bean.control1 = "1".equals(deviceControl.open) ? "开" : "关";//控制器1    开 关
        bean.control2 = "1".equals(deviceControl.open) ? "开" : "关";//控制器2
        bean.automatic = deviceControl.auto + "";//手动自动
        return bean;
    }

    /**
     * 添加设备列表
     */
    private boolean submitDeviceList() {
        installSubmit.appData.bindDeviceList.clear();
        HashSet<String> deviceSet = new HashSet<>();
        for (ChildDeviceListBean bindInfo : mChildDeviceList) {
            if (deviceSet.contains(bindInfo.identifier)) {
                showToast("安装设备重复");
                return false;
            } else {
                deviceSet.add(bindInfo.identifier);
            }
            DeviceBind deviceBind = new DeviceBind();
            deviceBind.deviceId = bindInfo.identifier;
            deviceBind.pondId = bindInfo.pondId;
            deviceBind.pondName = bindInfo.pondName;
            deviceBind.pondAddr = bindInfo.pondAddr;
            deviceBind.latitude = bindInfo.latitude;
            deviceBind.longitude = bindInfo.longitude;

            deviceBind.contacters = bindInfo.contacters;//               白班联系人姓名
            deviceBind.contactPhone = bindInfo.contactPhone;//             白班联系人电话
            deviceBind.standbyContactPhone = bindInfo.standbyContactPhone;//      白班备用联系人姓名
            deviceBind.standbyContact = bindInfo.standbyContact;//           白班备用联系人电话

            deviceBind.nightContactPhone = bindInfo.nightContactPhone;//        晚班联系人姓名
            deviceBind.nightContacters = bindInfo.nightContacters;//          晚班联系人电话
            deviceBind.standbyNightContact = bindInfo.standbyNightContact;//      晚班备用联系人姓名
            deviceBind.standbyNightContactPhone = bindInfo.standbyNightContactPhone;// 晚班备用联系人电话

            installSubmit.appData.bindDeviceList.add(deviceBind);
        }
        return true;
    }

    /**
     * 判断设备清单
     */
    private void aaa() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageSelect.mSelectedImage.clear();
    }

}
