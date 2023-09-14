package com.dreamhunterztech.cgcfacultyportal;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.Vibrator;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.michael.easydialog.EasyDialog;

/**
 * Created by suwas on 21-10-2016.
 */

public class Setting extends AppCompatActivity {
    String [] colors = {"  Red","  Blue","  Green","  Yellow","  Cyan","  Purple","  White"};
    ArrayAdapter adapter;
    String pass="";
    Button usersettings,settingbtn,updateprofilebtn,changepassdubbtn;
    ImageView closechangepassdialog,closechangeprodialog;
    Spinner selectcolor;
    EditText Name,emailid,mobno;
    RadioButton radiomale,radiofmale;
    CheckBox vibratorset;
    String addrs,deleteref,rollnoref;
    FloatingActionButton soundsetter;
    TextView setsoundtitile,rollno,oldpass,newpass,confirmpass;
    Uri sounduri;
    Dialog userprofiledialog,userpassdialog;
    NotificationCompat.Builder noti;
    ImageButton changepassbtn,updateprobtn,deleteprodata;
    private SharedPreferences sp;
    private   SharedPreferences.Editor Ed;
    int noticolor;
    String propicref;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        sp=getSharedPreferences("Notificationsetting",0);
        Ed=sp.edit();
        addrs = getIntent().getExtras().getString("addr");
        deleteref = addrs.substring(0,addrs.lastIndexOf("/"));
        rollnoref = addrs.substring(addrs.lastIndexOf("/")+1,addrs.length());
        Toolbar toolbar = (Toolbar) findViewById(R.id.settingtoolbar);
        usersettings = (Button) findViewById(R.id.usersetting);
        vibratorset = (CheckBox) findViewById(R.id.vibratorsetting);
        soundsetter = (FloatingActionButton) findViewById(R.id.notifactionsoundselecter);
        setsoundtitile = (TextView) findViewById(R.id.lbnot);
        settingbtn = (Button) findViewById(R.id.settingbutton);
        //passdialog
        userpassdialog = new Dialog(Setting.this);
        userpassdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        userpassdialog.getWindow().setWindowAnimations(R.style.animateddialog);
        userpassdialog.setCanceledOnTouchOutside(false);
        userpassdialog.setCancelable(false);
        userpassdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //profiledialog
        userprofiledialog = new Dialog(Setting.this);
        userprofiledialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        userprofiledialog.getWindow().setWindowAnimations(R.style.animateddialog);
        userprofiledialog.setCanceledOnTouchOutside(false);
        userprofiledialog.setCancelable(false);
        userprofiledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        userprofiledialog.setContentView(R.layout.userproupdatelayout);
        Name = (EditText) userprofiledialog.findViewById(R.id.settingfirstname);
        emailid = (EditText) userprofiledialog.findViewById(R.id.settingemailid);
        rollno = (TextView) userprofiledialog.findViewById(R.id.settinguniversityrollno);
        mobno = (EditText) userprofiledialog.findViewById(R.id.settingphoneno);
        radiomale = (RadioButton) userprofiledialog.findViewById(R.id.settingmale);
        radiofmale = (RadioButton) userprofiledialog.findViewById(R.id.settingfemale);
        updateprofilebtn = (Button) userprofiledialog.findViewById(R.id.settingupdatebutton);
        closechangeprodialog = (ImageView) userprofiledialog.findViewById(R.id.changeprodialogclose);
        closechangeprodialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userprofiledialog.dismiss();
            }
        });
        updateprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedata();
                Toast.makeText(Setting.this,"Date Sucessfully Updated", Toast.LENGTH_SHORT).show();
            }
        });

        noti= new NotificationCompat.Builder(Setting.this)
                .setSmallIcon(R.drawable.setting,30)
                .setContentTitle("Notification Setting")
                .setContentText("Sucessfully saved Notification config")
                .setAutoCancel(true)
                .setSound(sounduri)
                .setVibrate(new long[]{0,100,1000,0,0,1000})
                .setColor(Color.BLACK)
                .setContentIntent(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        selectcolor = (Spinner) findViewById(R.id.lightselector);
        adapter = new ArrayAdapter(Setting.this,R.layout.spinnerlayout,colors);
        selectcolor.setAdapter(adapter);
        usersettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view =getLayoutInflater().inflate(R.layout.userprofilesetting, null);
                changepassbtn = (ImageButton) view.findViewById(R.id.changepass);
                updateprobtn = (ImageButton) view.findViewById(R.id.updatepro);
                deleteprodata = (ImageButton) view.findViewById(R.id.deleteprodata);
                changepassbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userpassdialog.setContentView(R.layout.userpasswordsetlayout);
                        closechangepassdialog = (ImageView) userpassdialog.findViewById(R.id.changepassdialogclose);
                        oldpass = (TextView) userpassdialog.findViewById(R.id.oldpass);
                        newpass = (TextView) userpassdialog.findViewById(R.id.newpass);
                        confirmpass = (TextView) userpassdialog.findViewById(R.id.confirmpass);
                        changepassdubbtn = (Button) userpassdialog.findViewById(R.id.passupbtn);
                        changepassdubbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updatepass();
                            }
                        });
                        closechangepassdialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                userpassdialog.dismiss();
                            }
                        });
                        userpassdialog.show();
                    }
                });

                updateprobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getprofiledata();
                    }
                });

                deleteprodata.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteprodata();
                    }
                });
                new EasyDialog(Setting.this)
                        .setLayout(view)
                        .setBackgroundColor(Setting.this.getResources().getColor(R.color.bg_screen3))
                        .setLocationByAttachedView(usersettings)
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
        });

        vibratorset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibratorset.isChecked()==true) {
                    Vibrator vib = (Vibrator) getSystemService(Setting.VIBRATOR_SERVICE);
                    vib.vibrate(400);
                    noti.setVibrate(new long[]{100,1000});
                }
                else
                {
                    noti.setVibrate(null);
                }
            }
        });

        soundsetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select one");
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                    startActivityForResult(intent,03);

            }
        });


        settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap notificicon = BitmapFactory.decodeResource(getResources(), R.drawable.cgclogonotific);
                noti.setLargeIcon(notificicon);
                if (selectcolor.getSelectedItem().equals("  Red")) {
                    noti.setLights(Color.RED, 500, 1000);
                    noticolor = Color.RED;
                }

                else if (selectcolor.getSelectedItem().equals("  Blue")) {
                    noti.setLights(Color.BLUE, 500, 1000);
                    noticolor = Color.BLUE;
                }

                else if (selectcolor.getSelectedItem().equals("  Green")) {
                    noti.setLights(Color.GREEN, 500, 1000);
                    noticolor=Color.GREEN;
                }

                else if (selectcolor.getSelectedItem().equals("  Yellow")) {
                    noti.setLights(Color.YELLOW, 500, 1000);
                    noticolor=Color.YELLOW;
                }

                else if (selectcolor.getSelectedItem().equals("  Cyan")) {
                    noti.setLights(Color.CYAN, 500, 1000);
                    noticolor=Color.CYAN;
                }

                else if (selectcolor.getSelectedItem().equals("  Purple"))
                {
                    noti.setLights(Color.parseColor("#b206f6"),500,1000);
                    noticolor=Color.parseColor("#b206f6");
                }

                else if(selectcolor.getSelectedItem().equals("  White")) {
                    noti.setLights(Color.WHITE, 500, 1000);
                    noticolor=Color.WHITE;

                }
                if(setsoundtitile.getText().equals("Default")) {
                    sounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                }

                noti.setSound(sounduri);

                savenotificationsetting(sounduri.getPath(), String.valueOf(noticolor));
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0,noti.build());


                AlertDialog.Builder display = new AlertDialog.Builder(Setting.this);
                display.setTitle("Message");
                display.setMessage("This is demo as we were working on it.we will provide this part of app later on.\n\nThank you!!");
                display.setPositiveButton("OK",null);
                display.show();
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            super.onActivityResult(requestCode,resultCode,data);
            if (requestCode == 03) {
                sounduri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Cursor nameset = getContentResolver().query(sounduri, null, null, null, null);
                nameset.moveToFirst();
                setsoundtitile.setText(nameset.getString(nameset.getColumnIndex(OpenableColumns.DISPLAY_NAME)).toUpperCase());
                nameset.close();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(Setting.this,"Sound selection is cancelled", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getprofiledata()
    {
        userprofiledialog.show();

        Firebase userfirstnameref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Name");
        userfirstnameref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                Name.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        Firebase emailref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Email ID");
        emailref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                emailid.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase mobnoref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Mobile Number");
        mobnoref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                mobno.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase genderref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Gender");
        genderref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("Male"))
                {
                    radiomale.setChecked(true);
                }
                else
                {
                    radiofmale.setChecked(true);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase rollnoref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Empid");
        rollnoref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                rollno.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


    private void setPropicref(String propicref)
    {
        this.propicref=propicref;
        Log.e(">data",propicref);
    }

    private void updatepass()
    {

        if(oldpass.getText().toString().equals("") && newpass.getText().toString().equals("") && confirmpass.getText().toString().equals(""))
        {
            oldpass.setError("is Empty");
            newpass.setError("is Empty");
            confirmpass.setError("is Empty");
        }

        else {

            Firebase passref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Password");
            passref.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pass = dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            if (pass.equals(oldpass.getText().toString().trim())) {
                if (newpass.getText().toString().trim().equals(confirmpass.getText().toString().trim())) {
                    Firebase setpassword = passref;
                    setpassword.setValue(newpass.getText().toString());
                    AlertDialog.Builder passmsg = new AlertDialog.Builder(Setting.this);
                    passmsg.setTitle("Message");
                    passmsg.setMessage("Sucessfully changed your password");
                    passmsg.setPositiveButton("Ok", null);
                    passmsg.show();
                    userpassdialog.dismiss();
                } else {
                    confirmpass.setError("not matched");
                }

            } else {
                oldpass.setError("Password is Invalid");
            }

        }
    }

    private void deleteprodata()
    {
        Firebase getrollnoref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/Empid");
        getrollnoref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                setPropicref("propics/"+dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        AlertDialog.Builder deleteprodialog = new AlertDialog.Builder(Setting.this);
        deleteprodialog.setTitle("Delete Profile");
        deleteprodialog.setMessage("Do you want to Delete Profile Data?");
        deleteprodialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sp=getSharedPreferences("Login", 0);
                Ed=sp.edit();
                Ed.clear().commit();

                StorageReference storageRef = storage.getReference();
                StorageReference desertRef = storageRef.child(propicref+"/propics.jpeg");
                desertRef.delete();

            Firebase deletedataref = new Firebase("https://cgc-database.firebaseio.com/Faculty/" + deleteref);
            deletedataref.child(rollnoref).removeValue();
            startActivity(new Intent(Setting.this,Login_frame.class));
            }
        });
        deleteprodialog.setNeutralButton("Cancel",null);
        deleteprodialog.show();

    }

    private  void updatedata()
    {
        Firebase userref = new Firebase("hhttps://cgc-database.firebaseio.com/Faculty/"+addrs);

        Firebase setfirstname=userref.child("Name");
        setfirstname.setValue(Name.getText().toString());

        Firebase setemailid=userref.child("Email ID");
        setemailid.setValue(emailid.getText().toString());

        Firebase setmobilenumber=userref.child("Mobile Number");
        setmobilenumber.setValue(mobno.getText().toString());

        Firebase setrollnumber=userref.child("Rollno");
        setrollnumber.setValue(rollno.getText().toString());

        Firebase setgender = userref.child("Gender");

        if (radiomale.isChecked()==true)
        {
            setgender.setValue(radiomale.getText().toString());
        }

        else
        {
            setgender.setValue(radiofmale.getText().toString());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class tasks extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] params) {

            return null;
        }
    }


    private void savenotificationsetting(String sounduri,String lightcolor)
    {
        Ed.putString("sounduri",sounduri);
        Ed.putString("lightcolor",lightcolor);
        Ed.commit();
        Log.e(sounduri,lightcolor);
    }

}


