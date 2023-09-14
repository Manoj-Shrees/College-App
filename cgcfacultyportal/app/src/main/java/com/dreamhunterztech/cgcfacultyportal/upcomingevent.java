package com.dreamhunterztech.cgcfacultyportal;

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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by Dreamer on 29-07-2017.
 */

public class upcomingevent extends Fragment {
    SwipeRefreshLayout refreshLayout;
    RecyclerView upcomingeventlist;
   private Firebase mdatabase;
   private Query query;

    upcmevntadapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_noti,container,false);
        mdatabase = new Firebase("https://cgc-database.firebaseio.com/Events/Upcomingevent");
        query = mdatabase.orderByChild("eventdate");
        upcomingeventlist = (RecyclerView) view.findViewById(R.id.notilists);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        upcomingeventlist.setLayoutManager(mLayoutManager);
        upcomingeventlist.setItemAnimator(new DefaultItemAnimator());
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.notirefresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setSoundEffectsEnabled(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new geteventdata().execute();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new geteventdata().execute();
    }

    public class  upcmevntadapter extends FirebaseRecyclerAdapter<upcomingeventdata,upcmevntviewholder> {


        public upcmevntadapter(Class<upcomingeventdata> modelClass, int modelLayout, Class<upcmevntviewholder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(upcmevntviewholder upcmevntviewholder, upcomingeventdata upcomingeventdata, int i) {

            upcmevntviewholder.seteventname(upcomingeventdata.getEventname());
            upcmevntviewholder.seteventtype(upcomingeventdata.getEventtype());
            upcmevntviewholder.seteventdate(upcomingeventdata.getEventdate());
            upcmevntviewholder.setseventimg(upcomingeventdata.getEventimgurl(),getContext());
            upcmevntviewholder.setlistener(getActivity(),upcomingeventdata.getEventopentype());
            upcmevntviewholder.setanimation();
        }
    }


    public static class upcmevntviewholder extends RecyclerView.ViewHolder
    {
        View view;
        TextView eventname,eventtype,eventdate;
        ImageView eventimg;
        Button openeventdetail;
        public upcmevntviewholder(View itemView) {
            super(itemView);
            view = itemView;
            eventname = (TextView) view.findViewById(R.id.eventnametxt);
            eventtype = (TextView) view.findViewById(R.id.eventtypetxt);
            eventdate = (TextView) view.findViewById(R.id.eventdatetxt);
            eventimg = (ImageView) view.findViewById(R.id.eventimg);
            openeventdetail = (Button) view.findViewById(R.id.eventbtn);

        }

        public void seteventname(String eventnames)
        {
            eventname.setText(eventnames);
        }

        public void seteventtype(String eventtypes)
        {
            eventtype.setText(eventtypes);
        }

        public void  seteventdate(String dates)
        {
            eventdate.setText(dates);
        }

        public void setseventimg(String imgurls, Context context)
        {


            try {
                Picasso.with(context).load(imgurls).fit().into(eventimg);
            }

            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        private void setlistener(final Context context, final String type)
        {
            openeventdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent eventdata = new Intent(context,Eventinfo.class);
                    eventdata.putExtra("event_topic",eventname.getText().toString());
                    eventdata.putExtra("event_type",type);
                    view.getContext().startActivity(eventdata);
                }
            });
        }


        public void setanimation()
        {
            try {
                CardView noticard = (CardView) view.findViewById(R.id.eventcard);
                YoYo.with(Techniques.FadeIn).playOn(noticard);
            }

            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }

    public class geteventdata extends AsyncTask
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            adapter = new upcmevntadapter(upcomingeventdata.class,R.layout.upcomingeventlayout,upcmevntviewholder.class,query);
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
            upcomingeventlist.setAdapter(adapter);
            refreshLayout.setRefreshing(false);
        }
    }
}
