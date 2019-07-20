package com.qyt.om.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.qyt.om.R;
import com.qyt.om.response.LinkManBean;

/**
 * @author: sunzhibin
 * @date: 2019-07-14 17:41
 * @description:
 * @e-mail:
 */
public class LinkManMap implements Parcelable {
    public String label;
    public LinkManBean linkManBean;
    public int id;// 0, 白班鱼塘联系人 getString(R.string.text_day_contacts)
    // 1, 晚班鱼塘联系人getString(R.string.text_night_contacts)
    // 2, 白班鱼塘备用联系人getString(R.string.text_day_contacts_2)
    // 3, 晚班鱼塘备用联系人getString(R.string.text_night2_contacts_2)

    public LinkManMap(int id, String label, LinkManBean linkManBean) {
        this.label = label;
        this.id = id;
        this.linkManBean = linkManBean;
    }

    protected LinkManMap(Parcel in) {
        label = in.readString();
        id = in.readInt();
    }

    public static final Creator<LinkManMap> CREATOR = new Creator<LinkManMap>() {
        @Override
        public LinkManMap createFromParcel(Parcel in) {
            return new LinkManMap(in);
        }

        @Override
        public LinkManMap[] newArray(int size) {
            return new LinkManMap[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeInt(id);
    }
}
