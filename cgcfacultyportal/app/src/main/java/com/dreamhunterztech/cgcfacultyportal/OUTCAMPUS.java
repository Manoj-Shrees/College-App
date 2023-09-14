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

public class OUTCAMPUS extends Fragment
{
    Firebase outcamdatabase;
    Query query;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    outcampuseventsadapter outcamevntadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layoutlist,container,false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.eventrefresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        outcamdatabase = new Firebase("https://cgc-database.firebaseio.com/Events/Outcampus");
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
                new getoutcameventdata().execute();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new getoutcameventdata().execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        new getoutcameventdata().cancel(true);
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
            }
            else
                {
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


    public class  outcampuseventsadapter extends FirebaseRecyclerAdapter<outcampusevents,outcampuseventviewholder> {


        public outcampuseventsadapter(Class<outcampusevents> modelClass, int modelLayout, Class<outcampuseventviewholder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }


        @Override
        protected void populateViewHolder(outcampuseventviewholder outcampuseventviewholder, outcampusevents outcampusevents, int i) {
            outcampuseventviewholder.seteventname(outcampusevents.getOutcameventname());
            outcampuseventviewholder.seteventtype(outcampusevents.getOutcameventtype());
            outcampuseventviewholder.setEventimg(outcampusevents.getOutcameventimgurl(),getContext());
            outcampuseventviewholder.setAnimation();
            outcampuseventviewholder.setlistner(getContext());
        }
    }


    public static class outcampuseventviewholder extends RecyclerView.ViewHolder {
        View view;
        TextView outcameventnametxt, outcameventtypetxt;
        ImageView outcameventimg;
        CardView outcard;
        ProgressBar outloadimg;

        public outcampuseventviewholder(View itemView) {
            super(itemView);
            view = itemView;
            outcameventnametxt = (TextView) itemView.findViewById(R.id.eventtxt);
            outcameventnametxt.setSelected(true);
            outcameventtypetxt = (TextView) itemView.findViewById(R.id.eventtype);
            outloadimg = (ProgressBar)itemView.findViewById(R.id.loadeventimg);
            outcard = (CardView) itemView.findViewById(R.id.eventcard);
            outloadimg.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
            outcameventimg = (ImageView) itemView.findViewById(R.id.eventimg);
        }

        public void seteventname(String eventname) {
            outcameventnametxt.setText(eventname);
        }

        public void seteventtype(String eventtype) {
            outcameventtypetxt.setText(eventtype);
        }

        public void setEventimg(String eventimg,Context cx) {

            Picasso.with(cx).load(eventimg).into(outcameventimg, new Callback() {
                @Override
                public void onSuccess() {
                    outloadimg.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    outcameventimg.setImageResource(R.drawable.brokenpic);
                    outloadimg.setVisibility(View.GONE);
                }
            });

        }

        public void setAnimation()
        {
            YoYo.with(Techniques.Landing).playOn(outcard);
        }

        public void setlistner(final Context contxt)
        {
            outcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent eventdata = new Intent(contxt,Eventinfo.class);
                    eventdata.putExtra("event_topic",outcameventnametxt.getText().toString());
                    eventdata.putExtra("event_type","Outcampus");
                    view.getContext().startActivity(eventdata);
                }
            });
        }
    }

    public class getoutcameventdata extends AsyncTask
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            outcamevntadapter= new outcampuseventsadapter(outcampusevents.class,R.layout.listlayout,outcampuseventviewholder.class,outcamdatabase);
            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException e) {

                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            recyclerView.setAdapter(outcamevntadapter);
            refreshLayout.setRefreshing(false);
        }
    }
}
