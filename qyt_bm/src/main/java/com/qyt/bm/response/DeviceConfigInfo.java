package com.qyt.bm.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-13 0013.
 */

public class DeviceConfigInfo implements Parcelable {

    public int id;
    public String identifier;
    public String name;
    public String type;
    public String dissolvedOxygen;
    public String temperature;
    public String ph;
    public boolean enabled;
    public boolean automatic;
    public String workStatus;
    public String oxyLimitUp;
    public String oxyLimitDownOne;
    public String oxyLimitDownTwo;
    public String alertlineOne;
    public String alertlineTwo;
    public boolean scheduled;
    public ArrayList<AeratorControlItem> aeratorControlList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.identifier);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.dissolvedOxygen);
        dest.writeString(this.temperature);
        dest.writeString(this.ph);
        dest.writeByte(this.enabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.automatic ? (byte) 1 : (byte) 0);
        dest.writeString(this.workStatus);
        dest.writeString(this.oxyLimitUp);
        dest.writeString(this.oxyLimitDownOne);
        dest.writeString(this.oxyLimitDownTwo);
        dest.writeString(this.alertlineOne);
        dest.writeString(this.alertlineTwo);
        dest.writeByte(this.scheduled ? (byte) 1 : (byte) 0);
        dest.writeList(this.aeratorControlList);
    }

    public DeviceConfigInfo() {
    }

    protected DeviceConfigInfo(Parcel in) {
        this.id = in.readInt();
        this.identifier = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.dissolvedOxygen = in.readString();
        this.temperature = in.readString();
        this.ph = in.readString();
        this.enabled = in.readByte() != 0;
        this.automatic = in.readByte() != 0;
        this.workStatus = in.readString();
        this.oxyLimitUp = in.readString();
        this.oxyLimitDownOne = in.readString();
        this.oxyLimitDownTwo = in.readString();
        this.alertlineOne = in.readString();
        this.alertlineTwo = in.readString();
        this.scheduled = in.readByte() != 0;
        this.aeratorControlList = new ArrayList<AeratorControlItem>();
        in.readList(this.aeratorControlList, AeratorControlItem.class.getClassLoader());
    }

    public static final Parcelable.Creator<DeviceConfigInfo> CREATOR = new Parcelable.Creator<DeviceConfigInfo>() {
        @Override
        public DeviceConfigInfo createFromParcel(Parcel source) {
            return new DeviceConfigInfo(source);
        }

        @Override
        public DeviceConfigInfo[] newArray(int size) {
            return new DeviceConfigInfo[size];
        }
    };
}
