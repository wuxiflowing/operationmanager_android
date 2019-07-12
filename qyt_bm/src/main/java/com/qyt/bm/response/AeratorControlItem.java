package com.qyt.bm.response;

import android.os.Parcel;
import android.os.Parcelable;

public class AeratorControlItem implements Parcelable {
    public boolean open;
    public String ammeterType;
    public String name;
    public boolean hasAmmeter;
    public String ammeterId;
    public String power;
    public String voltageUp;
    public String voltageDown;
    public String electricCurrentUp;
    public String electricCurrentDown;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.open ? (byte) 1 : (byte) 0);
        dest.writeString(this.ammeterType);
        dest.writeString(this.name);
        dest.writeByte(this.hasAmmeter ? (byte) 1 : (byte) 0);
        dest.writeString(this.ammeterId);
        dest.writeString(this.power);
        dest.writeString(this.voltageUp);
        dest.writeString(this.voltageDown);
        dest.writeString(this.electricCurrentUp);
        dest.writeString(this.electricCurrentDown);
    }

    public AeratorControlItem() {
    }

    protected AeratorControlItem(Parcel in) {
        this.open = in.readByte() != 0;
        this.ammeterType = in.readString();
        this.name = in.readString();
        this.hasAmmeter = in.readByte() != 0;
        this.ammeterId = in.readString();
        this.power = in.readString();
        this.voltageUp = in.readString();
        this.voltageDown = in.readString();
        this.electricCurrentUp = in.readString();
        this.electricCurrentDown = in.readString();
    }

    public static final Parcelable.Creator<AeratorControlItem> CREATOR = new Parcelable.Creator<AeratorControlItem>() {
        @Override
        public AeratorControlItem createFromParcel(Parcel source) {
            return new AeratorControlItem(source);
        }

        @Override
        public AeratorControlItem[] newArray(int size) {
            return new AeratorControlItem[size];
        }
    };
}
