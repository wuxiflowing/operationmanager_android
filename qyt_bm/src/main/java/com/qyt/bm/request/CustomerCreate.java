package com.qyt.bm.request;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-9 0009.
 */

public class CustomerCreate {

    public String loginid;
    public CustomerSubmit appData = new CustomerSubmit();

    public class CustomerSubmit {
        public String loginid;
        public String farmerId;
        public String farmerName;//姓名
        public String farmerType;//签约用户，意向用户
        public String contactInfo;//联系方式
        public String birthday;//生日
        public String sex;//性别
        public String region;//行政区域
        public String homeAddress;//家庭住址
        public String idNumber;//身份证号
        public String areaId;//行政区域区ID
        public ArrayList<String> url = new ArrayList<>();//图片附件
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
