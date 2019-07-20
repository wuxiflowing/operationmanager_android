package com.qyt.om.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018-8-14 0014.
 */

public class BindDeviceInfo implements Parcelable {
    public String pondId;//鱼塘Id
    public String pondName;//鱼塘名称
    public String pondAddr;
    public String longitude;
    public String latitude;
    public String deviceId;//设备Id
    public String deviceIdentifier;//设备identifier
    public String deviceModel;//设备型号
    public String state;//设备状态      workStatus
    public String oxyValue;//溶氧值
    public String temperature;//温度
    public String ph;//PH
    public String control1;//控制器1    开 关
    public String control2;//控制器2
    public String automatic;//手动自动

    public BindDeviceInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pondId);
        dest.writeString(this.pondName);
        dest.writeString(this.pondAddr);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeString(this.deviceId);
        dest.writeString(this.deviceIdentifier);
        dest.writeString(this.deviceModel);
        dest.writeString(this.state);
        dest.writeString(this.oxyValue);
        dest.writeString(this.temperature);
        dest.writeString(this.ph);
        dest.writeString(this.control1);
        dest.writeString(this.control2);
        dest.writeString(this.automatic);
    }

    protected BindDeviceInfo(Parcel in) {
        this.pondId = in.readString();
        this.pondName = in.readString();
        this.pondAddr = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.deviceId = in.readString();
        this.deviceIdentifier = in.readString();
        this.deviceModel = in.readString();
        this.state = in.readString();
        this.oxyValue = in.readString();
        this.temperature = in.readString();
        this.ph = in.readString();
        this.control1 = in.readString();
        this.control2 = in.readString();
        this.automatic = in.readString();
    }

    public static final Creator<BindDeviceInfo> CREATOR = new Creator<BindDeviceInfo>() {
        @Override
        public BindDeviceInfo createFromParcel(Parcel source) {
            return new BindDeviceInfo(source);
        }

        @Override
        public BindDeviceInfo[] newArray(int size) {
            return new BindDeviceInfo[size];
        }
    };
}
