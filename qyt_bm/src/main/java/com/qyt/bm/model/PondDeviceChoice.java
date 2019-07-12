package com.qyt.bm.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PondDeviceChoice implements Parcelable {
    public int deviceId;
    public String identifier;
    public String kind;
    public boolean isChoice = false;

    public void changeState() {
        isChoice = !isChoice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.deviceId);
        dest.writeString(this.identifier);
        dest.writeString(this.kind);
        dest.writeByte(this.isChoice ? (byte) 1 : (byte) 0);
    }

    public PondDeviceChoice() {
    }

    protected PondDeviceChoice(Parcel in) {
        this.deviceId = in.readInt();
        this.identifier = in.readString();
        this.kind = in.readString();
        this.isChoice = in.readByte() != 0;
    }

    public static final Parcelable.Creator<PondDeviceChoice> CREATOR = new Parcelable.Creator<PondDeviceChoice>() {
        @Override
        public PondDeviceChoice createFromParcel(Parcel source) {
            return new PondDeviceChoice(source);
        }

        @Override
        public PondDeviceChoice[] newArray(int size) {
            return new PondDeviceChoice[size];
        }
    };
}
