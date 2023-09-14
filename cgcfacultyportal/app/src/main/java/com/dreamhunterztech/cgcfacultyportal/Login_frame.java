package com.dreamhunterztech.cgcfacultyportal;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cipherthinkers.shapeflyer.ShapeFlyer;
import com.cipherthinkers.shapeflyer.flyschool.FPoint;
import com.cipherthinkers.shapeflyer.flyschool.FlyBluePrint;
import com.cipherthinkers.shapeflyer.flyschool.FlyPath;
import com.cipherthinkers.shapeflyer.flyschool.PATHS;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.michael.easydialog.EasyDialog;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static java.security.AccessController.getContext;

@RuntimePermissions
public class Login_frame extends AppCompatActivity{
    Button loginbutton,signupbutton,submit,Done,Reset,uploadIDbtn;
    EditText userid,password,jobprofile,fname,lname,eid,spass,scpass,rno,mobno;
    TextView head,forgotpassbtn;
    NotificationCompat.Builder IDNotification;
    NotificationManager notificationManager;
    private Firebase checkref,mref,passref,singnupref,blockuserref;
    private CheckBox hideshowpass;
    private CardView logcard;
    protected String blockuserchk="";
    SendMailTask sendMailTask=null;
    private  int acounter=0;
    private Boolean connection,check=false,loginstate=false,blockuser=false;
    ImageView signupdialogclose,report,idverficdialogclose;
    ImageButton adharcard,collegecard;
    RelativeLayout Internetmessagelayout;
    ScrollView scrollView;
    FrameLayout frgtfrmlayout;
    CheckBox agreement;
    CardView loginslccardview;
    Dialog signuppannel, loginframedialog,loginselectdialog,Dialog;
    Spinner signupDeptlist,signupbranchlist,loginbranchlist,IDDept,IDbranch,IDsem,IDsec;
    String [] defaultitem ={"Branch"};
    String [] Department ={"Department","CEC","COE","OTHER"};
    String [] CECbranches = {"Branch","BTECH-ME","BTECH-ECE","BTECH-CSE","BTECH-IT"};
    String [] COEbranches ={"Branch","BTECH-ME","BTECH-CSE"};
    String [] OTHERbranches ={"Branch","B-pharma","M-pharma","B-HMCT","B-ATHM","Bsc-BioTech","Msc-BioTech","BBA","MBA","BCA","MCA","B-ED"};
    String [] sems ={"Sem","1","2","3","4","5","6","7","8"};
    String [] Sections = {"Sec","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"};
    ArrayAdapter listdefaultval,listDeptartment,listadapcecbranch,listadapcocbranch,listadapotherbranch,listadapsec,listadapsem;
    Snackbar snackbar;
    String EmailID;
    private  String user="";
    private  String pass="";
    private  String dataloc="";
    private  String depart ="";
    private  String branch="";
    private  String sem="";
    private  String sec="";
    private  String type="adhar";
    FirebaseMessaging fm;
    private SharedPreferences sp;
    private   SharedPreferences.Editor Ed;
    private Random random = new Random();
    RadioButton maleradio,femaleradio,radiocec,radiocoe,radioother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = FirebaseMessaging.getInstance();
        blockuserchk = getIntent().getExtras().getString("blockuser").trim();
        setContentView(R.layout.login_frame);
        userid = (EditText) findViewById(R.id.uid);
        password = (EditText) findViewById(R.id.pass);
        loginbutton = (Button) findViewById(R.id.bt1);
        signupbutton = (Button) findViewById(R.id.bt2);
        Internetmessagelayout = (RelativeLayout) findViewById(R.id.layout2);
        scrollView = (ScrollView) findViewById(R.id.loginscroll);
        forgotpassbtn = (TextView) findViewById(R.id.forgotpass);
        logcard = (CardView) findViewById(R.id.logincard);
        logcard.setCardBackgroundColor(Color.parseColor("#0F060606"));
        hideshowpass = (CheckBox) findViewById(R.id.showpass);
        sp=getSharedPreferences("Login", 0);
        Ed=sp.edit();

