package com.dreamhunterztech.cgcstudentportal;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;

/**
 * Created by suwas on 30-10-2016.
 */

public class Report extends Fragment
{
    reportpageradater adapter;
    TabLayout reporthead;
    ViewPager reportpager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reportlayout, container, false);
        reporthead = (TabLayout) view.findViewById(R.id.reporttabs);
        reportpager = (ViewPager) view.findViewById(R.id.reportpager);
        adapter=new reportpageradater(getFragmentManager());
        adapter.deletechecker(true);
        adapter.getdata(new Reportacademics(),"Academics");
        adapter.getdata(new ReportHostel(),"Hostel");
        adapter.getdata(new ReportApp(),"App");
        reportpager.setAdapter(adapter);
        reportpager.setPageTransformer(true,new ForegroundToBackgroundTransformer());
        reporthead.setupWithViewPager(reportpager);
        return view;
    }
}
