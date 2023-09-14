package com.dreamhunterztech.cgcstudentportal;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

public class Newsfeedupdateselective extends Fragment {
    Button  sharebutton;
    Firebase mdatabase;
    RecyclerView newsfeedlist;
    String addrs,url;
    newsfeedadapter feedsadapter;
    SwipeRefreshLayout feedrefreshlayout;
    Query query;
    Firebase database;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_newsfeedupdate, container, false);
        addrs = getActivity().getIntent().getExtras().getString("addr");
        String childaddrs = addrs.substring(addrs.lastIndexOf("/")-2,addrs.lastIndexOf("/"));
        addrs = addrs.substring(0,addrs.lastIndexOf("/")-3);
        mdatabase = new Firebase(getActivity().getString(R.string.dburl)+addrs+"/Newsfeed/"+childaddrs);
        query = mdatabase.orderByChild("feeddate");
        newsfeedlist = (RecyclerView) view.findViewById(R.id.newsfeedlists);
        feedrefreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.feedrefresh);
        feedrefreshlayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        feedrefreshlayout.setSoundEffectsEnabled(true);
        feedrefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new gettingdata().execute();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        newsfeedlist.setLayoutManager(mLayoutManager);
        newsfeedlist.setItemAnimator(new DefaultItemAnimator());
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        feedrefreshlayout.setRefreshing(true);
        new gettingdata().execute();
    }



    public class  newsfeedadapter extends FirebaseRecyclerAdapter<Feeds,Feedsviewholder>  {

        Button sharebutton;
        ImageButton openfeedimg;
        public newsfeedadapter(Class<Feeds> modelClass, int modelLayout, Class<Feedsviewholder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(final Feedsviewholder feedsviewholder, Feeds feeds, int i) {
            newsfeedlist.scrollToPosition(i);
            feedsviewholder.setFeedtopic(feeds.getFeedtopic());
            feedsviewholder.setFeedcontext(feeds.getFeedcontext());
            feedsviewholder.setFeeddate(feeds.getFeeddate());
            feedsviewholder.setFeedtime(feeds.getFeedtime());
            feedsviewholder.setFeedauther(feeds.getFeedauther());
            feedsviewholder.setanimation();
            sharebutton=feedsviewholder.getbutton();
            sharebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String data = feedsviewholder.feedtopictxt.getText().toString()+":\n\n"+feedsviewholder.feedcontexttxt.getText().toString()+"\n\n -   "+feedsviewholder.feedauthertxt.getText().toString();
                    Intent shareintent = new Intent(Intent.ACTION_SEND);
                    shareintent.setType("text/plain");
                    shareintent.putExtra(Intent.EXTRA_TEXT,data);
                    startActivity(Intent.createChooser(shareintent,"Share with"));
                }
            });

            openfeedimg = feedsviewholder.getImagebutton();
            openfeedimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   String feedname =feedsviewholder.feedtopictxt.getText().toString();
                   Firebase feedimgref = new Firebase(getActivity().getString(R.string.dburl)+addrs+"/Newsfeed/"+feedname+"/feedimgurl");
                    feedimgref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue().toString().equals("N/A"))
                            {
                                Toast.makeText(getContext(),"No Image Found or not uploaded Yet.",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                opengallery(dataSnapshot.getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            });
        }

    }


    public static class Feedsviewholder extends RecyclerView.ViewHolder
    {
        View view;
        Button sharebutton;
        TextView feedtopictxt,feedcontexttxt,feeddatetxt,feedtimetxt,feedauthertxt;
        ImageButton openimg;
        public Feedsviewholder(View itemView) {
            super(itemView);
             view = itemView;
            feedtopictxt = (TextView) view.findViewById(R.id.newsfeedhead);
            feedcontexttxt = (TextView) view.findViewById(R.id.newsfeedcontext);
            feeddatetxt = (TextView) view.findViewById(R.id.feedsdate);
            feedtimetxt = (TextView) view.findViewById(R.id.feedstime);
            feedauthertxt = (TextView) view.findViewById(R.id.newsfeedauther);
        }

        public void setFeedtopic(String feedtopic)
        {
            feedtopictxt.setText(feedtopic);
        }

        public void setFeedcontext(String feedcontext)
        {
            feedcontexttxt.setText(feedcontext);
        }

        public void setFeeddate(String feeddate)
        {
            feeddatetxt.setText(feeddate);
        }

        public void setFeedtime(String feedtime)
        {
            feedtimetxt.setText(feedtime);
        }

        public void setFeedauther(String feedauther)
        {
            feedauthertxt.setText(feedauther);
        }


        public void setanimation()
        {
           CardView feedscard = (CardView) view.findViewById(R.id.newsfeedcardview);
            YoYo.with(Techniques.Landing).playOn(feedscard);
        }

        public Button getbutton()
        {
              sharebutton = (Button) view.findViewById(R.id.newsfeedsharebutton);
              return sharebutton;
        }
        public ImageButton getImagebutton()
        {
            openimg = (ImageButton) view.findViewById(R.id.picopen);
            return openimg;
        }

    }

    public class gettingdata extends AsyncTask
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] params) {
            feedsadapter = new newsfeedadapter(Feeds.class,R.layout.newsfeedlistlayout,Feedsviewholder.class,query);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {

                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            feedrefreshlayout.setRefreshing(false);
            newsfeedlist.setAdapter(feedsadapter);
        }
    }

    private void opengallery(String ref)
    {

        Intent  open = new Intent(getContext(),ImageGallery.class);
        open.putExtra("img_url",ref);
        startActivity(open);
    }

}
