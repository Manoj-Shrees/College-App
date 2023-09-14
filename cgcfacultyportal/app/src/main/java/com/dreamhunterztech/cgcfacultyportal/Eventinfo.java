package com.dreamhunterztech.cgcfacultyportal;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import pl.droidsonroids.gif.GifImageView;

public class Eventinfo extends AppCompatActivity {
    Toolbar toolbarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String eventname,refname;
    TextView eventdesc,eventdate,eventvenue,eventregdate,eventreglink,contactno,eventmsg1,eventmsg2;
    GifImageView imgview;
    private String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventinfo);
        toolbarLayout = (Toolbar) findViewById(R.id.Eventname);
        setSupportActionBar(toolbarLayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsetoolbar);
        eventname = getIntent().getExtras().get("event_topic").toString();
        refname =  getIntent().getExtras().get("event_type").toString();
        imgview = (GifImageView) findViewById(R.id.eventimgview);
        eventdesc = (TextView) findViewById(R.id.Eventdescrption);
        eventdate = (TextView) findViewById(R.id.Eventdate);
        eventvenue = (TextView) findViewById(R.id.Eventvenue);
        eventregdate = (TextView) findViewById(R.id.Eventregdate);
        eventreglink = (TextView) findViewById(R.id.Eventreglink);
        contactno = (TextView) findViewById(R.id.Eventcontactno);
        eventmsg1 = (TextView) findViewById(R.id.Eventmsg1);
        eventmsg2 = (TextView) findViewById(R.id.Eventmsg2);
        geteventdata();
        collapsingToolbarLayout.setTitle(eventname);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        toolbarLayout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

private void geteventdata()
{

    if(refname.equals("Incampus"))
    {
        type = "in";
    }
    else
    {
        type = "out";
    }

    Log.e(">>info1",eventname);
    Log.e(">>info2",type);

    DatabaseReference eventimgref = FirebaseDatabase.getInstance().getReference().child("Events").child(refname).child(eventname).child(type+"cameventimgurl");
   Log.e(">>>ref",eventimgref.toString());
    eventimgref.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   String imageref = dataSnapshot.getValue().toString();
                    Log.e(">>imgref",imageref);
                    Picasso.with(getApplicationContext()).load(imageref).into(imgview);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    DatabaseReference eventdescref = FirebaseDatabase.getInstance().getReference().child("Events").child(refname).child(eventname).child(type+"cameventdescrp");
    eventdescref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
             eventdesc.setText(dataSnapshot.getValue().toString());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    DatabaseReference eventdateref = FirebaseDatabase.getInstance().getReference().child("Events").child(refname).child(eventname).child(type+"cameventdate");
    eventdateref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            eventdate.setText(dataSnapshot.getValue().toString());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    DatabaseReference eventvenueref = FirebaseDatabase.getInstance().getReference().child("Events").child(refname).child(eventname).child(type+"cameventvenue");
    eventvenueref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            eventvenue.setText(dataSnapshot.getValue().toString());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    DatabaseReference eventregdateref = FirebaseDatabase.getInstance().getReference().child("Events").child(refname).child(eventname).child(type+"cameventregdate");
    eventregdateref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            eventregdate.setText(dataSnapshot.getValue().toString());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    DatabaseReference eventreglinkref = FirebaseDatabase.getInstance().getReference().child("Events").child(refname).child(eventname).child(type+"cameventreglink");
    eventreglinkref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            eventreglink.setText(dataSnapshot.getValue().toString());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    DatabaseReference contactnoref = FirebaseDatabase.getInstance().getReference().child("Events").child(refname).child(eventname).child(type+"cameventcontactno");
    contactnoref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            contactno.setText(dataSnapshot.getValue().toString());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    DatabaseReference eventmsg1ref = FirebaseDatabase.getInstance().getReference().child("Events").child(refname).child(eventname).child(type+"cameventmsg1");
    eventmsg1ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            eventmsg1.setText(dataSnapshot.getValue().toString());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });


    DatabaseReference eventmsg2ref = FirebaseDatabase.getInstance().getReference().child("Events").child(refname).child(eventname).child(type+"cameventmsg2");
    eventmsg2ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            eventmsg2.setText(dataSnapshot.getValue().toString());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    type="";
}

}
