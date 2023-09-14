package com.dreamhunterztech.cgcfacultyportal;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.michael.easydialog.EasyDialog;
import com.onesignal.OneSignal;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import r21nomi.com.glrippleview.GLRippleView;

import static android.app.TaskStackBuilder.create;
import static android.graphics.Typeface.NORMAL;


public class MainFrame extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final static String sample_url = "https://firebasestorage.googleapis.com/v0/b/test-e713f.appspot.com/o/images%2Fcropped2068367751.jpg?alt=media&token=f91b1f63-107a-4274-a280-57d33174661a";
    private final static int BIG_PICTURE_STYLE = 0x02;
    Boolean chkconnection = false;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    RelativeLayout messsagelayout;
    int onbackpresscount = 0,taskcounter=0;
    private String updialogcode = null;
    FragmentManager manager;
    private SharedPreferences sp;
    private   SharedPreferences.Editor Ed;
    ImageView feedimg , detainimg , dateimg , timeimg;
    LayoutInflater inflater;
    private InterstitialAd interstitial;
    Button imageview;
    GLRippleView rippleBackground;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    AdView mAdView;
    Uri imgpath;
    private String addrs,branch,subaddr;
    String [] sems ={"Sem","1","2","3","4","5","6","7","8"};
    String [] Sections = {"Sec","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"};
    NotificationManager mNotificationManager;
    ActionBarDrawerToggle mDrawerToggle;
    MenuItem loaddailog;
    Dialog dailogpannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        SharedPreferences sp=getSharedPreferences("Login", 0);
        addrs = getIntent().getExtras().getString("addr");
        subaddr=addrs.substring(0,addrs.lastIndexOf("/"));
        branch = subaddr.substring(subaddr.lastIndexOf("/")+1,subaddr.length());
        OneSignal.sendTag("user_id",sp.getString("userid",""));
        OneSignal.sendTag("Branch",branch);
        OneSignal.sendTag("type","all");
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_home__frame);
        manager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        messsagelayout = (RelativeLayout) findViewById(R.id.eventlayout);
        mAdView = (AdView) findViewById(R.id.mainpageaddbanner);
        getSupportActionBar().setTitle("\t\t\t\tHome");
        toolbar.setLogo(R.drawable.menuhome);
        toolbar.setTitleTextColor(Color.WHITE);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        Menu menu  = navigationView.getMenu();
        MenuItem acad= menu.findItem(R.id.menuacad);
        SpannableString s1 = new SpannableString(acad.getTitle());
        s1.setSpan(new TextAppearanceSpan(this, R.style.textmenuitemtitle), 0, s1.length(), 0);
        acad.setTitle(s1);
        MenuItem other= menu.findItem(R.id.menuother);
        SpannableString s2 = new SpannableString(other.getTitle());
        s2.setSpan(new TextAppearanceSpan(this, R.style.textmenuitemtitle), 0, s2.length(), 0);
        other.setTitle(s2);
        View headerview = navigationView.getHeaderView(0);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        imageview = (Button) headerview.findViewById(R.id.nagivationheaderimg);
        rippleBackground = (GLRippleView) headerview.findViewById(R.id.headercontent);
        rippleBackground.setRippleOffset(0.01f);
        rippleBackground.setCameraDistance(0.5f);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountDownTimer timer = new CountDownTimer(3000, 100) {
                    @Override
                    public void onTick(long l) {
                        rippleBackground.setVisibility(View.VISIBLE);
                        imageview.setEnabled(false);
                        imageview.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFinish() {
                        rippleBackground.setVisibility(View.INVISIBLE);
                        imageview.setEnabled(true);
                        imageview.setVisibility(View.VISIBLE);
                    }
                }.start();
            }
        });


        dailogpannel = new Dialog(MainFrame.this);
        dailogpannel.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailogpannel.getWindow().setWindowAnimations(R.style.animateddialog);
        dailogpannel.setCanceledOnTouchOutside(false);
        dailogpannel.setCancelable(false);
        dailogpannel.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, new Home());
        transaction.commit();
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerShadow(null, GravityCompat.START);
        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        countDownTimer.start();
        interstitial = new InterstitialAd(MainFrame.this);
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));


    }

    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }


    @Override
    public boolean onOptionsItemSelected
            (MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.ulpoadmenu)
        {
            View view =getLayoutInflater().inflate(R.layout.submenudailoglayout, null);
            CardView openuploaddata = view.findViewById(R.id.pdfupload);
            CardView openfilter = view.findViewById(R.id.pdffilter);

            openuploaddata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (updialogcode)
                    {
                        case "Datesheet" :
                            Intent strtds = new Intent(getApplicationContext(),PostDatesheet.class);
                            strtds.putExtra("addr",addrs);
                            startActivity(strtds);
                            break;

                        case "Detainlist" :
                            Intent strtdl = new Intent(getApplicationContext(),PostDetainlist.class);
                            strtdl.putExtra("addr",addrs);
                            startActivity(strtdl);
                            break;

                        case "Timetable" :
                            Intent strttb = new Intent(getApplicationContext(),PostTimetable.class);
                            strttb.putExtra("addr",addrs);
                            startActivity(strttb);
                            break;

                        case "Newsfeed" :
                            Intent strtnf = new Intent(getApplicationContext(),PostFeeds.class);
                            strtnf.putExtra("addr",addrs);
                            startActivity(strtnf);
                            break;

                    }
                }
            });

            openfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dailogpannel.setContentView(R.layout.setsemdata);
                    final ImageView closebtn = (ImageView) dailogpannel.findViewById(R.id.semfilterclose);
                    final Spinner semselecter = (Spinner) dailogpannel.findViewById(R.id.filtersem);
                    final Spinner secselecter = (Spinner) dailogpannel.findViewById(R.id.filtersection);
                    ArrayAdapter adapter1 = new ArrayAdapter(MainFrame.this,android.R.layout.simple_spinner_item,sems);
                    ArrayAdapter adapter2 = new ArrayAdapter(MainFrame.this,android.R.layout.simple_spinner_item,Sections);
                    semselecter.setAdapter(adapter1);
                    secselecter.setAdapter(adapter2);
                    Button setdata = (Button) dailogpannel.findViewById(R.id.filterbtn);
                    setdata.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Firebase datacheck = new Firebase("https://cgc-database.firebaseio.com/"+subaddr);
                            datacheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.child(semselecter.getSelectedItem().toString().trim()+secselecter.getSelectedItem().toString().trim()).exists())
                                    {
                                        sp=getSharedPreferences("Login", 0);
                                        Ed=sp.edit();
                                        Ed.putString("sem",semselecter.getSelectedItem().toString().trim()+secselecter.getSelectedItem().toString().trim());
                                        Ed.commit();
                                        Toast.makeText(getApplicationContext(),"data updated",Toast.LENGTH_SHORT).show();
                                        dailogpannel.dismiss();
                                        switch (updialogcode)
                                        {
                                            case "Datesheet" :
                                                FragmentTransaction transaction1 = manager.beginTransaction()
                                                        .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
                                                transaction1.replace(R.id.container, new Datesheet());
                                                toolbar.setLogo(R.drawable.menudatesheet);
                                                getSupportActionBar().setTitle("\t\t\t\tDatesheet");
                                                updialogcode = "Datesheet";
                                                loaddailog.setVisible(true);
                                                transaction1.commit();
                                                break;

                                            case "Detainlist" :
                                                FragmentTransaction transaction2 = manager.beginTransaction()
                                                        .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
                                                transaction2.replace(R.id.container, new Detaillist());
                                                toolbar.setLogo(R.drawable.menudetainlist);
                                                getSupportActionBar().setTitle("\t\t\t\tDetainlist");
                                                loaddailog.setVisible(true);
                                                updialogcode = "Detainlist";
                                                transaction2.commit();
                                                break;

                                            case "Timetable" :
                                                FragmentTransaction transaction3 = manager.beginTransaction()
                                                        .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
                                                transaction3.replace(R.id.container, new Timetable());
                                                toolbar.setLogo(R.drawable.menutimetable);
                                                getSupportActionBar().setTitle("\t\t\t\tTimetable");
                                                loaddailog.setVisible(true);
                                                updialogcode = "Timetable";
                                                transaction3.commit();
                                                break;

                                            case "Newsfeed" :
                                                FragmentTransaction transaction4 = manager.beginTransaction()
                                                        .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
                                                transaction4.replace(R.id.container, new NewsFeed());
                                                toolbar.setLogo(R.drawable.menunewsfeed);
                                                getSupportActionBar().setTitle("\t\t\t\tNewsfeed");
                                                loaddailog.setVisible(true);
                                                updialogcode = "Newsfeed";
                                                transaction4.commit();
                                                break;

                                        }
                                    }

                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"sem and sec doesnot exist or not applied for this",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                        }
                    });
                    closebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dailogpannel.dismiss();
                        }
                    });
                    dailogpannel.show();
                }
            });

            new EasyDialog(MainFrame.this)
                    .setLayout(view)
                    .setLocationByAttachedView(toolbar)
                    .setBackgroundColor(Color.BLACK)
                    .setGravity(EasyDialog.GRAVITY_BOTTOM)
                    .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
                    .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                    .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
                    .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                    .setTouchOutsideDismiss(true)
                    .setMatchParent(true)
                    .setMarginLeftAndRight(24, 24)
                    .show();

        }

        if (id == R.id.action_FeedBack) {
            Intent feedbackpage = new Intent(this, Feedback.class);
            feedbackpage.putExtra("addr",addrs);
            startActivity(feedbackpage);
        }
        if (id == R.id.action_About_us) {
            startActivity(new Intent(this, About_Us.class));
        }
        if (id == R.id.action_help) {
            startActivity(new Intent(this, Help.class));
        }

        if (id == R.id.action_closebanner) {
            mAdView.setVisibility(View.GONE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        loaddailog = menu.getItem(0);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.home) {
            FragmentTransaction transaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
            transaction.replace(R.id.container, new Home());
            toolbar.setLogo(R.drawable.menuhome);
            getSupportActionBar().setTitle("\t\t\t\tHome");
            updialogcode=null;
            transaction.commit();
            drawerLayout.closeDrawers();
            loaddailog.setVisible(false);
            FragmentTransaction transaction1 = manager.beginTransaction();
            transaction1.add(R.id.proimgframe, new propic()).commit();
        }
        if (item.getItemId() == R.id.Datesheet) {

            FragmentTransaction transaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
            transaction.replace(R.id.container, new Datesheet());
            toolbar.setLogo(R.drawable.menudatesheet);
            getSupportActionBar().setTitle("\t\t\t\tDatesheet");
            updialogcode = "Datesheet";
            loaddailog.setVisible(true);
            transaction.commit();
            drawerLayout.closeDrawers();
        }

        if (item.getItemId() == R.id.Detainlist) {
            FragmentTransaction transaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
            transaction.replace(R.id.container, new Detaillist());
            toolbar.setLogo(R.drawable.menudetainlist);
            getSupportActionBar().setTitle("\t\t\t\tDetainlist");
            loaddailog.setVisible(true);
            updialogcode = "Detainlist";
            transaction.commit();
            drawerLayout.closeDrawers();
        }

        if (item.getItemId() == R.id.campusnewsfeed) {
            FragmentTransaction transaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
            transaction.replace(R.id.container, new NewsFeed());
            toolbar.setLogo(R.drawable.menunewsfeed);
            getSupportActionBar().setTitle("\t\t\t\tNewsfeed");
            loaddailog.setVisible(true);
            updialogcode = "Newsfeed";
            transaction.commit();
            drawerLayout.closeDrawers();
        }


        if (item.getItemId() == R.id.Timetable) {
            FragmentTransaction transaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
            transaction.replace(R.id.container, new Timetable());
            toolbar.setLogo(R.drawable.menutimetable);
            getSupportActionBar().setTitle("\t\t\t\tTimetable");
            loaddailog.setVisible(true);
            updialogcode = "Timetable";
            transaction.commit();
            drawerLayout.closeDrawers();
        }

        /*if (item.getItemId() == R.id.Library) {
            FragmentTransaction transaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
            transaction.replace(R.id.container, new library());
            toolbar.setLogo(R.drawable.menulibrary);
            getSupportActionBar().setTitle("\t\t\t\tLibrary");
            transaction.commit();
            drawerLayout.closeDrawers();
        }*/

        if (item.getItemId() == R.id.Syllabus) {
            FragmentTransaction transaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
            transaction.replace(R.id.container, new Syllabus());
            transaction.addToBackStack("Syllabus");
            getSupportActionBar().setTitle("\t\t\t\tSyllabus");
            toolbar.setLogo(R.drawable.menusyllabus);
            loaddailog.setVisible(false);
            updialogcode = null;
            transaction.commit();
            drawerLayout.closeDrawers();
        }

        if (item.getItemId() == R.id.events) {
            FragmentTransaction transaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
            transaction.replace(R.id.container, new viewpagerfragmentset());
            toolbar.setLogo(R.drawable.menuevent);
            getSupportActionBar().setTitle("\t\t\t\tEvents");
            loaddailog.setVisible(false);
            updialogcode = null;
            transaction.commit();
            drawerLayout.closeDrawers();
        }

        if (item.getItemId() == R.id.report) {
            FragmentTransaction transaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.fraganim_tomid, R.anim.fraganim_mid);
            transaction.replace(R.id.container, new Report());
            toolbar.setLogo(R.drawable.menureport);
            getSupportActionBar().setTitle("\t\t\t\tReport");
            loaddailog.setVisible(false);
            updialogcode = null;
            transaction.commit();
            drawerLayout.closeDrawers();
        }

        if (item.getItemId() == R.id.Browser) {
            drawerLayout.closeDrawers();
            startActivity(new Intent(this, Browser.class).putExtra("url", "http://www.google.co.in/"));
        }

        if (item.getItemId() == R.id.setting) {
            drawerLayout.closeDrawers();
            startActivity(new Intent(this, Setting.class).putExtra("addr", addrs));
        }

        if (item.getItemId() == R.id.logout) {
            drawerLayout.closeDrawers();
            logoutdialog();
            OneSignal.sendTag("user_id","");
            OneSignal.sendTag("Branch","");
            OneSignal.sendTag("type","");
            OneSignal.sendTag("sem","");
        }

        return true;
    }

    private void logoutdialog() {
        AlertDialog.Builder buililder = new AlertDialog.Builder(MainFrame.this);
        buililder.setTitle("LOGOUT");
        buililder.setMessage("Do you want to Logout?");
        buililder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent strt = new Intent(getApplicationContext(), Login_frame.class);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                strt.putExtra("blockuser","false");
                startActivity(strt);
                getApplicationContext().getSharedPreferences("Login", 0).edit().clear().commit();
                Toast.makeText(MainFrame.this, "Logout Sucussfully", Toast.LENGTH_SHORT).show();
            }
        });
        buililder.setNeutralButton("Cancel", null);
        buililder.create();
        buililder.show();
    }

    private Notification setBigPictureStyleNotification() {
        Bitmap remote_picture = null;

        // Create the style object with BigPictureStyle subclass.
        android.support.v7.app.NotificationCompat.BigPictureStyle notiStyle = new android.support.v4.app.NotificationCompat.BigPictureStyle();
        notiStyle.setBigContentTitle("Big Picture Expanded");
        notiStyle.setSummaryText("Nice big picture.");

        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(sample_url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add the big picture to the style.
        notiStyle.bigPicture(remote_picture);

        // Creates an explicit intent for an ResultActivity to receive.
        Intent resultIntent = new Intent(this, MainFrame.class);

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = create(this);

        // Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(MainFrame.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        return new android.support.v7.app.NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notify)
                .setAutoCancel(true)
                .setLargeIcon(remote_picture)
                .setContentIntent(resultPendingIntent)
                .setContentTitle("Big Picture Normal")
                .setLights(Color.GREEN, 300, 300)
                .setContentText("This is an example of a Big Picture Style.")
                .setStyle(notiStyle)
                .setColor(Color.BLACK)
                .build();
    }

    public class CreateNotification extends AsyncTask<Void, Void, Void> {


        int style = NORMAL;

        public CreateNotification(int style) {
            this.style = style;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Notification noti = new Notification();

            switch (style) {

                case BIG_PICTURE_STYLE:
                    noti = setBigPictureStyleNotification();
                    break;

            }

            noti.defaults |= Notification.DEFAULT_VIBRATE;
            noti.defaults |= Notification.DEFAULT_SOUND;
            noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
            noti.flags |= Notification.FLAG_SHOW_LIGHTS;

            mNotificationManager.notify(1, noti);

            return null;

        }
    }

    @Override
    public void onBackPressed() {
        if (onbackpresscount == 0) {
            Toast.makeText(MainFrame.this, "Press again to Logout", Toast.LENGTH_SHORT).show();
            onbackpresscount = 1;
            onbackcount();
        } else if (onbackpresscount == 1) {
            logoutdialog();
            onbackpresscount = 0;
        }
    }


    CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        public void onTick(long millisUntilFinished) {

        }

        public void onFinish() {
            AdRequest adRequest = new AdRequest.Builder().build();
            AdRequest madRequest = new AdRequest.Builder().build();
            mAdView.setVisibility(View.VISIBLE);
            mAdView.loadAd(madRequest);
            interstitial.loadAd(adRequest);
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    displayInterstitial();
                }
            });

            countDownTimer.start();
        }
    };


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Firebase usernameref = new Firebase("https://cgc-database.firebaseio.com/Faculty/" + addrs + "/Name");
        usernameref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                Snackbar snackbar = Snackbar.make(drawerLayout, "Welcome     \"  " + dataSnapshot.getValue().toString() + "  \" ", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.proimgframe, new propic()).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownTimer.start();
  /*      Networkcheck();*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
       /* Networkcheck();*/
    }

    private void onbackcount() {
        CountDownTimer countback = new CountDownTimer(7000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                onbackpresscount = 0;
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
     /*   Networkcheck();*/
    }


    /*private void  Networkcheck()
    {
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true  && chkconnection == true )
        {
            Snackbar snackbar = Snackbar.make(messsagelayout, "CONGRATULATION", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.CYAN);
            snackbar.setAction("You are  online  now", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            snackbar.show();
            reloadfrag();
            chkconnection = false;
        }

        if(networkInfo != null && networkInfo.isConnected()==true)
        {

        }

        else
        {
            NetworkErrorsnackbar();
        }

        }

    private void NetworkErrorsnackbar()
    {
        Snackbar snackbar = Snackbar.make(messsagelayout, " No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.RED);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkconnection = true;
                Networkcheck();
            }
        });
        snackbar.show();
    }

    private void reloadfrag()
    {
        FragmentTransaction ft = manager.beginTransaction();
        ft.detach(new Home()).attach(new Home()).commit();
    }*/

}
