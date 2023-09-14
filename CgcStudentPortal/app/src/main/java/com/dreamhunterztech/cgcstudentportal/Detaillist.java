package com.dreamhunterztech.cgcstudentportal;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dreamers on 08-10-2016.
 */

public class Detaillist extends Fragment
{
    private String addrs = "";
    ExpandableListView expListView;
    ArrayList<String> menu;
    CardView cardView;
    List<String> fn1,fn2,fn3,fn4,fn5,fn6;
    HashMap<String,List<String>> listDataChild;
    expandabledetainlistadapter  listAdapter;
    RelativeLayout detainlistlayout;
    SharedPreferences sp;
    SharedPreferences.Editor dwned;
    Boolean notificstate=false;
    NotificationCompat.Builder eventsNotification;
    NotificationManager notificationManager;
    Boolean state=false;
    private File localFile;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.expandablelistlayout2,container,false);
        addrs = getActivity().getIntent().getExtras().getString("addr");
        addrs = addrs.substring(0,addrs.lastIndexOf("/"));
        detainlistlayout = (RelativeLayout) view.findViewById(R.id.Detainlayout);
        sp=getActivity().getSharedPreferences("Detailist", 0);
        sp.edit().putString("state","null");
        menu = new ArrayList<>();
        fn1= new ArrayList<String>();
        fn2= new ArrayList<String>();
        fn3= new ArrayList<String>();
        fn4= new ArrayList<String>();
        fn5= new ArrayList<String>();
        fn6= new ArrayList<String>();
        getfileaddrs();
        listDataChild = new HashMap<>();
        expListView = (ExpandableListView) view.findViewById(R.id.detainlistexplist);
        setarraylist();
        listAdapter = new expandabledetainlistadapter(getActivity(),menu, listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (state==false)
                {
                    if (groupPosition == 0) {
                        if (fn1.get(childPosition).equals("N/A"))
                        {
                            Toast.makeText(getContext(),"File Not Found or not Uploaded Yet.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                          downloadfile(fn1.get(childPosition));
                        }
                    }

                    if (groupPosition == 1) {
                        if (fn2.get(childPosition).equals("N/A"))
                        {
                            Toast.makeText(getContext(),"File Not Found or not Uploaded Yet.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            downloadfile(fn2.get(childPosition));
                        }
                    }

                    if (groupPosition == 2) {
                        if (fn3.get(childPosition).equals("N/A"))
                        {
                            Toast.makeText(getContext(),"File Not Found or not Uploaded Yet.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            downloadfile(fn3.get(childPosition));
                        }
                    }

                    if (groupPosition == 3) {
                        if (fn4.get(childPosition).equals("N/A"))
                        {
                            Toast.makeText(getContext(),"File Not Found or not Uploaded Yet.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            downloadfile(fn4.get(childPosition));
                        }
                    }

                    if (groupPosition == 4) {
                        if (fn5.get(childPosition).equals("N/A"))
                        {
                            Toast.makeText(getContext(),"File Not Found or not Uploaded Yet.",Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                                downloadfile(fn5.get(childPosition));
                        }

                        }

                    if (groupPosition == 5) {
                        if (fn6.get(childPosition).equals("N/A"))
                        {
                            Toast.makeText(getContext(),"File Not Found or not Uploaded Yet.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            downloadfile(fn6.get(childPosition));
                        }
                    }

                }

                else
                {
                    Toast.makeText(getContext(),"Downloading is in Progress wait for a minute",Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        View card = inflater.inflate(R.layout.expandlistsubmenulayout2,container,false);
        cardView = (CardView) card.findViewById(R.id.detainlistcard);
        cardView.setCardBackgroundColor(Color.parseColor("#c4ef0404"));
        return view;
    }

    private void setarraylist()
    {
        menu.add("1st. Fortnight");
        menu.add("2nd. Fortnight");
        menu.add("3rd. Fortnight");
        menu.add("4th. Fortnight");
        menu.add("5th. Fortnight");
        menu.add("6th. Fortnight");


        listDataChild.put(menu.get(0), fn1);
        listDataChild.put(menu.get(1), fn2);
        listDataChild.put(menu.get(2), fn3);
        listDataChild.put(menu.get(3), fn4);
        listDataChild.put(menu.get(4), fn5);
        listDataChild.put(menu.get(5), fn6);

    }




private void openfile()
{

    Snackbar snackbar = Snackbar.make(detainlistlayout,"Opening File"+localFile.getName(),Snackbar.LENGTH_SHORT);
    snackbar.show();

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.fromFile(localFile), "application/pdf");
    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    startActivity(intent);

}

private void getfileaddrs()
{

            Firebase fort1ref = new Firebase(getActivity().getString(R.string.dburl)+addrs+"/Detainlist/1stfortnightfileurl");
            fort1ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    fn1.clear();
                    fn1.add(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            Firebase fort2ref = new Firebase(getActivity().getString(R.string.dburl)+addrs+"/Detainlist/2ndfortnightfileurl");
            fort2ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   fn2.clear();
                   fn2.add(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


            Firebase fort3ref = new Firebase(getActivity().getString(R.string.dburl)+addrs+"/Detainlist/3rdfortnightfileurl");
            fort3ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    fn3.clear();
                    fn3.add(dataSnapshot.getValue().toString());                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


            Firebase fort4ref = new Firebase(getActivity().getString(R.string.dburl)+addrs+"/Detainlist/4thfortnightfileurl");
            fort4ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  fn4.clear();
                  fn4.add(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            Firebase fort5ref = new Firebase(getActivity().getString(R.string.dburl)+addrs+"/Detainlist/5thfortnightfileurl");
            fort5ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   fn5.clear();
                   fn5.add(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            Firebase fort6ref = new Firebase(getActivity().getString(R.string.dburl)+addrs+"/Detainlist/6thfortnightfileurl");
            fort6ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    fn6.clear();
                    fn6.add(dataSnapshot.getValue().toString());

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

}


    private void downloadfile(String fileaddrs)
    {

        try {
            final Bitmap notificicon = BitmapFactory.decodeResource(getResources(),R.drawable.cgclogonotific);
            final Intent emptyIntent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            eventsNotification = new NotificationCompat.Builder(getActivity())
                    .setProgress(0,0,true)
                    .setSmallIcon(R.drawable.detainlistnotiifcon,30)
                    .setContentTitle("Filename")
                    .setContentText("processing")
                    .setLargeIcon(notificicon)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setColor(Color.parseColor("#c4ef0404"))
                    .setContentInfo("counting");

            notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReferenceFromUrl(fileaddrs);
            File rootPath = new File(Environment.getExternalStorageDirectory(),getActivity().getString(R.string.libfileurl));
            if(!rootPath.exists()) {
                rootPath.mkdirs();
            }

              localFile = new File(rootPath,storageRef.getName());
            if(localFile.exists())
            {
                state=false;
                openfile();

            }
            else {
                state=true;
                notificstate=true;
                notificationManager.notify(1, eventsNotification.build());
                Snackbar snackbar = Snackbar.make(detainlistlayout,"starting Download ....",Snackbar.LENGTH_SHORT);
                snackbar.show();
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        eventsNotification.setContentText("Done");
                        eventsNotification.setOngoing(false);
                        notificationManager.notify(1, eventsNotification.build());
                        notificstate=false;
                        state=false;
                        openfile();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        eventsNotification.setContentText("Failed");
                        eventsNotification.setOngoing(false);
                        notificationManager.notify(1, eventsNotification.build());
                        state=false;
                        if (localFile.exists())
                        {
                            localFile.delete();
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
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        String fileinfo = taskSnapshot.getBytesTransferred() + "/" + taskSnapshot.getTotalByteCount() + " bytes";
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

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notificstate.equals(true)) {
            eventsNotification.setOngoing(false);
            notificationManager.cancel(1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (notificstate.equals(true)) {
            eventsNotification.setOngoing(false);
            notificationManager.cancel(1);
        }
    }
}
