package com.qyt.om.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author: sunzhibin
 * @date: 2019-07-14 17:37
 * @description:
 * @e-mail:
 */
public class LinkManBean implements Parcelable {
    /**
     * linkManID : null
     * name : 联系人姓名
     * sex : 性别
     * birthday : 出生年月
     * iDNumber : 身份证号码
     * homeAddress : 地址
     * phoneNumber : 电话号码
     * email : null
     * farmerId : 养殖户ID
     */

    public String linkManID;
    public String name;
    public String sex;
    public String birthday;
    public String iDNumber;
    public String homeAddress;
    public String phoneNumber;
    public String email;
    public String farmerId;

    public LinkManBean() {
    }

    protected LinkManBean(Parcel in) {
        linkManID = in.readString();
        name = in.readString();
        sex = in.readString();
        birthday = in.readString();
        iDNumber = in.readString();
        homeAddress = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        farmerId = in.readString();
    }

    public static final Creator<LinkManBean> CREATOR = new Creator<LinkManBean>() {
        @Override
        public LinkManBean createFromParcel(Parcel in) {
            return new LinkManBean(in);
        }

        @Override
        public LinkManBean[] newArray(int size) {
            return new LinkManBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(linkManID);
        dest.writeString(name);
        dest.writeString(sex);
        dest.writeString(birthday);
        dest.writeString(iDNumber);
        dest.writeString(homeAddress);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeString(farmerId);
    }
}
