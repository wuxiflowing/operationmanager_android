package com.qyt.om.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.qyt.om.R;
import com.qyt.om.model.WorkMenuModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15052286501 on 2017/8/29.
 */
public class WorkMenuAdapter extends BaseSimpleAdapter<WorkMenuModel> {

    public WorkMenuAdapter(Context mContext, List<WorkMenuModel> mData) {
        super(mContext, mData);
    }

    @Override
    protected View getViewAtPosition(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_work_menu, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        WorkMenuModel model = mData.get(position);
        viewHolder.mainTabText.setText(model.name);
        viewHolder.mainTabImage.setImageResource(model.resource);
        if (TextUtils.isEmpty(model.count) || "0".equals(model.count)) {
            viewHolder.mainTabMsg.setVisibility(View.GONE);
        } else {
            viewHolder.mainTabMsg.setText(model.count + "");
            viewHolder.mainTabMsg.setVisibility(View.VISIBLE);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.main_tab_image)
        ImageView mainTabImage;
        @BindView(R.id.main_tab_text)
        TextView mainTabText;
        @BindView(R.id.main_tab_msg)
        TextView mainTabMsg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
