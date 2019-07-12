package com.qyt.om.utils;

import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;

import java.io.File;

/**
 * Created by Administrator on 2018-4-19 0019.
 */

public class MapNaviUtil {

    private static MapNaviUtil instance = null;
    private Context mContext;

    public MapNaviUtil(Context mContext) {
        this.mContext = mContext;
    }

    public static MapNaviUtil getInstance(Context context) {
        if (instance == null)
            instance = new MapNaviUtil(context);
        return instance;
    }

    public void openBaiduMap(LatLng origin, LatLng destination, String describle) {
        LogInfo.error(destination.latitude + "," + destination.longitude);
        double[] gd_lat_lon = bdToGaoDe(destination.longitude, destination.latitude);
        LogInfo.error(gd_lat_lon[0] + "," + gd_lat_lon[1]);
        NaviParaOption para = new NaviParaOption()
                .startPoint(origin)
                .startName("我的位置")
                .endPoint(destination)
                .endName(describle);
        if (isInstallPackage("com.baidu.BaiduMap")) {
            BaiduMapNavigation.openBaiduMapNavi(para, mContext.getApplicationContext());
            return;
        }
        if (isInstallPackage("com.autonavi.minimap")) {
            openGaoDeMap(destination, describle);
            return;
        }
        BaiduMapNavigation.openBaiduMapNavi(para, mContext.getApplicationContext());
    }

    public void openGaoDeMap(LatLng destination, String describle) {
        try {
            double[] gd_lat_lon = bdToGaoDe(destination.longitude, destination.latitude);
            StringBuilder loc = new StringBuilder();
            loc.append("androidamap://viewMap?sourceApplication=XX");
            loc.append("&poiname=");
            loc.append(describle);
            loc.append("&lat=");
            loc.append(gd_lat_lon[0]);
            loc.append("&lon=");
            loc.append(gd_lat_lon[1]);
            loc.append("&dev=0");
            Intent intent = Intent.getIntent(loc.toString());
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    private double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    private double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

}
