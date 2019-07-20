package com.qyt.om.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bangqu.lib.utils.AppUtils;
import com.qyt.om.R;

import java.util.logging.StreamHandler;

/**
 * @author: sunzhibin
 * @date: 2019-07-13 13:50
 * @description:
 * @e-mail:
 */
public class EditContactsDialog extends Dialog implements View.OnClickListener {
    private Context mContext;

    private TextView tvContacts;
    private EditText etContacts;
    private TextView tvPhone;
    private EditText etPhone;
    private Button edittextCancel;
    private Button edittextOk;

    public EditContactsDialog(@NonNull Context context, OnEditContactListener listener) {
        super(context, R.style.menu_dialog_style);
        this.mContext = context;
        this.mListener = listener;
        setContentView(R.layout.dialog_edit_contacts);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (AppUtils.getDisplayMetrics(mContext).widthPixels * 0.8); //
        window.setAttributes(p);

        //edittextTitle = findViewById(R.id.edittext_title);
        //tvContacts = findViewById(R.id.tv_contacts);
        etContacts = findViewById(R.id.et_contacts);
        //tvPhone = findViewById(R.id.tv_phone);
        etPhone = findViewById(R.id.et_phone);

        findViewById(R.id.edittext_cancel).setOnClickListener(this);
        findViewById(R.id.edittext_ok).setOnClickListener(this);
    }

    private void initData() {

    }


    private void initListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edittext_cancel:
                dismiss();
                break;
            case R.id.edittext_ok:
                // fixme 调用接口添加联系人

                if (mListener != null) {
                    mListener.onEditContact("", etContacts.getText().toString(), etPhone.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    public interface OnEditContactListener {
        void onEditContact(String tag, String people, String phoneNum);
    }

    private OnEditContactListener mListener;

    public void setmListener(OnEditContactListener mListener) {
        this.mListener = mListener;
    }
}
