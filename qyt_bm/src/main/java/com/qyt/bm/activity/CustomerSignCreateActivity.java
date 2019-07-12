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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.utils.CameraUtil;
import com.bangqu.lib.utils.DateFormatUtil;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.ConfirmDialog;
import com.bangqu.lib.widget.MenuDialog;
import com.bangqu.lib.widget.RoundImageView;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.lib.widget.UnScrollListView;
import com.bangqu.photos.GalleryActivity;
import com.bangqu.photos.PhotosActivity;
import com.bangqu.photos.util.ImageSelect;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qyt.bm.BmApplication;
import com.qyt.bm.R;
import com.qyt.bm.adapter.DeviceChoiceAdapter;
import com.qyt.bm.adapter.PhotosChoiceAdapter;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.model.ContactItem;
import com.qyt.bm.model.DeviceChoiceModel;
import com.qyt.bm.request.CustomerPact;
import com.qyt.bm.request.PactSubmit;
import com.qyt.bm.request.UploadModel;
import com.qyt.bm.utils.LogInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class CustomerSignCreateActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.sign_customer_header)
    RoundImageView signCustomerHeader;
    @BindView(R.id.sign_customer_name)
    TextView signCustomerName;
    @BindView(R.id.sign_customer_address)
    TextView signCustomerAddress;
    @BindView(R.id.sign_bill_list)
    LinearLayout signBillList;

    private ArrayList<PactSubmit> signBillItems = new ArrayList<>();
    private ContactItem contactItem;
    private String loginid;
    protected LayoutInflater mInflater;
    private ArrayList<ViewHolder> viewHolders = new ArrayList<>();
    final int PERMISSIONS_REQUEST_STORAGE = 1001;
    final int PERMISSIONS_REQUEST_CAMERA = 1002;
    final int REQUEST_PHOTO = 1;
    final int REQUEST_CAMERA = 3;
    private File cameraFile;
    private MenuDialog menuDialog;
    private ArrayList<DeviceChoiceModel> deviceChoiceModels = new ArrayList<>();

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_customersigncreate);
        contactItem = getIntent().getParcelableExtra(Constants.INTENT_OBJECT);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("签约资料");
        loginid = sharedPref.getString(Constants.LOGIN_ID);
        signCustomerName.setText(contactItem.name);
        signCustomerAddress.setText(contactItem.farmerAdd);
        mInflater = LayoutInflater.from(mContext);
        if (!TextUtils.isEmpty(contactItem.farmerPic)) {
            Glide.with(mContext).load(contactItem.farmerPic.trim()).apply(new RequestOptions().placeholder(R.mipmap.contacts_header).error(R.mipmap.contacts_header)).into(signCustomerHeader);
        } else {
            Glide.with(mContext).load(R.mipmap.contacts_header).into(signCustomerHeader);
        }
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();

    }

    @Override
    protected void requestData() {
        super.requestData();
        /*获取设备清单*/
        getData(HttpConfig.INSTALL_DEVICE_LIST, new ResponseCallBack<ArrayList<DeviceChoiceModel>>() {
            @Override
            public void onSuccessResponse(ArrayList<DeviceChoiceModel> d, String msg) {
                deviceChoiceModels.clear();
                if (d != null && d.size() > 0) {
                    deviceChoiceModels.addAll(d);
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

    @OnClick({R.id.sign_customer_call, R.id.sign_add_bill, R.id.submit_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_customer_call:
                new ConfirmDialog(this, "拨打电话", contactItem.mobile, "拨号", "取消", new DialogConfirmListener() {
                    @Override
                    public void onDialogConfirm(boolean result, Object v) {
                        if (result) {
                            AppUtils.dialPhone(mContext, contactItem.mobile);
                        }
                    }
                }).show();
                break;
            case R.id.sign_add_bill:
                addNewBillItems();
                break;
            case R.id.submit_confirm:
                if (checkNull()) {
                    showLoading();
                    dealPactPhotos();
                }
                break;
        }
    }

    private boolean checkNull() {
        if (signBillItems.size() == 0) {
            showToast("请新增合同类型");
            return false;
        }
        for (PactSubmit pactSubmit : signBillItems) {
            if (TextUtils.isEmpty(pactSubmit.contractName) || TextUtils.isEmpty(pactSubmit.contractAmount)) {
                showToast("签约单资料不完整");
                return false;
            }
            if (pactSubmit.contractDeviceList.size() == 0) {
                showToast("请选择设备型号数量");
                return false;
            }
        }
        for (ViewHolder viewHolder : viewHolders) {
            if (viewHolder.pactPhotos.size() == 0) {
                showToast("请上传合同附件");
                return false;
            }
        }
        return true;
    }

    private int uploadIndex = 0;
    private int sumPhotos = 0; //当前条目图片总数

    private void dealPactPhotos() {
        if (viewHolders.size() != signBillItems.size()) {
            showToast("数据处理错误");
            return;
        }
        if (uploadIndex < viewHolders.size()) {
            ViewHolder viewHolder = viewHolders.get(uploadIndex);
            sumPhotos = viewHolder.pactPhotos.size() + viewHolder.payPhotos.size();
            for (String pact : viewHolder.pactPhotos) {
                uploadPhoto("contract", pact);
            }
            for (String pay : viewHolder.payPhotos) {
                uploadPhoto("pay", pay);
            }
        } else {
            createSignBill();
        }
    }

    private void uploadPhoto(String type, String path) {
        final UploadModel uploadModel = new UploadModel();
        uploadModel.type = type;
        uploadModel.imageData = AppUtils.fileToBase64(path);
        uploadModel.imageName = System.currentTimeMillis() + path.substring(path.lastIndexOf("."));
        postData(HttpConfig.UPLOAD_IMAGE.replace("{loginid}", loginid), uploadModel.toString(), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
                signBillItems.get(uploadIndex).url.add(d);
                if (signBillItems.get(uploadIndex).url.size() == sumPhotos) {
                    uploadIndex++;
                    dealPactPhotos();
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

    private void createSignBill() {
        CustomerPact customerPact = new CustomerPact();
        customerPact.loginid = loginid;
        customerPact.farmerId = contactItem.farmerId;
        customerPact.appData.contractList = signBillItems;
        LogInfo.error(customerPact.toString());
        postData(HttpConfig.CREATE_TASK_CUSTOMER.replace("{PROID}", Constants.CUSTOMER_SIGN), customerPact.toString(), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
                LogInfo.error(d);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.INTENT_OBJECT, contactItem);
                goToActivity(CustomerSignCreateSuccessActivity.class, bundle);
                dismissLoading();
                BmApplication.getInstance().clearSteamActivity();
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

    private void addNewBillItems() {
        View billView = mInflater.inflate(R.layout.item_signbill, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, AppUtils.dp2px(this, 8));
        billView.setLayoutParams(lp);
        viewHolders.add(new ViewHolder(signBillList.getChildCount(), billView));
        signBillList.addView(billView);
        signBillItems.add(new PactSubmit(loginid, contactItem.farmerId));
    }

    class ViewHolder {
        @BindView(R.id.signbill_del)
        TextView signbillDel;
        @BindView(R.id.signbill_type)
        RadioGroup signbillType;
        @BindView(R.id.signbill_date)
        TextViewPlus signbillDate;
        @BindView(R.id.signbill_name)
        TextView signbillName;
        @BindView(R.id.signbill_amount)
        EditText signbillAmount;
        @BindView(R.id.signbill_ispay)
        RadioGroup signbillIspay;
        @BindView(R.id.signbill_paytype)
        RadioGroup signbillPaytype;
        @BindView(R.id.signbill_paysum)
        EditText signbillPaysum;
        @BindView(R.id.pay_info)
        LinearLayout payInfo;
        @BindView(R.id.pact_pictures)
        RecyclerView pactPictures;
        @BindView(R.id.pay_pictures)
        RecyclerView payPictures;
        @BindView(R.id.pay_file)
        LinearLayout payFile;
        @BindView(R.id.install_device_list)
        UnScrollListView installDeviceList;
        @BindView(R.id.install_device_count)
        TextView installDeviceCount;

        private int position;
        private TimePickerView mTimePickerView;
        private PhotosChoiceAdapter pactPhotosAdapter;
        private ArrayList<String> pactPhotos = new ArrayList<>();
        private PhotosChoiceAdapter payPhotosAdapter;
        private ArrayList<String> payPhotos = new ArrayList<>();
        private DeviceChoiceAdapter deviceChoiceAdapter;
        private ArrayList<DeviceChoiceModel> deviceModels = new ArrayList<>();
        private int deviceCount = 0;

        RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getId()) {
                    case R.id.signbill_ispay:
                        switch (checkedId) {
                            case R.id.ispay_false:
                                payInfo.setVisibility(View.GONE);
                                payFile.setVisibility(View.GONE);
                                break;
                            case R.id.ispay_true:
                                payInfo.setVisibility(View.VISIBLE);
                                payFile.setVisibility(View.VISIBLE);
                                break;
                        }
                        break;
                    case R.id.signbill_type:
                        switch (checkedId) {
                            case R.id.type_deposit:
                                signBillItems.get(position).contractType = "押金合同";
                                break;
                            case R.id.type_service:
                                signBillItems.get(position).contractType = "服务费合同";
                                break;
                            case R.id.type_prepay:
                                signBillItems.get(position).contractType = "预付款合同";
                                break;
                        }
                        if (!TextUtils.isEmpty(signBillItems.get(position).contractDate)) {
                            String pactName = CustomerSignCreateActivity.this.contactItem.name +
                                    signBillItems.get(position).contractType +
                                    signBillItems.get(position).contractDate.replace("-", "");
                            signbillName.setText(pactName);
                            signBillItems.get(position).contractName = pactName;
                        }
                        break;
                    case R.id.signbill_paytype:
                        switch (checkedId) {
                            case R.id.pay_crash:
                                signBillItems.get(position).paymentMethod = "现金";
                                break;
                            case R.id.pay_bank:
                                signBillItems.get(position).paymentMethod = "银行汇款";
                                break;
                        }
                        break;
                }
            }
        };

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signbill_del:
                        new ConfirmDialog(mContext, "是否删除签约单？", new DialogConfirmListener() {
                            @Override
                            public void onDialogConfirm(boolean result, Object value) {
                                if (result) {
                                    signBillItems.remove(position);
                                    signBillList.removeViewAt(position);
                                    for (int i = position + 1; i < viewHolders.size(); i++) {
                                        viewHolders.get(i).position = i - 1;
                                    }
                                    viewHolders.remove(position);
                                }
                            }
                        }).show();
                        break;
                    case R.id.signbill_date:
                        showPickDate();
                        break;
                }
            }
        };

        ViewHolder(int position, View view) {
            this.position = position;
            ButterKnife.bind(this, view);
            pactPhotosAdapter = new PhotosChoiceAdapter(mContext, pactPhotos);
            pactPictures.setLayoutManager(
                    new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            pactPictures.setAdapter(pactPhotosAdapter);
            payPhotosAdapter = new PhotosChoiceAdapter(mContext, payPhotos);
            payPictures.setLayoutManager(
                    new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            payPictures.setAdapter(payPhotosAdapter);
            deviceModels.clear();
            for (DeviceChoiceModel model : deviceChoiceModels) {
                deviceModels.add(model.clone());
            }
            deviceChoiceAdapter = new DeviceChoiceAdapter(mContext, deviceModels);
            installDeviceList.setAdapter(deviceChoiceAdapter);
            signbillIspay.setOnCheckedChangeListener(onCheckedChangeListener);
            signbillType.setOnCheckedChangeListener(onCheckedChangeListener);
            signbillPaytype.setOnCheckedChangeListener(onCheckedChangeListener);
            signbillDel.setOnClickListener(onClickListener);
            signbillDate.setOnClickListener(onClickListener);
            deviceChoiceAdapter.setOnItemCountChange(new DeviceChoiceAdapter.OnItemCountChange() {
                @Override
                public void onCountChange(int change) {
                    deviceCount += change;
                    installDeviceCount.setText("合计：" + deviceCount + "套");
                    signBillItems.get(ViewHolder.this.position).contractDeviceList.clear();
                    for (DeviceChoiceModel model : deviceModels) {
                        if (model.selected)
                            signBillItems.get(ViewHolder.this.position).contractDeviceList
                                    .add(new PactSubmit.DeviceInfo(model.deviceTypeId, model.deviceTypeName, model.count));
                    }
                }
            });
            pactPhotosAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
                @Override
                public void onItemClick(int position, String value) {
                    ImageSelect.mSelectedImage.clear();
                    ImageSelect.mSelectedImage.addAll(pactPhotos);
                    if (position == pactPhotos.size()) {
                        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_STORAGE)) {
                            showMenuDialog(ViewHolder.this.position, "pact");
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
            payPhotosAdapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener<String>() {
                @Override
                public void onItemClick(int position, String value) {
                    ImageSelect.mSelectedImage.clear();
                    ImageSelect.mSelectedImage.addAll(payPhotos);
                    if (position == payPhotos.size()) {
                        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_STORAGE)) {
                            showMenuDialog(ViewHolder.this.position, "pay");
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
            signbillAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    signBillItems.get(ViewHolder.this.position).contractAmount = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            signbillPaysum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    signBillItems.get(ViewHolder.this.position).receivedAmount = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        private void showPickDate() {
            Calendar calendar = Calendar.getInstance();
            if (!TextUtils.isEmpty(signbillDate.getText())) {
                calendar.setTime(DateFormatUtil.getString2Date(signbillDate.getText().toString(), DateFormatUtil.DATE_FORMAT));
            }
            //时间选择器
            if (mTimePickerView == null) {
                mTimePickerView = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        String birth = DateFormatUtil.getDate2Str(date, DateFormatUtil.DATE_FORMAT);
                        signbillDate.setText(birth);
                        signBillItems.get(position).contractDate = birth;
                        String pactName = CustomerSignCreateActivity.this.contactItem.name +
                                signBillItems.get(position).contractType + birth.replace("-", "");
                        signbillName.setText(pactName);
                        signBillItems.get(position).contractName = pactName;
                    }
                }).setType(new boolean[]{true, true, true, false, false, false}).build();
            }
            mTimePickerView.setDate(calendar);
            mTimePickerView.show();
        }
    }

    private int position;
    private String pact;

    private void showMenuDialog(int position, String pact) {
        this.position = position;
        this.pact = pact;
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
            cameraFile = new File(AppUtils.getDiskCacheDir(this), CameraUtil.getPhotoFileName());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    lubanCompress(cameraFile);
                    break;
                case REQUEST_PHOTO:
                    switch (pact) {
                        case "pact":
                            viewHolders.get(position).pactPhotos.clear();
                            viewHolders.get(position).pactPhotos.addAll(ImageSelect.mSelectedImage);
                            viewHolders.get(position).pactPhotosAdapter.notifyDataSetChanged();
                            break;
                        case "pay":
                            viewHolders.get(position).payPhotos.clear();
                            viewHolders.get(position).payPhotos.addAll(ImageSelect.mSelectedImage);
                            viewHolders.get(position).payPhotosAdapter.notifyDataSetChanged();
                            break;
                    }
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
                        switch (pact) {
                            case "pact":
                                viewHolders.get(position).pactPhotos.clear();
                                ImageSelect.mSelectedImage.add(file.getPath());
                                viewHolders.get(position).pactPhotos.addAll(ImageSelect.mSelectedImage);
                                viewHolders.get(position).pactPhotosAdapter.notifyDataSetChanged();
                                break;
                            case "pay":
                                viewHolders.get(position).payPhotos.clear();
                                ImageSelect.mSelectedImage.add(file.getPath());
                                viewHolders.get(position).payPhotos.addAll(ImageSelect.mSelectedImage);
                                viewHolders.get(position).payPhotosAdapter.notifyDataSetChanged();
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        switch (pact) {
                            case "pact":
                                viewHolders.get(position).pactPhotos.clear();
                                ImageSelect.mSelectedImage.add(cameraFile.getPath());
                                viewHolders.get(position).pactPhotos.addAll(ImageSelect.mSelectedImage);
                                viewHolders.get(position).pactPhotosAdapter.notifyDataSetChanged();
                                break;
                            case "pay":
                                viewHolders.get(position).payPhotos.clear();
                                ImageSelect.mSelectedImage.add(cameraFile.getPath());
                                viewHolders.get(position).payPhotos.addAll(ImageSelect.mSelectedImage);
                                viewHolders.get(position).payPhotosAdapter.notifyDataSetChanged();
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
