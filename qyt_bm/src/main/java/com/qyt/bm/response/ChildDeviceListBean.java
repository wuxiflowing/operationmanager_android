package com.qyt.bm.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ChildDeviceListBean implements Parcelable {
    /**
     * identifier : 0401F5
     * id : 5175
     * name : 辛弃疾设备3
     * type : KD326
     * workStatus : 0
     * deviceControlInfoBeanList : [{"controlId":0,"oxyLimitUp":0,"oxyLimitDown":0,"electricityUp":null,"electricityDown":null,"open":0,"auto":1},{"controlId":1,"oxyLimitUp":0,"oxyLimitDown":0,"electricityUp":null,"electricityDown":null,"open":0,"auto":1},{"controlId":2,"oxyLimitUp":0,"oxyLimitDown":0,"electricityUp":null,"electricityDown":null,"open":null,"auto":null},{"controlId":3,"oxyLimitUp":0,"oxyLimitDown":0,"electricityUp":null,"electricityDown":null,"open":null,"auto":null}]
     * connectionType : 0
     * alertline1 : 2
     * alertline2 : 1
     * oxy : 8
     * temp : 23
     * ph : -1
     */

    public String identifier;
    public String id;
    public String name;
    public String type;
    public int workStatus;
    public int connectionType;
    public String alertline1;
    public String alertline2;
    public String oxy;
    public String temp;
    public String ph;
    public List<DeviceControlInfoBean> deviceControlInfoBeanList;

    public transient String pondId;
    public transient String pondName;

    protected ChildDeviceListBean(Parcel in) {
        identifier = in.readString();
        id = in.readString();
        name = in.readString();
        type = in.readString();
        workStatus = in.readInt();
        connectionType = in.readInt();
        alertline1 = in.readString();
        alertline2 = in.readString();
        oxy = in.readString();
        temp = in.readString();
        ph = in.readString();
        deviceControlInfoBeanList = in.createTypedArrayList(DeviceControlInfoBean.CREATOR);
    }

    public static final Creator<ChildDeviceListBean> CREATOR = new Creator<ChildDeviceListBean>() {
        @Override
        public ChildDeviceListBean createFromParcel(Parcel in) {
            return new ChildDeviceListBean(in);
        }

        @Override
        public ChildDeviceListBean[] newArray(int size) {
            return new ChildDeviceListBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identifier);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeInt(workStatus);
        dest.writeInt(connectionType);
        dest.writeString(alertline1);
        dest.writeString(alertline2);
        dest.writeString(oxy);
        dest.writeString(temp);
        dest.writeString(ph);
        dest.writeTypedList(deviceControlInfoBeanList);
    }
}
