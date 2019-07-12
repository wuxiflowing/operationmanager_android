package com.qyt.om;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.bangqu.lib.EshowApplication;
import com.tencent.bugly.crashreport.CrashReport;

public class OmApplication extends EshowApplication {

    private static OmApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        CrashReport.initCrashReport(this, "e1c351fd2c", false);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    public static OmApplication getInstance() {
        return mApplication;
    }
}
