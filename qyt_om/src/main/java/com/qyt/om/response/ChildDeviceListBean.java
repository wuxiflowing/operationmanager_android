package com.qyt.om.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Objects;

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
    public String workStatus;
    public int connectionType;
    public String alertline1;
    public String alertline2;
    public String oxy;
    public String temp;
    public String ph;
    public List<DeviceControlInfoBean> deviceControlInfoBeanList;




    //额外添加字段，与接口返回数据无关
    public String pondId;
    public String pondName;
    public String pondAddr;
    public String latitude;
    public String longitude;

    public String contacters;//               白班联系人姓名
    public String contactPhone;//             白班联系人电话
    public String standbyContactPhone;//      白班备用联系人姓名
    public String standbyContact;//           白班备用联系人电话

    public String nightContactPhone;//        晚班联系人姓名
    public String nightContacters;//          晚班联系人电话
    public String standbyNightContact;//      晚班备用联系人姓名
    public String standbyNightContactPhone;// 晚班备用联系人电话

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChildDeviceListBean that = (ChildDeviceListBean) o;
        return identifier.equals(that.identifier) &&
                id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, id);
    }

    public ChildDeviceListBean() {
    }

    protected ChildDeviceListBean(Parcel in) {
        identifier = in.readString();
        id = in.readString();
        name = in.readString();
        type = in.readString();
        workStatus = in.readString();
        connectionType = in.readInt();
        alertline1 = in.readString();
        alertline2 = in.readString();
        oxy = in.readString();
        temp = in.readString();
        ph = in.readString();
        deviceControlInfoBeanList = in.createTypedArrayList(DeviceControlInfoBean.CREATOR);
        pondId = in.readString();
        pondName = in.readString();
        pondAddr = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        contacters = in.readString();
        contactPhone = in.readString();
        standbyContactPhone = in.readString();
        standbyContact = in.readString();
        nightContactPhone = in.readString();
        nightContacters = in.readString();
        standbyNightContact = in.readString();
        standbyNightContactPhone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identifier);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(workStatus);
        dest.writeInt(connectionType);
        dest.writeString(alertline1);
        dest.writeString(alertline2);
        dest.writeString(oxy);
        dest.writeString(temp);
        dest.writeString(ph);
        dest.writeTypedList(deviceControlInfoBeanList);
        dest.writeString(pondId);
        dest.writeString(pondName);
        dest.writeString(pondAddr);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(contacters);
        dest.writeString(contactPhone);
        dest.writeString(standbyContactPhone);
        dest.writeString(standbyContact);
        dest.writeString(nightContactPhone);
        dest.writeString(nightContacters);
        dest.writeString(standbyNightContact);
        dest.writeString(standbyNightContactPhone);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
