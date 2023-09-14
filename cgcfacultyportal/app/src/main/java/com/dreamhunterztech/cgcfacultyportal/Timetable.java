package com.dreamhunterztech.cgcfacultyportal;


import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static android.content.Context.WINDOW_SERVICE;

public class Timetable extends Fragment {
    ImageView timetableview;
    FloatingActionButton reloadimg;
    ProgressBar progressBar;
    FragmentManager manager;
    SharedPreferences sp,sp2;
    SharedPreferences.Editor dwned;
    String addrs,sem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        manager = getFragmentManager();
        View view = inflater.inflate(R.layout.timetablelayout, container, false);
        addrs = getActivity().getIntent().getExtras().getString("addr");
        addrs = addrs.substring(0,addrs.lastIndexOf("/"));
        sp=getActivity().getSharedPreferences("Login", 0);
        sem= "/"+sp.getString("sem","");
        timetableview = (ImageView) view.findViewById(R.id.timetableimgview);
        reloadimg = (FloatingActionButton) view.findViewById(R.id.reloadtimetable);
        Animation zoomout = AnimationUtils.loadAnimation(getActivity(),R.anim.zoomout);
        timetableview.setAnimation(zoomout);
        timetableview.setOnTouchListener(new touchlistener());
        progressBar = (ProgressBar) view.findViewById(R.id.imgloadprogress);
        reloadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadimg.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotation));
                progressBar.setVisibility(View.VISIBLE);
                new CountDownTimer(1000,1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        progressBar.setVisibility(View.GONE);
                        FragmentTransaction transaction=manager.beginTransaction();
                        transaction.replace(R.id.container, new Timetable());
                        transaction.addToBackStack("Timetable");
                        transaction.commit();
                    }

                }.start();
            }
        });

        loadtimetable();
        return view;
    }


private void loadtimetable() {
    Firebase loadtimetable = new Firebase("https://cgc-database.firebaseio.com/"+addrs+sem+"/Timetable/imgurl");
    loadtimetable.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getValue().toString().equals("N/A"))
            {
                Toast.makeText(getContext(),"Timetable Not Found or Not uploaded Yet.",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            else {
                final String imageref = dataSnapshot.getValue(String.class).toString();
                Picasso.with(getActivity().getApplicationContext()).load(imageref).fit().into(timetableview, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        try {
                            Toast.makeText(getActivity(), "Error\nFail to load the profile image", Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e)
                        {

                        }
                        progressBar.setProgress(View.GONE);
                    }
                });
            }

        }
        @Override
        public void onCancelled (FirebaseError firebaseError){

        }
    });
}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        loadtimetable();

    }

    @Override
    public void onStart() {
        super.onStart();
        sem="";
        sem="/"+sp.getString("sem","");
    }
}