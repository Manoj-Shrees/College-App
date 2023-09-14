package com.dreamhunterztech.cgcstudentportal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Dreamer on 21-05-2017.
 */

public class libmainpage extends Fragment {
    FastScrollRecyclerView librecyclerview;
    Libbookdatadadapter libbookdatadadapter;
    Firebase mdatabase;
    SwipeRefreshLayout librefresh;
    Query query;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.libmainlayout, container, false);
        mdatabase = new Firebase(getActivity().getString(R.string.dburl)+"Library/topbook");
        query = mdatabase.orderByPriority();
        librecyclerview = (FastScrollRecyclerView) view.findViewById(R.id.librecyclerview);
        librefresh = (SwipeRefreshLayout) view.findViewById(R.id.librefresh);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        librecyclerview.setLayoutManager(mLayoutManager);
        librecyclerview.setItemAnimator(new DefaultItemAnimator());
        YoYo.with(Techniques.Landing).playOn(librecyclerview);
        librefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        librefresh.setSoundEffectsEnabled(true);
        librefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new gettinglibdata().execute();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        librefresh.setRefreshing(true);
        new gettinglibdata().execute();
    }


    public class Libbookdatadadapter extends FirebaseRecyclerAdapter<libmainpagebookdata, Libbookdataviewholder> {


        public Libbookdatadadapter(Class<libmainpagebookdata> modelClass, int modelLayout, Class<Libbookdataviewholder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(final Libbookdataviewholder libbookdataviewholder, libmainpagebookdata libmainpagebookdata, int i) {
            librecyclerview.scrollToPosition(i);
            libbookdataviewholder.setcontext(getActivity().getApplicationContext());

            libbookdataviewholder.setBooktypetxt(libmainpagebookdata.getBooktype());

            libbookdataviewholder.setBook1topictxt(libmainpagebookdata.getBook1topic());
            libbookdataviewholder.setbook1authertxt(libmainpagebookdata.getBook1auther());
            libbookdataviewholder.setBook1coverimg(libmainpagebookdata.getBook1coverpicurl());

            libbookdataviewholder.setBook2topictxt(libmainpagebookdata.getBook2topic());
            libbookdataviewholder.setbook2authertxt(libmainpagebookdata.getBook2auther());
            libbookdataviewholder.setBook2coverimg(libmainpagebookdata.getBook2coverpicurl());


            libbookdataviewholder.setBook3topictxt(libmainpagebookdata.getBook3topic());
            libbookdataviewholder.setbook3authertxt(libmainpagebookdata.getBook3auther());
            libbookdataviewholder.setBook3coverimg(libmainpagebookdata.getBook3coverpicurl());


            libbookdataviewholder.setBook4topictxt(libmainpagebookdata.getBook4topic());
            libbookdataviewholder.setbook4authertxt(libmainpagebookdata.getBook4auther());
            libbookdataviewholder.setBook4coverimg(libmainpagebookdata.getBook4coverpicurl());

            libbookdataviewholder.setBook5topictxt(libmainpagebookdata.getBook5topic());
            libbookdataviewholder.setbook5authertxt(libmainpagebookdata.getBook5auther());
            libbookdataviewholder.setBook5coverimg(libmainpagebookdata.getBook5coverpicurl());

            libbookdataviewholder.libmorebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("bookurl",libbookdataviewholder.booktypetxt.getText().toString());
                    librarybookslist libindbooks = new librarybookslist();
                    libindbooks.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out);
                    transaction.replace(R.id.libframe,libindbooks);
                    transaction.commit();
                }
            });

            libbookdataviewholder.setAnimation();


        }

    }


    public static class Libbookdataviewholder extends RecyclerView.ViewHolder {
        View view;
        Context context;
        TextView booktypetxt, book1topictxt, book1authertxt,book2topictxt, book2authertxt,book3topictxt, book3authertxt,book4topictxt, book4authertxt,book5topictxt, book5authertxt;
        ImageView book1coverimg,book2coverimg,book3coverimg,book4coverimg,book5coverimg;
        CardView book1card ,book2card , book3card,book4card,book5card;
        TextView libmorebtn;
        LinearLayout layout;
        Intent strt ;

        public Libbookdataviewholder(View itemView) {
            super(itemView);
            view = itemView;
            libmorebtn = (TextView) view.findViewById(R.id.morebtn);
            layout = (LinearLayout) view.findViewById(R.id.libmainpagelinlay);

            booktypetxt = (TextView) view.findViewById(R.id.libtopic);
            book1topictxt = (TextView) view.findViewById(R.id.book1name);
            book1authertxt = (TextView) view.findViewById(R.id.book1auther);
            book1coverimg = (ImageView) view.findViewById(R.id.book1cover);

            book2topictxt = (TextView) view.findViewById(R.id.book2name);
            book2authertxt = (TextView) view.findViewById(R.id.book2auther);
            book2coverimg = (ImageView) view.findViewById(R.id.book2cover);

            book3topictxt = (TextView) view.findViewById(R.id.book3name);
            book3authertxt = (TextView) view.findViewById(R.id.book3auther);
            book3coverimg = (ImageView) view.findViewById(R.id.book3cover);


            book4topictxt = (TextView) view.findViewById(R.id.book4name);
            book4authertxt = (TextView) view.findViewById(R.id.book4auther);
            book4coverimg = (ImageView) view.findViewById(R.id.book4cover);


            book5topictxt = (TextView) view.findViewById(R.id.book5name);
            book5authertxt = (TextView) view.findViewById(R.id.book5auther);
            book5coverimg = (ImageView) view.findViewById(R.id.book5cover);

            book1card = (CardView) view.findViewById(R.id.book1);
            book2card = (CardView) view.findViewById(R.id.book2);
            book3card = (CardView) view.findViewById(R.id.book3);
            book4card = (CardView) view.findViewById(R.id.book4);
            book5card = (CardView) view.findViewById(R.id.book5);

            libmorebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            book1card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(book1topictxt.getText().equals("N/A"))
                    {
                        Toast.makeText(v.getContext(),"Book is unavialable",Toast.LENGTH_SHORT).show();
                    }

                    else {
                        openbookdetail(booktypetxt.getText().toString().trim() + "/" + book1topictxt.getText().toString().trim());
                    }
                }
            });

            book2card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(book2topictxt.getText().equals("N/A"))
                    {
                        Toast.makeText(v.getContext(),"Book is unavialable",Toast.LENGTH_SHORT).show();
                    }

                    else {
                        openbookdetail(booktypetxt.getText().toString().trim() + "/" + book1topictxt.getText().toString().trim());
                    }
                }
            });

            book3card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(book3topictxt.getText().equals("N/A"))
                    {
                        Toast.makeText(v.getContext(),"Book is unavialable",Toast.LENGTH_SHORT).show();
                    }

                    else {
                        openbookdetail(booktypetxt.getText().toString().trim() + "/" + book1topictxt.getText().toString().trim());
                    }
                }
            });

            book4card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(book4topictxt.getText().equals("N/A"))
                    {
                        Toast.makeText(v.getContext(),"Book is unavialable",Toast.LENGTH_SHORT).show();
                    }

                    else {
                        openbookdetail(booktypetxt.getText().toString().trim() + "/" + book1topictxt.getText().toString().trim());
                    }
                }
            });

            book5card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(book5topictxt.getText().equals("N/A"))
                    {
                        Toast.makeText(v.getContext(),"Book is unavialable",Toast.LENGTH_SHORT).show();
                    }

                    else {
                        openbookdetail(booktypetxt.getText().toString().trim() + "/" + book1topictxt.getText().toString().trim());
                    }
                }
            });




        }

        public void openbookdetail(String addrs)
        {
            strt = new Intent(view.getContext(), libbookdetailactivity.class);
            strt.putExtra("bookaddrs",addrs);
            view.getContext().startActivity(strt);
        }

        public void setcontext(Context context)
        {
            this.context = context;
        }



        public void setBooktypetxt(String booktype) {
            booktypetxt.setText(booktype);
        }

        public void setBook1coverimg(String book1cover) {
            Picasso.with(context).load(book1cover).fit().into(book1coverimg, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    book1coverimg.setImageResource(R.drawable.brokenpic);
                }
            });

        }

        public void setBook1topictxt(String book1topic) {
            book1topictxt.setText(book1topic);
        }

        public void setbook1authertxt(String book1auther) {
            book1authertxt.setText("- "+book1auther);
        }

        public void setBook2coverimg(String book2cover) {
            Picasso.with(context).load(book2cover).fit().into(book2coverimg, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    book2coverimg.setImageResource(R.drawable.brokenpic);
                }
            });

        }

        public void setBook2topictxt(String book2topic) {
            book2topictxt.setText(book2topic);
        }

        public void setbook2authertxt(String book2auther) {
            book2authertxt.setText("- "+book2auther);
        }


        public void setBook3coverimg(String book3cover) {
            Picasso.with(context).load(book3cover).fit().into(book3coverimg, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    book3coverimg.setImageResource(R.drawable.brokenpic);
                }
            });

        }

        public void setBook3topictxt(String book3topic) {
            book3topictxt.setText(book3topic);
        }

        public void setbook3authertxt(String book3auther) {
            book3authertxt.setText("- "+book3auther);
        }



        public void setBook4coverimg(String book4cover) {
            Picasso.with(context).load(book4cover).fit().into(book4coverimg, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    book4coverimg.setImageResource(R.drawable.brokenpic);
                }
            });

        }

        public void setBook4topictxt(String book4topic) {
            book4topictxt.setText(book4topic);
        }

        public void setbook4authertxt(String book4auther) {
            book4authertxt.setText("- "+book4auther);
        }



        public void setBook5coverimg(String book5cover) {
            Picasso.with(context).load(book5cover).fit().into(book5coverimg, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    book5coverimg.setImageResource(R.drawable.brokenpic);
                }
            });

        }

        public void setBook5topictxt(String book5topic) {
            book5topictxt.setText(book5topic);
        }

        public void setbook5authertxt(String book5auther) {
            book5authertxt.setText("- "+book5auther);
        }

        public void setAnimation()
        {
            YoYo.with(Techniques.FadeIn).playOn(layout);
        }


    }

    public class gettinglibdata extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected Object doInBackground(Object[] params) {
            libbookdatadadapter = new Libbookdatadadapter(libmainpagebookdata.class, R.layout.libmainpagelayout, Libbookdataviewholder.class,query);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {

                e.printStackTrace();

            }
            libbookdatadadapter.notifyDataSetChanged();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            librefresh.setRefreshing(false);
            librecyclerview.setAdapter(libbookdatadadapter);
        }

    }



}
