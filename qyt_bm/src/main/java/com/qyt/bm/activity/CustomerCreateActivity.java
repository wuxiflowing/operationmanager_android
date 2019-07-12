package com.qyt.bm.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.utils.CameraUtil;
import com.bangqu.lib.utils.DateFormatUtil;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.MenuDialog;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.photos.GalleryActivity;
import com.bangqu.photos.PhotosActivity;
import com.bangqu.photos.util.ImageSelect;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.qyt.bm.BmApplication;
import com.qyt.bm.R;
import com.qyt.bm.adapter.PhotosChoiceAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.ContactItem;
import com.qyt.bm.model.UploadMap;
import com.qyt.bm.request.CustomerCreate;
import com.qyt.bm.request.UploadModel;
import com.qyt.bm.response.AreaItem;
import com.qyt.bm.response.CustomerData;
import com.qyt.bm.utils.LogInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class CustomerCreateActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.cc_form_pictures)
    RecyclerView ccFormPictures;
    @BindView(R.id.cc_customer_pictures)
    ImageView ccCustomerPictures;
    @BindView(R.id.cc_fishpond_pictures)
    RecyclerView ccFishpondPictures;
    @BindView(R.id.cc_name)
    EditText ccName;
    @BindView(R.id.cc_mobile)
    EditText ccMobile;
    @BindView(R.id.cc_age)
    TextViewPlus ccBirth;
    @BindView(R.id.cc_area)
    TextViewPlus ccArea;
    @BindView(R.id.customer_male)
    RadioButton customerMale;
    @BindView(R.id.customer_female)
    RadioButton customerFemale;
    @BindView(R.id.cc_ads_detail)
    EditText ccAdsDetail;
    @BindView(R.id.cc_identify_no)
    EditText ccIdentifyNo;
    @BindView(R.id.cc_card_face)
    ImageView ccCardFace;
    @BindView(R.id.cc_card_back)
    ImageView ccCardBack;
    /*登记表*/
    private PhotosChoiceAdapter formPhotosAdapter;
    private ArrayList<String> formPhotos = new ArrayList<>();
    private File cameraFile;
    final int REQUEST_AREA = 10;
    final int PERMISSIONS_REQUEST_STORAGE = 1001;
    final int PERMISSIONS_REQUEST_CAMERA = 1002;
    private MenuDialog menuDialog;
    /*鱼塘照片*/
    private PhotosChoiceAdapter fishpondPhotosAdapter;
    private ArrayList<String> fishpondPhotos = new ArrayList<>();
    final int REQUEST_ID_FACE_PHOTO = 10001;
    final int REQUEST_ID_FACE_CAMERA = 10002;
    final int REQUEST_ID_BACK_PHOTO = 20001;
    final int REQUEST_ID_BACK_CAMERA = 20002;
    final int REQUEST_FORM_PHOTO = 30001;
    final int REQUEST_FORM_CAMERA = 30002;
    final int REQUEST_FARMER_PHOTO = 40001;
    final int REQUEST_FARMER_CAMERA = 40002;
    final int REQUEST_POND_PHOTO = 50001;
    final int REQUEST_POND_CAMERA = 50002;
    final String PHOTO_ID_FACE = "photo_id_face";
    final String PHOTO_ID_BACK = "photo_id_back";
    final String PHOTO_FORM = "photo_form";
    final String PHOTO_FARMER = "photo_farmer";
    final String PHOTO_POND = "photo_pond";
    private TimePickerView mTimePickerView;
    private String path_id_face, path_id_back, customer_pic;
    private String loginId;
    private CustomerCreate customerCreate = new CustomerCreate();
    private CustomerData customerData;
    private boolean isMobileOk = false;
    private boolean isIdCardOk = false;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_customercreate);
        customerData = getIntent().getParcelableExtra(Constants.INTENT_OBJECT);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        menuDialog = new MenuDialog(this);
        loginId = sharedPref.getString(Constants.LOGIN_ID);
        /*登记表*/
        formPhotosAdapter = new PhotosChoiceAdapter(this, formPhotos);
        ccFormPictures.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        ccFormPictures.setAdapter(formPhotosAdapter);
        /*鱼塘照片*/
        fishpondPhotosAdapter = new PhotosChoiceAdapter(this, fishpondPhotos);
        ccFishpondPictures.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        ccFishpondPictures.setAdapter(fishpondPhotosAdapter);
        if (customerData != null) {
            title.setText("完善资料");
            customerCreate.appData.farmerId = customerData.farmerId;
            customerCreate.appData.region = customerData.region;
            ccName.setText(customerData.name);
            ccMobile.setText(customerData.contactInfo);
            ccBirth.setText(customerData.birthday);
            ccArea.setText(customerData.region);
            ccAdsDetail.setText(customerData.homeAddress);
            if ("男".equals(customerData.sex))
                customerMale.setChecked(true);
            else
                customerFemale.setChecked(true);
            ccIdentifyNo.setText(customerData.idNumber);
            if (!TextUtils.isEmpty(customerData.picture)) {
                customer_pic = customerData.picture;
                Glide.with(this).load(customerData.picture).into(ccCustomerPictures);
            }
            if (!TextUtils.isEmpty(customerData.pondPicture)) {
                fishpondPhotos.addAll(Arrays.asList(customerData.pondPicture.split(",")));
                fishpondPhotosAdapter.notifyDataSetChanged();
            }
            if (!TextUtils.isEmpty(customerData.idPicture)) {
                String[] ids = customerData.idPicture.split(",");
                if (ids.length > 0) {
                    path_id_face = ids[0];
                    Glide.with(this).load(path_id_face).into(ccCardFace);
                }
                if (ids.length > 1) {
                    path_id_back = ids[1];
                    Glide.with(this).load(path_id_back).into(ccCardBack);
                }
            }
        } else {
            title.setText("开户申请");
        }
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        formPhotosAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                ImageSelect.mSelectedImage.clear();
                ImageSelect.mSelectedImage.addAll(formPhotos);
                if (position == formPhotos.size()) {
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
        ccCustomerPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickHeaderMenu(PHOTO_FARMER);
            }
        });
        fishpondPhotosAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String value) {
                ImageSelect.mSelectedImage.clear();
                ImageSelect.mSelectedImage.addAll(fishpondPhotos);
                if (position == fishpondPhotos.size()) {
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
        ccMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.length() == 11) {
                    getData(HttpConfig.CHECKPHONENUMBER + s.toString(), new ResponseCallBack() {
                        @Override
                        public void onSuccessResponse(Object d, String msg) {
                            isMobileOk = true;
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
            }
        });
        ccIdentifyNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.length() == 18) {
                    getData(HttpConfig.CHECKIDCARD + s.toString(), new ResponseCallBack() {
                        @Override
                        public void onSuccessResponse(Object d, String msg) {
                            isIdCardOk = true;
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
            }
        });
    }

    @Override
    protected void requestData() {
        super.requestData();
    }

    @OnClick({R.id.submit_confirm, R.id.cc_card_face, R.id.cc_card_back, R.id.cc_age, R.id.cc_area})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit_confirm:
                showLoading();
                if (checkParams()) {
                    customerCreate.appData.url.clear();
                    uploadPhotoList();
                } else {
                    dismissLoading();
                }
                break;
            case R.id.cc_card_face:
                showPickHeaderMenu(PHOTO_ID_FACE);
                break;
            case R.id.cc_card_back:
                showPickHeaderMenu(PHOTO_ID_BACK);
                break;
            case R.id.cc_age:
                AppUtils.hideSoftInput(this, getCurrentFocus());
                showPickBirthday();
                break;
            case R.id.cc_area:
                goToActivityForResult(ChoiceAreaActivity.class, REQUEST_AREA);
                break;
        }
    }

    private int photoSize = 0;

    private void uploadPhotoList() {
        ArrayList<UploadMap> uploadPaths = new ArrayList<>();
        //计算上传图片数量
        if (!TextUtils.isEmpty(path_id_face)) {
            if (path_id_face.startsWith("http")) {
                customerCreate.appData.url.add(path_id_face);
            } else {
                uploadPaths.add(new UploadMap("ID", path_id_face));
            }
        }
        if (!TextUtils.isEmpty(path_id_back)) {
            if (path_id_back.startsWith("http")) {
                customerCreate.appData.url.add(path_id_back);
            } else {
                uploadPaths.add(new UploadMap("ID", path_id_back));
            }
        }
        if (!TextUtils.isEmpty(customer_pic)) {
            if (customer_pic.startsWith("http")) {
                customerCreate.appData.url.add(customer_pic);
            } else {
                uploadPaths.add(new UploadMap("farmer", customer_pic));
            }
        }
        for (String path : fishpondPhotos) {
            if (path.startsWith("http")) {
                customerCreate.appData.url.add(path);
            } else {
                uploadPaths.add(new UploadMap("pond", path));
            }
        }
        for (String path : formPhotos) {
            if (path.startsWith("http")) {
                customerCreate.appData.url.add(path);
            } else {
                uploadPaths.add(new UploadMap("account", path));
            }
        }
        if (uploadPaths.size() > 0) { //没有图片直接提交
            photoSize = uploadPaths.size();
            for (UploadMap map : uploadPaths) {
                uploadPhoto(map.type, map.path);
            }
        } else {
            createCustomer();
        }
    }

    private boolean checkParams() {
        if (TextUtils.isEmpty(ccName.getText())) {
            showToast("请输入姓名");
            return false;
        }
        if (TextUtils.isEmpty(ccMobile.getText())) {
            showToast("请输入联系电话");
            return false;
        } else if (ccMobile.getText().length() != 11) {
            showToast("手机号码位数错误");
            return false;
        } else if (customerData == null && !isMobileOk) {
            showToast("手机号码已注册");
            return false;
        }
        if (TextUtils.isEmpty(ccBirth.getText())) {
            showToast("请选择出生日期");
            return false;
        }
        if (TextUtils.isEmpty(ccArea.getText())) {
            showToast("请选择行政区域");
            return false;
        }
        if (TextUtils.isEmpty(ccAdsDetail.getText())) {
            showToast("请输入家庭详细地址");
            return false;
        }
        customerCreate.loginid = loginId;
        customerCreate.appData.loginid = loginId;
        customerCreate.appData.farmerName = ccName.getText().toString();
        customerCreate.appData.contactInfo = ccMobile.getText().toString();
        customerCreate.appData.birthday = ccBirth.getText().toString();
        customerCreate.appData.sex = customerMale.isChecked() ? "男" : "女";
        customerCreate.appData.homeAddress = ccAdsDetail.getText().toString();
        customerCreate.appData.idNumber = ccIdentifyNo.getText().toString();
        if (formPhotos.size() > 0) {
            if (TextUtils.isEmpty(path_id_face) || TextUtils.isEmpty(path_id_back)) {
                showToast("请上传身份证图片");
                return false;
            }
            if (TextUtils.isEmpty(ccIdentifyNo.getText())) {
                showToast("请填写身份证号");
                return false;
            } else if (ccIdentifyNo.getText().length() != 18) {
                showToast("身份证号位数错误");
                return false;
            } else if (customerData == null && !isIdCardOk) {
                showToast("身份证号已注册");
                return false;
            }
            if (TextUtils.isEmpty(ccBirth.getText())) {
                showToast("请选择出生日期");
                return false;
            }
            customerCreate.appData.farmerType = "签约用户";
        } else {
            customerCreate.appData.farmerType = "意向用户";
        }
        return true;
    }

    private void uploadPhoto(String type, String path) {
        UploadModel uploadModel = new UploadModel();
        uploadModel.type = type;
        uploadModel.imageData = AppUtils.fileToBase64(path);
        uploadModel.imageName = System.currentTimeMillis() + path.substring(path.lastIndexOf("."));
        postData(HttpConfig.UPLOAD_IMAGE.replace("{loginid}", loginId), uploadModel.toString(), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
                customerCreate.appData.url.add(d);
                photoSize--;
                if (photoSize == 0) {
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
        LogInfo.error(customerCreate.toString());
        postData(HttpConfig.CREATE_TASK_CUSTOMER.replace("{PROID}", Constants.CUSTOMER_CREATE), customerCreate.toString(), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
                LogInfo.error(d);
                ContactItem contactItem = new ContactItem();
                contactItem.pinyin = "签约用户".equals(customerCreate.appData.farmerType) ? "s" : "n";
                contactItem.name = customerCreate.appData.farmerName;
                contactItem.farmerId = d;
                contactItem.farmerAdd = customerCreate.appData.region + customerCreate.appData.homeAddress;
                contactItem.mobile = customerCreate.appData.contactInfo;
                contactItem.farmerPic = customer_pic;
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.INTENT_OBJECT, contactItem);
                goToActivity(CustomerCreateSuccessActivity.class, bundle);
                onBackPressed();
                BmApplication.getInstance().clearSteamActivity();
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

    private void showPickBirthday() {
        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(ccBirth.getText())) {
            calendar.setTime(DateFormatUtil.getString2Date(ccBirth.getText().toString(), DateFormatUtil.DATE_FORMAT));
        }
        //时间选择器
        if (mTimePickerView == null) {
            mTimePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    String birth = DateFormatUtil.getDate2Str(date, DateFormatUtil.DATE_FORMAT);
                    ccBirth.setText(birth);
                }
            }).setType(new boolean[]{true, true, true, false, false, false}).build();
        }
        mTimePickerView.setDate(calendar);
        mTimePickerView.show();
    }

    private void showPickHeaderMenu(final String tag) {
        if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_STORAGE)) {
            return;
        }
        menuDialog.setMenuItems(new String[]{"拍照", "图库选择"}, new MenuDialog.OnMenuItemClickedListener() {
            @Override
            public void MenuItemClicked(String value, int position) {
                switch (position) {
                    case 0:
                        switch (tag) {
                            case PHOTO_ID_FACE:
                                takePhoto(REQUEST_ID_FACE_CAMERA);
                                break;
                            case PHOTO_ID_BACK:
                                takePhoto(REQUEST_ID_BACK_CAMERA);
                                break;
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
                            case PHOTO_ID_FACE:
                                pickPhotos(REQUEST_ID_FACE_PHOTO);
                                break;
                            case PHOTO_ID_BACK:
                                pickPhotos(REQUEST_ID_BACK_PHOTO);
                                break;
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
            Uri fileUri = FileProvider.getUriForFile(this, "com.qyt.bm.fileprovider", cameraFile);
            startActivityForResult(CameraUtil.getTakePickIntent(fileUri), requestCode);
        }
    }

    private void pickPhotos(int requestCode) {
        Bundle bundle = new Bundle();
        switch (requestCode) {
            case REQUEST_ID_FACE_PHOTO:
            case REQUEST_ID_BACK_PHOTO:
            case REQUEST_FARMER_PHOTO:
                bundle.putBoolean("single", true);
                break;
        }
        goToActivityForResult(PhotosActivity.class, bundle, requestCode);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_AREA:
                    AreaItem area = data.getParcelableExtra(Constants.INTENT_OBJECT);
                    ccArea.setText(area.name);
                    customerCreate.appData.areaId = area.id;
                    customerCreate.appData.region = area.name;
                    break;
                case REQUEST_ID_FACE_CAMERA:
                    path_id_face = cameraFile.getPath();
                    lubanCompress(REQUEST_ID_FACE_CAMERA, cameraFile);
                    Glide.with(this).load(cameraFile).into(ccCardFace);
                    break;
                case REQUEST_ID_BACK_CAMERA:
                    path_id_back = cameraFile.getPath();
                    lubanCompress(REQUEST_ID_BACK_CAMERA, cameraFile);
                    Glide.with(this).load(cameraFile).into(ccCardBack);
                    break;
                case REQUEST_ID_FACE_PHOTO:
                    path_id_face = data.getExtras().getString("photo");
                    Glide.with(this).load(path_id_face).into(ccCardFace);
                    break;
                case REQUEST_ID_BACK_PHOTO:
                    path_id_back = data.getExtras().getString("photo");
                    Glide.with(this).load(path_id_back).into(ccCardBack);
                    break;
                case REQUEST_FORM_CAMERA:
                    lubanCompress(REQUEST_FORM_CAMERA, cameraFile);
//                    formPhotos.clear();
//                    ImageSelect.mSelectedImage.add(cameraFile.getPath());
//                    formPhotos.addAll(ImageSelect.mSelectedImage);
//                    formPhotosAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_FORM_PHOTO:
                    formPhotos.clear();
                    formPhotos.addAll(ImageSelect.mSelectedImage);
                    formPhotosAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_FARMER_CAMERA:
                    customer_pic = cameraFile.getPath();
                    lubanCompress(REQUEST_FARMER_CAMERA,cameraFile);
                    Glide.with(this).load(cameraFile).into(ccCustomerPictures);
                    break;
                case REQUEST_FARMER_PHOTO:
                    customer_pic = data.getExtras().getString("photo");
                    Glide.with(this).load(customer_pic).into(ccCustomerPictures);
                    break;
                case REQUEST_POND_CAMERA:
                    lubanCompress(REQUEST_POND_CAMERA,cameraFile);
//                    fishpondPhotos.clear();
//                    ImageSelect.mSelectedImage.add(cameraFile.getPath());
//                    fishpondPhotos.addAll(ImageSelect.mSelectedImage);
//                    fishpondPhotosAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_POND_PHOTO:
                    fishpondPhotos.clear();
                    fishpondPhotos.addAll(ImageSelect.mSelectedImage);
                    fishpondPhotosAdapter.notifyDataSetChanged();
                    break;
            }
        }
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
                        LogInfo.error(file.getPath());
                        switch (code) {
                            case REQUEST_ID_FACE_CAMERA:
                                path_id_face = file.getPath();
                                break;
                            case REQUEST_ID_BACK_CAMERA:
                                path_id_back = file.getPath();
                                break;
                            case REQUEST_FARMER_CAMERA:
                                customer_pic = file.getPath();
                                break;
                            case REQUEST_FORM_CAMERA:
                                formPhotos.clear();
                                ImageSelect.mSelectedImage.add(file.getPath());
                                formPhotos.addAll(ImageSelect.mSelectedImage);
                                formPhotosAdapter.notifyDataSetChanged();
                                break;
                            case REQUEST_POND_CAMERA:
                                fishpondPhotos.clear();
                                ImageSelect.mSelectedImage.add(file.getPath());
                                fishpondPhotos.addAll(ImageSelect.mSelectedImage);
                                fishpondPhotosAdapter.notifyDataSetChanged();
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        switch (code) {
                            case REQUEST_FORM_CAMERA:
                                formPhotos.clear();
                                ImageSelect.mSelectedImage.add(cameraFile.getPath());
                                formPhotos.addAll(ImageSelect.mSelectedImage);
                                formPhotosAdapter.notifyDataSetChanged();
                                break;
                            case REQUEST_POND_CAMERA:
                                fishpondPhotos.clear();
                                ImageSelect.mSelectedImage.add(cameraFile.getPath());
                                fishpondPhotos.addAll(ImageSelect.mSelectedImage);
                                fishpondPhotosAdapter.notifyDataSetChanged();
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
