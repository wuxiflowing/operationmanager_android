package com.qyt.bm.response;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceControlInfoBean implements Parcelable {
    /**
     * controlId : 0
     * oxyLimitUp : 18
     * oxyLimitDown : 14
     * electricityUp : null
     * electricityDown : null
     * open : 1
     * auto : 0
     */

    public int controlId;          //控制器编号
    public String oxyLimitUp;         //溶氧上限
    public String oxyLimitDown;       //溶氧上限
    public String electricityUp;   //电流上限
    public String electricityDown; //电流下限
    public int open;               //开关状态（1开、0关）
    public int auto;               //控制状态 （1 自动、0 手动）

    protected DeviceControlInfoBean(Parcel in) {
        controlId = in.readInt();
        oxyLimitUp = in.readString();
        oxyLimitDown = in.readString();
        electricityUp = in.readString();
        electricityDown = in.readString();
        open = in.readInt();
        auto = in.readInt();
    }

    public static final Creator<DeviceControlInfoBean> CREATOR = new Creator<DeviceControlInfoBean>() {
        @Override
        public DeviceControlInfoBean createFromParcel(Parcel in) {
            return new DeviceControlInfoBean(in);
        }

        @Override
        public DeviceControlInfoBean[] newArray(int size) {
            return new DeviceControlInfoBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(controlId);
        dest.writeString(oxyLimitUp);
        dest.writeString(oxyLimitDown);
        dest.writeString(electricityUp);
        dest.writeString(electricityDown);
        dest.writeInt(open);
        dest.writeInt(auto);
    }
}
