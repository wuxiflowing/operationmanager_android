package com.qyt.om.request;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2018-8-9 0009.
 */

public class MaintainSubmit {

    public String loginid;
    public MaintainFinish appData = new MaintainFinish();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
