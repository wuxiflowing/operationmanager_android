package com.qyt.om.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018-8-6 0006.
 */

public class RepairTaskItem implements Parcelable {

    public String repairEqpKind;
    public String pondPhone;
    public String matnerMembName;
    public String pondsName;
    public String farmerPhone;
    public String pondAddr;
    public String formNo;
    public String farmerName;
    public String tskType;
    public String resOth;
    public String maintenDetail;
    public String tskID;
    public String calOT;//接单时间
    public String calDT;//派单时间
    public String calCT;//完成时间
    public String txtHKName;
    public String txtMonMembName;

    public RepairTaskItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.repairEqpKind);
        dest.writeString(this.pondPhone);
        dest.writeString(this.matnerMembName);
        dest.writeString(this.pondsName);
        dest.writeString(this.farmerPhone);
        dest.writeString(this.pondAddr);
        dest.writeString(this.formNo);
        dest.writeString(this.farmerName);
        dest.writeString(this.tskType);
        dest.writeString(this.resOth);
        dest.writeString(this.maintenDetail);
        dest.writeString(this.tskID);
        dest.writeString(this.calOT);
        dest.writeString(this.calDT);
        dest.writeString(this.calCT);
        dest.writeString(this.txtHKName);
        dest.writeString(this.txtMonMembName);
    }

    protected RepairTaskItem(Parcel in) {
        this.repairEqpKind = in.readString();
        this.pondPhone = in.readString();
        this.matnerMembName = in.readString();
        this.pondsName = in.readString();
        this.farmerPhone = in.readString();
        this.pondAddr = in.readString();
        this.formNo = in.readString();
        this.farmerName = in.readString();
        this.tskType = in.readString();
        this.resOth = in.readString();
        this.maintenDetail = in.readString();
        this.tskID = in.readString();
        this.calOT = in.readString();
        this.calDT = in.readString();
        this.calCT = in.readString();
        this.txtHKName = in.readString();
        this.txtMonMembName = in.readString();
    }

    public static final Creator<RepairTaskItem> CREATOR = new Creator<RepairTaskItem>() {
        @Override
        public RepairTaskItem createFromParcel(Parcel source) {
            return new RepairTaskItem(source);
        }

        @Override
        public RepairTaskItem[] newArray(int size) {
            return new RepairTaskItem[size];
        }
    };
}
