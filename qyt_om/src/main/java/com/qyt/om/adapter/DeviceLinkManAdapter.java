package com.qyt.om.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.bangqu.lib.utils.AppUtils;
import com.qyt.om.R;
import com.qyt.om.activity.DeviceConfigActivity;
import com.qyt.om.model.LinkManMap;
import com.qyt.om.response.LinkManBean;
import com.qyt.om.widget.ListLinkManPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: sunzhibin
 * @date: 2019-07-13 13:24
 * @description:
 * @e-mail:
 */
public class DeviceLinkManAdapter extends BaseSimpleAdapter<LinkManMap> {

    private ListLinkManPopupWindow mListLinkManPopupWindow;
    private Activity mContext;
    private List<LinkManBean> mLinkManBeans = new ArrayList<>();

    public DeviceLinkManAdapter(Activity mContext, List<LinkManMap> mData) {
        super(mContext, mData);
        this.mContext = mContext;
    }

    public void setLinkManBeans(List<LinkManBean> linkManBeans) {
        this.mLinkManBeans = linkManBeans;
    }

    @Override
    protected View getViewAtPosition(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_device_contacts, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final LinkManMap model = mData.get(position);
        viewHolder.label.setText(model.label);
        if (model.linkManBean != null) {
            viewHolder.value.setText(new StringBuilder(model.linkManBean.name).append("(").append(model.linkManBean.phoneNumber).append(")"));
        }
        //fixme
        //viewHolder.value.setText(model.linkManBean);
        viewHolder.value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置联系人
                if (mListLinkManPopupWindow == null) {
                    mListLinkManPopupWindow = new ListLinkManPopupWindow(mContext,
                            mLinkManBeans, new ListLinkManPopupWindow.OnItemClickListener() {
                        @Override
                        public void itemClick(LinkManBean linkManBean) {
                            //
                            model.linkManBean = linkManBean;
                            viewHolder.value.setText(new StringBuilder(linkManBean.name).append("(").append(linkManBean.phoneNumber).append(")"));
                            notifyDataSetChanged();
                        }
                    });
                }
                mListLinkManPopupWindow.showAsDropDown(viewHolder.value, 0, AppUtils.dp2px(mContext, 16), Gravity.END);
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.infomap_label)
        TextView label;
        @BindView(R.id.infomap_value)
        TextView value;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
