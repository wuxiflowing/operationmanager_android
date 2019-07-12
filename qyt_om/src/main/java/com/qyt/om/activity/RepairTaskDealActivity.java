package com.qyt.om.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.bangqu.lib.listener.DialogConfirmListener;
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
import com.qyt.om.R;
import com.qyt.om.adapter.OrderItemAdapter;
import com.qyt.om.adapter.PhotosAdapter;
import com.qyt.om.adapter.PhotosChoiceAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.BindDeviceInfo;
import com.qyt.om.request.RepairFinish;
import com.qyt.om.request.RepairSubmit;
import com.qyt.om.request.UploadModel;
import com.qyt.om.response.RepairTaskData;
import com.qyt.om.response.RepairTaskItem;
import com.qyt.om.utils.BaiduLocManager;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.utils.MapNaviUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class RepairTaskDealActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.install_customer_name)
    TextView installCustomerName;
    @BindView(R.id.install_customer_adress)
    TextView installCustomerAdress;
    @BindView(R.id.install_order_info)
    UnScrollListView installOrderInfo;
    @BindView(R.id.repair_pictures)
    RecyclerView repairPictures;
    @BindView(R.id.repair_receives)
    RecyclerView repairReceives;
    @BindView(R.id.install_task_state)
    TextView installTaskState;
    @BindView(R.id.repair_issue_list)
    GridLayout repairIssueList;
    @BindView(R.id.repair_context_list)
    GridLayout repairContextList;
    @BindView(R.id.repair_device_id)
    TextView repairDeviceId;
    @BindView(R.id.repair_issue_input)
    EditText repairIssueInput;
    @BindView(R.id.repair_remarks)
    EditText repairRemarks;
    @BindView(R.id.repair_context_input)
    EditText repairContextInput;
    @BindView(R.id.callback_approve_agree)
    RadioButton callbackApproveAgree;
    @BindView(R.id.task_create_pics)
    RecyclerView taskCreatePics;
    @BindView(R.id.install_customer_header)
    RoundImageView installCustomerHeader;
    @BindView(R.id.repair_pond_address)
    TextViewPlus repairPondAddress;

    private ArrayList<String> createPhotos = new ArrayList<>();
    private PhotosAdapter createAdapter;
    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private PhotosChoiceAdapter repairAdapter;
    private ArrayList<String> repairPhotos = new ArrayList<>();
    private PhotosChoiceAdapter payAdapter;
    private ArrayList<String> payPhotos = new ArrayList<>();
    private final int REQUEST_DEVICE_CONFIG1 = 1000; //更换设备
    private final int REQUEST_DEVICE_CONFIG2 = 1001; //修改设备
    private String farmerID, deviceId;
    private RepairFinish repairFinish = new RepairFinish();
    private ArrayList<CheckBox> issueChecks = new ArrayList<>();
    private ArrayList<CheckBox> contextChecks = new ArrayList<>();
    final int REQUEST_FORM_PHOTO = 30001;
    final int REQUEST_FORM_CAMERA = 30002;
    final int REQUEST_FARMER_PHOTO = 40001;
    final int REQUEST_FARMER_CAMERA = 40002;
    final String PHOTO_FORM = "photo_form";
    final String PHOTO_FARMER = "photo_farmer";
    final int PERMISSIONS_REQUEST_STORAGE = 1001;
    final int PERMISSIONS_REQUEST_CAMERA = 1002;
    private MenuDialog menuDialog;
    private File cameraFile;
    private String takID, pondsName, mobile;
    private RepairTaskData taskData;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_repairtaskdeal);
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
        repairAdapter = new PhotosChoiceAdapter(this, repairPhotos);
        repairPictures.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        repairPictures.setAdapter(repairAdapter);
        payAdapter = new PhotosChoiceAdapter(this, payPhotos);
        repairReceives.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        repairReceives.setAdapter(payAdapter);
        createAdapter = new PhotosAdapter(mContext, createPhotos);
        taskCreatePics.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        taskCreatePics.setAdapter(createAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        createAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("path", createPhotos);
                bundle.putBoolean("show", true);
                goToActivity(GalleryActivity.class, bundle);
            }

            @Override
            public void onItemOpera(String tag, int position, String value) {

            }
        });
        repairAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                ImageSelect.mSelectedImage.clear();
                ImageSelect.mSelectedImage.addAll(repairPhotos);
                if (position == repairPhotos.size()) {
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
        payAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                ImageSelect.mSelectedImage.clear();
                ImageSelect.mSelectedImage.addAll(payPhotos);
                if (position == payPhotos.size()) {
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
    protected void requestData() {
        super.requestData();
        getData(HttpConfig.REPAIR_ISSUE_LIST, new ResponseCallBack<ArrayList<String>>() {
            @Override
            public void onSuccessResponse(ArrayList<String> d, String msg) {
                addIssueItems(d, true);
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
        getData(HttpConfig.REPAIR_CONTEXT_LIST, new ResponseCallBack<ArrayList<String>>() {
            @Override
            public void onSuccessResponse(ArrayList<String> d, String msg) {
                addIssueItems(d, false);
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
        getData(HttpConfig.TASK_DETAIL.replace("{taskid}", takID), new ResponseCallBack<RepairTaskData>() {
            @Override
            public void onSuccessResponse(RepairTaskData d, String msg) {
                showTaskData(d);
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

    private void addIssueItems(ArrayList<String> d, boolean isIssue) {
        for (String issue : d) {
            CheckBox check = new CheckBox(this);
            check.setText(issue);
            check.setButtonDrawable(R.drawable.rc_checkbox);
            check.setPadding(20, 20, 20, 20);
            check.setTextColor(Color.BLACK);
            if (isIssue) {
                repairIssueList.addView(check);
                issueChecks.add(check);
            } else {
                repairContextList.addView(check);
                contextChecks.add(check);
            }
        }
    }

    private void showTaskData(RepairTaskData d) {
        taskData = d;
        mobile = d.txtFarmerPhone;
        farmerID = d.txtFarmerID;
        pondsName = d.txtPondsName;
        installCustomerName.setText(d.txtFarmerName);
        installCustomerAdress.setText(d.txtFarmerAddr);
        Glide.with(this).load(d.picture).apply(Constants.REQUEST_OPTIONS).into(installCustomerHeader);
        installTaskState.setText("进行中");
        repairPondAddress.setText("鱼塘位置：" + d.txtPondAddr);
        orderItems.add("鱼塘名称：" + d.txtPondsName);
        if (!TextUtils.isEmpty(d.txtHKName)) {
            orderItems.add("养殖管家：" + d.txtHKName);
        }
        if (!TextUtils.isEmpty(d.txtMonMembName)) {
            orderItems.add("监控人员：" + d.txtMonMembName);
        }
        orderItems.add("设备型号：" + d.txtRepairEqpKind);
        orderItems.add("故障描述：" + d.txtMaintenDetail);
        if (!TextUtils.isEmpty(d.txtMonMembName)) {
            orderItems.add("备注：" + (TextUtils.isEmpty(d.txtRemarks) ? "-" : d.txtRemarks));
        }
        orderItemAdapter.notifyDataSetChanged();
        if (!TextUtils.isEmpty(d.txtAppRepairImg)) {
            createPhotos.addAll(Arrays.asList(d.txtAppRepairImg.split(",")));
            createAdapter.notifyDataSetChanged();
        }
        repairDeviceId.setText(d.txtRepairEqpID);
        deviceId = d.txtRepairEqpID;
        repairFinish.oldDeviceId = deviceId;
    }

    @OnClick({R.id.install_customer_call, R.id.repair_finish, R.id.repair_change_device, R.id.repair_device_reset, R.id.repair_device_detail
            , R.id.repair_device_config, R.id.repair_pond_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
            case R.id.repair_finish:
                repairTaskSubmit();
                break;
            case R.id.repair_change_device:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isRepair", true);
                bundle.putString("pondsName", pondsName);
                bundle.putString(Constants.INTENT_OBJECT, farmerID);
                goToActivityForResult(DeviceConfigActivity.class, bundle, REQUEST_DEVICE_CONFIG1);
                break;
            case R.id.repair_device_config:
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("isRepair", true);
                bundle1.putString("deviceId", repairDeviceId.getText().toString());
                bundle1.putString("pondsName", pondsName);
                bundle1.putString(Constants.INTENT_OBJECT, farmerID);
                goToActivityForResult(DeviceConfigActivity.class, bundle1, REQUEST_DEVICE_CONFIG2);
                break;
            case R.id.repair_device_detail:
                Bundle bundle2 = new Bundle();
                bundle2.putString(Constants.INTENT_OBJECT, repairDeviceId.getText().toString());
                goToActivity(DeviceDetailActivity.class, bundle2);
                break;
            case R.id.repair_device_reset:
                testReset();
                break;
            case R.id.repair_pond_address:
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
                                                    taskData.txtPondAddr);
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
        }
    }

    public void testReset() {
        String deviceId = repairDeviceId.getText().toString();
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

    private void repairTaskSubmit() {
        showLoading();
        String issueTxt = "";
        for (CheckBox issue : issueChecks) {
            if (issue.isChecked()) {
                issueTxt = issueTxt + "," + issue.getText().toString();
            }
        }
        String contentTxt = "";
        for (CheckBox cont : contextChecks) {
            if (cont.isChecked()) {
                contentTxt = contentTxt + "," + cont.getText().toString();
            }
        }
        repairFinish.resMulti = issueTxt.length() > 0 ? issueTxt.substring(1) : "";
        repairFinish.conMulti = contentTxt.length() > 0 ? contentTxt.substring(1) : "";
        repairFinish.resOth = repairIssueInput.getText().toString();
        repairFinish.conOth = repairContextInput.getText().toString();
        repairFinish.remarks = repairRemarks.getText().toString();
        repairFinish.selfYes = callbackApproveAgree.isChecked() ? "0" : "1";
        uploadPhotoList();
    }

    private int photoSize = 0;

    private void uploadPhotoList() {
        photoSize = repairPhotos.size() + payPhotos.size();
        if (photoSize == 0) {
            createCustomer();
            return;
        }
        for (String path : repairPhotos) {
            uploadPhoto("repairPic", path);
        }
        for (String path : payPhotos) {
            uploadPhoto("repairFee", path);
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
                if (d.contains("repairPic")) {
                    repairFinish.repairUrls.add(d);
                } else if (d.contains("repairFee")) {
                    repairFinish.payUrls.add(d);
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
        RepairSubmit repairSubmit = new RepairSubmit();
        repairSubmit.loginid = sharedPref.getString(Constants.LOGIN_ID);
        repairSubmit.appData = repairFinish;
        LogInfo.error(repairSubmit.toString());
        postData(HttpConfig.CONFIRM_TASK_FINISH.replace("{taskid}", takID), repairSubmit.toString(), new ResponseCallBack<JsonElement>() {
            @Override
            public void onSuccessResponse(JsonElement d, String msg) {
                LogInfo.error(d);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_DEVICE_CONFIG1:
                    BindDeviceInfo info = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    repairFinish.newDeviceId = info.deviceIdentifier;
                    repairDeviceId.setText(info.deviceIdentifier);
                    repairFinish.pondAddr = info.pondAddr;
                    repairFinish.latitude = info.latitude;
                    repairFinish.longitude = info.longitude;
                    break;
                case REQUEST_DEVICE_CONFIG2:
                    BindDeviceInfo info1 = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    repairFinish.oldDeviceId = info1.deviceIdentifier;
                    repairDeviceId.setText(info1.deviceIdentifier);
                    break;
                case REQUEST_FORM_CAMERA:
                    lubanCompress(REQUEST_FORM_CAMERA,cameraFile);
//                    repairPhotos.clear();
//                    ImageSelect.mSelectedImage.add(cameraFile.getPath());
//                    repairPhotos.addAll(ImageSelect.mSelectedImage);
//                    repairAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_FORM_PHOTO:
                    repairPhotos.clear();
                    repairPhotos.addAll(ImageSelect.mSelectedImage);
                    repairAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_FARMER_CAMERA:
                    lubanCompress(REQUEST_FARMER_CAMERA,cameraFile);
//                    payPhotos.clear();
//                    ImageSelect.mSelectedImage.add(cameraFile.getPath());
//                    payPhotos.addAll(ImageSelect.mSelectedImage);
//                    payAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_FARMER_PHOTO:
                    payPhotos.clear();
                    payPhotos.addAll(ImageSelect.mSelectedImage);
                    payAdapter.notifyDataSetChanged();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                                repairPhotos.clear();
                                ImageSelect.mSelectedImage.add(file.getPath());
                                repairPhotos.addAll(ImageSelect.mSelectedImage);
                                repairAdapter.notifyDataSetChanged();
                                break;
                            case REQUEST_FARMER_CAMERA:
                                payPhotos.clear();
                                ImageSelect.mSelectedImage.add(file.getPath());
                                payPhotos.addAll(ImageSelect.mSelectedImage);
                                payAdapter.notifyDataSetChanged();
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        switch (code) {
                            case REQUEST_FORM_CAMERA:
                                repairPhotos.clear();
                                ImageSelect.mSelectedImage.add(cameraFile.getPath());
                                repairPhotos.addAll(ImageSelect.mSelectedImage);
                                repairAdapter.notifyDataSetChanged();
                                break;
                            case REQUEST_FARMER_CAMERA:
                                payPhotos.clear();
                                ImageSelect.mSelectedImage.add(cameraFile.getPath());
                                payPhotos.addAll(ImageSelect.mSelectedImage);
                                payAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                }).launch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageSelect.mSelectedImage.clear();
    }

}
