package com.dreamhunterztech.cgcfacultyportal;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dreamer on 17-09-2017.
 */

public class PostTimetable extends AppCompatActivity {
    ImageView closebtn,timeimg;
    Button tbupdatebtn;
    Uri resulturi=null;
    Firebase ttref;
    int taskcounter=0;
    NotificationCompat.Builder eventsNotification;
    NotificationManager notificationManager;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Spinner selectsem , selectsec;
    ProgressBar progressBar;
    private String addrs,branch,timetableaddrs;
    String [] sems ={"Sem","1","2","3","4","5","6","7","8"};
    String [] sections = {"Sec","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addrs = getIntent().getExtras().getString("addr");
        timetableaddrs = addrs.substring(0,addrs.lastIndexOf("/"));
        branch = timetableaddrs.substring(timetableaddrs.lastIndexOf("/")+1,timetableaddrs.length());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        setContentView(R.layout.timetableuploadlayout);
        closebtn = (ImageView) findViewById(R.id.timetablepostdialogclose);
        progressBar = findViewById(R.id.tbpbar);
        selectsem = (Spinner) findViewById(R.id.tipmetableoptionsem);
        selectsec = (Spinner) findViewById(R.id.tipmetableoptionsec);
        ArrayAdapter adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sems);
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sections);
        selectsem.setAdapter(adapter1);
        selectsec.setAdapter(adapter2);
        tbupdatebtn =(Button) findViewById(R.id.tbupbtn);
        tbupdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectsem.getSelectedItemPosition()==0)
                {
                    ((TextView) selectsem.getChildAt(0)).setError("Invalid Selection");
                    taskcounter+=1;
                }

                if(selectsec.getSelectedItemPosition()==0) {
                    ((TextView) selectsec.getChildAt(0)).setError("Invalid Selection");
                    taskcounter+=1;
                }

               if(resulturi==null)
               {
                   Toast.makeText(getApplicationContext(),"please Image first",Toast.LENGTH_SHORT).show();
                   taskcounter+=1;
               }

               if(taskcounter == 0)
               {
                   uploadfile(resulturi);
               }

               taskcounter=0;
            }
        });

        timeimg = (ImageView) findViewById(R.id.timetableimageinput);
        timeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto,04);
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
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == 04 && resultCode == RESULT_OK) {
                Uri imageuri = data.getData();
                CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(16,21).start(this);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                resulturi = result.getUri();
                timeimg.setImageURI(resulturi);
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


    private void settimetablepostdata(String sem,String sec,String url)
    {
        ttref = new Firebase("https://cgc-database.firebaseio.com/"+timetableaddrs+"/"+sem+sec+"/Timetable/imgurl");
        ttref.setValue(url);
        Calendar c = Calendar.getInstance();
        Firebase setnoti = new Firebase("https://cgc-database.firebaseio.com/"+timetableaddrs+"/Notification/ALL");
        Firebase subnode = setnoti.child("Timetable sem "+sem+" sec "+sec);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        Map<String, String> notiData = new HashMap<String, String>();

        notiData.put("title", "Timetable");
        notiData.put("content","TimeTable for sem:"+sem+sec+" is Updated on \n"+df.format(c.getTime())+" "+sdf.format(c.getTime()));
        notiData.put("type","all");
        subnode.setValue(notiData);

        new sendnotification().sendNotification(branch,"Timetable is Updated");
        finish();
    }


    private void uploadfile(Uri uri)
    {
        try {
            final Bitmap notificicon = BitmapFactory.decodeResource(getResources(),R.drawable.cgclogonotific);
            final Intent emptyIntent = new Intent();
            final PendingIntent pendingIntent = PendingIntent.getActivity(this,0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            eventsNotification = new NotificationCompat.Builder(this)
                    .setProgress(0,0,true)
                    .setSmallIcon(R.drawable.notitimetable,30)
                    .setContentTitle("Filename")
                    .setContentText("processing")
                    .setLargeIcon(notificicon)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setColor(Color.parseColor("#c4ef0404"))
                    .setContentInfo("counting");
            progressBar.setVisibility(View.VISIBLE);
            StorageReference storageref = storage.getReferenceFromUrl("gs://cgc-database.appspot.com");
            StorageReference filepath =  storageref.child(" Timetable/"+timetableaddrs+"/TB"+selectsem.getSelectedItem().toString().trim()+selectsec.getSelectedItem().toString().trim());
            filepath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    String fileinfo = taskSnapshot.getBytesTransferred() + "/" + taskSnapshot.getTotalByteCount() + " bytes";
                    eventsNotification.setProgress(100, progress.intValue(), false);
                    eventsNotification.setContentInfo(fileinfo);
                    eventsNotification.setContentText("uploading");
                    eventsNotification.setContentTitle("Timateble data");
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
                   settimetablepostdata(selectsem.getSelectedItem().toString().trim(),selectsec.getSelectedItem().toString().trim(),imageuri);
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


}
