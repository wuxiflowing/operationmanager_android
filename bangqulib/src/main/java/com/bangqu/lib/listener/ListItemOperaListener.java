package com.bangqu.lib.listener;

/**
 * Created by Administrator on 2018-5-23 0023.
 */

public interface ListItemOperaListener<T> {
    void onItemOpera(String tag, int position, T value);
}
