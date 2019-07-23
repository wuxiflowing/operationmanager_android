package com.qyt.om.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.TextureView;

/**
 * Created by jary on 2016/11/3.
 * 设置小数位数控制
 */
public class PointLengthFilter implements InputFilter {

    /**
     * 输入框小数的位数  示例保留一位小数
     */
    private static final int DECIMAL_DIGITS = 1;

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
            return null;
        }
        if (".".equals(source.toString()) && TextUtils.isEmpty(dest)) {
            return "0.";
        }
        String dValue = dest.toString();
        String[] splitArray = dValue.split("\\.");
        if (splitArray.length > 1) {
            String dotValue = splitArray[1];
            int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
            if (diff > 0) {
                return source.subSequence(start, end - diff);
            }
        }
        return null;
    }
}