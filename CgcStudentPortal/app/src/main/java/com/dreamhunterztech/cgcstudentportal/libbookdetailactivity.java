package com.dreamhunterztech.cgcstudentportal;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.xiaochen.progressroundbutton.AnimDownloadProgressButton;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


/**
 * Created by Dreamer on 28-06-2017.
 */

public class libbookdetailactivity extends AppCompatActivity {

    AnimDownloadProgressButton downbtn;
    NotificationCompat.Builder eventsNotification;
    NotificationManager notificationManager;
    private File localFile;
    Boolean notificstate=false;
    private StorageReference storageRef;
    private Boolean dwntag=false;
    private String fileaddrs;
    ImageView bookcover;
    String bookurl;
    private  File rootPath;
    private offlinedatahandler filedata;
    TextView bookname,bookauthername,bookdesc,booktype,bookuploaddate,bookuploadertxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.librarybookdetaillayout);
        bookurl = getIntent().getExtras().getString("bookaddrs");
        bookcover = (ImageView) findViewById(R.id.bookcover);
        bookname = (TextView) findViewById(R.id.bookname);
        bookauthername = (TextView) findViewById(R.id.bookauther);
        booktype = (TextView) findViewById(R.id.booktype);
        bookdesc = (TextView) findViewById(R.id.bookdescrp);
        bookuploadertxt = (TextView)findViewById(R.id.bookowner);
        bookuploaddate = findViewById(R.id.bookuploaddate);
        downbtn = (AnimDownloadProgressButton) findViewById(R.id.dwnbtn);
        downbtn.setButtonRadius(0.2f);
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                downbtn.setTextSize(40f);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                downbtn.setTextSize(40f);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                downbtn.setTextSize(40f);
                break;

        }
        downbtn.setCurrentText("Download");
        downbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadfile();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.libdetailtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fileaddrs= null;
    }

    private void openfile()
    {

        Intent  openpdf = new Intent(getApplicationContext(),PDFviewer.class);
        openpdf.putExtra("pdf_url",localFile);
        openpdf.putExtra("datatype","file");
        startActivity(openpdf);

    }


    private void getbookdate()
    {
        Firebase booknameref = new Firebase(getApplicationContext().getString(R.string.dburl)+"Library/libfilerecord/"+bookurl+"/bookname");
        booknameref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                bookname.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase booktyperef = new Firebase(getApplicationContext().getString(R.string.dburl)+"Library/libfilerecord/"+bookurl+"/booktype");
        booktyperef.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                booktype.setText("Type : "+dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase bookuploadadteref = new Firebase(getApplicationContext().getString(R.string.dburl)+"Library/libfilerecord/"+bookurl+"/bookuploaddate");
        bookuploadadteref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
               bookuploaddate.setText("uploaded on - "+dataSnapshot.getValue(String.class).toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase bookpicref = new Firebase(getApplicationContext().getString(R.string.dburl)+"Library/libfilerecord/"+bookurl+"/bookcoverpic");
        bookpicref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                String imageref = dataSnapshot.getValue(String.class).toString();
                Picasso.with(getApplicationContext()).load(imageref).fit().into(bookcover);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase bookautherref = new Firebase(getApplicationContext().getString(R.string.dburl)+"Library/libfilerecord/"+bookurl+"/bookauther");
        bookautherref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                bookauthername.setText("Auther : "+dataSnapshot.getValue(String.class).toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase bookdescref = new Firebase(getApplicationContext().getString(R.string.dburl)+"Library/libfilerecord/"+bookurl+"/bookdesc");
        bookdescref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                   bookdesc.setText(dataSnapshot.getValue(String.class).toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

         Firebase bookuploaderref = new Firebase(getApplicationContext().getString(R.string.dburl)+"Library/libfilerecord/"+bookurl+"/bookuploader");
        bookuploaderref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                final Firebase nameref = new Firebase(getApplicationContext().getString(R.string.dburl)+dataSnapshot.getValue(String.class).toString()+"/Name");
                nameref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e(">>chktag",nameref.toString());
                        bookuploadertxt.setText("Uploaded By - "+dataSnapshot.getValue(String.class).toString());
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        Firebase bookfileurlref = new Firebase(getApplicationContext().getString(R.string.dburl)+"Library/libfilerecord/"+bookurl+"/bookfileurl");
        bookfileurlref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                setFileaddrs(dataSnapshot.getValue(String.class).toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void setFileaddrs(String fileaddrs) {
        this.fileaddrs = fileaddrs;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl(fileaddrs);
        filedata  = new  offlinedatahandler(getApplicationContext().getString(R.string.libfileurl),getApplicationContext().getString(R.string.filename),storageRef.getName());
        rootPath = new File(Environment.getExternalStorageDirectory(),getApplicationContext().getString(R.string.libfileurl));
        File datafile = new File(rootPath,getApplicationContext().getString(R.string.filename));
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        if(!datafile.exists())
        {
            try {
                filedata.createfile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        localFile = new File(rootPath,storageRef.getName());
        if (localFile.exists() && dwntag==false)
        {
            downbtn.setCurrentText("open");
        }
    }

    private void downloadfile()
    {

        try {
            final Bitmap notificicon = BitmapFactory.decodeResource(getResources(),R.drawable.cgclogonotific);
            final Intent emptyIntent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(libbookdetailactivity.this,0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            eventsNotification = new NotificationCompat.Builder(libbookdetailactivity.this)
                    .setProgress(0,0,true)
                    .setSmallIcon(R.drawable.detainlistnotiifcon,30)
                    .setContentTitle("Filename")
                    .setContentText("processing")
                    .setLargeIcon(notificicon)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setColor(Color.parseColor("#c4ef0404"))
                    .setContentInfo("counting");

            notificationManager = (NotificationManager) getSystemService(libbookdetailactivity.this.NOTIFICATION_SERVICE);

            if(localFile.exists())
            {
                openfile();
            }
            else {
                dwntag=true;
                downbtn.setState(AnimDownloadProgressButton.DOWNLOADING);
                notificstate=true;
                notificationManager.notify(1, eventsNotification.build());
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        eventsNotification.setContentText("Done");
                        eventsNotification.setOngoing(false);
                        notificationManager.notify(1, eventsNotification.build());
                        notificstate=false;
                        dwntag=false;
                        if (downbtn.getProgress() == 100) {
                            downbtn.setState(AnimDownloadProgressButton.INSTALLING);
                            downbtn.setCurrentText("wait");
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    downbtn.setState(AnimDownloadProgressButton.NORMAL);
                                    downbtn.setCurrentText("open");
                                }
                            }, 2000);
                            try {

                                filedata.createnode();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            } catch (SAXException e) {
                                e.printStackTrace();
                            } catch (TransformerException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        eventsNotification.setContentText("Failed");
                        eventsNotification.setOngoing(false);
                        downbtn.setCurrentText("failed");
                        downbtn.setTextColor(Color.RED);
                        dwntag=false;
                        notificationManager.notify(1, eventsNotification.build());
                        if (localFile.exists())
                        {
                            localFile.delete();
                        }
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        String fileinfo = taskSnapshot.getBytesTransferred() + "/" + taskSnapshot.getTotalByteCount() + " bytes";
                        downbtn.setProgressText("Downloading",progress);
                        eventsNotification.setProgress(100, progress.intValue(), false);
                        eventsNotification.setContentInfo(fileinfo);
                        eventsNotification.setContentText("Downloading");
                        eventsNotification.setContentTitle(localFile.getName());
                        notificationManager.notify(1, eventsNotification.build());
                    }
                });
            }
        }

        catch (Exception e)
        {
            notificationManager.cancel(1);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        getbookdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificstate=false;
    }
}
