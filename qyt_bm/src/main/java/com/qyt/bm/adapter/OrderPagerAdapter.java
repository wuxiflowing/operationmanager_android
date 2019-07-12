package com.qyt.bm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qyt.bm.activity.fragment.CustomerOrderFragment;
import com.qyt.bm.activity.fragment.CustomerPactFragment;

/**
 * Created by Hello on 2015/8/27.
 */
public class OrderPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"服务订单", "押金订单", "预付款订单", "饲料订单"};

    public OrderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        return CustomerOrderFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
