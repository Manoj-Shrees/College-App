package com.dreamhunterztech.cgcstudentportal;

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
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

/**
 * Created by Dreamer on 30-07-2017.
 */

public class Reportstatus extends Fragment {

    SwipeRefreshLayout refreshLayout;
    reportadapter adapter;
    RecyclerView reportlist;
    Firebase mdatabase;
    Query query;
    String addrs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_noti,container,false);
        Firebase.setAndroidContext(getActivity());
        addrs = getActivity().getIntent().getExtras().getString("addr");
        reportlist = (RecyclerView) view.findViewById(R.id.notilists);
        mdatabase = new Firebase(getActivity().getString(R.string.dburl)+"/Report/Academics");
        query = mdatabase.orderByChild("Reportby").equalTo(addrs.toString());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        reportlist.setLayoutManager(mLayoutManager);
        reportlist.setItemAnimator(new DefaultItemAnimator());
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.notirefresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setSoundEffectsEnabled(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getreportdata().execute();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new getreportdata().execute();
    }

    public class  reportadapter extends FirebaseRecyclerAdapter<repstatusdata,reportviewholder> {


        public reportadapter(Class<repstatusdata> modelClass, int modelLayout, Class<reportviewholder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(reportviewholder reportviewholder, repstatusdata repstatusdata, int i) {
                reportviewholder.setreptitle(repstatusdata.getReporttopic());
                reportviewholder.setrepcontext(repstatusdata.getReportcontext());
                reportviewholder.setRepstatus(repstatusdata.getReportStatus());
                reportviewholder.setReptype(repstatusdata.getReporttype());
                reportviewholder.setanimation();

        }
    }


    public static class reportviewholder extends RecyclerView.ViewHolder
    {
        View view;
        TextView reptitle,repcontext,repstatus,reptype;
        public reportviewholder(View itemView) {
            super(itemView);
            view = itemView;
            reptitle = (TextView) view.findViewById(R.id.reporttitle);
            repcontext = (TextView) view.findViewById(R.id.reportcontext);
            repstatus = (TextView) view.findViewById(R.id.reportstatus);
            reptype = (TextView) view.findViewById(R.id.reporttype);
        }

        public void setreptitle(String reptitles)
        {
            reptitle.setText(reptitles);
        }

        public void setrepcontext(String repcontexts)
        {
            repcontext.setText(repcontexts);
        }

        public void setRepstatus(String repstat)
        {
            repstatus.setText(repstat);
        }


        public void setReptype(String reptypes)
        {
            reptype.setText(reptypes);
        }

        public  String setReportby(String Reportby)
        {
            return Reportby;
        }


        public void setanimation()
        {
            CardView noticard = (CardView) view.findViewById(R.id.repcardview);
            YoYo.with(Techniques.BounceIn).playOn(noticard);
        }


    }

    public class getreportdata extends AsyncTask
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            adapter = new reportadapter(repstatusdata.class,R.layout.reportstatlayout,reportviewholder.class,query);
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
            reportlist.setAdapter(adapter);
            refreshLayout.setRefreshing(false);
        }
    }
}
