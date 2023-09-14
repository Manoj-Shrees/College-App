package com.dreamhunterztech.cgcfacultyportal;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class INCAMPUS extends Fragment

{
    Firebase incamdatabase;
    Query query;
    RecyclerView recyclerView;
    incampuseventsadapter incamevntadapter;
    SwipeRefreshLayout refreshLayout;
    private Map<String,List<String>> incamevntdatalist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layoutlist, container, false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.eventrefresh);
        incamevntdatalist = new HashMap<>();
        incamdatabase = new Firebase("https://cgc-database.firebaseio.com/Events/Incampus");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setSoundEffectsEnabled(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getincameventdata().execute();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new getincameventdata().execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        new getincameventdata().cancel(true);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class  incampuseventsadapter extends FirebaseRecyclerAdapter<Incampusevents,incampuseventviewholder> {

        public incampuseventsadapter(Class<Incampusevents> modelClass, int modelLayout, Class<incampuseventviewholder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(incampuseventviewholder incampuseventviewholder, Incampusevents incampusevents, int i) {
            incampuseventviewholder.seteventname(incampusevents.getIncameventname());
            incampuseventviewholder.seteventtype(incampusevents.getIncameventtype());
            incampuseventviewholder.setEventimg(incampusevents.getIncameventimgurl(),getContext());
            incampuseventviewholder.setAnimation();
            incampuseventviewholder.setlistner(getContext());
        }
    }


    public static class incampuseventviewholder extends RecyclerView.ViewHolder {
        View view;
        TextView incameventnametxt, incameventtypetxt;
        ImageView incameventimg;
        CardView card;
        ProgressBar loadimg;
        String exebntimgurl;

        public incampuseventviewholder(View itemView) {
            super(itemView);
            view = itemView;
            incameventnametxt = (TextView) itemView.findViewById(R.id.eventtxt);
            incameventnametxt.setSelected(true);
            incameventtypetxt = (TextView) itemView.findViewById(R.id.eventtype);
            loadimg = (ProgressBar)itemView.findViewById(R.id.loadeventimg);
            card = (CardView) itemView.findViewById(R.id.eventcard);
            loadimg.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
            incameventimg = (ImageView) itemView.findViewById(R.id.eventimg);
        }

        public void seteventname(String eventname) {
            incameventnametxt.setText(eventname);
        }

        public void seteventtype(String eventtype) {
            incameventtypetxt.setText(eventtype);
        }

        public void setEventimg(String eventimg,Context cx) {

            Picasso.with(cx).load(eventimg).into(incameventimg, new Callback() {
                @Override
                public void onSuccess() {
                    loadimg.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    incameventimg.setImageResource(R.drawable.brokenpic);
                    loadimg.setVisibility(View.GONE);
                }
            });

        }

        public void setAnimation()
        {
            YoYo.with(Techniques.Landing).playOn(card);
        }

        public void setlistner(final Context contxt)
        {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent eventdata = new Intent(contxt,Eventinfo.class);
                    eventdata.putExtra("event_topic",incameventnametxt.getText().toString());
                    eventdata.putExtra("event_type","Incampus");
                    view.getContext().startActivity(eventdata);
                }
            });
        }
    }

    public class getincameventdata extends AsyncTask
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected Object doInBackground(Object[] params) {
              incamevntadapter= new incampuseventsadapter(Incampusevents.class,R.layout.listlayout,incampuseventviewholder.class,incamdatabase);
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
            recyclerView.setAdapter(incamevntadapter);
            refreshLayout.setRefreshing(false);

        }
    }
}