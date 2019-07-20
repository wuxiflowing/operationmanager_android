package com.qyt.om.request;

import com.google.gson.Gson;
import com.qyt.om.response.AeratorControlItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-8-14 0014.
 */

public class DeviceConfigPut2 {
    //public String identifierID;
    public String alertline1;
    public String alertline2;
    public List<DeviceControl> deviceControlInfoBeanList;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }



}
