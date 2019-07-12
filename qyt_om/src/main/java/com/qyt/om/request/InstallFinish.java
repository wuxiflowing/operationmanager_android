package com.qyt.om.request;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-14 0014.
 */

public class InstallFinish {

    public String loginid;
    public String farmerId;
    public String realServiceAmount = "0";
    public String realDepositAmount = "0";
    public String payServiceMethod;
    public String payDepositMethod;
    public String serviceNote;
    public String depositNote;
    public ArrayList<String> confirmUrls = new ArrayList<>();
    public ArrayList<String> servicePayUrls = new ArrayList<>();
    public ArrayList<String> depositPayUrls = new ArrayList<>();
    public ArrayList<DeviceBind> bindDeviceList = new ArrayList<>();

    public class DeviceBind {
        public String deviceId;
        public String pondId;
        public String pondName;
        public String pondAddr;
        public String longitude;
        public String latitude;
    }
}
