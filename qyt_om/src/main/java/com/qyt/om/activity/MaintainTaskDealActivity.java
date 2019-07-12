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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
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
import com.qyt.om.adapter.PhotosChoiceAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.BindDeviceInfo;
import com.qyt.om.request.MaintainSubmit;
import com.qyt.om.request.RepairSubmit;
import com.qyt.om.request.UploadModel;
import com.qyt.om.response.MaintainTaskData;
import com.qyt.om.utils.BaiduLocManager;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.utils.MapNaviUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class MaintainTaskDealActivity extends BaseActivity {

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
    @BindView(R.id.maintain_pictures)
    RecyclerView maintainPictures;
    @BindView(R.id.maintain_sign)
    Button maintainSign;
    @BindView(R.id.maintain_contents)
    GridLayout maintainContextList;
    @BindView(R.id.maintain_remark)
    EditText maintainRemark;
    @BindView(R.id.install_customer_header)
    RoundImageView installCustomerHeader;
    @BindView(R.id.maintain_pond_address)
    TextViewPlus maintainPondAddress;

    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private PhotosChoiceAdapter photosAdapter;
    private ArrayList<String> photos = new ArrayList<>();
    private MaintainTaskData taskData;
    private String takID, mobile;
    private MaintainSubmit maintainSubmit = new MaintainSubmit();
    private ArrayList<CheckBox> contextChecks = new ArrayList<>();
    final int REQUEST_FORM_PHOTO = 30001;
    final int REQUEST_FORM_CAMERA = 30002;
    final int PERMISSIONS_REQUEST_STORAGE = 1001;
    final int PERMISSIONS_REQUEST_CAMERA = 1002;
    private MenuDialog menuDialog;
    private File cameraFile;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_maintaintaskdeal);
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
        photosAdapter = new PhotosChoiceAdapter(this, photos);
        maintainPictures.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        maintainPictures.setAdapter(photosAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        photosAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                ImageSelect.mSelectedImage.clear();
                ImageSelect.mSelectedImage.addAll(photos);
                if (position == photos.size()) {
                    showPickHeaderMenu();
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
    }

    private void showPickHeaderMenu() {
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
                        takePhoto();
                        break;
                    case 1:
                        goToActivityForResult(PhotosActivity.class, REQUEST_FORM_PHOTO);
                        break;
                }
            }
        }).show();
    }

    private void takePhoto() {
        if (checkPermission(Manifest.permission.CAMERA, PERMISSIONS_REQUEST_CAMERA)) {
            cameraFile = new File(AppUtils.getDiskCacheDir(this), CameraUtil.getPhotoFileName());
            Uri fileUri = FileProvider.getUriForFile(this, "com.qyt.om.fileprovider", cameraFile);
            startActivityForResult(CameraUtil.getTakePickIntent(fileUri), REQUEST_FORM_CAMERA);
        }
    }


    @Override
    protected void requestData() {
        super.requestData();
        showLoading();
        getData(HttpConfig.TASK_DETAIL.replace("{taskid}", takID), new ResponseCallBack<MaintainTaskData>() {
            @Override
            public void onSuccessResponse(MaintainTaskData d, String msg) {
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
        getData(HttpConfig.MAINTAIN_CONTEXT_LIST, new ResponseCallBack<ArrayList<String>>() {
            @Override
            public void onSuccessResponse(ArrayList<String> d, String msg) {
                addIssueItems(d);
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

    private void addIssueItems(ArrayList<String> d) {
        for (String issue : d) {
            CheckBox check = new CheckBox(this);
            check.setText(issue);
            check.setButtonDrawable(R.drawable.rc_checkbox);
            check.setPadding(20, 20, 20, 20);
            check.setTextColor(Color.BLACK);
            maintainContextList.addView(check);
            contextChecks.add(check);
        }
    }

    private void showTaskData(MaintainTaskData d) {
        String loginid = sharedPref.getString(Constants.LOGIN_ID);
        maintainSubmit.loginid = loginid;
        maintainSubmit.appData.loginid = loginid;
        maintainSubmit.appData.farmerId = d.txtFarmerID;
        taskData = d;
        mobile = d.txtFarmerPhone;
        installCustomerName.setText(d.txtFarmerName);
        installCustomerAdress.setText(d.txtFarmerAddr);
        Glide.with(this).load(d.picture).apply(Constants.REQUEST_OPTIONS).into(installCustomerHeader);
        installTaskState.setText("进行中");
        maintainPondAddress.setText("鱼塘位置：" + d.txtPondAddr);
        orderItems.add("工单号：" + d.txtFormNo);
        orderItems.add("鱼塘名称：" + d.txtPondsName);
//        orderItems.add("鱼塘位置：" + d.txtPondAddr);
        orderItems.add("设备类型：" + d.txtRepairEqpKind);
        if (!TextUtils.isEmpty(d.txtHKName)) {
            orderItems.add("养殖管家：" + d.txtHKName);
        }
        if (!TextUtils.isEmpty(d.txtMonMembName)) {
            orderItems.add("监控人员：" + d.txtMonMembName);
            orderItems.add("备注：" + (TextUtils.isEmpty(d.tarMonRemarks) ? "-" : d.tarMonRemarks));
        }
        orderItemAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.install_customer_call, R.id.task_finish, R.id.maintain_sign, R.id.maintain_pond_address})
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
            case R.id.task_finish:
                maintainTaskSubmit();
                break;
            case R.id.maintain_sign:
                BaiduLocManager.getInstance(getApplicationContext()).startLocation(new BaiduLocManager.OnLocationComplete() {
                    @Override
                    public void onLocationComplete(BDLocation location) {
                        int code = location.getLocType();
                        if (code == 61 || code == 161) {
                            maintainSign.setText("已打卡");
                            maintainSign.setEnabled(false);
                            maintainSubmit.appData.txtClockTime = System.currentTimeMillis();
                            maintainSubmit.appData.txtClockLatitude = location.getLatitude();
                            maintainSubmit.appData.txtClockLongitude = location.getLongitude();
                        } else {
                            showToast("未定位到您的当前位置，打卡失败！");
                        }
                    }
                });
                break;
            case R.id.maintain_pond_address:
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

    private void maintainTaskSubmit() {
        if (maintainSign.isEnabled()) {
            showToast("请先打卡");
            return;
        }
        showLoading();
        String issueTxt = "";
        for (CheckBox issue : contextChecks) {
            if (issue.isChecked()) {
                issueTxt = issueTxt + "," + issue.getText().toString();
            }
        }
        maintainSubmit.appData.tarMaintainCon = issueTxt.length() > 0 ? issueTxt.substring(1) : "";
        maintainSubmit.appData.tarRemarks = maintainRemark.getText().toString();
        uploadPhotoList();
    }

    private int photoSize = 0;

    private void uploadPhotoList() {
        photoSize = photos.size();
        if (photoSize == 0) {
            createCustomer();
            return;
        }
        for (String path : photos) {
            uploadPhoto("maPic", path);
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
                maintainSubmit.appData.txtMaintainImgSrc.add(d);
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
        LogInfo.error(maintainSubmit.toString());
        postData(HttpConfig.CONFIRM_TASK_FINISH.replace("{taskid}", takID), maintainSubmit.toString(), new ResponseCallBack<JsonElement>() {
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
                case REQUEST_FORM_CAMERA:
                    lubanCompress(cameraFile);
//                    photos.clear();
//                    ImageSelect.mSelectedImage.add(cameraFile.getPath());
//                    photos.addAll(ImageSelect.mSelectedImage);
//                    photosAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_FORM_PHOTO:
                    photos.clear();
                    photos.addAll(ImageSelect.mSelectedImage);
                    photosAdapter.notifyDataSetChanged();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void lubanCompress(File file) {
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
                        photos.clear();
                        ImageSelect.mSelectedImage.add(file.getPath());
                        photos.addAll(ImageSelect.mSelectedImage);
                        photosAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        photos.clear();
                        ImageSelect.mSelectedImage.add(cameraFile.getPath());
                        photos.addAll(ImageSelect.mSelectedImage);
                        photosAdapter.notifyDataSetChanged();
                    }
                }).launch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageSelect.mSelectedImage.clear();
    }

}
