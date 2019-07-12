package com.qyt.bm.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-8-10 0010.
 */

public class PactItem {

    /**
     * farmerId : 13819211063
     * contractData : [{"contractImage":"aaa.jpg,bbb.jpg,ccc.jpg","contractId":4,"contractAmount":0,"contractName":"name"}]
     */

    public String farmerId;
    public List<ContractDataBean> contractData;

    public static class ContractDataBean {

        public String contractImage;
        public String contractType;
        public String collectState;
        public String contractId;
        public int contractAmount;
        public String contractName;
        public ArrayList<PactDeviceInfo> contractDeviceList;

    }
}
