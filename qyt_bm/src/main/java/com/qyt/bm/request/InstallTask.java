package com.qyt.bm.request;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-9 0009.
 */

public class InstallTask {

    public String loginid;
    public String farmerId;
    public CustomerSubmit appData = new CustomerSubmit();

    public class CustomerSubmit {
        public String loginid;
        public String farmerId;
        public String contractId;//服务合同ID
        public ArrayList<DeviceInfo> deviceList = new ArrayList<>();//设备安装清单
        public String installAddress;//安装地址
        public String latitude;
        public String longitude;
        public String memId;//运维人员ID
        public String reckonDate;//预计安装时间
        public String depositAmount = "0";//押金费用
        public String serviceAmount = "0";//服务费用
    }

    public class DeviceInfo {
        public String deviceTypeId;
        public String deviceTypeName;
        public String deviceCount;

        public DeviceInfo() {
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
