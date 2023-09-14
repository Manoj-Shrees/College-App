package com.dreamhunterztech.cgcfacultyportal;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.client.annotations.Nullable;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by Dreamer on 27-07-2017.
 */

public class Homenotification extends Fragment {
    String addrs,sem;
    RecyclerView notilist;
    Firebase mdatabase;
    notiadapter notiadapter;
    SwipeRefreshLayout refreshLayout;
    Query query;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addrs = getActivity().getIntent().getExtras().getString("addr");
        addrs = addrs.substring(0,addrs.lastIndexOf("/"));
        Log.e(">>check",addrs);
       /* sem = addrs.substring(addrs.indexOf("/")+1,addrs.lastIndexOf("/"));
        sem = sem.substring(sem.indexOf("/")+1,sem.length()).trim();*/
        View view=inflater.inflate(R.layout.activity_noti,container,false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.notirefresh);
        notilist = (RecyclerView) view.findViewById(R.id.notilists);
        mdatabase = new Firebase("https://cgc-database.firebaseio.com/"+addrs+"/Notification/ALL");
        query = mdatabase;
        LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        notilist.setLayoutManager(mLayoutManager);
        notilist.setItemAnimator(new DefaultItemAnimator());
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setSoundEffectsEnabled(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getnotidata().execute();
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        new getnotidata().execute();
    }

    public class  notiadapter extends FirebaseRecyclerAdapter<homenotificationdata,notiviewholder> {


        public notiadapter(Class<homenotificationdata> modelClass, int modelLayout, Class<notiviewholder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(notiviewholder notiviewholder, homenotificationdata homenotificationdata, int i) {

                notiviewholder.setnotititle(homenotificationdata.getTitle());
                notiviewholder.setnoticontext(homenotificationdata.getContent());
                notiviewholder.setanimation();

        }
    }


    public static class notiviewholder extends RecyclerView.ViewHolder
    {
        View view;
        TextView notititle,noticontext;
        public notiviewholder(View itemView) {
            super(itemView);
            view = itemView;
            notititle = (TextView) view.findViewById(R.id.notititle);
            noticontext = (TextView) view.findViewById(R.id.noticontext);
        }

        public void setnotititle(String notititles)
        {
            notititle.setText(notititles);
        }

        public void setnoticontext(String notiontexts)
        {
            noticontext.setText(notiontexts);
        }

        public String settype(String type)
        {
         return type;
        }

        public void setanimation()
        {
            try {
                CardView noticard = (CardView) view.findViewById(R.id.noticardview);
                YoYo.with(Techniques.FadeIn).playOn(noticard);
            }

            catch (Exception e)
            {
                e.printStackTrace();
            }

        }


    }

    public class getnotidata extends AsyncTask
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            notiadapter = new notiadapter(homenotificationdata.class,R.layout.homepagenotificationlayout,notiviewholder.class,query);
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
            notilist.setAdapter(notiadapter);
            refreshLayout.setRefreshing(false);
        }
    }


}
