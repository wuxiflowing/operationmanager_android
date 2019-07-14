package com.qyt.bm.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author: sunzhibin
 * @date: 2019-07-13 21:22
 * @description:
 * @e-mail:
 */
public class FishPondInfo2 implements Parcelable {
    /**
     * pondId : PND11110000000000833
     * name : 辛弃疾3号鱼塘
     * area : 0
     * fishVariety :
     * putInDate : null
     * reckonSaleDate : null
     * region :
     * pondAddress : 广西壮族自治区河池市罗城仫佬族自治县怀群镇刚好黄金季节
     * childDeviceList : [{"identifier":"0401F5","id":5175,"name":"辛弃疾设备3","type":"KD326","workStatus":"0","deviceControlInfoBeanList":[{"controlId":0,"oxyLimitUp":0,"oxyLimitDown":0,"electricityUp":null,"electricityDown":null,"open":0,"auto":1},{"controlId":1,"oxyLimitUp":0,"oxyLimitDown":0,"electricityUp":null,"electricityDown":null,"open":0,"auto":1},{"controlId":2,"oxyLimitUp":0,"oxyLimitDown":0,"electricityUp":null,"electricityDown":null,"open":null,"auto":null},{"controlId":3,"oxyLimitUp":0,"oxyLimitDown":0,"electricityUp":null,"electricityDown":null,"open":null,"auto":null}],"connectionType":0,"alertline1":2,"alertline2":1,"oxy":8,"temp":23,"ph":-1}]
     */

    public String pondId;          //鱼塘ID
    public String name;            //鱼塘名称
    public int area;            //鱼汤面积
    public String fishVariety;     //鱼种
    public String putInDate;       //投放鱼苗时间
    public String reckonSaleDate;  //预计卖鱼时间
    public String region;          //区域
    public String pondAddress;     //鱼塘地址
    public List<ChildDeviceListBean> childDeviceList;// 设备信息


    protected FishPondInfo2(Parcel in) {
        pondId = in.readString();
        name = in.readString();
        area = in.readInt();
        fishVariety = in.readString();
        putInDate = in.readString();
        reckonSaleDate = in.readString();
        region = in.readString();
        pondAddress = in.readString();
    }

    public static final Creator<FishPondInfo2> CREATOR = new Creator<FishPondInfo2>() {
        @Override
        public FishPondInfo2 createFromParcel(Parcel in) {
            return new FishPondInfo2(in);
        }

        @Override
        public FishPondInfo2[] newArray(int size) {
            return new FishPondInfo2[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pondId);
        dest.writeString(name);
        dest.writeInt(area);
        dest.writeString(fishVariety);
        dest.writeString(putInDate);
        dest.writeString(reckonSaleDate);
        dest.writeString(region);
        dest.writeString(pondAddress);
    }
}
