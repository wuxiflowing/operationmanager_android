package com.qyt.bm.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.download.presenter.DownLoadImpl;
import com.bangqu.download.service.DownloadService;
import com.bangqu.download.utils.FileUtil;
import com.bangqu.download.widget.DownloadDialog;
import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.utils.AppUtils;
import com.bangqu.lib.volley.ResponseCallBack;
import com.bangqu.lib.widget.UpdateAppDialog;
import com.igexin.sdk.PushManager;
import com.qyt.bm.BmApplication;
import com.qyt.bm.R;
import com.qyt.bm.activity.fragment.CustomerFragment;
import com.qyt.bm.activity.fragment.MessageFragment;
import com.qyt.bm.activity.fragment.MineFragment;
import com.qyt.bm.activity.fragment.WorkFragment;
import com.qyt.bm.base.BaseActivity;
import com.qyt.bm.comm.HttpConfig;
import com.qyt.bm.response.VersionInfo;
import com.qyt.bm.service.GtuiIntentService;
import com.qyt.bm.service.GtuiPushService;
import com.qyt.bm.utils.LogInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements DownLoadImpl {

    @BindView(R.id.main_tabLayout)
    TabLayout mainTabLayout;

    private int[] images = new int[]{
            R.drawable.tabbar_message, R.drawable.tabbar_work, R.drawable.tabbar_customer, R.drawable.tabbar_mine
    };

    private String[] tabTitles;
    private WorkFragment workFragment;
    private CustomerFragment customerFragment;
    private MessageFragment messageFragment;
    private MineFragment mineFragment;
    private boolean isBindService;
    DownloadDialog progressDialog;
    DownloadService downloadService;

    @Override
    protected void setLayoutView(Bundle savedInstanceState) {
        super.setLayoutView(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        super.initView();
        tabTitles = getResources().getStringArray(R.array.main_tab_array);
        for (int i = 0; i < tabTitles.length; i++) {
            mainTabLayout.addTab(mainTabLayout.newTab().setCustomView(getTabView(i)));
        }
        replaceFragment(0);
//        setMessageCount(99);
        PushManager.getInstance().initialize(this.getApplicationContext(), GtuiPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GtuiIntentService.class);
        PushManager.getInstance().turnOnPush(getApplicationContext());
        checkAppVersion();
    }

    private void checkAppVersion() {
        getData(HttpConfig.VERSION_UPDATE, new ResponseCallBack<VersionInfo>() {
            @Override
            public void onSuccessResponse(final VersionInfo d, String msg) {
                if (AppUtils.getVersionCode(mContext) < d.buildVersionNo) {
                    new UpdateAppDialog(MainActivity.this, d.build_version, d.buildUpdateDescription, new DialogConfirmListener() {
                        @Override
                        public void onDialogConfirm(boolean result, Object value) {
                            if (result) {
                                downLoadApk(d.downloadURL);
                            }
                        }
                    }).show();
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

    private void downLoadApk(String url) {
        this.bindService(url, "yangzhiguanjia.apk");
        progressDialog = new DownloadDialog(this, "下载中", "后台下载", "取消", new DownloadDialog.DialogConfirmListener() {
            @Override
            public void onDialogConfirm(boolean result, Object value) {
                if (result) {
                    Uri fileUri = FileProvider.getUriForFile(mContext, "com.qyt.bm.fileprovider", (File) value);
                    FileUtil.openFile(MainActivity.this, (File) value, fileUri);
                } else {
                    if (downloadService != null)
                        downloadService.cancelService();
                }
            }
        });
        progressDialog.show();
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
            downloadService = binder.getService();
            //接口回调，下载进度
            downloadService.setOnProgressListener(new DownloadService.OnProgressListener() {
                @Override
                public void onProgress(float fraction) {
                    if (progressDialog != null) {
                        progressDialog.setProgress((int) (fraction * 100));
                    }
                }

                @Override
                public void onProgressDone(File downloadFile) {
                    //判断是否真的下载完成进行安装了，以及是否注册绑定过服务
                    if (isBindService) {
                        getApplicationContext().unbindService(conn);
                        isBindService = false;
                    }
                    if (progressDialog.isShowing()) {
                        progressDialog.setProgress(100);
                        progressDialog.setDownloadFile(downloadFile);
                    } else {
                        Uri fileUri = FileProvider.getUriForFile(mContext, "com.qyt.bm.fileprovider", downloadFile);
                        FileUtil.openFile(MainActivity.this, downloadFile, fileUri);
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void bindService(String apkUrl, String fileName) {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, apkUrl);
        intent.putExtra(DownloadService.BUNDLE_KEY_FILENAME, fileName);
        isBindService = getApplicationContext().bindService(intent, conn, Activity.BIND_AUTO_CREATE);
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(this).inflate(R.layout.item_main_tab, null);
        TextView tv = v.findViewById(R.id.main_tab_text);
        tv.setText(tabTitles[position]);
        ImageView img = v.findViewById(R.id.main_tab_image);
        img.setImageResource(images[position]);
        return v;
    }

    private void setMessageCount(int count) {
        View v = mainTabLayout.getTabAt(0).getCustomView();
        TextView msg = v.findViewById(R.id.main_tab_msg);
        if (count > 0) {
            msg.setVisibility(View.VISIBLE);
            msg.setText(count > 99 ? "99+" : count + "");
        } else {
            msg.setVisibility(View.GONE);
        }
    }

    @Override
    protected void addViewListener() {
        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                replaceFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void replaceFragment(int n) {
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        hideFragment(fts);
        switch (n) {
            case 0:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    fts.add(R.id.main_content, messageFragment, tabTitles[n]);
                }
                fts.show(messageFragment).commit();
                break;
            case 1:
                if (workFragment == null) {
                    workFragment = new WorkFragment();
                    fts.add(R.id.main_content, workFragment, tabTitles[n]);
                }
                fts.show(workFragment).commit();
                break;
            case 2:
                if (customerFragment == null) {
                    customerFragment = new CustomerFragment();
                    fts.add(R.id.main_content, customerFragment, tabTitles[n]);
                }
                fts.show(customerFragment).commit();
                break;
            case 3:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    fts.add(R.id.main_content, mineFragment, tabTitles[n]);
                }
                fts.show(mineFragment).commit();
                break;
        }
    }

    private void hideFragment(FragmentTransaction fts) {
        if (messageFragment != null)
            fts.hide(messageFragment);
        if (workFragment != null)
            fts.hide(workFragment);
        if (customerFragment != null)
            fts.hide(customerFragment);
        if (mineFragment != null)
            fts.hide(mineFragment);
    }

    //获取权限相关操作
    private List<String> needPermission;
    private final int REQUEST_CODE_PERMISSION = 0;
    private String[] permissionArray = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    @Override
    protected void requestData() {
        super.requestData();
        askMultiplePermission();
    }

    public void askMultiplePermission() {
        needPermission = new ArrayList<>();
        for (String permissionName :
                permissionArray) {
            if (!checkIsAskPermission(this, permissionName)) {
                needPermission.add(permissionName);
            }
        }
        if (needPermission.size() > 0) {
            //开始申请权限
            ActivityCompat.requestPermissions(this, needPermission.toArray(new String[needPermission.size()]), REQUEST_CODE_PERMISSION);
        }
    }

    public boolean checkIsAskPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkIsAskPermissionState(Map<String, Integer> maps, String[] list) {
        for (int i = 0; i < list.length; i++) {
            if (maps.get(list[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                Map<String, Integer> permissionMap = new HashMap<>();
                for (String name : needPermission) {
                    permissionMap.put(name, PackageManager.PERMISSION_GRANTED);
                }
                for (int i = 0; i < permissions.length; i++) {
                    permissionMap.put(permissions[i], grantResults[i]);
                }
                if (checkIsAskPermissionState(permissionMap, permissions)) {
                    //获取数据
                } else {
                    //提示权限获取不完成，可能有的功能不能使用
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /*双击返回键退出*/
    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                showToast(R.string.press_again_to_exit);
                mExitTime = System.currentTimeMillis();
            } else {
                BmApplication.getInstance().exitApplication();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
