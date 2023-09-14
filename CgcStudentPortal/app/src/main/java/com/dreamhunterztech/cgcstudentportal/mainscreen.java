package com.dreamhunterztech.cgcstudentportal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.onesignal.OneSignal;


/**
 * Created by Dreamer on 05-05-2017.
 */

public class mainscreen extends AppCompatActivity
{

    private SharedPreferences sp;
    private String user,dataloc,sem;
    private Boolean blockuser=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        MultiDex.install(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreenlayout);
        sp=getSharedPreferences("Login", 0);
        user=sp.getString("userid","");
        sem = sp.getString("sem","");
        dataloc = sp.getString("dataloc","");

    }

    @Override
    protected void onStart() {
        super.onStart();
        userdetail();
        CountDownTimer countback = new CountDownTimer(4000, 100) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {

                if(!user.equals("") && !dataloc.equals(""))
                {
                    if (blockuser==true)
                    {
                        Intent strt = new Intent(getApplicationContext(), Login_frame.class);
                        finish();
                        strt.putExtra("blockuser","true");
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        startActivity(strt);
                    }

                    else {
                        Intent strt = new Intent(getApplicationContext(), MainFrame.class);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        strt.putExtra("addr", dataloc + "/" + user);
                        strt.putExtra("sem", sem);
                        startActivity(strt);
                    }
                }

                else
                {
                    Intent strt = new Intent(getApplicationContext(), Login_frame.class);
                    finish();
                    strt.putExtra("blockuser","false");
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    startActivity(strt);
                }
            }
        }.start();
    }

    private void userdetail() {
        final Firebase blockuserref = new Firebase(getApplicationContext().getString(R.string.dburl)+"/Blockedusers");
        blockuserref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(user).exists())
                {
                    blockuser=true;
                }

                else
                {
                    blockuser=false;
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
