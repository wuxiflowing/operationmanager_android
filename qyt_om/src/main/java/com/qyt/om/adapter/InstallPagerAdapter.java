package com.qyt.om.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qyt.om.activity.fragment.InstallTaskFragment;

/**
 * Created by Hello on 2015/8/27.
 */
public class InstallPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"待接单", "进行中", "已完成"};

    public InstallPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        return InstallTaskFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
