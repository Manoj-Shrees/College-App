package com.dreamhunterztech.cgcstudentportal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by suwas on 30-10-2016.
 */

public class reportpageradater extends FragmentStatePagerAdapter
{
    ArrayList<Fragment> window=new ArrayList<>();
    ArrayList<String>Title=new ArrayList<>();

    public reportpageradater(FragmentManager fm) {
        super(fm);
    }

    public void deletechecker(Boolean chk)
    {
        if(chk==true) {
            Title.clear();
            window.clear();
        }
    }

    public void getdata(Fragment data1, String data2)
    {
        window.add(data1);
        Title.add(data2);
    }

    @Override
    public Fragment getItem(int position) {
        return window.get(position);
    }

    @Override
    public int getCount() {
        return window.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {

        return Title.get(position);
    }
}