        signuppannel = new Dialog(Login_frame.this);
        signuppannel.requestWindowFeature(Window.FEATURE_NO_TITLE);
        signuppannel.getWindow().setWindowAnimations(R.style.animateddialog);
        signuppannel.setCanceledOnTouchOutside(false);
        signuppannel.setCancelable(false);
        signuppannel.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        signuppannel.setContentView(R.layout.signuppage);
        signupDeptlist = (Spinner) signuppannel.findViewById(R.id.Dept);
        signupbranchlist = (Spinner) signuppannel.findViewById(R.id.branch);
        Reset = (Button) signuppannel.findViewById(R.id.resetall);
        report = (ImageView) signuppannel.findViewById(R.id.reportgen);

        listDeptartment = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, Department);
        signupDeptlist.setAdapter(listDeptartment);
        listdefaultval = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, defaultitem);
        signupbranchlist.setAdapter(listdefaultval);
        listadapcecbranch = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CECbranches);
        listadapcocbranch = new ArrayAdapter(this, android.R.layout.simple_spinner_item, COEbranches);
        listadapotherbranch = new ArrayAdapter(this, android.R.layout.simple_spinner_item, OTHERbranches);
        signupdialogclose = (ImageView) signuppannel.findViewById(R.id.signupdialogclose);
        signupdialogclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signuppannel.dismiss();
            }
        });
        head = (TextView) signuppannel.findViewById(R.id.notice);
        head.setSelected(true);

        loginframedialog = new Dialog(Login_frame.this);
        loginframedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loginframedialog.getWindow().setWindowAnimations(R.style.animateddialog);
        loginframedialog.setContentView(R.layout.layout);
        loginframedialog.setCanceledOnTouchOutside(false);
        loginframedialog.setCancelable(false);

        loginselectdialog = new Dialog(Login_frame.this);
        loginselectdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loginselectdialog.getWindow().setWindowAnimations(R.style.animateddialog);
        loginselectdialog.setContentView(R.layout.loginuseriddetail);
        loginslccardview = (CardView) loginselectdialog.findViewById(R.id.loginselectcardview);
        loginslccardview.setCardBackgroundColor(Color.parseColor("#0F060606"));

        Dialog = new Dialog(Login_frame.this);
        agreement = (CheckBox) signuppannel.findViewById(R.id.agreed);
        submit = (Button) signuppannel.findViewById(R.id.submitbutton);
        maleradio = (RadioButton) signuppannel.findViewById(R.id.male);
        femaleradio = (RadioButton) signuppannel.findViewById(R.id.female);
        fname = (EditText) signuppannel.findViewById(R.id.firstname);
        lname = (EditText) signuppannel.findViewById(R.id.lastname);
        eid = (EditText) signuppannel.findViewById(R.id.emailid);
        spass = (EditText) signuppannel.findViewById(R.id.pass);
        scpass = (EditText) signuppannel.findViewById(R.id.confirmpass);
        rno = (EditText) signuppannel.findViewById(R.id.universityrollno);
        mobno = (EditText) signuppannel.findViewById(R.id.phoneno);
        jobprofile = (EditText) signuppannel.findViewById(R.id.jobpro);

    }

private void uploadsignupdetail(String ref)
{
    loginframedialog.show();
    singnupref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+signupDeptlist.getSelectedItem()+"/"+signupbranchlist.getSelectedItem());
    singnupref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.child(rno.getText()+ "").exists())
            {
                check = true;

            }

            if (check==true) {
                    loginframedialog.dismiss();
                    AlertDialog.Builder showsignupmsgdialog = new AlertDialog.Builder(Login_frame.this);
                    showsignupmsgdialog.setTitle("Error to Sign up");
                    showsignupmsgdialog.setMessage("This ID is already Exists.\n\nIn case your ID is not Registererd by you can genrate by clicking red button at the End of Signup Dialog\n\nThank You!!");
                    showsignupmsgdialog.setPositiveButton("OK", null);
                    showsignupmsgdialog.show();
                    check = false;
                }

                else
                {
                    uploaddata();

                }

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }

    });

}

