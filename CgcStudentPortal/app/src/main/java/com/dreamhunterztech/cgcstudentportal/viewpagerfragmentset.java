package com.dreamhunterztech.cgcstudentportal;

        import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
        import android.support.v4.widget.NestedScrollView;
        import android.util.Log;
        import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
        import android.widget.ImageView;

        import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
        import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.squareup.picasso.Picasso;

/**
 * Created by Dreamers on 05-07-2016.
 */
public class  viewpagerfragmentset extends Fragment
{
    ViewPager vpager;
    eventpageradapter adapter;
    ImageView eventimgad1;
    ImageView eventimgad2;
    ImageView eventimgad3;
    ImageView eventimgad4;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TabLayout tab;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpagerlayout,container,false);
        eventimgad1 = (ImageView) view.findViewById(R.id.eventad1);
        eventimgad2 = (ImageView) view.findViewById(R.id.eventad2);
        eventimgad3 = (ImageView) view.findViewById(R.id.eventad3);
        eventimgad4 = (ImageView) view.findViewById(R.id.eventad4);
        vpager = (ViewPager)view.findViewById(R.id.pager);
        adapter=new eventpageradapter(getFragmentManager());
        collapsingToolbarLayout= (CollapsingToolbarLayout) view.findViewById(R.id.collapsetoolbar);
        collapsingToolbarLayout.setTitle("ADDS");

        tab= (TabLayout)view.findViewById(R.id.tabs);
        adapter.deletechecker(true);
        adapter.getdata(new INCAMPUS(), "In Campus");
        adapter.getdata(new OUTCAMPUS(), "Out Campus");
        vpager.setAdapter(adapter);
        vpager.setPageTransformer(true, new CubeOutTransformer());
        tab.setupWithViewPager(vpager);
        tab.setBackgroundColor(0x3565fd);
        setads ();
        return view;
    }



    private void setads ()
    {
        DatabaseReference eventad1imgref = FirebaseDatabase.getInstance().getReference().child("Events").child("Eventflash").child("flash1imgurl");
        eventad1imgref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String imageref = dataSnapshot.getValue(String.class).toString();
                        Picasso.with(getContext()).load(imageref).fit().into(eventimgad1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        DatabaseReference eventad2imgref = FirebaseDatabase.getInstance().getReference().child("Events").child("Eventflash").child("flash2imgurl");
        eventad2imgref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String imageref = dataSnapshot.getValue(String.class).toString();
                        Picasso.with(getContext()).load(imageref).fit().into(eventimgad2);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        DatabaseReference eventad3imgref = FirebaseDatabase.getInstance().getReference().child("Events").child("Eventflash").child("flash3imgurl");
        eventad3imgref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String imageref = dataSnapshot.getValue(String.class).toString();
                        Picasso.with(getContext()).load(imageref).fit().into(eventimgad3);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        DatabaseReference eventad4imgref = FirebaseDatabase.getInstance().getReference().child("Events").child("Eventflash").child("flash4imgurl");
        eventad4imgref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String imageref = dataSnapshot.getValue(String.class).toString();
                        Picasso.with(getContext()).load(imageref).fit().into(eventimgad4);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



}
