package com.dreamhunterztech.cgcfacultyportal;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.security.AccessController.getContext;

/**
 * Created by Dreamer on 17-09-2017.
 */

public class PostFeeds  extends AppCompatActivity {
    private String feedsemlist [] ={"Select Type","ALL","Selective"};
    private String [] sems ={"Sem","1","2","3","4","5","6","7","8"};
    private  String [] Sections = {"Sec","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"};
    Firebase feedref;
    NotificationCompat.Builder eventsNotification;
    NotificationManager notificationManager;
    Boolean strtcheck = false;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri resulturi=null;
    Spinner semselecter,sem,sec;
    ImageView closebtn,feedimg;
    int taskcounter=0;
    EditText topicinput,msginput;
    ProgressBar progressBar;
    Button post;
    String addrs ,branch,feedddrs,addrsref=null,auther="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.newsfeedpostlayout);
        addrs = getIntent().getExtras().getString("addr");
        feedddrs = addrs.substring(0,addrs.lastIndexOf("/"));
        branch = feedddrs.substring(feedddrs.lastIndexOf("/")+1,feedddrs.length());
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        closebtn = (ImageView) findViewById(R.id.newsfeedpostclose);
        semselecter = (Spinner) findViewById(R.id.semselection);
        progressBar = findViewById(R.id.nwpbar);
        sem = (Spinner) findViewById(R.id.feedsem);
        sec = (Spinner) findViewById(R.id.feedsection);
        sem.setEnabled(false);
        sec.setEnabled(false);
        ArrayAdapter adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sems);
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Sections);
        ArrayAdapter adapter3 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,feedsemlist);
        sem.setAdapter(adapter1);
        sec.setAdapter(adapter2);
        semselecter.setAdapter(adapter3);
        semselecter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==2)
                {
                    sem.setEnabled(true);
                    sec.setEnabled(true);
                }

                else
                {
                    sem.setEnabled(false);
                    sec.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        feedimg = (ImageView) findViewById(R.id.newsfeedimageinput);
        feedimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto,03);
                }
                catch (Exception e)
                {
                    final Dialog storagemessage = new Dialog(getApplicationContext());
                    storagemessage.setContentView(R.layout.storagemessage);
                    Button storageokbtn = (Button) storagemessage.findViewById(R.id.storagepermibtn);
                    storageokbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            storagemessage.dismiss();
                        }
                    });
                    storagemessage.show();
                }
            }
        });

        post = (Button) findViewById(R.id.feedpost);
         topicinput = (EditText) findViewById(R.id.newsfeedtopictxt);
        msginput = (EditText) findViewById(R.id.newsfeedcontent);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (topicinput.getText().toString().trim().length() == 0) {
                    topicinput.setError("is Empty");
                    taskcounter+=1;
                }

                if (msginput.getText().toString().trim().length() == 0) {
                    msginput.setError("is Empty");
                    taskcounter+=1;
                }

                if(semselecter.getSelectedItemPosition()==0)
                {
                    ((TextView) semselecter.getChildAt(0)).setError("Invalid Selection");
                    taskcounter+=1;
                }

                if(sem.getSelectedItemPosition()==0 && sem.isEnabled()) {
                    ((TextView) sem.getChildAt(0)).setError("Invalid Selection");
                    taskcounter+=1;
                }

                if(sec.getSelectedItemPosition()==0 && sec.isEnabled()) {
                    ((TextView) sec.getChildAt(0)).setError("Invalid Selection");
                    taskcounter+=1;
                }

                if(semselecter.getSelectedItemPosition()!=2) {
                    ((TextView) sem.getChildAt(0)).setError(null);
                }

                if(semselecter.getSelectedItemPosition()!=2) {
                    ((TextView) sec.getChildAt(0)).setError(null);
                }

                if(semselecter.getSelectedItemPosition()!=2) {
                    ((TextView) sec.getChildAt(0)).setError(null);
                }


                if(taskcounter == 0) {

                    if(semselecter.getSelectedItemPosition()==1)
                    {
                        addrsref = semselecter.getSelectedItem().toString();
                    }

                    else
                    {
                        addrsref = sem.getSelectedItem().toString()+sec.getSelectedItem().toString();
                    }

                    feedref = new Firebase("https://cgc-database.firebaseio.com/"+feedddrs+"/Newsfeed").child(addrsref);
                    feedref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(topicinput.getText().toString().trim()).exists())
                            {
                                topicinput.setError("already exists");
                                topicinput.requestFocus();
                            }

                            else
                            {
                                if(resulturi == null)
                                {
                                    setnewsfeedpostdata(topicinput.getText().toString().trim(), msginput.getText().toString(),"N/A",auther);
                                }

                                if(resulturi!=null)
                                {
                                    uploadfile(resulturi);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                }

                taskcounter=0;
            }
        });
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setnewsfeedpostdata(String topic,String msg,String imgurl,String auther)
    {
        Firebase setfeed = feedref.child(topic);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        Map<String, String> userData = new HashMap<String, String>();

        userData.put("feedtopic", topic);
        userData.put("feedcontext",msg);
        userData.put("feedauther",auther);
        userData.put("feedimgurl", imgurl);
        userData.put("feeddate",df.format(c.getTime()));
        userData.put("feedtime",sdf.format(c.getTime()));
        setfeed.setValue(userData);

        Firebase setnoti = new Firebase("https://cgc-database.firebaseio.com/"+feedddrs+"/Notification/ALL");
        Firebase subnode = setnoti.child(topic);

        Map<String, String> notiData = new HashMap<String, String>();

        notiData.put("title", topic);
        notiData.put("content","New feed is posted by\n"+auther);
        notiData.put("type","all");
        subnode.setValue(notiData);

        new sendnotification().sendNotification(branch,"New feed updated by "+auther);

        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == 03 && resultCode == RESULT_OK) {
                Uri imageuri = data.getData();
                CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(4,3).start(this);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                 resulturi = result.getUri();
               feedimg.setImageURI(resulturi);
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "image error to load", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,"Error to upload file as selected app or file is not valid!",Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadfile(Uri uri)
    {
        try {
            final Bitmap notificicon = BitmapFactory.decodeResource(getResources(),R.drawable.cgclogonotific);
            final Intent emptyIntent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            eventsNotification = new NotificationCompat.Builder(this)
                    .setProgress(0,0,true)
                    .setSmallIcon(R.drawable.notifeed,30)
                    .setContentTitle("Filename")
                    .setContentText("processing")
                    .setLargeIcon(notificicon)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setColor(Color.parseColor("#c4ef0404"))
                    .setContentInfo("counting");
            progressBar.setVisibility(View.VISIBLE);
            StorageReference storageref = storage.getReferenceFromUrl("gs://cgc-database.appspot.com");
            StorageReference filepath =  storageref.child("Newsfeed/Faculty/"+addrs+"/"+topicinput.getText().toString().trim());
            filepath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    String fileinfo = taskSnapshot.getBytesTransferred() + "/" + taskSnapshot.getTotalByteCount() + " bytes";
                    eventsNotification.setProgress(100, progress.intValue(), false);
                    eventsNotification.setContentInfo(fileinfo);
                    eventsNotification.setContentText("uploading");
                    eventsNotification.setContentTitle("Newsfeed data");
                    notificationManager.notify(1, eventsNotification.build());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    eventsNotification.setContentText("Done");
                    eventsNotification.setOngoing(false);
                    Vibrator vib = (Vibrator) getSystemService(Setting.VIBRATOR_SERVICE);
                    vib.vibrate(400);
                    notificationManager.notify(1, eventsNotification.build());
                    String imageuri = taskSnapshot.getMetadata().getDownloadUrl().toString();
                    setnewsfeedpostdata(topicinput.getText().toString().trim(), msginput.getText().toString(),imageuri,auther);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                   eventsNotification.setOngoing(false);
                    final Dialog storagemessage = new Dialog(getApplicationContext());
                    storagemessage.setContentView(R.layout.storagemessage);
                    Button storageokbtn = (Button) storagemessage.findViewById(R.id.storagepermibtn);
                    storageokbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            storagemessage.dismiss();
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                    storagemessage.show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                }
            });
        }

        catch (Exception e)
        {

        }

    }


    private  void getuserdetail()
    {

        Firebase profileref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Job Profile");
        profileref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                setAuther("( "+dataSnapshot.getValue().toString().trim()+" - ");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase userfirstnameref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Name");
        userfirstnameref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                setAuther(dataSnapshot.getValue().toString().trim()+" )");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




    }


    private void setAuther(String data)
    {
        if(auther.equals(""))
        {
            auther=data;
        }

        else
        {
            auther+=data;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (strtcheck==false)
        {
            getuserdetail();
            strtcheck = true;
        }
    }
}
