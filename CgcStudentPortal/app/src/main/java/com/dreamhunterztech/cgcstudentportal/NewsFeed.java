package com.dreamhunterztech.cgcstudentportal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;

/**
 * Created by Dreamer on 12-09-2017.
 */

public class NewsFeed extends Fragment{
    TabLayout newsfeedhead;
    ViewPager newsfeedpager;
    newsfeedadapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newsfeedlayout, container, false);
        newsfeedhead = (TabLayout) view.findViewById(R.id.newsfeedtabs);
        newsfeedpager = (ViewPager) view.findViewById(R.id.newsfeedpager);
        adapter=new newsfeedadapter(getFragmentManager());
        adapter.deletechecker(true);
        adapter.getdata(new Newsfeedupdateall(),"All");
        adapter.getdata(new Newsfeedupdateselective(),"Selective");
        newsfeedpager.setAdapter(adapter);
        newsfeedpager.setPageTransformer(true,new ForegroundToBackgroundTransformer());
        newsfeedhead.setupWithViewPager(newsfeedpager);
        return view;
    }
}
