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
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
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

public class PostDatesheet extends AppCompatActivity {
    ImageView closebtn,dateimg;
    Button dsupbtn;
    Firebase dsref;
    int taskcounter=0;
    Uri resulturi=null;
    ProgressBar progressBar;
    NotificationCompat.Builder eventsNotification;
    NotificationManager notificationManager;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String semlist [] = {"Select Semester","sem1","sem2","sem3","sem4","sem5","sem6","sem7","sem8"},
            Datesheetselectionlist [] ={"Select Sessional type","sessional1","sessional2","sessional3","final"};
    Spinner semselecter,datesheetoptionselecter;
    private String addrs , dsaddrs,branch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addrs = getIntent().getExtras().getString("addr");
        dsaddrs = addrs.substring(0,addrs.lastIndexOf("/"));
        branch = dsaddrs.substring(dsaddrs.lastIndexOf("/")+1,dsaddrs.length());
        setContentView(R.layout.datesheetuploadlayout);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        closebtn = (ImageView) findViewById(R.id.datesheetpostdialogclose);
        semselecter = (Spinner) findViewById(R.id.datesheetoption1);
        progressBar = (ProgressBar) findViewById(R.id.dspbar);
        datesheetoptionselecter = (Spinner) findViewById(R.id.datesheetoption2);
        ArrayAdapter adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,semlist);
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Datesheetselectionlist);
        semselecter.setAdapter(adapter1);
        datesheetoptionselecter.setAdapter(adapter2);
        dsupbtn =  (Button) findViewById(R.id.dsbtn);
        dsupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(semselecter.getSelectedItemPosition()==0)
                {
                    ((TextView) semselecter.getChildAt(0)).setError("Invalid Selection");
                    taskcounter+=1;
                }

                if(datesheetoptionselecter.getSelectedItemPosition()==0) {
                    ((TextView) datesheetoptionselecter.getChildAt(0)).setError("Invalid Selection");
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
        dateimg = (ImageView) findViewById(R.id.datesheetimageinput);
        dateimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 05);
                } catch (Exception e) {
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

            if (requestCode == 05 && resultCode == RESULT_OK) {
                Uri imageuri = data.getData();
                CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(4,3).start(this);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                resulturi = result.getUri();
                dateimg.setImageURI(resulturi);
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


    private void settimetablepostdata(String sem,String type,String url)
    {
        dsref = new Firebase("https://cgc-database.firebaseio.com/"+dsaddrs+"/Datesheet/"+sem+"/"+type+"/imgurl");
        dsref.setValue(url);
        Calendar c = Calendar.getInstance();
        Firebase setnoti = new Firebase("https://cgc-database.firebaseio.com/"+dsaddrs+"/Notification/ALL");
        Firebase subnode = setnoti.child("Datesheet sem "+sem+" sec "+type);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        Map<String, String> notiData = new HashMap<String, String>();

        notiData.put("title","Datesheet");
        notiData.put("content","Datesheet  for sem:"+sem+" "+type+" is Updated on "+df.format(c.getTime())+"  "+sdf.format(c.getTime()));
        notiData.put("type","all");
        subnode.setValue(notiData);

        new sendnotification().sendNotification(branch,"New Datesheet is avialable");

        finish();
    }


    private void uploadfile(Uri uri)
    {
        try {
            final Bitmap notificicon = BitmapFactory.decodeResource(getResources(),R.drawable.cgclogonotific);
            final Intent emptyIntent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            eventsNotification = new NotificationCompat.Builder(this)
                    .setProgress(0,0,true)
                    .setSmallIcon(R.drawable.notify,30)
                    .setContentTitle("Filename")
                    .setContentText("processing")
                    .setLargeIcon(notificicon)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setColor(Color.parseColor("#c4ef0404"))
                    .setContentInfo("counting");
            progressBar.setVisibility(View.VISIBLE);
            StorageReference storageref = storage.getReferenceFromUrl("gs://cgc-database.appspot.com");
            StorageReference filepath =  storageref.child(" Datesheet/"+dsaddrs+"/DS"+semselecter.getSelectedItem().toString().trim()+datesheetoptionselecter.getSelectedItem().toString().trim());
            filepath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    String fileinfo = taskSnapshot.getBytesTransferred() + "/" + taskSnapshot.getTotalByteCount() + " bytes";
                    eventsNotification.setProgress(100, progress.intValue(), false);
                    eventsNotification.setContentInfo(fileinfo);
                    eventsNotification.setContentText("uploading");
                    eventsNotification.setContentTitle("Datesheet data");
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
                    settimetablepostdata(semselecter.getSelectedItem().toString().trim(),datesheetoptionselecter.getSelectedItem().toString().trim(),imageuri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Error ! to upload profile pic.",Toast.LENGTH_SHORT).show();
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
            e.printStackTrace();
        }

    }


}
