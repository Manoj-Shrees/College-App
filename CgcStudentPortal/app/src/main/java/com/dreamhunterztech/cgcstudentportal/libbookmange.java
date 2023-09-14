package com.dreamhunterztech.cgcstudentportal;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;




public class libbookmange extends AppCompatActivity
{
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Spinner selecttype;
    RecyclerView booksviews;
    ArrayAdapter typelistadap;
    SwipeRefreshLayout refreshLayout;
    libbookadapter adapter;
    Firebase database;
    Query query;
    private String [] booktypelist = {"Engineering ( ME )","Engineering ( ECE )","Engineering ( CSE and IT )","Pharmacy ( B-pharma )","Pharmacy ( M-pharma )","Hotel Management ( B-HMCT )","Hotel Management ( B-ATHM )","Biotechnology ( Bsc-BioTech )","Biotechnology (Msc-BioTech )","Management ( BBA )","Management ( MBA )","Computer Application ( BCA )","Computer Application ( MCA )","Education ( B-ED )","Sample & Previous year Question Papers"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libbookmanagelayout);
        database = new Firebase(getApplicationContext().getString(R.string.dburl)+"Library/libfilerecord/");
        Toolbar toolbar = (Toolbar) findViewById(R.id.libmangementtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage Books");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selecttype  = (Spinner) findViewById(R.id.bookmangementspin);
        typelistadap = new ArrayAdapter(libbookmange.this, android.R.layout.simple_spinner_item,booktypelist);
        selecttype.setAdapter(typelistadap);

        selecttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                checkdata();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        booksviews  = findViewById(R.id.libmanagebooklist);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        booksviews.setLayoutManager(mLayoutManager);
        booksviews.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        booksviews.setItemAnimator(new DefaultItemAnimator());
        refreshLayout = findViewById(R.id.libmanagementswipe);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setSoundEffectsEnabled(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkdata();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkdata();
    }

    private void checkdata()
    {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(selecttype.getSelectedItem().toString()).exists())
                {
                    new getlibbmangebookdata().execute();
                }

                else
                {
                    query=null;
                    new getlibbmangebookdata().execute();
                    Toast.makeText(getApplicationContext(),"Record Not Found or not Uploaded Yet",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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

    public class  libbookadapter extends FirebaseRecyclerAdapter<libmanagedata,libbookviewholder> {


        public libbookadapter(Class<libmanagedata> modelClass, int modelLayout, Class<libbookviewholder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(final libbookviewholder libbookviewholder, libmanagedata libmanagedata, int i) {
            libbookviewholder.setbookname(libmanagedata.getBookname());
            libbookviewholder.setBookauther(libmanagedata.getBookauther());
            libbookviewholder.setbookimg(libmanagedata.getBookcoverpic(),getApplicationContext());
            libbookviewholder.setAnimation();
            libbookviewholder.bookedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent  strt = new Intent(getApplicationContext(), libbookdataedit.class);
                    strt.putExtra("selectionaddr",selecttype.getSelectedItem().toString());
                    strt.putExtra("booknameaddr",libbookviewholder.bookname.getText().toString());
                    startActivity(strt);
                }
            });


        }
    }


    public static class libbookviewholder extends RecyclerView.ViewHolder {
        TextView bookname,bookauther;
        ImageView bookimage;
        CardView card;
        ImageButton  bookedit;
        ImageView deletebook;

        public libbookviewholder(final View itemView) {
            super(itemView);
                bookname = (TextView) itemView.findViewById(R.id.managebookname);
                bookname.setSelected(true);
                bookauther = (TextView) itemView.findViewById(R.id.managebookauther);
                card = (CardView) itemView.findViewById(R.id.libmanagbook);
                bookimage = (ImageView) itemView.findViewById(R.id.managebookcover);
                bookedit = (ImageButton) itemView.findViewById(R.id.libbookeditbtn);
                deletebook = (ImageView) itemView.findViewById(R.id.libmanagebookclose);



            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   card.setEnabled(false);
                    visiblecount();
                }
            });


            deletebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder deletefiledialog= new AlertDialog.Builder(view.getContext());
                    deletefiledialog.setTitle(bookname.getText().toString());
                    deletefiledialog.setMessage("Do you want to Delete this Book ?");
                    deletefiledialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    deletefiledialog.setNeutralButton("Cancel",null);
                    deletefiledialog.show();

                }
            });
        }


        private void visiblecount() {
            CountDownTimer counter = new CountDownTimer(3000, 100) {
                @Override
                public void onTick(long l) {
                   bookedit.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {
                    bookedit.setVisibility(View.GONE);
                    card.setEnabled(true);
                }
            }.start();
        }


        public void setbookname(String eventname) {
            bookname.setText(eventname);
        }

        public void setBookauther(String eventtype) {
            bookauther.setText(eventtype);
        }

        public void setbookimg(String eventimg,Context cx) {

            Picasso.with(cx).load(eventimg).fit().into(bookimage);

        }

        public void setAnimation()
        {
            YoYo.with(Techniques.Landing).playOn(card);
        }

    }

    public class getlibbmangebookdata extends AsyncTask
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshLayout.setRefreshing(true);
            query = database.child(selecttype.getSelectedItem().toString()).orderByChild("bookuploader").equalTo("CEC/BTECH-IT/7C/1406359");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            adapter = new libbookadapter(libmanagedata.class,R.layout.bookmanage,libbookviewholder.class,query);
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
            booksviews.setAdapter(adapter);
            refreshLayout.setRefreshing(false);

        }
    }


}