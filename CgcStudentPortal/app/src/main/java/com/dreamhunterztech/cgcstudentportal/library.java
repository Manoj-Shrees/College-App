package com.dreamhunterztech.cgcstudentportal;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.xiaochen.progressroundbutton.AnimDownloadProgressButton;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by Dreamer on 27-06-2017.
 */

public class library extends Fragment
{
    FloatingActionButton uploadfab;
    Dialog uploaddialog;
    ImageView libuploaddialogclose,bookcoverbtn;
    FloatingSearchView booksearch;
    Spinner typelistselection;
    ArrayAdapter  typelistadap;
    ImageButton bookfile;
    RadioButton rb1,rb2,rb3,rb4,rb5,rb6,rb7,rb8,rb9,rb10,rb11,rb12,rb13,rb14,rb15;
    Button upbtn;
    TextView filenametxt,upbookname,upbookauther,upbookdescp;
    ScrollView scrollView;
    Firebase libref;
    Boolean  imgnull=true;
    int errcount=0;
    Uri fileuri=null,imageuri=null;
    private String uploaderdata,addrs;
    private String mLastQuery = "";
    RadioGroup rg;
    private String selectiontype=null;
    ProgressBar upprogress;
    ArrayList<String> ids;
    NotificationCompat.Builder uploadNotification;
    NotificationManager notificationManager;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private String [] booktypelist = {"Engineering ( ME )","Engineering ( ECE )","Engineering ( CSE and IT )","Pharmacy ( B-pharma )","Pharmacy ( M-pharma )","Hotel Management ( B-HMCT )","Hotel Management ( B-ATHM )","Biotechnology ( Bsc-BioTech )","Biotechnology (Msc-BioTech )","Management ( BBA )","Management ( MBA )","Computer Application ( BCA )","Computer Application ( MCA )","Education ( B-ED )","Sample & Previous year Question Papers"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.librarylayout,container, false);
        addrs = getActivity().getIntent().getExtras().getString("addr");
        uploadfab = (FloatingActionButton) view.findViewById(R.id.libuploadbtn);
        booksearch = (FloatingSearchView) view.findViewById(R.id.Searchbar);
        libref = new Firebase(getActivity().getString(R.string.dburl)+"Library/libfilerecord");

        rb1 = view.findViewById(R.id.librb1);
        rb2 = view.findViewById(R.id.librb2);
        rb3 = view.findViewById(R.id.librb3);
        rb4 = view.findViewById(R.id.librb4);
        rb5 = view.findViewById(R.id.librb5);
        rb6 = view.findViewById(R.id.librb6);
        rb7 = view.findViewById(R.id.librb7);
        rb8 = view.findViewById(R.id.librb8);
        rb9 = view.findViewById(R.id.librb9);
        rb10 = view.findViewById(R.id.librb10);
        rb11 = view.findViewById(R.id.librb11);
        rb12 = view.findViewById(R.id.librb12);
        rb13 = view.findViewById(R.id.librb13);
        rb14 = view.findViewById(R.id.librb14);
        rb15 = view.findViewById(R.id.librb15);
        rg = view.findViewById(R.id.librg);
        ids = new ArrayList<String>();


        try {
            final Bitmap notificicon = BitmapFactory.decodeResource(getResources(),R.drawable.cgclogonotific);
            final Intent emptyIntent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            uploadNotification = new NotificationCompat.Builder(getContext())
                    .setProgress(0,0,true)
                    .setSmallIcon(R.drawable.detainlistnotiifcon,30)
                    .setContentTitle("Filename")
                    .setContentText("processing")
                    .setLargeIcon(notificicon)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setColor(Color.parseColor("#c4ef0404"))
                    .setContentInfo("counting");

            notificationManager = (NotificationManager) getContext().getSystemService(getActivity().NOTIFICATION_SERVICE);

            }

            catch (Exception e)
            {
                e.printStackTrace();
        }



        uploadfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final Dialog  d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().setWindowAnimations(R.style.animateddialog);
                d.setCanceledOnTouchOutside(false);
                d.setCancelable(false);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.setContentView(R.layout.libdialoglayout);
                ImageView dialogclose = (ImageView) d.findViewById(R.id.libdialogclose);
                TextView bookupd = (TextView) d.findViewById(R.id.bookupload);
                TextView bookdataupda = (TextView) d.findViewById(R.id.bookdataupdate);
                TextView bookoffline = (TextView) d.findViewById(R.id.bookdatadel);

