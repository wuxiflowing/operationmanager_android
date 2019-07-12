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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bangqu.lib.utils.CameraUtil;
import com.bangqu.lib.widget.MenuDialog;
import com.bangqu.lib.widget.RoundImageView;
import com.bangqu.lib.widget.TextViewPlus;
import com.bangqu.photos.PhotosActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.qyt.om.R;
import com.qyt.om.base.BaseActivity;
import com.qyt.om.comm.Constants;
import com.qyt.om.model.EdittextBundle;
import com.qyt.om.model.UserInfo;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.user_header)
    RoundImageView userHeader;
    @BindView(R.id.user_mobile)
    TextView userMobile;
    @BindView(R.id.user_nickname)
    TextViewPlus userNickname;
    @BindView(R.id.manager_area)
    TextView managerArea;

    final int REQUEST_PHOTO = 1;
    final int REQUEST_CROP = 2;
    final int REQUEST_CAMERA = 3;
    final int PERMISSIONS_REQUEST_STORAGE = 1001;
    final int PERMISSIONS_REQUEST_CAMERA = 1002;
    private MenuDialog menuDialog;
    private File cropFile, cameraFile;
    final int REQUEST_NICKNAME = 8001;
    final int REQUEST_INTRO = 8002;

    private UserInfo userInfo;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title.setText("个人中心");
        menuDialog = new MenuDialog(this);
        userInfo = sharedPref.getJsonInfo(Constants.USER_INFO, UserInfo.class);
        menuDialog = new MenuDialog(this);
        userNickname.setText(userInfo.userName);
        userMobile.setText(userInfo.phone);
        managerArea.setText(userInfo.dep);
        Glide.with(this).load(userInfo.photoUrl).apply(new RequestOptions().error(R.mipmap.default_header)).into(userHeader);
    }

    @OnClick({R.id.update_header, R.id.update_nickname, R.id.update_sex})
    public void onViewClicked(View view) {
        EdittextBundle edittextBundle = new EdittextBundle();
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.update_header:
//                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_STORAGE)) {
//                    showPickHeaderMenu();
//                }
                break;
            case R.id.update_nickname:
//                edittextBundle.title = "姓名";
//                edittextBundle.value = userInfo.userName;
//                edittextBundle.hint = "请输入姓名";
//                bundle.putParcelable(Constants.INTENT_OBJECT, edittextBundle);
//                goToActivityForResult(EdittextActivity.class, bundle, REQUEST_NICKNAME);
                break;
        }
    }

    private void showPickHeaderMenu() {
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

    private void takePhoto() {
        if (checkPermission(Manifest.permission.CAMERA, PERMISSIONS_REQUEST_CAMERA)) {
            cameraFile = new File(getDiskCacheDir(this), CameraUtil.getPhotoFileName());
            Uri fileUri = FileProvider.getUriForFile(this, "com.qyt.bm.fileprovider", cameraFile);
            startActivityForResult(CameraUtil.getTakePickIntent(fileUri), REQUEST_CAMERA);
        }
    }

    private void pickPhotos() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("single", true);
        goToActivityForResult(PhotosActivity.class, bundle, REQUEST_PHOTO);
    }

    private void cropPhoto(String path) {
        cropFile = new File(getDiskCacheDir(this), "header.jpg");
        try {
            if (cropFile.exists()) {
                cropFile.delete();
            }
            cropFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri resouseUri = CameraUtil.getImageContentUri(this, new File(path));
        startActivityForResult(CameraUtil.getPhotoCrop(resouseUri, Uri.fromFile(cropFile)), REQUEST_CROP);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_NICKNAME:
                    String nickname = data.getStringExtra(Constants.INTENT_OBJECT);
                    userNickname.setText(nickname);
                    break;
                case REQUEST_CAMERA:
                    cropPhoto(cameraFile.getPath());
                    break;
                case REQUEST_PHOTO:
                    Bundle bundle = data.getExtras();
                    String path = bundle.getString("photo");
                    cropPhoto(path);
                    break;
                case REQUEST_CROP:
                    Glide.with(this).load(Uri.fromFile(cropFile))
                            .apply(new RequestOptions().signature(new ObjectKey(cropFile.lastModified()))).into(userHeader);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PERMISSIONS_REQUEST_STORAGE:
                    showPickHeaderMenu();
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
}
