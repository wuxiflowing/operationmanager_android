package com.qyt.bm.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author: sunzhibin
 * @date: 2019-07-13 17:51
 * @description:
 * @e-mail:
 */
public class DeviceConfigInfo2 implements Parcelable {

    /**
     * identifier : 090000
     * id : 990000
     * name : MQTT設備090000
     * type : QY601
     * workStatus : 5
     * deviceControlInfoBeanList : [{"controlId":0,"oxyLimitUp":18,"oxyLimitDown":14,"electricityUp":null,"electricityDown":null,"open":1,"auto":0},{"controlId":1,"oxyLimitUp":17,"oxyLimitDown":13,"electricityUp":"14","electricityDown":"13","open":2,"auto":0},{"controlId":2,"oxyLimitUp":16,"oxyLimitDown":12,"electricityUp":"44","electricityDown":"32","open":1,"auto":0},{"controlId":3,"oxyLimitUp":15,"oxyLimitDown":11,"electricityUp":"15","electricityDown":"5","open":1,"auto":0}]
     * connectionType : 0
     * alertline1 : 6
     * alertline2 : 5
     * oxy : 6.3
     * temp : 34.9
     * ph : 0
     */
    public String identifier;  //设备6码Id
    public String id;             //设备Id
    public String name;        //设备名称
    public String type;        //设备型号
    public String workStatus;  //正常(0)、告警限1(1)、告警限2(2)、不在线告警(3)、设备告警(5)、数据解析异常(-1)
    public int connectionType; //连线方式 0:无线  1: 有线
    public String alertline1;     //告警线1
    public String alertline2;     //告警线1
    public String oxy;         //溶氧
    public String temp;        //温度
    public String ph;             //ph
    public List<DeviceControlInfoBean> deviceControlInfoBeanList;

    protected DeviceConfigInfo2(Parcel in) {
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
    }

    public static final Creator<DeviceConfigInfo2> CREATOR = new Creator<DeviceConfigInfo2>() {
        @Override
        public DeviceConfigInfo2 createFromParcel(Parcel in) {
            return new DeviceConfigInfo2(in);
        }

        @Override
        public DeviceConfigInfo2[] newArray(int size) {
            return new DeviceConfigInfo2[size];
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
        dest.writeString(workStatus);
        dest.writeInt(connectionType);
        dest.writeString(alertline1);
        dest.writeString(alertline2);
        dest.writeString(oxy);
        dest.writeString(temp);
        dest.writeString(ph);
    }
}
