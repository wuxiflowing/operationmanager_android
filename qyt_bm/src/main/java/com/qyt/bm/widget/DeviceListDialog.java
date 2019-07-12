package com.qyt.bm.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bangqu.lib.listener.DialogConfirmListener;
import com.qyt.bm.R;
import com.qyt.bm.adapter.RepairDevListAdapter;
import com.qyt.bm.response.DeviceConfigInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 * 密码操作弹窗
 */
public class DeviceListDialog extends Dialog {

    private ListView sentryList;
    private DialogConfirmListener onConfrimClickedListener;
    private RepairDevListAdapter sentryListAdapter;
    private Context mContext;
    private List<DeviceConfigInfo> mDatas;

    public DeviceListDialog(Context context, final List<DeviceConfigInfo> mDatas, DialogConfirmListener listener) {
        super(context, R.style.menu_dialog_style);
        this.mContext = context;
        this.mDatas = mDatas;
        onConfrimClickedListener = listener;
        setContentView(R.layout.dialog_sentrylist);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        sentryList = findViewById(R.id.sentry_list);
        sentryListAdapter = new RepairDevListAdapter(mContext, mDatas);
        sentryList.setAdapter(sentryListAdapter);
        sentryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onConfrimClickedListener != null) {
                    onConfrimClickedListener.onDialogConfirm(true, mDatas.get(position));
                }
                dismiss();
            }
        });
    }

}
