package com.qyt.om.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-6 0006.
 */

public class InstallTaskItem implements Parcelable {

    public String planFinishTime;
    public String pondPhone;
    public String matnerMembName;
    public String txtFarmerManager;
    public String farmerPhone;
    public String pondAddr;
    public String farmerName;
    public String fishPondCount;
    public String tskType;
    public String tskID;
    public String calOT;//接单时间
    public String calDT;//派单时间
    public String calCT;//完成时间
    public ArrayList<InstallDeviceInfo> contractDeviceList = new ArrayList<>();

    public InstallTaskItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.planFinishTime);
        dest.writeString(this.pondPhone);
        dest.writeString(this.matnerMembName);
        dest.writeString(this.txtFarmerManager);
        dest.writeString(this.farmerPhone);
        dest.writeString(this.pondAddr);
        dest.writeString(this.farmerName);
        dest.writeString(this.fishPondCount);
        dest.writeString(this.tskType);
        dest.writeString(this.tskID);
        dest.writeString(this.calOT);
        dest.writeString(this.calDT);
        dest.writeString(this.calCT);
        dest.writeTypedList(this.contractDeviceList);
    }

    protected InstallTaskItem(Parcel in) {
        this.planFinishTime = in.readString();
        this.pondPhone = in.readString();
        this.matnerMembName = in.readString();
        this.txtFarmerManager = in.readString();
        this.farmerPhone = in.readString();
        this.pondAddr = in.readString();
        this.farmerName = in.readString();
        this.fishPondCount = in.readString();
        this.tskType = in.readString();
        this.tskID = in.readString();
        this.calOT = in.readString();
        this.calDT = in.readString();
        this.calCT = in.readString();
        this.contractDeviceList = in.createTypedArrayList(InstallDeviceInfo.CREATOR);
    }

    public static final Creator<InstallTaskItem> CREATOR = new Creator<InstallTaskItem>() {
        @Override
        public InstallTaskItem createFromParcel(Parcel source) {
            return new InstallTaskItem(source);
        }

        @Override
        public InstallTaskItem[] newArray(int size) {
            return new InstallTaskItem[size];
        }
    };
}
