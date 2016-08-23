package com.dacheng.coordinatorlayout_recyclerview.sticklayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by dacheng on 16/8/22.
 * ViewPager的Adapter
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> listFragment;
    private List<String> listTitle;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> listFragment, List<String> listTitle) {
        super(fm);
        this.listFragment=listFragment;
        this.listTitle=listTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }
}
