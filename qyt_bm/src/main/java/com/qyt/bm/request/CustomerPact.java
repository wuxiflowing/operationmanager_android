package com.qyt.bm.request;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-9 0009.
 */

public class CustomerPact {

    public String loginid;
    public String farmerId;
    public AppData appData = new AppData();

    public class AppData {
        public ArrayList<PactSubmit> contractList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
