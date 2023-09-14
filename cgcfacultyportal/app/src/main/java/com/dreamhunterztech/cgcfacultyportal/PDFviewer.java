package com.dreamhunterztech.cgcfacultyportal;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class PDFviewer extends Activity {
    WebView pdfView;
    ProgressBar loadingpdf;
   String url = "";
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfviewer);
        loadingpdf = (ProgressBar) findViewById(R.id.loadingpdf);
        pdfView = (WebView) findViewById(R.id.loadpdf);
        url = getIntent().getExtras().get("pdf_url").toString();




        pdfView.setWebChromeClient(new WebChromeClient()
        {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if(newProgress ==  100)
                {
                    loadingpdf.setVisibility(View.GONE);
                }
            }
        });


        pdfView.setWebViewClient(new WebViewClient());
        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.getSettings().supportZoom();
        pdfView.getSettings().setPluginState(WebSettings.PluginState.ON);
        pdfView.getSettings().setLoadWithOverviewMode(true);
        pdfView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        pdfView.getSettings().setUseWideViewPort(true);
        pdfView.getSettings().setBuiltInZoomControls(true);
        pdfView.getSettings().setDisplayZoomControls(false);
        pdfView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        pdfView.setScrollbarFadingEnabled(false);
        pdfView.getSettings().setDefaultTextEncodingName("utf-8");
        pdfView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        pdfView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pdfView.destroy();
    }



}
