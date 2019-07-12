package com.qyt.bm.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018-7-28 0028.
 */

public class AreaItem implements Parcelable {
    public String id;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    public AreaItem() {
    }

    protected AreaItem(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<AreaItem> CREATOR = new Parcelable.Creator<AreaItem>() {
        @Override
        public AreaItem createFromParcel(Parcel source) {
            return new AreaItem(source);
        }

        @Override
        public AreaItem[] newArray(int size) {
            return new AreaItem[size];
        }
    };
}
