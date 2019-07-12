package com.qyt.om.request;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2018-8-9 0009.
 */

public class RecycleSubmit {

    public String loginid;
    public RecycleFinish appData = new RecycleFinish();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
