package com.qyt.bm.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018-7-5 0005.
 */

public class LocPoi implements Parcelable {
    public String address;
    public double lat;
    public double lng;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
    }

    public LocPoi() {
    }

    protected LocPoi(Parcel in) {
        this.address = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
    }

    public static final Parcelable.Creator<LocPoi> CREATOR = new Parcelable.Creator<LocPoi>() {
        @Override
        public LocPoi createFromParcel(Parcel source) {
            return new LocPoi(source);
        }

        @Override
        public LocPoi[] newArray(int size) {
            return new LocPoi[size];
        }
    };
}
