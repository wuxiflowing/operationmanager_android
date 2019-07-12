package com.qyt.bm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qyt.bm.activity.fragment.CallbackTaskFragment;
import com.qyt.bm.activity.fragment.CheckBackTaskFragment;
import com.qyt.bm.activity.fragment.InstallTaskFragment;

/**
 * Created by Hello on 2015/8/27.
 */
public class CallbackPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"管家审核", "大区审核", "待接单", "进行中", "已完成"};

    public CallbackPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        if (position > 1) {
            return CallbackTaskFragment.newInstance(position);
        } else {
            return CheckBackTaskFragment.newInstance(position);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
