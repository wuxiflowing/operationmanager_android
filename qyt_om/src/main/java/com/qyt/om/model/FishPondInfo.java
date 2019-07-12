package com.qyt.om.model;

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
    public ArrayList<ChildDeviceListBean> childDeviceList;

    public static class ChildDeviceListBean implements Parcelable {

        public int id;
        public String name;
        public String type;
        public String dissolvedOxygen;
        public String temperature;
        public String ph;
        public String alarmType;
        public boolean enabled;
        public boolean automatic;
        public int workStatus;
        public String oxyLimitUp;
        public String oxyLimitDownOne;
        public String alertlineTwo;
        public boolean scheduled;
        public ArrayList<AeratorControlsBean> aeratorControls;

        public static class AeratorControlsBean implements Parcelable {
            /**
             * port : 1
             */

            public int port;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.port);
            }

            public AeratorControlsBean() {
            }

            protected AeratorControlsBean(Parcel in) {
                this.port = in.readInt();
            }

            public static final Creator<AeratorControlsBean> CREATOR = new Creator<AeratorControlsBean>() {
                @Override
                public AeratorControlsBean createFromParcel(Parcel source) {
                    return new AeratorControlsBean(source);
                }

                @Override
                public AeratorControlsBean[] newArray(int size) {
                    return new AeratorControlsBean[size];
                }
            };
        }

        public ChildDeviceListBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.type);
            dest.writeString(this.dissolvedOxygen);
            dest.writeString(this.temperature);
            dest.writeString(this.ph);
            dest.writeString(this.alarmType);
            dest.writeByte(this.enabled ? (byte) 1 : (byte) 0);
            dest.writeByte(this.automatic ? (byte) 1 : (byte) 0);
            dest.writeInt(this.workStatus);
            dest.writeString(this.oxyLimitUp);
            dest.writeString(this.oxyLimitDownOne);
            dest.writeString(this.alertlineTwo);
            dest.writeByte(this.scheduled ? (byte) 1 : (byte) 0);
            dest.writeTypedList(this.aeratorControls);
        }

        protected ChildDeviceListBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.type = in.readString();
            this.dissolvedOxygen = in.readString();
            this.temperature = in.readString();
            this.ph = in.readString();
            this.alarmType = in.readString();
            this.enabled = in.readByte() != 0;
            this.automatic = in.readByte() != 0;
            this.workStatus = in.readInt();
            this.oxyLimitUp = in.readString();
            this.oxyLimitDownOne = in.readString();
            this.alertlineTwo = in.readString();
            this.scheduled = in.readByte() != 0;
            this.aeratorControls = in.createTypedArrayList(AeratorControlsBean.CREATOR);
        }

        public static final Creator<ChildDeviceListBean> CREATOR = new Creator<ChildDeviceListBean>() {
            @Override
            public ChildDeviceListBean createFromParcel(Parcel source) {
                return new ChildDeviceListBean(source);
            }

            @Override
            public ChildDeviceListBean[] newArray(int size) {
                return new ChildDeviceListBean[size];
            }
        };
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
        dest.writeList(this.childDeviceList);
    }

    public FishPondInfo() {
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
        this.childDeviceList = new ArrayList<ChildDeviceListBean>();
        in.readList(this.childDeviceList, ChildDeviceListBean.class.getClassLoader());
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