private void uploaddata()
{
    Firebase userref = singnupref.child(rno.getText().toString());

    Firebase setname = userref.child("Name");
    setname.setValue(fname.getText().toString()+" "+lname.getText().toString());

    Firebase setpropic = userref.child("propic");
    setpropic.setValue("N/A");

    Firebase setemailid = userref.child("Email ID");
    setemailid.setValue(eid.getText().toString());

    Firebase setpassword = userref.child("Password");
    setpassword.setValue(spass.getText().toString());

    Firebase setmobilenumber = userref.child("Mobile Number");
    setmobilenumber.setValue(mobno.getText().toString());

    Firebase setjobprofile = userref.child("Job Profile");
    setjobprofile.setValue(jobprofile.getText().toString());

    Firebase setrollnumber = userref.child("Empid");
    setrollnumber.setValue(rno.getText().toString());

    if (signupbranchlist.getSelectedItem().equals("Branch")) {
        Toast.makeText(Login_frame.this, "select the Branch", Toast.LENGTH_SHORT).show();
    }

    else {
        Firebase setBranch = userref.child("Branch");
        setBranch.setValue(signupbranchlist.getSelectedItem());
    }

    Firebase setstatus = userref.child("Status");
    setstatus.setValue("pending");

    Firebase setgender = userref.child("Gender");

    if (maleradio.isChecked() == true) {
        setgender.setValue(maleradio.getText().toString());
    } else {
        setgender.setValue(femaleradio.getText().toString());
    }
    loginframedialog.dismiss();
    resetall();
}

