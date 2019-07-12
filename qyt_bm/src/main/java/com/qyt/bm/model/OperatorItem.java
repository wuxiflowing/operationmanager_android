package com.qyt.bm.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018-6-26 0026.
 */

public class OperatorItem implements Parcelable {


    /**
     * memName : 十一郎
     * memId : MEM20151531983340711
     * taskCount : 0
     * totalTaskCount : 0
     */

    public String memName;
    public String memId;
    public String taskCount;
    public String totalTaskCount;
    public String picture;
    public String region;

    public OperatorItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.memName);
        dest.writeString(this.memId);
        dest.writeString(this.taskCount);
        dest.writeString(this.totalTaskCount);
        dest.writeString(this.picture);
        dest.writeString(this.region);
    }

    protected OperatorItem(Parcel in) {
        this.memName = in.readString();
        this.memId = in.readString();
        this.taskCount = in.readString();
        this.totalTaskCount = in.readString();
        this.picture = in.readString();
        this.region = in.readString();
    }

    public static final Creator<OperatorItem> CREATOR = new Creator<OperatorItem>() {
        @Override
        public OperatorItem createFromParcel(Parcel source) {
            return new OperatorItem(source);
        }

        @Override
        public OperatorItem[] newArray(int size) {
            return new OperatorItem[size];
        }
    };
}
