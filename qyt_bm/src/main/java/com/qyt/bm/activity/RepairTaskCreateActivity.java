package com.qyt.bm.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.utils.CameraUtil;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.MenuDialog;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.photos.GalleryActivity;
import com.bangqu.photos.PhotosActivity;
import com.bangqu.photos.util.ImageSelect;
import com.qyt.bm.R;
import com.qyt.bm.adapter.PhotosChoiceAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.ContactItem;
import com.qyt.bm.model.OperatorItem;
import com.qyt.bm.request.RepairTask;
import com.qyt.bm.request.UploadModel;
import com.qyt.bm.response.DeviceConfigInfo;
import com.qyt.bm.response.FishPondInfo;
import com.qyt.bm.utils.LogInfo;
import com.qyt.bm.widget.DeviceListDialog;
import com.qyt.bm.widget.PondListDialog;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepairTaskCreateActivity extends BaseActivity {

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
    @BindView(R.id.install_operator)
    TextViewPlus installOperator;
    @BindView(R.id.repair_pictures)
    RecyclerView repairPictures;
    @BindView(R.id.repair_fishpond)
    TextViewPlus repairFishpond;
    @BindView(R.id.repair_fishpond_address)
    TextView repairFishpondAddress;
    @BindView(R.id.repair_device_id)
    TextViewPlus repairDeviceId;
    @BindView(R.id.repair_device_model)
    TextView repairDeviceModel;
    @BindView(R.id.repair_fishpond_mobile)
    TextView repairFishpondMobile;
    @BindView(R.id.repair_reason)
    EditText repairReason;

    private PhotosChoiceAdapter photosAdapter;
    private ArrayList<String> photos = new ArrayList<>();
    private final int REQUEST_CUSTOMER = 100;
    private final int REQUEST_OPERATOR = 101;
    /*图片选择*/
    private File cameraFile;
    final int REQUEST_PHOTO = 1;
    final int REQUEST_CAMERA = 3;
    final int PERMISSIONS_REQUEST_STORAGE = 1001;
    final int PERMISSIONS_REQUEST_CAMERA = 1002;
    private MenuDialog menuDialog;
    private String loginid;
    private RepairTask repairTask = new RepairTask();
    private ArrayList<FishPondInfo> fishPondInfos = new ArrayList<>();
    private ArrayList<DeviceConfigInfo> deviceList = new ArrayList<>();
    private ContactItem contactItem;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_repairtaskcreate);
        contactItem = getIntent().getParcelableExtra(Constants.INTENT_OBJECT);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("故障报修");
        loginid = sharedPref.getString(Constants.LOGIN_ID);
        repairTask.loginid = loginid;
        repairTask.appData.loginid = loginid;
        photosAdapter = new PhotosChoiceAdapter(this, photos);
        repairPictures.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        repairPictures.setAdapter(photosAdapter);
        if (contactItem != null) {
            installCustomerName.setText(contactItem.name);
            installCustomerMobile.setText(contactItem.mobile);
            installCustomerAddress.setText(contactItem.farmerAdd);
            repairTask.farmerId = contactItem.farmerId;
            repairTask.appData.farmerId = contactItem.farmerId;
            getFishPonds(contactItem.farmerId);
        }
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        photosAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                if (position == photos.size()) {
                    if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_STORAGE)) {
                        showMenuDialog();
                    }
                } else {
                    Intent intent = new Intent(mContext, GalleryActivity.class);
                    intent.putStringArrayListExtra("path", new ArrayList<>(ImageSelect.mSelectedImage));
                    intent.putExtra("index", position);
                    startActivityForResult(intent, REQUEST_PHOTO);
                }
            }

            @Override
            public void onItemOpera(String tag, int position, String value) {

            }
        });
    }

    private void showMenuDialog() {
        if (menuDialog != null) {
            menuDialog.show();
        } else {
            menuDialog = new MenuDialog(this);
            menuDialog.setMenuItems(new String[]{"拍照", "图库选择"}, new MenuDialog.OnMenuItemClickedListener() {
                @Override
                public void MenuItemClicked(String value, int position) {
                    switch (position) {
                        case 0:
                            takePhoto();
                            break;
                        case 1:
                            pickPhotos();
                            break;
                    }
                }
            }).show();
        }
    }

    private void takePhoto() {
        if (checkPermission(Manifest.permission.CAMERA, PERMISSIONS_REQUEST_CAMERA)) {
            cameraFile = new File(getDiskCacheDir(this), CameraUtil.getPhotoFileName());
            Uri fileUri = FileProvider.getUriForFile(this, "com.qyt.bm.fileprovider", cameraFile);
            startActivityForResult(CameraUtil.getTakePickIntent(fileUri), REQUEST_CAMERA);
        }
    }

    private void pickPhotos() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("single", false);
        goToActivityForResult(PhotosActivity.class, bundle, REQUEST_PHOTO);
    }

    @Override
    protected void requestData() {
        super.requestData();

    }

    @OnClick({R.id.install_choice_customer, R.id.install_choice_operator, R.id.repair_pond, R.id.repair_device
            , R.id.submit_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.repair_device:
                new DeviceListDialog(mContext, deviceList, new DialogConfirmListener() {
                    @Override
                    public void onDialogConfirm(boolean result, Object value) {
                        if (result) {
                            DeviceConfigInfo bean = (DeviceConfigInfo) value;
                            repairDeviceId.setText(bean.identifier);
                            repairDeviceModel.setText(bean.type);
                            repairTask.appData.deviceId = bean.identifier;
                        }
                    }
                }).show();
                break;
            case R.id.repair_pond:
                new PondListDialog(mContext, fishPondInfos, new DialogConfirmListener() {
                    @Override
                    public void onDialogConfirm(boolean result, Object value) {
                        if (result) {
                            FishPondInfo fishPondInfo = (FishPondInfo) value;
                            repairFishpond.setText(fishPondInfo.name);
                            repairFishpondAddress.setText(fishPondInfo.pondAddress);
                            repairFishpondMobile.setText(fishPondInfo.phoneNumber);
                            repairTask.appData.pondId = fishPondInfo.pondId;
                            deviceList.clear();
                            deviceList.addAll(fishPondInfo.childDeviceList);
                        }
                    }
                }).show();
                break;
            case R.id.install_choice_customer:
                goToActivityForResult(ChoiceCustomerActivity.class, REQUEST_CUSTOMER);
                break;
            case R.id.install_choice_operator:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_OBJECT, "维修设备数量：");
                goToActivityForResult(ChoiceOperatorActivity.class, bundle, REQUEST_OPERATOR);
                break;
            case R.id.submit_confirm:
                if (checkRepair()) {
                    showLoading();
                    if (photos.size() > 0) {
                        for (String path : photos) {
                            uploadPhoto("repair", path);
                        }
                    } else {
                        createCustomer();
                    }
                }
                break;
        }
    }

    private void uploadPhoto(String type, String path) {
        UploadModel uploadModel = new UploadModel();
        uploadModel.type = type;
        uploadModel.imageData = AppUtils.fileToBase64(path);
        uploadModel.imageName = System.currentTimeMillis() + path.substring(path.lastIndexOf("."));
        postData(HttpConfig.UPLOAD_IMAGE.replace("{loginid}", loginid), uploadModel.toString(), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
                repairTask.appData.url.add(d);
                if (repairTask.appData.url.size() == photos.size()) {
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
        LogInfo.error(repairTask.toString());
        postData(HttpConfig.CREATE_TASK_CUSTOMER.replace("{PROID}", Constants.REPAIR_CREATE), repairTask.toString(), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
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

    private boolean checkRepair() {
        if (TextUtils.isEmpty(repairTask.farmerId)) {
            showToast("请选择养殖户");
            return false;
        }
        if (TextUtils.isEmpty(repairTask.appData.pondId)) {
            showToast("请选择鱼塘");
            return false;
        }
        if (TextUtils.isEmpty(repairTask.appData.deviceId)) {
            showToast("请选择设备");
            return false;
        }
        if (TextUtils.isEmpty(repairTask.appData.memId)) {
            showToast("请选择运维人员");
            return false;
        }
        if (!TextUtils.isEmpty(repairReason.getText())) {
            repairTask.appData.reason = repairReason.getText().toString();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CUSTOMER:
                    ContactItem item = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    installCustomerName.setText(item.name);
                    installCustomerMobile.setText(item.mobile);
                    installCustomerAddress.setText(item.farmerAdd);
                    repairTask.farmerId = item.farmerId;
                    repairTask.appData.farmerId = item.farmerId;
                    getFishPonds(item.farmerId);
                    break;
                case REQUEST_OPERATOR:
                    OperatorItem operator = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    installOperator.setText(operator.memName);
                    repairTask.appData.memId = operator.memId;
                    break;
                case REQUEST_CAMERA:
                    photos.clear();
                    ImageSelect.mSelectedImage.add(cameraFile.getPath());
                    photos.addAll(ImageSelect.mSelectedImage);
                    photosAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_PHOTO:
                    photos.clear();
                    photos.addAll(ImageSelect.mSelectedImage);
                    photosAdapter.notifyDataSetChanged();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PERMISSIONS_REQUEST_STORAGE:
                    showMenuDialog();
                    break;
                case PERMISSIONS_REQUEST_CAMERA:
                    takePhoto();
                    break;
            }
        } else {
            showToast("获取相关权限失败");
        }
    }

    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
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
    protected void onDestroy() {
        super.onDestroy();
        ImageSelect.mSelectedImage.clear();
    }
}
