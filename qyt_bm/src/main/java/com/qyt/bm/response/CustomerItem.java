package com.qyt.bm.response;

import java.util.List;

/**
 * Created by Administrator on 2018-7-28 0028.
 */

public class CustomerItem {

    /**
     * farmerName : bbb
     * farmerId : 123
     * farmerAdd :
     * farmerTel :
     * contractData : [{"contractName":"aaa","contractId":"123","contractAmount":"100","contractImage":""}]
     */

    public String farmerName;
    public String farmerId;
    public String farmerAdd;
    public String farmerRegion;
    public String farmerTel;
    public String farmerPic;
    public List<ContractDataBean> contractData;

    public static class ContractDataBean {
        /**
         * contractName : aaa
         * contractId : 123
         * contractAmount : 100
         * contractImage :
         */

        public String contractName;
        public String contractId;
        public String contractAmount;
        public String contractImage;
    }
}
