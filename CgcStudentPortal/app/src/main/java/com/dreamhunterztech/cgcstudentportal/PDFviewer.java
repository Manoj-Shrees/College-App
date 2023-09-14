package com.dreamhunterztech.cgcstudentportal;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class PDFviewer extends Activity {
    PDFView pdfView;
    ProgressBar loadingpdf;
    String url = "";
    Toolbar toolbar;
    String datafrom="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfviewer);
        url = getIntent().getExtras().get("pdf_url").toString();
        datafrom = getIntent().getExtras().get("datatype").toString();
        loadingpdf = (ProgressBar) findViewById(R.id.loadingpdf);
        pdfView = findViewById(R.id.loadpdf);
        pdfView.enableSwipe(true);
        pdfView.enableDoubletap(true);
        pdfView.enableAnnotationRendering(false);
        pdfView.enableAntialiasing(true);


    }


    @Override
    protected void onStart() {
        super.onStart();
        if(datafrom.equals("file"))
        {
            try {
                datafromfile(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        else {
            datafromweb(url);
        }
    }

    private void datafromfile(String url) throws MalformedURLException, URISyntaxException {
        File file = new File(url);
        pdfView.fromFile(file).onLoad(new OnLoadCompleteListener() {
            @Override
            public void loadComplete(int nbPages) {
                loadingpdf.setVisibility(View.GONE);
            }
        }).onError(new OnErrorListener() {
            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(),"Error to load data Please try Again\nThank you!!!",Toast.LENGTH_SHORT).show();
            }
        }).scrollHandle(new DefaultScrollHandle(PDFviewer.this)).load();
    }


    private void datafromweb(String url)
    {
        new retrivepdf().execute(url);
    }

    class retrivepdf extends AsyncTask<String,Void,InputStream>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),"Please wait .......",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try
            {
                URL url = new URL(strings[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == 200)
                {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }

            catch (IOException e)
            {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream stream) {
            pdfView.fromStream(stream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    loadingpdf.setVisibility(View.GONE);
                }
            }).onError(new OnErrorListener() {
                @Override
                public void onError(Throwable t) {
                    Toast.makeText(getApplicationContext(),"Error to load data Please try Again\nThank you!!!",Toast.LENGTH_SHORT).show();
                }
            }).scrollHandle(new DefaultScrollHandle(PDFviewer.this)).load();
        }
    }



}
