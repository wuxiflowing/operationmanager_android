package com.qyt.bm;

import android.app.Activity;
import android.os.Environment;
import android.os.StrictMode;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.bangqu.lib.EshowApplication;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Stack;

public class BmApplication extends EshowApplication {

    private static BmApplication mApplication;
    private Stack<Activity> streamStack = new Stack<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        String tmp = Environment.getExternalStorageDirectory().getAbsolutePath();
        CrashReport.initCrashReport(this, "3314bfd5d5", false);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
    }

    public void addToSteamActivity(Activity context) {
        streamStack.add(context);
    }

    public void clearSteamActivity() {
        for (Activity context : streamStack) {
            context.finish();
        }
        streamStack.clear();
    }

    public static BmApplication getInstance() {
        return mApplication;
    }
}
