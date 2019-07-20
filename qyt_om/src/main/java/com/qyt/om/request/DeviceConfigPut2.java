package com.qyt.om.request;

import com.google.gson.Gson;
import com.qyt.om.response.AeratorControlItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-14 0014.
 */

public class DeviceConfigPut2 {
    public String oxyLimitUp;// 上限
    public String oxyLimitDownOne;//下限1
    public String oxyLimitDownTwo;//下限2
    public String alertlineOne;//警報限1
    public String alertlineTwo;//警報限2
    public boolean automatic;// 手動或自動
    public ArrayList<AeratorControlItem> aeratorControlList = new ArrayList<>();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