private void makesnackbar()
{
    snackbar = Snackbar.make(Internetmessagelayout, " First Choose the following :", Snackbar.LENGTH_INDEFINITE);
    snackbar.setAction("click here", new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loginselectdialog.setCanceledOnTouchOutside(false);
            loginselectdialog.setCancelable(false);
            loginbranchlist= (Spinner)loginselectdialog.findViewById(R.id.loginbranch);
            radiocec = (RadioButton) loginselectdialog.findViewById(R.id.radiocec);
            radiocoe = (RadioButton) loginselectdialog.findViewById(R.id.radiocoe);
            radioother = (RadioButton) loginselectdialog.findViewById(R.id.radioother);
            Done= (Button) loginselectdialog.findViewById(R.id.setdata);
            radiocec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginbranchlist.setAdapter(listadapcecbranch);
                    depart=radiocec.getText().toString();
                }
            });


            radiocoe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginbranchlist.setAdapter(listadapcocbranch);
                    depart=radiocec.getText().toString();
                }
            });

            radioother.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginbranchlist.setAdapter(listadapotherbranch);
                    depart=radiocec.getText().toString();
                }
            });

            Done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(radiocec.isChecked()==true||radiocoe.isChecked()==true|| radioother.isChecked()==true) {
                        if (loginbranchlist.getSelectedItemPosition() == 0) {
                            Toast.makeText(Login_frame.this, "Kindly choose the valid format", Toast.LENGTH_SHORT).show();
                        } else {
                            branch=loginbranchlist.getSelectedItem().toString();
                            dataloc = depart + "/" + branch;
                            loginselectdialog.dismiss();
                            snackbar.show();
                        }
                    }

                 else
                    {
                        Toast.makeText(Login_frame.this,"Kindly choose the valid format",Toast.LENGTH_SHORT).show();
                    }

                }
            });
            loginselectdialog.show();
        }
    });
    snackbar.show();
}

    private void  Networkcheck()
    {
                ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()==true ) {
                        makesnackbar();
                    connection=true;
                }
                else
                {
                    NetworkErrorsnackbar();
                    connection=false;
                }

    }

    private void NetworkErrorsnackbar()
    {
        snackbar = Snackbar.make(Internetmessagelayout, " No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.RED);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Networkcheck();
            }
        });
        snackbar.show();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        Networkcheck();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Networkcheck();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Networkcheck();

    }

    private void resetall()
    {
        fname.setText("");
        lname.setText("");
        eid.setText("");
        spass.setText("");
        scpass.setText("");
        rno.setText("");
        mobno.setText("");
        fname.setError(null);
        lname.setError(null);
        eid.setError(null);
        spass.setError(null);
        scpass.setError(null);
        rno.setError(null);
        mobno.setError(null);
        maleradio.setChecked(true);
        femaleradio.setChecked(false);
        agreement.setChecked(false);
        submit.setEnabled(false);
        ((TextView) signupDeptlist.getChildAt(0)).setError(null);
        ((TextView) signupbranchlist.getChildAt(0)).setError(null);
        submit.setBackgroundColor(Color.parseColor("#a7e0ec"));
        signupDeptlist.setSelection(0);
        signupbranchlist.setSelection(0);
    }


    private void firebasedataread()
    {


        if(userid.getText().toString().trim().length() == 0)
        {
          userid.setError("is Empty");
            acounter+=1;
        }

        if(password.getText().toString().trim().length() == 0)
        {
            password.setError("is Empty");
            acounter+=1;
        }


        if(acounter==0) {
            blockuserref = new Firebase("https://cgc-database.firebaseio.com/Blockedusers");
            blockuserref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(userid.getText().toString().trim()).exists())
                    {
                        blockuser=true;
                    }

                    else
                    {
                        blockuser=false;
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            checkref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+dataloc);
            checkref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(userid.getText() + "").exists()) {
                        mref = new Firebase("https://cgc-database.firebaseio.com/Faculty/" + dataloc + "/" + userid.getText() + "/" + "Empid");
                        mref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                user = dataSnapshot.getValue(String.class);
                                passref = new Firebase("https://cgc-database.firebaseio.com/Faculty/" + dataloc + "/" + userid.getText() + "/" + "Password");
                                passref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        loginframedialog.dismiss();
                                        pass = dataSnapshot.getValue(String.class);
                                        if (!user.equals(userid.getText().toString())) {
                                            acounter+=1;
                                            userid.setError("Username Not Match");
                                            user="";
                                        }

                                        if (!pass.equals(password.getText().toString()))
                                        {
                                            acounter+=1;
                                            password.setError("Password not match");
                                            user="";
                                        }

                                        if (acounter==0)
                                        {
                                            if (blockuser == true) {
                                                Intent strt = new Intent(getApplicationContext(), Login_frame.class);
                                                finish();
                                                strt.putExtra("blockuser","true");
                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                startActivity(strt);
                                            }

                                            else {
                                                Intent strt = new Intent(getApplicationContext(), MainFrame.class);
                                                finish();
                                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                                strt.putExtra("addr", dataloc + "/" + userid.getText().toString());
                                                Ed.putString("userid", userid.getText().toString());
                                                Ed.putString("password", pass);
                                                Ed.putString("sem","1A");
                                                Ed.putString("dataloc", dataloc);
                                                Ed.commit();
                                                startActivity(strt);

                                            }
                                        }

                                        acounter=0;


                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                Toast.makeText(getApplication(), "Server time error please contact the service provider", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        loginframedialog.dismiss();
                        userid.setError("Invalid Rollno");
                        password.setError("Invalid password");
                }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        else
        {
            loginframedialog.dismiss();
            acounter=0;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(connection==true)
                {

                    if(userid.getText().toString().trim().length() == 0)
                    {
                        userid.setError("is Empty");
                        acounter+=1;
                    }

                    if(password.getText().toString().trim().length() == 0)
                    {
                        password.setError("is Empty");
                        acounter+=1;
                    }

                    if(dataloc.isEmpty())
                    {
                        Dialog d = new Dialog(Login_frame.this);
                        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        d.getWindow().setWindowAnimations(R.style.animateddialog);
                        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        d.setContentView(R.layout.downpointlayout);
                        d.show();
                        acounter+=1;
                    }

                    if(user.equals("")&& dataloc.contains("/") && acounter==0)
                    {
                        loginframedialog.show();
                        firebasedataread();
                        loginstate=true;
                    }


                    acounter=0;
                }
                else
                {
                    Toast.makeText(Login_frame.this,"Please turn on the intrenet (WIFI / Data Connection) to use this service",Toast.LENGTH_SHORT).show();
                }


            }
        });


        hideshowpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                      password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }

                else
                {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        forgotpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userid.getText().toString().trim().length() == 0)
                {
                    userid.setError("is Empty");
                    acounter+=1;
                }

                if(dataloc.isEmpty())
                {
                    Dialog d = new Dialog(Login_frame.this);
                    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    d.getWindow().setWindowAnimations(R.style.animateddialog);
                    d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    d.setContentView(R.layout.downpointlayout);
                    d.show();
                    acounter+=1;
                }

                if(acounter==0) {

                    checkref = new Firebase("https://cgc-database.firebaseio.com/Faculty/" + dataloc);
                    loginframedialog.show();
                    checkref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                       @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(userid.getText() + "").exists()) {
                            mref = new Firebase("https://cgc-database.firebaseio.com/Faculty/" + dataloc + "/" + userid.getText() + "/" + "Email ID");
                            mref.addValueEventListener(new ValueEventListener() {
                               @Override
                          public void onDataChange(DataSnapshot dataSnapshot) {
                                 EmailID = dataSnapshot.getValue(String.class);
                                 String toEmails = EmailID;
                                   String otp = generateotp();
                                 List<String> toEmail = Arrays.asList(toEmails.split("\\s*,\\s*"));
                                   loginframedialog.dismiss();
                                      sendMailTask = new SendMailTask(Login_frame.this);
                                   sendMailTask.execute("contact.dreamhunterztech@gmail.com",
                                           "Xdreamers77",toEmail,"Reset Password","<html>Note : Please don't share this code to anybody<br><br><body align=center><table border=1 ><th> OTP CODE </th> <tr border=0>&nbsp;"+otp+"</tr></table></body></html>",EmailID,dataloc,userid.getText().toString(),otp);
                                   EmailID ="";

                                                                   }

                                          @Override
                                           public void onCancelled(FirebaseError firebaseError) {
                                                         Toast.makeText(getApplication(), "Server time error please contact the service provider", Toast.LENGTH_SHORT).show();
                                                                   }
                                                               });
                                                           } else {
                                                               loginframedialog.dismiss();
                                                               userid.setError("Invalid Emp_ID");
                                                           }
                                                       }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Toast.makeText(getApplicationContext(),"data not found",Toast.LENGTH_SHORT).show();
                        }

                    });
                }


                        acounter=0;


            }

        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder signupmsg = new AlertDialog.Builder(Login_frame.this);
                signupmsg.setTitle("Message");
                signupmsg.setMessage("\nTo Generate Report for ID Login Error PLease click on mail us\n\nNote:\n\n1) you have to attached your College ID and Adharcard picture/Image Along with it.\n\n2) This is only for verification we ensure you about any misuse.\n\nThank YOU!!!!");
                signupmsg.setPositiveButton("mail us", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "contact.dreamhunterztech@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reset Account");
                        startActivity(Intent.createChooser(emailIntent,"Choose mail app to proceed"));
                    }
                });
                signupmsg.setNegativeButton("cancel",null);
                signupmsg.show();


            }
        });

        agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (agreement.isChecked()==true) {
                    if (fname.getText().toString().trim().length() == 0) {
                        fname.setError("is Empty");
                        acounter+=1;
                    }
                    if (lname.getText().toString().trim().length() == 0) {
                        lname.setError("is Empty");
                        acounter+=1;
                    }

                    if (eid.getText().toString().trim().length() == 0) {
                        eid.setError("is Empty");
                        acounter+=1;
                    }
                    else if(!eid.getText().toString().contains("@")&&!eid.getText().toString().contains(".")) {
                        eid.setError("Invalid Email format");
                    }

                    if (spass.getText().toString().trim().length() == 0) {
                        spass.setError("is Empty");
                        acounter+=1;
                    }

                    if (scpass.getText().toString().trim().length() == 0) {
                        scpass.setError("is Empty");
                        acounter+=1;
                    }

                    if (!spass.getText().toString().equals(scpass.getText().toString()))
                    {
                        scpass.setError("password is not matched");
                        acounter+=1;
                    }

                    if (rno.getText().toString().trim().length() == 0) {
                        rno.setError("is Empty");
                        acounter+=1;
                    }

                    if (mobno.getText().toString().trim().length() == 0) {
                        mobno.setError("is Empty");
                        acounter+=1;
                    }

                    if (mobno.getText().toString().trim().length() < 10) {
                        mobno.setError("number less than 10");
                        acounter+=1;
                    }

                    if (mobno.getText().toString().trim().length() == 0) {
                        mobno.setError("is Empty");
                        acounter+=1;
                    }

                    if (signupDeptlist.getSelectedItem().equals("Department")) {
                        ((TextView) signupDeptlist.getChildAt(0)).setError("Invalid Selection");
                    }
                    if (signupbranchlist.getSelectedItem().equals("Branch")) {
                        ((TextView) signupbranchlist.getChildAt(0)).setError("Invalid Selection");
                        acounter+=1;
                    }


                    if(acounter==0)
                    {
                        submit.setEnabled(true);
                        submit.setBackgroundColor(Color.parseColor("#14cef5"));
                        AlertDialog.Builder signupmsg = new AlertDialog.Builder(Login_frame.this);
                        signupmsg.setTitle("Term and Conditions");
                        signupmsg.setMessage(Html.fromHtml ("<!DOCTYPE html><html> <body> <p><b>By downloading or using the app, these terms will automatically apply to you –<br>you should make sure therefore that you read them carefully before using the app.</b><br><br> You’re not allowed to copy, or modify the app, any part of the app, or our trademarks in any way. <p>You’re not allowed to attempt to extract the source code of the app, and you also shouldn’t try to translate the app into other languages, or make derivative versions.The app itself, and all the trade marks, copyright, database rights and other intellectual property rights related to it, still belong to Dream Hunterz Tech.</p> <p>Dream Hunterz Tech is committed to ensuring that the app is as useful and efficient as possible. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it very clear to you exactly what you’re paying for.</p>  <p>The CGC Student Portal app stores and processes personal data that you have provided to us, in order to provide my Service. It’s your responsibility to keep your phone and access to the app secure. We therefore recommend that you do not jailbreak or root your phone, which is the process of removing software restrictions and limitations imposed by the official operating system of your device. It could make your phone vulnerable to malware/viruses/malicious programs, compromise your phone’s security features and it could mean that the CGC Student Portal app won’t work properly or at all. </p> <p>You should be aware that there are certain things that Dream Hunterz Tech will not take responsibility for. Certain functions of the app will require the app to have an active internet connection. The connection can be Wi-Fi, or provided by your mobile network provider, but Dream Hunterz Tech cannot take responsibility for the app not working at full functionality if you don’t have access to Wi-Fi, and you don’t have any of your data allowance left.</p><p></p><p>If you’re using the app outside of an area with Wi-Fi, you should remember that your terms of the agreement with your mobile network provider will still apply. As a result, you may be charged by your mobile provider for the cost of data for the duration of the connection while accessing the app, or other third party charges. In using the app, you’re accepting responsibility for any such charges, including roaming data charges if you use the app outside of your home territory (i.e. region or country) without turning off data roaming. If you are not the bill payer for the device on which you’re using the app, please be aware that we assume that you have received permission from the bill payer for using the app.</p>  <p>Along the same lines, Dream Hunterz Tech cannot always take responsibility for the way you use the app i.e. You need to make sure that your device stays charged – if it runs out of battery and you can’t turn it on to avail the Service, Dream Hunterz Tech cannot accept responsibility</p> <p>With respect to Dream Hunterz Tech’s responsibility for your use of the app, when you’re using the app, it’s important to bear in mind that although we endeavour to ensure that it is updated and correct at all times, we do rely on third parties to provide information to us so that we can make it available to you. Dream Hunterz Tech accepts no liability for any loss, direct or indirect, you experience as a result of relying wholly on this functionality of the app.</p>  <p> At some point, we may wish to update the app. The app is currently available on Android  – the requirements for both systems (and for any additional systems we decide to extend the availability of the app to) may change, and you’ll need to download the updates if you want to keep using the app. Dream Hunterz Tech does not promise that it will always update the app so that it is relevant to you and/or works with the Android version that you have installed on your device. However, you promise to always accept updates to the application when offered to you, We may also wish to stop providing the app, and may terminate use of it at any time without giving notice of termination to you. Unless we tell you otherwise, upon any termination, (a) the rights and licenses granted to you in these terms will end; (b) you must stop using the app, and (if needed) delete it from your device.</p> <p><strong>Changes to This Terms and Conditions</strong></p> <p> I may update our Terms and Conditions from time to time. Thus, you are advised to review\n" +
                                "                      this page periodically for any changes. I will notify you of any changes by posting\n" +
                                "                      the new Terms and Conditions on this page. These changes are effective immediately after they are posted on\n" +
                                "                      this page.\n" +
                                "                    </p> <p><strong>Contact Us</strong></p> <p>If you have any questions or suggestions about my Terms and Conditions, do not hesitate to\n" +
                                "  <br><a href = \"mailto: contact.dreamhunterz@gmail.com\">Mail us</a> on <br>contact.dreamhunterztech@gmail.com</p></body></html>"));
                        signupmsg.setPositiveButton("OK",null);
                        signupmsg.show();

                    }
                    else
                    {
                        agreement.setChecked(false);
                        acounter=0;
                    }
                }

                else
                {
                    submit.setEnabled(false);
                    submit.setBackgroundColor(Color.parseColor("#a7e0ec"));
                }
            }
        });

        signupDeptlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 1:
                        signupbranchlist.setAdapter(listadapcecbranch);
                        break;
                    case 2:
                        signupbranchlist.setAdapter(listadapcocbranch);
                        break;
                    case 3:
                        signupbranchlist.setAdapter(listadapotherbranch);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetall();
            }
        });
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signuppannel.show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connection==true) {
                    String ref = rno.getText().toString();
                    uploadsignupdetail(ref);
                    signuppannel.dismiss();
                    AlertDialog.Builder signupmsg = new AlertDialog.Builder(Login_frame.this);
                    signupmsg.setTitle("Sucessfully Signed up");
                    signupmsg.setMessage("\nYou were Sucessfully Signed Up to our service.\n\nNow you can Login to Our Service\n\nThank YOU!!!!");
                    signupmsg.setPositiveButton("OK",null);
                    signupmsg.show();
                }

                else
                {
                    Toast.makeText(Login_frame.this,"Please turn on the intrenet (WIFI/Data Connection) to use this service",Toast.LENGTH_SHORT).show();
                }
            }
        });

        femaleradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleradio.setChecked(false);
            }
        });

        maleradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleradio.setChecked(false);
            }
        });

        Networkcheck();

        if (blockuserchk.equals("true"))
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(Login_frame.this);
            dialog.setTitle("Acess denied");
            dialog.setMessage("you are blocked by Service Provider \n\nplease  mail us for further Query  on contact.dreamhunterztech@gmail.com \n\nThank You!!");
            dialog.setPositiveButton("Ok", null);
            dialog.show();
            getApplicationContext().getSharedPreferences("Login", 0).edit().clear().commit();
            blockuserchk="false";
        }


    }




    private String generateotp() {
        final String uCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String intChar = "0123456789";
        Random r = new Random();
        String pass = "";
        while (pass.length() != 6) {
            int rPick = r.nextInt(2);

            if (rPick == 0) {
                int spot = r.nextInt(25);
                pass += uCase.charAt(spot);

            } else if (rPick == 1) {
                int spot = r.nextInt(9);
                pass += intChar.charAt(spot);
            }
        }
        return pass;
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==0 && resultCode == RESULT_OK)
        {
            imgpath = data.getData();
            CropImage.activity(imgpath).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
             type="adhar";
        }


        if (requestCode ==1 && resultCode == RESULT_OK) {

            imgpath = data.getData();
            CropImage.activity(imgpath).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
            type = "ClgID";
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgpath = result.getUri();
            if (type.equals("adhar"))
            {
                adharcard.setImageURI(imgpath);
            }
            else
            {
                collegecard.setImageURI(imgpath);
            }

        }


    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loginstate.equals(true)) {
            mref.keepSynced(false);
            checkref.keepSynced(false);
            passref.keepSynced(false);
        }

        loginstate=false;

    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void requestpermission() {
    }



    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void requestpermissiononshow(final PermissionRequest request) {
        new AlertDialog.Builder(Login_frame.this)
                .setMessage("Enable Request Permission")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       request.proceed();
                    }
                }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request.cancel();
            }
        }).show();
    }


    public void closeasynctask()
    {
        try {
            sendMailTask.cancel(true);
        }

        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

    }



    /*   public void uploadadharcard(final Uri fileuri,String filename)
    {
        StorageReference storageref = storage.getReferenceFromUrl("gs://test-e713f.appspot.com/Library/"+filename+"/adharcard");
        StorageReference filepath =  storageref.child(filename);
        filepath.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String fileuri = taskSnapshot.getMetadata().getDownloadUrl().toString();
                uploadadharcarddetail(fileuri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                String fileinfo =  taskSnapshot.getBytesTransferred()+"/"+taskSnapshot.getTotalByteCount()+" bytes";
                IDNotification.setProgress(100,progress.intValue(),false);
                IDNotification.setContentTitle(filename);
                IDNotification.setContentInfo(fileinfo);
                IDNotification.setContentText("Uploading");
                notificationManager.notify(1, IDNotification.build());
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                IDNotification.setContentText("Done");
                notificationManager.notify(1,IDNotification.build());
            }
        });
    }



    public void uploadadharcarddetail(String propicdir)
    {
       Firebase  dataref = new Firebase("https://test-e713f.firebaseio.com/Library/"+selectlibtype.getSelectedItem()+"/imgurl");
        dataref.setValue(propicdir);
    }

    public void uploadcollegiddetail(String filedir)
    {
        dataref = new Firebase("");
        dataref.setValue(filedir);
    }*/


   /* private void resetid()
    {
        AlertDialog.Builder msgshow = new AlertDialog.Builder(Login_frame.this);
        msgshow.setTitle("Message");
        msgshow.setMessage("This Feature is Unaviable for now we will provide it soon. You can send mail to contact.dreamhunterztech@gmail.com\n\nNote :Please submit adhar card and college card photo along with it for verification after that you will recieved a reply mail from us.\n\nThank You!!!!");
        msgshow.setPositiveButton("OK",null);
        msgshow.show();
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog.getWindow().setWindowAnimations(R.style.animateddialog);
        Dialog.setCanceledOnTouchOutside(false);
        Dialog.setCancelable(false);
        Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Dialog.setContentView(R.layout.idverificationlayout);
        idverficdialogclose = (ImageView) Dialog.findViewById(R.id.idverificdialogclose);
        adharcard = (ImageButton) Dialog.findViewById(R.id.adharcarduploadbtn);
        collegecard = (ImageButton) Dialog.findViewById(R.id.collegeidcarduploadbtn);
        IDDept = (Spinner) Dialog.findViewById(R.id.IDveriDept);
        IDbranch = (Spinner) Dialog.findViewById(R.id.IDveribranch);
        IDsem = (Spinner) Dialog.findViewById(R.id.IDverisem);
        IDsec = (Spinner) Dialog.findViewById(R.id.IDverisection);
        uploadIDbtn = (Button) Dialog.findViewById(R.id.IDveriuploadbtn);
        IDDept.setAdapter(listDeptartment);
        IDbranch.setAdapter(listdefaultval);
        IDsec.setAdapter(listadapsec);
        IDsem.setAdapter(listadapsem);
        IDDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 1:
                        IDbranch.setAdapter(listadapcecbranch);
                        break;
                    case 2:
                        IDbranch.setAdapter(listadapcocbranch);
                        break;
                    case 3:
                        IDbranch.setAdapter(listadapotherbranch);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        uploadIDbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder signupmsg = new AlertDialog.Builder(Login_frame.this);
                signupmsg.setTitle("Message");
                signupmsg.setMessage("\nReport is Generated for Resetting Account We will Proceed it soon within 24 hrs.\n\nThank YOU!!!!");
                signupmsg.setPositiveButton("OK",null);
                signupmsg.show();
            }
        });

        idverficdialogclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.dismiss();
            }
        });

        adharcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT, provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                fileintent.setType("image*//*");
                startActivityForResult(fileintent,0);
            }
        });

        collegecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgintent = new Intent(Intent.ACTION_GET_CONTENT);
                imgintent.setType("image*//*");
                startActivityForResult(imgintent,1);
            }
        });

        Dialog.show();
    }*/

                /*  private void uploadadharcardpic(final Uri fileuri)
    {
        StorageReference storageref = libstorage.getReferenceFromUrl("gs://test-e713f.appspot.com/Library/"+selectlibtype.getSelectedItem()+"/Coverpageimage");
        StorageReference filepath =  storageref.child(Bookname.getText().toString());
        filepath.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String imageuri = taskSnapshot.getMetadata().getDownloadUrl().toString();
                uploadimagedetail(imageuri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void uploadclgcard(final Uri fileuri)
    {

    }*/

}






