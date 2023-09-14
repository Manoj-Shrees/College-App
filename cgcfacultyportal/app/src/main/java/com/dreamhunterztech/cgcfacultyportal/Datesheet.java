package com.dreamhunterztech.cgcfacultyportal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Datesheet extends Fragment
{
    ExpandableListView datesheetexpListView;
    ArrayList<String> menu;
    List<String> ss1,ss2,ss3,Final;
    HashMap<String, List<String>> listDataChild;
    Expandablelistadapter  listAdapter;
    ImageView imageView;
    CardView cardView;
    int acount=0;
    String url="",addrs="",sem="sem";
    private SharedPreferences sp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addrs = getActivity().getIntent().getExtras().getString("addr");
        url = addrs.substring(0,addrs.lastIndexOf("/"));
        sp=getActivity().getSharedPreferences("Login", 0);
        sem+=sp.getString("sem","").substring(0,1);
        View view=inflater.inflate(R.layout.expandablelistlayout1,container,false);
        View card = inflater.inflate(R.layout.expandlistsubmenulayout,container,false);
        cardView = (CardView) card.findViewById(R.id.datesheetcard);
        cardView.setCardBackgroundColor(Color.parseColor("#b107917a"));
        menu = new ArrayList<>();
        ss1= new ArrayList<String>();
        ss2= new ArrayList<String>();
        ss3= new ArrayList<String>();
        Final= new ArrayList<String>();
        Log.e(">>",addrs+url+sem);
        listDataChild = new HashMap<String, List<String>>();
        getdata();
        setarraylist();
        datesheetexpListView = (ExpandableListView) view.findViewById(R.id.datesheetexplist);
        listAdapter = new Expandablelistadapter(getActivity(),menu, listDataChild);
        datesheetexpListView.setAdapter(listAdapter);
        datesheetexpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                opengallery(groupPosition, childPosition, getContext());
                return false;
            }
        });
        return view;
    }

    private void setarraylist()
    {
        menu.add("1st. Sessional");
        menu.add("2nd. Sessional");
        menu.add("3rd. Sessional");
        menu.add("Final");

        listDataChild.put(menu.get(0), ss1);
        listDataChild.put(menu.get(1), ss2);
        listDataChild.put(menu.get(2), ss3);
        listDataChild.put(menu.get(3), Final);
    }

    private void  getdata()
    {

        Firebase datesheetss1ref = new Firebase("https://cgc-database.firebaseio.com/"+url+"/Datesheet/"+sem+"/sessional1/imgurl");
        datesheetss1ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ss1.clear();
                Log.e("ss1>>",dataSnapshot.getRef().toString());
                ss1.add(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        Firebase datesheetss2ref = new Firebase("https://cgc-database.firebaseio.com/"+url+"/Datesheet/"+sem+"/sessional2/imgurl");
        datesheetss2ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ss2.clear();
                ss2.add(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase datesheetss3ref = new Firebase("https://cgc-database.firebaseio.com/"+url+"/Datesheet/"+sem+"/sessional3/imgurl");
        datesheetss3ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ss3.clear();
                ss3.add(dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        Firebase datesheetfinalref = new Firebase("https://cgc-database.firebaseio.com/"+url+"/Datesheet/"+sem+"/final/imgurl");
        datesheetfinalref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Final.clear();
                Final.add(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void opengallery(int attr1, int attr2, Context contxt) {
        if (listDataChild.get(menu.get(attr1)).get(attr2).equals("N/A")) {
Toast.makeText(getContext(),"Datesheet Not Found or Not Uploaded Yet",Toast.LENGTH_LONG).show();
        }

        else {
            Intent open = new Intent(contxt, ImageGallery.class);
            String imgurl = listDataChild.get(menu.get(attr1)).get(attr2);
            open.putExtra("img_url", imgurl);
            startActivity(open);
        }
    }

    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public void onStart() {
        super.onStart();
        sem="sem";
        sem+=sp.getString("sem","").substring(0,1);
    }
}
