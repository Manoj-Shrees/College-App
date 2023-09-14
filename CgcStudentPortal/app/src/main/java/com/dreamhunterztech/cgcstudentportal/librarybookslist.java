package com.dreamhunterztech.cgcstudentportal;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Dreamer on 29-10-2017.
 */

public class librarybookslist extends Fragment {
    String strtext;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    bookdataadapter adapter;
    Firebase bookdatabase;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layoutlist,container,false);
        recyclerView = view.findViewById(R.id.recycler_view);
        refreshLayout = view.findViewById(R.id.eventrefresh);
        strtext = this.getArguments().getString("bookurl");
        bookdatabase = new Firebase("https://cgc-database.firebaseio.com/Library/libfilerecord/"+strtext);
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
                new getbooksdata().execute();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new getbooksdata().execute();
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



    public class  bookdataadapter extends FirebaseRecyclerAdapter<booksdata,bookdataviewholder> {


        public bookdataadapter(Class<booksdata> modelClass, int modelLayout, Class<bookdataviewholder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(bookdataviewholder bookdataviewholder, booksdata booksdata, int i) {

            Picasso.with(getContext()).load(booksdata.getBookcoverpic()).fit().into(bookdataviewholder.bookimg);
            bookdataviewholder.bookname.setText(booksdata.getBookname());
            bookdataviewholder.bookauther.setText(booksdata.getBookauther());
            bookdataviewholder.setAnimation();
            bookdataviewholder.setlistner(getContext(),strtext);

        }
    }


    public static class bookdataviewholder extends RecyclerView.ViewHolder {
        View view;
        TextView bookname, bookauther;
        ImageView bookimg;
        CardView card;


        public bookdataviewholder(View itemView) {
            super(itemView);
            view = itemView;
            bookname = (TextView) itemView.findViewById(R.id.bookname);
            bookauther = (TextView) itemView.findViewById(R.id.bookauther);
            bookimg = itemView.findViewById(R.id.bookcover);
            card = (CardView) itemView.findViewById(R.id.bookcard);
        }

        public void setBookname(TextView bookname) {
            this.bookname = bookname;
        }

        public void setBookauther(TextView bookauther) {
            this.bookauther = bookauther;
        }

        public void setBookimg(ImageView bookimg) {
            this.bookimg = bookimg;
        }

        public void setAnimation()
        {
            YoYo.with(Techniques.FadeIn).playOn(card);
        }

        public void setlistner(final Context contxt, final String data)
        {

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent strt = new Intent(view.getContext(),libbookdetailactivity.class);
                    strt.putExtra("bookaddrs",data+"/"+bookname.getText().toString().trim());
                    view.getContext().startActivity(strt);
                }
            });
        }
    }

    public class getbooksdata extends AsyncTask
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            adapter= new bookdataadapter(booksdata.class,R.layout.libbooklayout,bookdataviewholder.class,bookdatabase);
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
            recyclerView.setAdapter(adapter);
            refreshLayout.setRefreshing(false);

        }
    }

}
