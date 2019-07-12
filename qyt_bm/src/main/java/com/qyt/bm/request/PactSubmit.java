package com.qyt.bm.request;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-11 0011.
 */

public class PactSubmit {
    public String loginid;
    public String farmerId;
    public String contractType = "押金合同";//合同类型：押金合同，服务费合同，预付款合同
    public String contractName;//合同名称
    public String contractAmount;//合同金额
    public String contractDate;//签约日期
    public String paymentMethod = "现金";//付款方式： 现金，银行汇款
    public String receivedAmount = "0";//实收金额，实收金额0，未付
    public ArrayList<String> url = new ArrayList<>();//合同图片
    public ArrayList<DeviceInfo> contractDeviceList = new ArrayList<>();

    public PactSubmit(String loginid, String farmerId) {
        this.loginid = loginid;
        this.farmerId = farmerId;
    }

    public static class DeviceInfo {
        public String contractDeviceType;
        public String contractDeviceTypeId;
        public int contractDeviceNum;

        public DeviceInfo(String contractDeviceTypeId, String contractDeviceType, int contractDeviceNum) {
            this.contractDeviceTypeId = contractDeviceTypeId;
            this.contractDeviceType = contractDeviceType;
            this.contractDeviceNum = contractDeviceNum;
        }
    }
}
