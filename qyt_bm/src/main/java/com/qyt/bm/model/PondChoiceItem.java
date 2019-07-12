package com.qyt.bm.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PondChoiceItem implements Parcelable {
    public String pondId;
    public String pondName;
    public String pondAddress;
    public String maintainKeeper;
    public String maintainKeeperID;
    public ArrayList<PondDeviceChoice> deviceList = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pondId);
        dest.writeString(this.pondName);
        dest.writeString(this.pondAddress);
        dest.writeString(this.maintainKeeper);
        dest.writeString(this.maintainKeeperID);
        dest.writeList(this.deviceList);
    }

    public PondChoiceItem() {
    }

    protected PondChoiceItem(Parcel in) {
        this.pondId = in.readString();
        this.pondName = in.readString();
        this.pondAddress = in.readString();
        this.maintainKeeper = in.readString();
        this.maintainKeeperID = in.readString();
        this.deviceList = new ArrayList<PondDeviceChoice>();
        in.readList(this.deviceList, PondDeviceChoice.class.getClassLoader());
    }

    public static final Parcelable.Creator<PondChoiceItem> CREATOR = new Parcelable.Creator<PondChoiceItem>() {
        @Override
        public PondChoiceItem createFromParcel(Parcel source) {
            return new PondChoiceItem(source);
        }

        @Override
        public PondChoiceItem[] newArray(int size) {
            return new PondChoiceItem[size];
        }
    };
}
