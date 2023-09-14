package com.dreamhunterztech.cgcstudentportal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.michael.easydialog.EasyDialog;
import com.optimus.edittextfield.EditTextField;

import java.util.ArrayList;

/**
 * Created by Dreamer on 25-05-2017.
 */

public class Browser extends AppCompatActivity {

    WebView webView;
    ProgressBar webprogress;
    FrameLayout webviewframe;
    ImageView imageView;
    Toolbar toolbar;
    EditTextField webinputs;
    ViewFlipper viewFlipper;
    Boolean reloadstate=true;
    browserspin webinputselectionmode;
    SpaceNavigationView spaceNavigationView;
    private GestureDetector mGestureDetector;
    String url ;
    ArrayList<Integer> pics;

    Animation animation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browserlayout);
        url = getIntent().getExtras().getString("url");
        toolbar = (Toolbar) findViewById(R.id.topview);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        toolbar.setSubtitleTextColor(Color.parseColor("#FFFFFFFF"));
        toolbar.setNavigationIcon(R.drawable.close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final Spinner webinputselection = (Spinner) findViewById(R.id.webselection);
        pics=new ArrayList();
        pics.add(R.drawable.googleicon);
        pics.add(R.drawable.webicon);
        pics.add(R.drawable.wikipedia);
        pics.add(R.drawable.bing);
        pics.add(R.drawable.youtube);
        pics.add(R.drawable.yahoo);

        webinputselectionmode = new browserspin(Browser.this,pics);

        webinputselection.setAdapter(webinputselectionmode);
        viewFlipper = (ViewFlipper) findViewById(R.id.flip);

        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(this, customGestureDetector);

        imageView = (ImageView) findViewById(R.id.webimg);
        webinputs = (EditTextField) findViewById(R.id.webinput);
        webinputs.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (webinputs.getText().toString().trim().length() > 0)
                    {
                        if(webinputselection.getSelectedItemPosition()==1) {
                            if (webinputs.getText().toString().contains("https://") || webinputs.getText().toString().contains("https://www."))
                            webView.loadUrl(webinputs.getText().toString().trim());
                            else
                            webView.loadUrl("https://www." + webinputs.getText().toString().trim());
                        }

                        else
                        webView.loadUrl("https://www.google.com/search?q="+webinputs.getText().toString().trim());
                        webinputs.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(webinputs.getWindowToken(), 0);
                    }

                    viewFlipper.showPrevious();

                }
                return true;
            }
        });

        webView = (WebView) findViewById(R.id.mainpageweb);
        webprogress = (ProgressBar) findViewById(R.id.mainpagewebprogress);
        webprogress.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        webviewframe = (FrameLayout) findViewById(R.id.webviewframe);
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "download");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);

            }
        });
        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                webviewframe.addView(view);
                webviewframe.setVisibility(View.VISIBLE);
                webprogress.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                viewFlipper.setVisibility(View.GONE);
                enableImmersiveMode();
                getSupportActionBar().hide();
            }

            @Override
            public void onHideCustomView() {
                if (webviewframe == null)
                    return;
                // Hide the custom view.
                webviewframe.setVisibility(View.GONE);
                webprogress.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                viewFlipper.setVisibility(View.VISIBLE);
                getSupportActionBar().show();
                disableImmersiveMode();
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                imageView.setImageBitmap(icon);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                webprogress.setProgress(newProgress);
                if (newProgress == 100)
                {
                    webprogress.setVisibility(View.GONE);
                    spaceNavigationView.changeItemIconAtPosition(0,R.drawable.refreshicon);
                    reloadstate=true;

                }
                else {
                    webprogress.setVisibility(View.VISIBLE);
                    spaceNavigationView.changeItemIconAtPosition(0,R.drawable.webclose);
                }

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbar.setTitle(title);
            }

        });

        webView.setWebViewClient(new WebViewClient()
                                 {
                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view,String url) {
                                         view.loadUrl(url);
                                        toolbar.setSubtitle(webView.getUrl());
                                         return true;
                                     }

                                     @Override
                                     public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                         super.onReceivedError(view, request, error);

                                     }

                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                         return super.shouldOverrideUrlLoading(view, request);
                                     }
                                 }

        );
        webView.canGoBack();
        webView.canGoForward();
        webView.setSoundEffectsEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().supportZoom();
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });


        webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        webView.loadUrl(url);


        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.showIconOnly();
        spaceNavigationView.setCentreButtonIcon(R.drawable.home);
        spaceNavigationView.setCentreButtonColor(ContextCompat.getColor(this,android.R.color.holo_red_dark));
        spaceNavigationView.addSpaceItem(new SpaceItem(null,R.drawable.refreshicon));
        spaceNavigationView.setInActiveCentreButtonIconColor(Color.BLACK);
        spaceNavigationView.addSpaceItem(new SpaceItem(null, R.drawable.backarrow));
        spaceNavigationView.addSpaceItem(new SpaceItem(null, R.drawable.forwardarrow));
        spaceNavigationView.addSpaceItem(new SpaceItem(null, R.drawable.search));

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                webView.loadUrl("http://www.google.co.in/");
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex==0) {
                    if(reloadstate==true) {
                        webView.reload();
                        spaceNavigationView.changeItemIconAtPosition(0,R.drawable.webclose);
                    }
                    else {
                        webView.stopLoading();
                        reloadstate = false;
                        spaceNavigationView.changeItemIconAtPosition(0,R.drawable.refreshicon);
                    }
                }

                if (itemIndex==5)
                {
                    webView.stopLoading();
                }

                if (itemIndex==1)
                {
                    webView.goBack();
                }

                if (itemIndex==2)
                {
                    webView.goForward();
                }

                if (itemIndex==3)
                {
                    viewFlipper.showNext();
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                if (itemIndex==0) {
                    if(reloadstate==true) {
                        webView.reload();
                    }
                    else {
                        webView.stopLoading();
                        reloadstate = false;
                    }
                }

                if (itemIndex==1)
                {
                    webView.goBack();
                }

                if (itemIndex==2)
                {
                    webView.goForward();
                }

                if (itemIndex==3)
                {
                    viewFlipper.showNext();
                }
            }
        });
        
        
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (e1.getX() < e2.getX()) {
                viewFlipper.showPrevious();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

    private void enableImmersiveMode() {
        // True immersive mode is available only for android API 19 and above,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        }
    }


    private void disableImmersiveMode() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }

    }


    public class browserspin extends BaseAdapter

    {

        LayoutInflater inflater;
        Context context;
        ArrayList<Integer> img1;
        Hold holder;
        public browserspin(Context context,  ArrayList<Integer> pics) {
            context = context;
            img1 = pics;
            inflater = inflater.from(context);
        }

        @Override
        public int getCount() {
            return img1.size();
        }

        @Override
        public Object getItem(int i) {
            return img1.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if(view==null)
            {
                view = inflater.inflate(R.layout.browsetinputmodelayout,viewGroup,false);
                holder=new Hold(view);
                view.setTag(holder);
            }
            else
            {
                holder= (Hold) view.getTag();
            }
            holder.imgs.setImageResource(img1.get(i));
            return view;
        }

        class Hold
        {
            ImageView imgs;
            Hold(View view)
            {
                imgs = (ImageView) view.findViewById(R.id.selectionpic);
            }

        }
    }

}
