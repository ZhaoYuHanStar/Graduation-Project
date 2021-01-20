package com.example.user.toy.home.util;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author june
 */
public class FragmentAdapter extends FragmentPagerAdapter {


    private List <Fragment> fragments;
    private String[] titles;

    public FragmentAdapter(FragmentManager fm , List <Fragment> list , String[] titles ) {
        super (fm);
        this.fragments = list;
        this.titles = titles;

    }

    @Override
    public Fragment getItem(int i ) {
        return fragments.get (i);
    }

    @Override
    public int getCount() {
        return fragments.size ( );
    }


    /**
     * 返回的tab的标题
     * */
    @Nullable
    @Override
    public CharSequence getPageTitle( int position )  {
        return titles[position];
    }


}