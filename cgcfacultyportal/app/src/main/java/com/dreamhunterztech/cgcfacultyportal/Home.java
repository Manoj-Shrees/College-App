package com.dreamhunterztech.cgcfacultyportal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Home extends Fragment {

    private String addrs;
    CardView homecardview;
    ViewPager vpager;
    eventpageradapter adapter;
    TabLayout tab;
    TextView username,userbranch,userempid,userjobpro;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homelayout, container, false);
        Firebase.setAndroidContext(getActivity());
        addrs = getActivity().getIntent().getExtras().getString("addr");
        homecardview = (CardView) view.findViewById(R.id.profilecradview);
        username = (TextView) view.findViewById(R.id.username);
        userbranch = (TextView) view.findViewById(R.id.branch);
        userempid = (TextView) view.findViewById(R.id.RollNO);
        userjobpro = (TextView) view.findViewById(R.id.jobpro);
        vpager = (ViewPager)view.findViewById(R.id.pager);
        adapter=new eventpageradapter(getFragmentManager());

        tab= (TabLayout)view.findViewById(R.id.tabs);
        adapter.deletechecker(true);
        adapter.getdata(new Homenotification(), "Notification");
        adapter.getdata(new Homenews(), "News");
        adapter.getdata(new upcomingevent(), "upcoming Events");
      /*  adapter.getdata(new Reportstatus(), "Report status (Acadmics)");*/
        vpager.setAdapter(adapter);
        vpager.setPageTransformer(true, new ForegroundToBackgroundTransformer());
        tab.setupWithViewPager(vpager);
        tab.setBackgroundColor(0x3565fd);

        final Animation texviewanim = AnimationUtils.loadAnimation(getContext(), R.anim.clciktextanim);

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.startAnimation(texviewanim);
            }
        });




        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        homecardview.setCardBackgroundColor(Color.parseColor("#0F060606"));
        getdatabase();
    }

    private void getdatabase()
    {
        Firebase usernameref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Name");
        usernameref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                username.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase branchref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Branch");
        branchref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                userbranch.setText("Branch : "+dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase jobprofiledref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Job Profile");
        jobprofiledref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                userjobpro.setText("Job Profile : "+dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        Firebase empidref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Empid");
        empidref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                userempid.setText("Employee ID : "+dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

    }




}
