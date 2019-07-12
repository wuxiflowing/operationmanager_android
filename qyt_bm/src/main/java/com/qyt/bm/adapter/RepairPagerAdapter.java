package com.qyt.bm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qyt.bm.activity.fragment.InstallTaskFragment;
import com.qyt.bm.activity.fragment.RepairTaskFragment;

/**
 * Created by Hello on 2015/8/27.
 */
public class RepairPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"待接单", "进行中", "已完成"};

    public RepairPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        return RepairTaskFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
