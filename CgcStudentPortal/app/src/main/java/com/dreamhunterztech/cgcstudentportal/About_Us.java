package com.dreamhunterztech.cgcstudentportal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class About_Us extends AppCompatActivity {
Button linkinstagram,linkfacebook,linktwitter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        linkinstagram = (Button) findViewById(R.id.Instagrampage);
        linkfacebook= (Button) findViewById(R.id.facebookpage);
        linktwitter= (Button) findViewById(R.id.twitterpage);
        linkfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(About_Us.this, Browser.class).putExtra("url","https://www.facebook.com/drmhtrz"));
            }
        });

        linktwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(About_Us.this, Browser.class).putExtra("url","https://twitter.com/drmhtrz"));
            }
        });

        linkinstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(About_Us.this, Browser.class).putExtra("url","http://instagram.com/drmhtrz"));

            }
        });

}

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
    }

