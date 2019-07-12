package com.qyt.bm.model;

/**
 * Created by Administrator on 2018-6-26 0026.
 */

public class DeviceChoiceModel {
    public String deviceTypeId;
    public String deviceTypeName;
    public int count = 1;
    public boolean selected = false;

    public DeviceChoiceModel clone() {
        DeviceChoiceModel model = new DeviceChoiceModel();
        model.deviceTypeId = deviceTypeId;
        model.deviceTypeName = deviceTypeName;
        model.count = 1;
        model.selected = false;
        return model;
    }

}
