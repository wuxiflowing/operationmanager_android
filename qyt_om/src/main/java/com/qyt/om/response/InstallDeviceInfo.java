package com.qyt.om.response;

import android.os.Parcel;
import android.os.Parcelable;

public class InstallDeviceInfo implements Parcelable {
    public String contractDeviceType;
    public String contractDeviceTypeId;
    public int contractDeviceNum;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contractDeviceType);
        dest.writeString(this.contractDeviceTypeId);
        dest.writeInt(this.contractDeviceNum);
    }

    public InstallDeviceInfo() {
    }

    protected InstallDeviceInfo(Parcel in) {
        this.contractDeviceType = in.readString();
        this.contractDeviceTypeId = in.readString();
        this.contractDeviceNum = in.readInt();
    }

    public static final Parcelable.Creator<InstallDeviceInfo> CREATOR = new Parcelable.Creator<InstallDeviceInfo>() {
        @Override
        public InstallDeviceInfo createFromParcel(Parcel source) {
            return new InstallDeviceInfo(source);
        }

        @Override
        public InstallDeviceInfo[] newArray(int size) {
            return new InstallDeviceInfo[size];
        }
    };
}
