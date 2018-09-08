package com.vanh1200.newsfilter.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.vanh1200.newsfilter.Fragment.FavoriteFragment;
import com.vanh1200.newsfilter.Fragment.NewsListFragment;
import com.vanh1200.newsfilter.Fragment.SavedFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter{
    private static final String TAG = "ViewPagerAdapter";
    private final ArrayList<Fragment> arrFragment = new ArrayList<>();
    private final ArrayList<String> arrFragName = new ArrayList<>();
    private final ArrayList<Integer> arrTabIcon = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return arrFragment.get(position);
            case 1:
                return arrFragment.get(position);
            case 2:
                return arrFragment.get(position);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return arrFragName.get(position);
            case 1:
                return arrFragName.get(position);
            case 2:
                return arrFragName.get(position);
        }
        return "";
    }

    @Override
    public int getCount() {
        return arrFragment.size();
    }

    public void addFragment(Fragment fragment, String name){
        arrFragment.add(fragment);
        arrFragName.add(name);
    }


}
