package com.qyt.bm.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018-7-28 0028.
 */

public class CustomerData implements Parcelable {

    public int sysID;
    public String farmerId;
    public String name;
    public String picture;
    public String account;
    public String advanceCapital;
    public String arrears;
    public String arrearsQuota;
    public String farmerType;
    public String equipment;
    public String contactInfo;
    public String birthday;
    public String totalArea;
    public String pondNumber;
    public String sex;
    public String aquaExp;
    public String idNumber;
    public String credit;
    public String subordinate;
    public String customerLevel;
    public String region;
    public String integral;
    public String homeAddress;
    public String enrollDate;
    public String regionManager;
    public String openAccountDate;
    public String aquaKeeper;
    public String aquaKeeperID;
    public String beforeFeedBrand;
    public String beforeFeedNum;
    public String feedRelation;
    public String buyFishRelation;
    public String buyOxyEquip;
    public String joinCooperative;
    public String needSensor;
    public String needBankLoans;
    public String fishVariety;
    public String frySource;
    public String nowFeedBrand;
    public String trustFeedBrand;
    public String registrar;
    public String registDate;
    public String renewMan;
    public String renewDate;
    public String recommendUserID;
    public String registerPicture;
    public String idPicture;
    public String pondPicture;

    public CustomerData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sysID);
        dest.writeString(this.farmerId);
        dest.writeString(this.name);
        dest.writeString(this.picture);
        dest.writeString(this.account);
        dest.writeString(this.advanceCapital);
        dest.writeString(this.arrears);
        dest.writeString(this.arrearsQuota);
        dest.writeString(this.farmerType);
        dest.writeString(this.equipment);
        dest.writeString(this.contactInfo);
        dest.writeString(this.birthday);
        dest.writeString(this.totalArea);
        dest.writeString(this.pondNumber);
        dest.writeString(this.sex);
        dest.writeString(this.aquaExp);
        dest.writeString(this.idNumber);
        dest.writeString(this.credit);
        dest.writeString(this.subordinate);
        dest.writeString(this.customerLevel);
        dest.writeString(this.region);
        dest.writeString(this.integral);
        dest.writeString(this.homeAddress);
        dest.writeString(this.enrollDate);
        dest.writeString(this.regionManager);
        dest.writeString(this.openAccountDate);
        dest.writeString(this.aquaKeeper);
        dest.writeString(this.aquaKeeperID);
        dest.writeString(this.beforeFeedBrand);
        dest.writeString(this.beforeFeedNum);
        dest.writeString(this.feedRelation);
        dest.writeString(this.buyFishRelation);
        dest.writeString(this.buyOxyEquip);
        dest.writeString(this.joinCooperative);
        dest.writeString(this.needSensor);
        dest.writeString(this.needBankLoans);
        dest.writeString(this.fishVariety);
        dest.writeString(this.frySource);
        dest.writeString(this.nowFeedBrand);
        dest.writeString(this.trustFeedBrand);
        dest.writeString(this.registrar);
        dest.writeString(this.registDate);
        dest.writeString(this.renewMan);
        dest.writeString(this.renewDate);
        dest.writeString(this.recommendUserID);
        dest.writeString(this.registerPicture);
        dest.writeString(this.idPicture);
        dest.writeString(this.pondPicture);
    }

    protected CustomerData(Parcel in) {
        this.sysID = in.readInt();
        this.farmerId = in.readString();
        this.name = in.readString();
        this.picture = in.readString();
        this.account = in.readString();
        this.advanceCapital = in.readString();
        this.arrears = in.readString();
        this.arrearsQuota = in.readString();
        this.farmerType = in.readString();
        this.equipment = in.readString();
        this.contactInfo = in.readString();
        this.birthday = in.readString();
        this.totalArea = in.readString();
        this.pondNumber = in.readString();
        this.sex = in.readString();
        this.aquaExp = in.readString();
        this.idNumber = in.readString();
        this.credit = in.readString();
        this.subordinate = in.readString();
        this.customerLevel = in.readString();
        this.region = in.readString();
        this.integral = in.readString();
        this.homeAddress = in.readString();
        this.enrollDate = in.readString();
        this.regionManager = in.readString();
        this.openAccountDate = in.readString();
        this.aquaKeeper = in.readString();
        this.aquaKeeperID = in.readString();
        this.beforeFeedBrand = in.readString();
        this.beforeFeedNum = in.readString();
        this.feedRelation = in.readString();
        this.buyFishRelation = in.readString();
        this.buyOxyEquip = in.readString();
        this.joinCooperative = in.readString();
        this.needSensor = in.readString();
        this.needBankLoans = in.readString();
        this.fishVariety = in.readString();
        this.frySource = in.readString();
        this.nowFeedBrand = in.readString();
        this.trustFeedBrand = in.readString();
        this.registrar = in.readString();
        this.registDate = in.readString();
        this.renewMan = in.readString();
        this.renewDate = in.readString();
        this.recommendUserID = in.readString();
        this.registerPicture = in.readString();
        this.idPicture = in.readString();
        this.pondPicture = in.readString();
    }

    public static final Creator<CustomerData> CREATOR = new Creator<CustomerData>() {
        @Override
        public CustomerData createFromParcel(Parcel source) {
            return new CustomerData(source);
        }

        @Override
        public CustomerData[] newArray(int size) {
            return new CustomerData[size];
        }
    };
}
