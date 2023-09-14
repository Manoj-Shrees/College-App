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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Dreamer on 28-07-2017.
 */

public class Homenews extends Fragment {
    int counter=0;
    String News_url [] ={
            " https://newsapi.org/v1/articles?source=the-times-of-india&sortBy=latest&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=bbc-news&sortBy=top&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=bbc-sport&sortBy=top&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=financial-times&sortBy=latest&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ,"  https://newsapi.org/v1/articles?source=hacker-news&sortBy=latest&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=techradar&sortBy=top&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=the-wall-street-journal&sortBy=top&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=reddit-r-all&sortBy=top&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=google-news&sortBy=top&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=new-scientist&sortBy=top&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=espn&sortBy=top&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ,"  https://newsapi.org/v1/articles?source=mtv-news&sortBy=latest&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=entertainment-weekly&sortBy=top&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=usa-today&sortBy=latest&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ,"  https://newsapi.org/v1/articles?source=wired-de&sortBy=latest&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ,"  https://newsapi.org/v1/articles?source=national-geographic&sortBy=top&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=recode&sortBy=top&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=reuters&sortBy=latest&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ,"  https://newsapi.org/v1/articles?source=the-economist&sortBy=latest&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"
            ," https://newsapi.org/v1/articles?source=cnbc&sortBy=top&apiKey=7207873a43e14f2e8ecd7c8fe637fcd5"



    };
    ArrayList<String> title,description,weburl,imgurl,date;
    RecyclerView recyclerView;
    newsAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_noti,container,false);
        title = new ArrayList<>();
        description = new ArrayList<>();
        weburl = new ArrayList<>();
        imgurl = new ArrayList<>();
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.notirefresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.notilists);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setSoundEffectsEnabled(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getnewsdata().execute(News_url[counter]);
                counter++;
                if (counter==News_url.length)
                {
                    counter=0;
                }
            }
        });
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
       new  getnewsdata().execute(News_url[counter]);
        counter++;
        if (counter==News_url.length)
        {
            counter=0;
        }
    }

    private class getnewsdata extends AsyncTask<String,String,String>
    {

        String result="";
        URL url;
        HttpsURLConnection urlConnection=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            title.clear();
            description.clear();
            weburl.clear();
            imgurl.clear();
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... urls) {
            try
            {
               url = new URL(urls[0]);
               urlConnection = (HttpsURLConnection) url.openConnection();
               String response = streamtostring(urlConnection.getInputStream());
               parsejson(response);
               return  result;
            }

            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter = new newsAdapter(title,description,weburl,imgurl,getActivity());
            recyclerView.setAdapter(adapter);
            refreshLayout.setRefreshing(false);
        }
    }

    String streamtostring(InputStream stream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String data,result="";

        while((data = bufferedReader.readLine()) != null)
        {
            result+=data;
        }

        if(null !=stream)
        {
            stream.close();
        }

        return  result;
    }

private void parsejson(String result)
{
    JSONObject response= null;

    try {
        response = new JSONObject(result);
        JSONArray articles = response.optJSONArray("articles");

        for (int i=0; i<articles.length(); i++)
        {
            JSONObject arcticle = articles.optJSONObject(i);
            title.add(arcticle.optString("title"));
            description.add(arcticle.optString("description"));
            weburl.add(arcticle.optString("url"));
            imgurl.add(arcticle.optString("urlToImage"));
        }
    }

    catch (Exception e)
    {
        e.printStackTrace();
    }


}


    public class newsAdapter extends RecyclerView.Adapter<newsAdapter.MyViewHolder> {

        private ArrayList<String> newsdatatitle,newsdatadesc,newsdataweburl,newsdataimgurl,newsdatadate;
        Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            ExpandableTextView  description;
            ImageView newsimg;
            CardView newscard;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.newstitle);
                description = (ExpandableTextView) view.findViewById(R.id.newsdesc);
                newsimg = (ImageView) view.findViewById(R.id.newsimg);
                newscard = (CardView) view.findViewById(R.id.newscard);

            }
        }

        public newsAdapter(ArrayList<String> newsdatatitle, ArrayList<String> newsdatadesc, ArrayList<String> newsdataweburl, ArrayList<String> newsdataimgurl, Context context) {
            this.newsdatatitle = newsdatatitle;
            this.newsdatadesc = newsdatadesc;
            this.newsdataweburl = newsdataweburl;
            this.newsdataimgurl = newsdataimgurl;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.newslayout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.title.setText(newsdatatitle.get(position));
            holder.description.setText(newsdatadesc.get(position));

            YoYo.with(Techniques.FadeIn).playOn(holder.newscard);

            try {
                Picasso.with(context).load(newsdataimgurl.get(position)).fit().into(holder.newsimg);
            }

            catch (Exception e)
            {
                e.printStackTrace();
            }

            holder.newsimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        if (newsdataweburl.get(position).equals(null)) {
                            Toast.makeText(getContext(), "Url not found", Toast.LENGTH_SHORT).show();
                        } else {

                            startActivity(new Intent(getActivity(), Browser.class).putExtra("url", newsdataweburl.get(position).toString()));
                        }
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(getContext(), "error to open browser", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return newsdatatitle.size();
        }
    }

}
