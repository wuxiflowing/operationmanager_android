package com.qyt.bm.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bangqu.lib.listener.DialogConfirmListener;
import com.bangqu.lib.utils.AppUtils;
import com.qyt.bm.R;

/**
 * Created by Administrator on 2016/7/4.
 * 密码操作弹窗
 */
public class LineTypeDialog extends Dialog {

    private ListView sentryList;
    private DialogConfirmListener onConfrimClickedListener;
    private Context mContext;
    private String[] datas = new String[]{"溶氧曲线", "温度曲线", "PH曲线"};

    public LineTypeDialog(Context context, DialogConfirmListener listener) {
        super(context, R.style.menu_dialog_style);
        this.mContext = context;
        onConfrimClickedListener = listener;
        setContentView(R.layout.dialog_linetype);
        Window window = getWindow();
        final WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (AppUtils.getDisplayMetrics(context).widthPixels * 0.8); //
        window.setAttributes(p);
        window.setGravity(Gravity.CENTER);
        sentryList = findViewById(R.id.sentry_list);
        sentryList.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_textview, datas));
        sentryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onConfrimClickedListener != null) {
                    onConfrimClickedListener.onDialogConfirm(true, datas[position]);
                }
                dismiss();
            }
        });
    }

}
