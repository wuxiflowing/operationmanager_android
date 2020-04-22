package com.qyt.om.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bangqu.lib.base.BaseSimpleAdapter;
import com.bangqu.lib.utils.AppUtils;
import com.qyt.om.R;
import com.qyt.om.model.LinkManMap;
import com.qyt.om.response.LinkManBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: sunzhibin
 * @date: 2019-07-14 16:58
 * @description:
 * @e-mail:
 */

public class ListLinkManPopupWindow extends PopupWindow {

    private Activity mContext;
    private List<LinkManBean> mListData;
    private ListAdapter mListAdapter;
    private View mRootView;
    private ListView listView;
    private OnItemClickListener onItemClicklistener;

    public ListLinkManPopupWindow(Activity context, List<LinkManBean> mListData, OnItemClickListener onItemClicklistener) {
        super(context);
        this.mContext = context;
        this.mListData = mListData;
        this.onItemClicklistener = onItemClicklistener;
        initView();
        initData();
        initListener();
    }

    @SuppressLint("NewApi")
    private void initView() {
        mRootView = View.inflate(mContext, R.layout.popup_contact_list, null);
        setContentView(mRootView);

        setWidth(AppUtils.dp2px(mContext, 300));
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);

        setBackgroundDrawable(new BitmapDrawable());
        //setAnimationStyle(R.style.PopupAnimation);

        listView = mRootView.findViewById(R.id.listView);
        if (mListData == null) {
            mListData = new ArrayList<>();
        }
        mListAdapter = new ListAdapter(mContext, mListData);
        listView.setAdapter(mListAdapter);
    }

    private void initData() {

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 0.5f;
        mContext.getWindow().setAttributes(lp);
        mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//多加这一句，问题就解决了！这句的官方文档解释是：让窗口背景后面的任何东西变暗
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);

        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 0.5f;
        mContext.getWindow().setAttributes(lp);
        mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//多加这一句，问题就解决了！这句的官方文档解释是：让窗口背景后面的任何东西变暗

    }

    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 1f;
        mContext.getWindow().setAttributes(lp);
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClicklistener != null) {
                    onItemClicklistener.itemClick(mListData.get(position));
                }
                dismiss();
            }
        });

    }

    public void setmListData(List<LinkManBean> mListData) {
        this.mListData = mListData;
        mListAdapter.setListData(mListData);
    }

    public static class ListAdapter extends BaseSimpleAdapter<LinkManBean> {

        public ListAdapter(Context mContext, List<LinkManBean> mData) {
            super(mContext, mData);
        }

        public void setListData(List<LinkManBean> mData) {
            this.mData = mData;
            notifyDataSetChanged();
        }

        @Override
        protected View getViewAtPosition(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_popup_contact_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            LinkManBean linkManBean = getItem(position);
            //fixme
            if (linkManBean != null) {
                viewHolder.tvName.setText(new StringBuilder(linkManBean.name).append("(").append(linkManBean.phoneNumber).append(")"));
            }
            return convertView;
        }

        static class ViewHolder {
            @BindView(R.id.tv_name)
            TextView tvName;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    public interface OnItemClickListener {
        void itemClick(LinkManBean linkManBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClicklistener) {
        this.onItemClicklistener = onItemClicklistener;

    }


}
