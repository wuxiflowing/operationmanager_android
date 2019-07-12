package com.qyt.om.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qyt.om.R;
import com.qyt.om.model.FishPondInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-7-23 0023.
 */

public class PondListAdapter extends BaseAdapter {

    private Context mContext;
    private List<FishPondInfo> mDatas;

    public PondListAdapter(Context mContext, List<FishPondInfo> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sentrylist, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.sentryName.setText(mDatas.get(position).name);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.sentry_name)
        TextView sentryName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