                bookupd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bookupload();
                        d.dismiss();
                    }
                });

                bookdataupda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent  strt = new Intent(getContext(), libbookmange.class);
                        startActivity(strt);
                    }
                });

                bookoffline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent  strt = new Intent(getContext(), offlinefile.class);
                        startActivity(strt);
                    }
                });

                dialogclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                d.show();

            }
        });

        booksearch.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals(""))
                {
                    booksearch.clearSuggestions();
                }

                else
                {

                    booksearch.showProgress();
                    DataHelper.findSuggestions(getActivity(), newQuery, 5,
                            250, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<ColorSuggestion> results) {

                                    //this will swap the data and
                                    //render the collapse/expand animations as necessary
                                    booksearch.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    booksearch.hideProgress();
                                }
                            });

                }

            }
        });


        booksearch.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                mLastQuery = searchSuggestion.getBody();
                getbookdata(mLastQuery);
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                if(query.isEmpty())
                {
                    Toast.makeText(getContext(),"Book name is necessary",Toast.LENGTH_SHORT).show();
                }

                else {
                    getbookdata(query);
                }


            }
        });

        booksearch.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)
                booksearch.swapSuggestions(DataHelper.getHistory(getActivity(), 3));
                rg.setVisibility(View.GONE);
                uploadfab.setVisibility(View.GONE);
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                booksearch.setSearchBarTitle(mLastQuery);
                rg.setVisibility(View.VISIBLE);
                uploadfab.setVisibility(View.VISIBLE);
                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns


            }
        });

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectiontype=null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out);
                transaction.replace(R.id.libframe,new libmainpage());
                transaction.commit();
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb2.getText().toString().trim());
                selectiontype=rb2.getText().toString().trim();
            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb3.getText().toString().trim());
                selectiontype=rb3.getText().toString().trim();
            }
        });

        rb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb4.getText().toString().trim());
                selectiontype=rb4.getText().toString().trim();
            }
        });

        rb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb5.getText().toString().trim());
                selectiontype=rb5.getText().toString().trim();
            }
        });


        rb6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb6.getText().toString().trim());
                selectiontype=rb6.getText().toString().trim();
            }
        });

        rb7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb7.getText().toString().trim());
                selectiontype=rb7.getText().toString().trim();
            }
        });

        rb8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb8.getText().toString().trim());
                selectiontype=rb8.getText().toString().trim();
            }
        });

        rb9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb9.getText().toString().trim());
                selectiontype=rb9.getText().toString().trim();
            }
        });


        rb10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb10.getText().toString().trim());
                selectiontype=rb10.getText().toString().trim();
            }
        });

        rb11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb11.getText().toString().trim());
                selectiontype=rb11.getText().toString().trim();
            }
        });

        rb12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb12.getText().toString().trim());
                selectiontype=rb12.getText().toString().trim();
            }
        });

        rb13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb13.getText().toString().trim());
                selectiontype=rb13.getText().toString().trim();
            }
        });


        rb14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb14.getText().toString().trim());
                selectiontype=rb14.getText().toString().trim();
            }
        });


        rb15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getindividualbook(rb15.getText().toString().trim());
                selectiontype=rb15.getText().toString().trim();
            }
        });




        return view;
    }

    private void getbookdata(final String bookname)
    {
        Firebase url = new Firebase(getActivity().getString(R.string.dburl)+"Library/masterbookrecord");
        url.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(bookname).exists()) {

                    Firebase dataurl = new Firebase(getActivity().getString(R.string.dburl)+"Library/masterbookrecord/"+bookname+"/type");
                    dataurl.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            openbookdetail(dataSnapshot.getValue(String.class).toString());
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
                else
                {
                    Toast.makeText(getContext(),"Book is unavailable",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void openbookdetail(String addrs)
    {
      Intent  strt = new Intent(getContext(), libbookdetailactivity.class);
        strt.putExtra("bookaddrs",addrs);
        startActivity(strt);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(selectiontype == null)
        {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.libframe, new libmainpage());
            transaction.commit();
        }

        else
        {
            getindividualbook(selectiontype);
        }

    }

    private void getindividualbook(String data)
    {
        Bundle bundle = new Bundle();
        bundle.putString("bookurl",data);
        librarybookslist libindbooks = new librarybookslist();
        libindbooks.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.libframe,libindbooks);
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK)
        {
            fileuri = data.getData();
            filenametxt.setText(fileuri.getPath());
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageuri = data.getData();
            CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(12, 16).start(getContext(), this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resulturi = result.getUri();
            bookcoverbtn.setImageURI(resulturi);
            imgnull=false;

        }

        else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Toast.makeText(getActivity(), "image error to load", Toast.LENGTH_SHORT).show();
        }
    }


   private void uploadbookcoverpic(final Uri fileuri) {

       StorageReference storageref = storage.getReferenceFromUrl("gs://cgc-database.appspot.com");
       StorageReference filepath =  storageref.child("Library/"+typelistselection.getSelectedItem().toString().trim()+"/"+upbookname.getText().toString().trim()+"/"+"bookcoverpic");
       filepath.putFile(fileuri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

           }
       }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               String bookimgurl= taskSnapshot.getMetadata().getDownloadUrl().toString();
               uploadNotification.setContentText("Done");
               uploadNotification.setOngoing(false);
               notificationManager.notify(1, uploadNotification.build());
               updatepicdata(bookimgurl);
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               uploadNotification.setContentText("Failed");
               uploadNotification.setOngoing(false);
               notificationManager.notify(1, uploadNotification.build());
               Toast.makeText(getContext(),"Error ! to upload profile pic.",Toast.LENGTH_SHORT).show();
           }
       }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
               Long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
               String fileinfo = taskSnapshot.getBytesTransferred() + "/" + taskSnapshot.getTotalByteCount() + " bytes";
               uploadNotification.setProgress(100, progress.intValue(), false);
               uploadNotification.setContentInfo(fileinfo);
               uploadNotification.setContentText("Uploading");
               uploadNotification.setContentTitle("Library  cover Image : "+upbookname.getText());
               notificationManager.notify(1, uploadNotification.build());
           }
       });

   }

    private void uploadbookfile(final Uri fileuri) {
        StorageReference storageref = storage.getReferenceFromUrl("gs://cgc-database.appspot.com");
        StorageReference filepath =  storageref.child("Library/"+typelistselection.getSelectedItem().toString().trim()+"/"+upbookname.getText().toString().trim()+"/"+upbookname.getText().toString().trim());
        filepath.putFile(fileuri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               String  bookfileurl= taskSnapshot.getMetadata().getDownloadUrl().toString();
                uploadmasterbookdata();
                updatefiledate(bookfileurl);
                uploadNotification.setContentText("Done");
                uploadNotification.setOngoing(false);
                notificationManager.notify(2, uploadNotification.build());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                uploadNotification.setContentText("Failed");
                uploadNotification.setOngoing(false);
                notificationManager.notify(2, uploadNotification.build());
                Toast.makeText(getContext(),"Error ! to upload profile pic.",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                String fileinfo = taskSnapshot.getBytesTransferred() + "/" + taskSnapshot.getTotalByteCount() + " bytes";
                uploadNotification.setProgress(100, progress.intValue(), false);
                uploadNotification.setContentInfo(fileinfo);
                uploadNotification.setContentText("Uploading");
                uploadNotification.setContentTitle("Library file : "+upbookname.getText());
                notificationManager.notify(2, uploadNotification.build());
            }
        });

    }

    private void uploadmasterbookdata()
    {

        Firebase masterdataref = new Firebase(getActivity().getString(R.string.dburl)+"Library/masterbookrecord");
        Firebase dataref = masterdataref.child(upbookname.getText().toString());

        Firebase settype = dataref.child("type");
        settype.setValue(typelistselection.getSelectedItem().toString()+"/"+upbookname.getText().toString());
    }


    private void  getuserdetail()
    {
        Firebase getname= new Firebase(getActivity().getString(R.string.dburl)+addrs+"/Name");
        Firebase getrollno= new Firebase(getActivity().getString(R.string.dburl)+addrs+"/Rollno");

        getname.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String   data = dataSnapshot.getValue().toString();
                setuploaderdata(data,0);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        getrollno.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String   data = dataSnapshot.getValue().toString();
                setuploaderdata(data,1);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    private void setuploaderdata(String data,int chk)
    {
        if (chk==0)
        {
            uploaderdata=data;
        }

        else
        {
            uploaderdata += "\n"+data;
        }
    }


    private void updatepicdata(String url)
    {
        Firebase libtypedataref = libref.child(typelistselection.getSelectedItem().toString());
        Firebase libpicref =libtypedataref.child(upbookname.getText().toString());
        Firebase setpicurl = libpicref.child("bookcoverpic");
        setpicurl.setValue(url);
    }

    private void updatefiledate(String url)
    {
        Firebase libtypedataref = libref.child(typelistselection.getSelectedItem().toString());
        Firebase libfilerref =libtypedataref.child(upbookname.getText().toString());
        Firebase setfileurl= libfilerref.child("bookfileurl");
        setfileurl.setValue(url);
        upprogress.setVisibility(View.GONE);
        uploaddialog.dismiss();
        AlertDialog.Builder showsignupmsgdialog = new AlertDialog.Builder(getActivity());
        showsignupmsgdialog.setTitle("Sucessfully uploaded :");
        showsignupmsgdialog.setMessage("Your File will soon avialable within 24 hrs in App \n\nThank You!!");
        showsignupmsgdialog.setPositiveButton("OK", null);
        showsignupmsgdialog.show();
        fileuri=null;
        imageuri=null;
    }


    private void uploaddata() {
        Firebase libtypedataref = libref.child(typelistselection.getSelectedItem().toString());
        Firebase libdataref =libtypedataref.child(upbookname.getText().toString());;

        Firebase setbookname = libdataref.child("bookname");
        setbookname.setValue(upbookname.getText().toString().trim());

        Firebase setauthername = libdataref.child("bookauther");
        setauthername.setValue(upbookauther.getText().toString());

        Firebase setbooktype = libdataref.child("booktype");
        setbooktype.setValue(typelistselection.getSelectedItem().toString().trim());

        Firebase setbookdesc = libdataref.child("bookdesc");
        setbookdesc.setValue(upbookdescp.getText().toString());

        Firebase setbookuploader = libdataref.child("bookuploader");
        setbookuploader.setValue(addrs);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        Firebase setbookuploaddate = libdataref.child("bookuploaddate");
        setbookuploaddate.setValue(df.format(c.getTime())+" "+sdf.format(c.getTime()));

        }


        private  void bookupload()
        {
            uploaddialog = new Dialog(getActivity());
            uploaddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            uploaddialog.getWindow().setWindowAnimations(R.style.animateddialog);
            uploaddialog.setCanceledOnTouchOutside(false);
            uploaddialog.setCancelable(false);
            uploaddialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            uploaddialog.setContentView(R.layout.libebookuploadactivity);
            libuploaddialogclose = (ImageView) uploaddialog.findViewById(R.id.libuploaddialogclose);
            scrollView = uploaddialog.findViewById(R.id.libscroll);
            filenametxt = (TextView) uploaddialog.findViewById(R.id.libfiletxt);
            upbookname = (TextView) uploaddialog.findViewById(R.id.libbookname);
            upbookauther = (TextView) uploaddialog.findViewById(R.id.libauthername);
            upbookdescp = (TextView) uploaddialog.findViewById(R.id.libdesc);
            upbtn = (Button) uploaddialog.findViewById(R.id.libfileuploadbtn);
            upprogress = (ProgressBar) uploaddialog.findViewById(R.id.libuppbar);
            typelistselection = (Spinner) uploaddialog.findViewById(R.id.libtypeselecter);
            typelistadap = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,booktypelist);
            typelistselection.setAdapter(typelistadap);
            bookcoverbtn = (ImageView) uploaddialog.findViewById(R.id.Bookcoveruploadbtn);
            bookcoverbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickPhoto.setType("image/*");
                        startActivityForResult(pickPhoto, 1);
                    }
                    catch (Exception e)
                    {
                        final Dialog storagemessage = new Dialog(getContext());
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

            bookfile = (ImageButton) uploaddialog.findViewById(R.id.libopenfile);
            bookfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent pickfile = new Intent(Intent.ACTION_GET_CONTENT);
                        pickfile.setType("application/pdf");
                        startActivityForResult(pickfile, 0);
                    }
                    catch (Exception e)
                    {
                        final Dialog storagemessage = new Dialog(getContext());
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
            libuploaddialogclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploaddialog.dismiss();
                }
            });

            upbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
                    Matcher hasSpecial = special.matcher(upbookname.getText().toString().trim());

                    if(upbookname.getText().toString().trim().length() == 0)
                    {
                        upbookname.setError("is Empty");
                        errcount++;
                    }

                    if(hasSpecial.find() )
                    {
                        upbookname.setError("all should be in character");
                        errcount++;
                    }


                    if(upbookauther.getText().toString().trim().length() == 0)
                    {
                        upbookauther.setError("is Empty");
                        errcount++;
                    }

                    if(upbookdescp.getText().toString().trim().length() == 0)
                    {
                        upbookdescp.setError("is Empty");
                        errcount++;
                    }

                    if (filenametxt.getText().toString().trim().length() == 0)
                    {
                        Toast.makeText(getContext(),"File not Found\nplease select the file",Toast.LENGTH_SHORT).show();
                        errcount++;
                    }


                    if(errcount==0) {
                        libref.child(typelistselection.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(upbookname.getText() + "").exists()) {
                                    upbookname.setError("Book name is already in use");

                                }

                                else {
                                    upprogress.setVisibility(View.VISIBLE);
                                    upbtn.requestFocus();
                                    getuserdetail();
                                    if (imgnull == false)
                                        uploadbookcoverpic(imageuri);
                                    else
                                    updatepicdata(getActivity().getString(R.string.defaultimgurl)+"%2Fbrokenpic.png?alt=media&token=ba6b3c74-1676-450e-a318-fc7134a839fb");
                                    uploadbookfile(fileuri);
                                    uploaddata();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }

                    errcount=0;
                }
            });


            uploaddialog.show();
        }


}



