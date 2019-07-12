package com.qyt.bm.request;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2018-7-28 0028.
 */

public class LoginModel {
    public String loginid;
    public String password;
    public String device;
    public String queryType = "AquaKeeper";//"": "Operation"  or ""

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
