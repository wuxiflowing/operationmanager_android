package com.qyt.bm.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;
import com.qyt.bm.comm.Constants;

/**
 * Created by Administrator on 2017-12-15 0015.
 */

public class ContactItem implements Comparable<ContactItem>, Parcelable {
    public String name;
    public String mobile;
    public String pinyin = "#";
    public char index = '#';
    public String farmerId;
    public String farmerAdd;
    public String farmerPic;

    public void setPinyin(String name) {
        if (!TextUtils.isEmpty(name) && name.trim().length() > 0) {
            pinyin = Pinyin.toPinyin(name.trim(), "");
            pinyin = pinyin != null ? pinyin.toUpperCase() : "#";
            index = pinyin.charAt(0);
            if (!Constants.LETTER_LIST.contains(String.valueOf(index))) {
                index = '#';
            }
        }
    }

    @Override
    public int compareTo(@NonNull ContactItem o) {
        return this.pinyin.compareToIgnoreCase(o.pinyin);
    }

    public ContactItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.pinyin);
        dest.writeInt(this.index);
        dest.writeString(this.farmerId);
        dest.writeString(this.farmerAdd);
        dest.writeString(this.farmerPic);
    }

    protected ContactItem(Parcel in) {
        this.name = in.readString();
        this.mobile = in.readString();
        this.pinyin = in.readString();
        this.index = (char) in.readInt();
        this.farmerId = in.readString();
        this.farmerAdd = in.readString();
        this.farmerPic = in.readString();
    }

    public static final Creator<ContactItem> CREATOR = new Creator<ContactItem>() {
        @Override
        public ContactItem createFromParcel(Parcel source) {
            return new ContactItem(source);
        }

        @Override
        public ContactItem[] newArray(int size) {
            return new ContactItem[size];
        }
    };
}
