package com.qyt.bm.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-7-28 0028.
 */

public class FishPondInfo implements Parcelable {

    public String pondId;
    public String farmerId;
    public String name;
    public int area;
    public String fishVariety;
    public int fryNumber;
    public String putInDate;
    public String putInBatch;
    public String reckonSaleDate;
    public String realSaleDate;
    public String region;
    public String trafficCondition;
    public String pondAddress;
    public String linkMan;
    public String phoneNumber;
    public String receiveAddress;
    public String longitude;
    public String latitude;
    public String maintainKeeper;
    public String maintainKeeperID;

    public ArrayList<DeviceConfigInfo> childDeviceList;

    public FishPondInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pondId);
        dest.writeString(this.farmerId);
        dest.writeString(this.name);
        dest.writeInt(this.area);
        dest.writeString(this.fishVariety);
        dest.writeInt(this.fryNumber);
        dest.writeString(this.putInDate);
        dest.writeString(this.putInBatch);
        dest.writeString(this.reckonSaleDate);
        dest.writeString(this.realSaleDate);
        dest.writeString(this.region);
        dest.writeString(this.trafficCondition);
        dest.writeString(this.pondAddress);
        dest.writeString(this.linkMan);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.receiveAddress);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeString(this.maintainKeeper);
        dest.writeString(this.maintainKeeperID);
        dest.writeTypedList(this.childDeviceList);
    }

    protected FishPondInfo(Parcel in) {
        this.pondId = in.readString();
        this.farmerId = in.readString();
        this.name = in.readString();
        this.area = in.readInt();
        this.fishVariety = in.readString();
        this.fryNumber = in.readInt();
        this.putInDate = in.readString();
        this.putInBatch = in.readString();
        this.reckonSaleDate = in.readString();
        this.realSaleDate = in.readString();
        this.region = in.readString();
        this.trafficCondition = in.readString();
        this.pondAddress = in.readString();
        this.linkMan = in.readString();
        this.phoneNumber = in.readString();
        this.receiveAddress = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.maintainKeeper = in.readString();
        this.maintainKeeperID = in.readString();
        this.childDeviceList = in.createTypedArrayList(DeviceConfigInfo.CREATOR);
    }

    public static final Creator<FishPondInfo> CREATOR = new Creator<FishPondInfo>() {
        @Override
        public FishPondInfo createFromParcel(Parcel source) {
            return new FishPondInfo(source);
        }

        @Override
        public FishPondInfo[] newArray(int size) {
            return new FishPondInfo[size];
        }
    };
}
