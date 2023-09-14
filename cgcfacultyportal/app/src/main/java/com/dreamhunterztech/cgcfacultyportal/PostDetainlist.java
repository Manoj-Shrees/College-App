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
import android.widget.ImageButton;
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
import com.hanks.htextview.base.HTextView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dreamer on 17-09-2017.
 */

public class PostDetainlist extends AppCompatActivity {
    ImageView closebtn;
    ImageButton openfile;
    TextView filetxt;
    Firebase dlref;
    int taskcounter=0;
    NotificationCompat.Builder eventsNotification;
    NotificationManager notificationManager;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri fileuri=null;
    Button upbtn;
    ProgressBar progressBar;
    String addrs,DLaddrs,branch;
    Spinner fortlist,semselecter,secselecter;
    private String    sems [] ={"Sem","1","2","3","4","5","6","7","8"}, Sections [] = {"Sec","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"},
    Detainlistselectionlist [] ={"Select Fortnight type","1stfortnight","2ndfortnight","3rdfortnight","4thfortnight","5thfortnight","6thfortnight"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.detianlistuploadlayout);
        addrs = getIntent().getExtras().getString("addr");
        DLaddrs = addrs.substring(0,addrs.lastIndexOf("/"));
        branch = DLaddrs.substring(DLaddrs.lastIndexOf("/")+1,DLaddrs.length());
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        openfile = (ImageButton) findViewById(R.id.dlopenfile);
        filetxt = (TextView) findViewById(R.id.filetxt);
        progressBar = (ProgressBar) findViewById(R.id.dlpbar);
        upbtn = (Button) findViewById(R.id.dlupbtn);
        closebtn = (ImageView) findViewById(R.id.deatinlistpostdialogclose);
         fortlist = (Spinner) findViewById(R.id.detainlistoption);
         semselecter = (Spinner) findViewById(R.id.detainlistsem);
         secselecter = (Spinner) findViewById(R.id.detainlistsection);
        ArrayAdapter adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sems);
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Sections);
        ArrayAdapter adapter3 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Detainlistselectionlist);
        semselecter.setAdapter(adapter1);
        secselecter.setAdapter(adapter2);
        fortlist.setAdapter(adapter3);
        upbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(semselecter.getSelectedItemPosition()==0) {
                    ((TextView) semselecter.getChildAt(0)).setError("Invalid Selection");
                    taskcounter+=1;
                }

                if(secselecter.getSelectedItemPosition()==0) {
                    ((TextView) secselecter.getChildAt(0)).setError("Invalid Selection");
                    taskcounter+=1;
                }

                if(fortlist.getSelectedItemPosition()==0) {
                    ((TextView) fortlist.getChildAt(0)).setError("Invalid Selection");
                    taskcounter+=1;
                }

                if(fileuri==null)
                {
                    Toast.makeText(getApplicationContext(),"please Image first",Toast.LENGTH_SHORT).show();
                    taskcounter+=1;
                }

                if(taskcounter == 0)
                {
                    uploadfile(fileuri);
                }

                taskcounter=0;

            }
        });
        openfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent pickfile = new Intent(Intent.ACTION_GET_CONTENT);
                    pickfile.setType("application/pdf");
                    startActivityForResult(pickfile, 06);
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

            if (requestCode == 06 && resultCode == RESULT_OK) {
                fileuri = data.getData();
                filetxt.setText(fileuri.getPath());
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this,"Error to upload file as selected app or file is not valid!",Toast.LENGTH_SHORT).show();
        }
    }


    private void setdlpostdata(String sem,String sec,String forttype,String url)
    {
        dlref = new Firebase("https://cgc-database.firebaseio.com/"+DLaddrs+"/"+sem+sec+"/Detainlist/"+forttype+"fileurl");
        dlref.setValue(url);
        Calendar c = Calendar.getInstance();
        Firebase setnoti = new Firebase("https://cgc-database.firebaseio.com/"+DLaddrs+"/Notification/ALL");
        Firebase subnode = setnoti.child("Detainlist"+sem+sec+forttype);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        Map<String, String> notiData = new HashMap<String, String>();

        notiData.put("title", "Detainlist ");
        notiData.put("content","Detainlist of sem :"+sem+sec+" for "+forttype+"\non "+df.format(c.getTime())+"  "+sdf.format(c.getTime()));
        notiData.put("type","all");
        subnode.setValue(notiData);

        new sendnotification().sendNotification(branch,"New detainlist list is avialable");
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
            StorageReference filepath =  storageref.child("Detainlist/"+DLaddrs+"/DL"+semselecter.getSelectedItem().toString().trim()+secselecter.getSelectedItem().toString().trim()+fortlist.getSelectedItem().toString().trim());
            filepath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    String fileinfo = taskSnapshot.getBytesTransferred() + "/" + taskSnapshot.getTotalByteCount() + " bytes";
                    eventsNotification.setProgress(100, progress.intValue(), false);
                    eventsNotification.setContentInfo(fileinfo);
                    eventsNotification.setContentText("uploading");
                    eventsNotification.setContentTitle("Detainlist data");
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
                    setdlpostdata(semselecter.getSelectedItem().toString().trim(),secselecter.getSelectedItem().toString().trim(),fortlist.getSelectedItem().toString().trim(),imageuri);
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
                    storagemessage.show();
                    progressBar.setVisibility(View.GONE);
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
