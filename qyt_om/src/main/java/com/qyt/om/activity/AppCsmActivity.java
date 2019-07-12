package com.qyt.om.activity;

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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bangqu.lib.listener.RecyclerViewItemClickListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.utils.CameraUtil;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.MenuDialog;
import com.bangqu.photos.GalleryActivity;
import com.bangqu.photos.PhotosActivity;
import com.bangqu.photos.util.ImageSelect;
import com.qyt.om.R;
import com.qyt.om.adapter.PhotosChoiceAdapter;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.comm.HttpConfig;
import com.qyt.om.request.AppCsm;
import com.qyt.om.request.UploadModel;
import com.qyt.om.utils.LogInfo;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class AppCsmActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.csm_types)
    RadioGroup csmTypes;
    @BindView(R.id.csm_title)
    EditText csmTitle;
    @BindView(R.id.csm_context)
    EditText csmContext;
    @BindView(R.id.csm_files)
    RecyclerView csmFiles;

    /*图片选择*/
    private File cameraFile;
    final int REQUEST_PHOTO = 1;
    final int REQUEST_CAMERA = 3;
    final int PERMISSIONS_REQUEST_STORAGE = 1001;
    final int PERMISSIONS_REQUEST_CAMERA = 1002;
    private MenuDialog menuDialog;
    private PhotosChoiceAdapter photosAdapter;
    private ArrayList<String> photos = new ArrayList<>();
    private String type = "建议";
    private AppCsm appCsm = new AppCsm();
    private String loginid;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_appcsm);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("意见反馈");
        loginid = sharedPref.getString(Constants.LOGIN_ID);
        photosAdapter = new PhotosChoiceAdapter(this, photos);
        csmFiles.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        csmFiles.setAdapter(photosAdapter);
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
        csmTypes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.csm_type1:
                        type = "建议";
                        break;
                    case R.id.csm_type2:
                        type = "意见";
                        break;
                    case R.id.csm_type3:
                        type = "投诉";
                        break;
                    case R.id.csm_type4:
                        type = "其他";
                        break;
                }
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
            Uri fileUri = FileProvider.getUriForFile(this, "com.qyt.om.fileprovider", cameraFile);
            startActivityForResult(CameraUtil.getTakePickIntent(fileUri), REQUEST_CAMERA);
        }
    }

    private void pickPhotos() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("single", false);
        goToActivityForResult(PhotosActivity.class, bundle, REQUEST_PHOTO);
    }

    @OnClick(R.id.submit_confirm)
    public void onViewClicked() {
        if (TextUtils.isEmpty(csmTitle.getText())) {
            showToast("请输入标题");
            return;
        }
        if (TextUtils.isEmpty(csmContext.getText())) {
            showToast("请输入反馈内容");
            return;
        }
        showLoading();
        if (photos.size() > 0) {
            for (String path : photos) {
                uploadPhoto("csm", path);
            }
        } else {
            submitCsm();
        }
    }

    private void submitCsm() {
        appCsm.type = type;
        appCsm.title = csmTitle.getText().toString();
        appCsm.context = csmContext.getText().toString();
        appCsm.loginId = loginid;
        LogInfo.error(appCsm.toString());
        postData(HttpConfig.POST_APPCSM, appCsm.toString(), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
                showToast("提交成功");
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

    private void uploadPhoto(String type, String path) {
        UploadModel uploadModel = new UploadModel();
        uploadModel.type = type;
        uploadModel.imageData = AppUtils.fileToBase64(path);
        uploadModel.imageName = System.currentTimeMillis() + path.substring(path.lastIndexOf("."));
        postData(HttpConfig.UPLOAD_IMAGE.replace("{loginid}", loginid), uploadModel.toString(), new ResponseCallBack<String>() {
            @Override
            public void onSuccessResponse(String d, String msg) {
                appCsm.attachfile.add(d);
                if (appCsm.attachfile.size() == photos.size()) {
                    submitCsm();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageSelect.mSelectedImage.clear();
    }
}
