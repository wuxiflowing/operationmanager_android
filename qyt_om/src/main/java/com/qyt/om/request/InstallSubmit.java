package com.qyt.om.request;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-9 0009.
 */

public class InstallSubmit {

    public String loginid;
    public InstallFinish appData = new InstallFinish();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
