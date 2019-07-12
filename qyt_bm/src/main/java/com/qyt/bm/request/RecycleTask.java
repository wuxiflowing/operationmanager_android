package com.qyt.bm.request;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-9 0009.
 */

public class RecycleTask {

    public String loginid;
    public String farmerId;
    public RecycleSubmit appData = new RecycleSubmit();

    public class RecycleSubmit {

        public String txtFarmerID;
        public String txtHKID;
        public String txtResMulti;
        public String tarReson;
        public String bankName;
        public String bankPerson;
        public String bankAccount;
        public ArrayList<RecyclePond> fishPondList = new ArrayList<>();

    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
