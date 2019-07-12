package com.qyt.bm.request;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-9 0009.
 */

public class RepairTask {

    public String loginid;
    public String farmerId;
    public RepairSubmit appData = new RepairSubmit();

    public class RepairSubmit {
        public String loginid;
        public String farmerId;
        public String pondId;//鱼塘ID
        public String deviceId;//设备ID
        public String memId;//运维人员ID
        public String reason;//故障描述
        public ArrayList<String> url = new ArrayList<>();//报修照片
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
