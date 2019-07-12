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
import android.widget.EditText;
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
import com.qyt.om.R;
import com.qyt.om.adapter.DeviceListAdapter;
import com.qyt.om.adapter.DeviceRetrieveAdapter;
import com.qyt.om.adapter.OrderItemAdapter;
import com.qyt.om.adapter.PhotosChoiceAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.model.BindDeviceInfo;
import com.qyt.om.model.DeviceChoiceModel;
import com.qyt.om.request.RecycleFinish;
import com.qyt.om.request.RecycleSubmit;
import com.qyt.om.request.RepairSubmit;
import com.qyt.om.request.UploadModel;
import com.qyt.om.response.RecycleTaskData;
import com.qyt.om.utils.BaiduLocManager;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.utils.MapNaviUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class RetrieveTaskDealActivity extends BaseActivity {

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
    @BindView(R.id.install_device_list)
    UnScrollListView installDeviceList;
    @BindView(R.id.broken_pictures)
    RecyclerView brokenPictures;
    @BindView(R.id.retrieve_forms)
    RecyclerView retrieveForms;
    @BindView(R.id.install_pond_address)
    TextViewPlus installPondAddress;
    @BindView(R.id.install_customer_header)
    RoundImageView installCustomerHeader;
    @BindView(R.id.callback_approve_agree)
    RadioButton deviceGood;
    @BindView(R.id.recycle_explain)
    EditText recycleExplain;
    @BindView(R.id.recycle_remark)
    EditText recycleRemark;

    private ArrayList<String> orderItems = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<DeviceChoiceModel> deviceItems = new ArrayList<>();
    private DeviceRetrieveAdapter deviceListAdapter;
    private PhotosChoiceAdapter repairAdapter;
    private ArrayList<String> repairPhotos = new ArrayList<>();
    private PhotosChoiceAdapter payAdapter;
    private ArrayList<String> payPhotos = new ArrayList<>();
    private String takID, status;
    private RecycleTaskData recycleTask;
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

    private RecycleFinish recycleFinish = new RecycleFinish();

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_retrievetaskdeal);
        takID = getIntent().getStringExtra(Constants.INTENT_OBJECT);
        status = getIntent().getStringExtra(Constants.INTENT_FLAG);
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
        deviceListAdapter = new DeviceRetrieveAdapter(this, deviceItems);
        installDeviceList.setAdapter(deviceListAdapter);
        repairAdapter = new PhotosChoiceAdapter(this, repairPhotos);
        brokenPictures.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        brokenPictures.setAdapter(repairAdapter);
        payAdapter = new PhotosChoiceAdapter(this, payPhotos);
        retrieveForms.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        retrieveForms.setAdapter(payAdapter);
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        deviceListAdapter.setListItemOperaListener(new ListItemOperaListener<DeviceChoiceModel>() {
            @Override
            public void onItemOpera(String tag, final int position, final DeviceChoiceModel value) {
                switch (tag) {
                    case "unbind":
                        new ConfirmDialog(mContext, "是否解绑设备", new DialogConfirmListener() {
                            @Override
                            public void onDialogConfirm(boolean result, Object val) {
                                if (result)
                                    unbindService(position, value.device_id);
                            }
                        }).show();
                        break;
                }
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

    private void unbindService(final int position, String deviceId) {
        String url = HttpConfig.DEL_UNBIND_SERVICE.replace("{dIdentifier}", deviceId).replace("{gIdentifier}", recycleTask.txtPondID);
        delData(url, new ResponseCallBack() {
            @Override
            public void onSuccessResponse(Object d, String msg) {
                deviceItems.get(position).selected = true;
                deviceListAdapter.notifyDataSetChanged();
                showToast("解绑成功");
            }

            @Override
            public void onFailResponse(String msg) {
                if ("删除失败".equals(msg)) {
                    deviceItems.get(position).selected = true;
                    deviceListAdapter.notifyDataSetChanged();
                    showToast("设备已解绑");
                } else {
                    showToast(msg);
                }
            }

            @Override
            public void onVolleyError(int code, String msg) {
                showToast(msg);
            }
        });
    }

    @Override
    protected void requestData() {
        super.requestData();
        showLoading();
        getData(HttpConfig.TASK_DETAIL.replace("{taskid}", takID), new ResponseCallBack<RecycleTaskData>() {
            @Override
            public void onSuccessResponse(RecycleTaskData d, String msg) {
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

    private void showTaskData(RecycleTaskData d) {
        recycleTask = d;
        installCustomerName.setText(d.txtFarmerName);
        installCustomerAdress.setText(d.txtFarmerAddr);
        Glide.with(this).load(d.picture).apply(Constants.REQUEST_OPTIONS).into(installCustomerHeader);
        installTaskState.setText("进行中");
        installPondAddress.setText("鱼塘地址：" + d.txtPondAddr);
        orderItems.add("鱼塘名称：" + d.txtPondName);
        orderItems.add("联系方式：" + d.txtPondPhone);
        orderItems.add("回收原因：" + d.txtResMulti);
        orderItems.add("备注信息：" + d.tarReson);
        orderItems.add("养殖管家：" + d.txtHK);
        orderItemAdapter.notifyDataSetChanged();
        if (d.tarEqps != null) {
            for (RecycleTaskData.TarEqpsBean eqpsBean : d.tarEqps) {
                DeviceChoiceModel model = new DeviceChoiceModel();
                model.device_model = eqpsBean.ITEM5;
                model.device_id = eqpsBean.ITEM2;
                model.selected = false;
                deviceItems.add(model);
            }
            deviceListAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.install_customer_call, R.id.task_opera, R.id.install_pond_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.install_customer_call:
                new ConfirmDialog(this, "拨打电话", recycleTask.txtFarmerPhone, "拨号", "取消", new DialogConfirmListener() {
                    @Override
                    public void onDialogConfirm(boolean result, Object v) {
                        if (result) {
                            AppUtils.dialPhone(mContext, recycleTask.txtFarmerPhone);
                        }
                    }
                }).show();
                break;
            case R.id.task_opera:
                recycleTaskSubmit();
                break;
            case R.id.install_pond_address:
                if (recycleTask != null && !TextUtils.isEmpty(recycleTask.latitude) && !TextUtils.isEmpty(recycleTask.longitude)) {
                    try {
                        final double lat = Double.parseDouble(recycleTask.latitude);
                        final double lon = Double.parseDouble(recycleTask.longitude);
                        BaiduLocManager.getInstance(getApplicationContext()).startLocation(new BaiduLocManager.OnLocationComplete() {
                            @Override
                            public void onLocationComplete(BDLocation location) {
                                int code = location.getLocType();
                                if (code == 61 || code == 161) {
                                    MapNaviUtil.getInstance(mContext).
                                            openBaiduMap(new LatLng(location.getLatitude(), location.getLongitude()),
                                                    new LatLng(lat, lon),
                                                    recycleTask.txtPondAddr);
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

    private void recycleTaskSubmit() {
        for (DeviceChoiceModel device : deviceItems) {
            if (!device.selected) {
                showToast("有设备未解绑");
                return;
            }
        }
        showLoading();
        recycleFinish.isGood = deviceGood.isChecked() ? "0" : "1";
        recycleFinish.explain = recycleExplain.getText().toString();
        recycleFinish.remarks = recycleRemark.getText().toString();
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
            uploadPhoto("recyclingPic", path);
        }
        for (String path : payPhotos) {
            uploadPhoto("recyclingForm", path);
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
                if (d.contains("recyclingPic")) {
                    recycleFinish.brokenUrls.add(d);
                } else if (d.contains("recyclingForm")) {
                    recycleFinish.recycleUrls.add(d);
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
        RecycleSubmit recycleSubmit = new RecycleSubmit();
        recycleSubmit.loginid = sharedPref.getString(Constants.LOGIN_ID);
        recycleSubmit.appData = recycleFinish;
        LogInfo.error(recycleSubmit.toString());
        postData(HttpConfig.CONFIRM_TASK_FINISH.replace("{taskid}", takID), recycleSubmit.toString(), new ResponseCallBack<JsonElement>() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
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
