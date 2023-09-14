package com.dreamhunterztech.cgcstudentportal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by Dreamer on 12-11-2017.
 */

public class libbookdataedit extends AppCompatActivity {
    EditText bookname,bookauther , bookdescription;
    ProgressBar uppbar;
    ImageView bookimg;
    String selection , bname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libbookeditlayout);
        selection = getIntent().getExtras().getString("selectionaddr");
        bname = getIntent().getExtras().getString("booknameaddr");
        Toolbar toolbar = (Toolbar) findViewById(R.id.libbookdataedittoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Book Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bookname = findViewById(R.id.updatebookname);
        bookauther = findViewById(R.id.updatebookauther);
        bookdescription = findViewById(R.id.updatebookdescription);
        bookimg = findViewById(R.id.updatebookimage);
        uppbar = findViewById(R.id.updatepbar);
        Button upbtn = findViewById(R.id.updatebtn);


        bookimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {

                    Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto, 1);
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

        upbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        getdata();
    }

    private void setdata()
    {

    }

    public void getdata()
    {
        Firebase booknameref = new Firebase(libbookdataedit.this.getString(R.string.dburl)+"Library/libfilerecord/"+selection+"/"+bname+"/bookname");
        booknameref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                bookname.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        Firebase emailref = new Firebase("https://cgc-database.firebaseio.com/"+"Library/libfilerecord/"+selection+"/"+bname+"/bookauther");
        emailref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                bookauther.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase mobnoref = new Firebase("https://cgc-database.firebaseio.com/"+"Library/libfilerecord/"+selection+"/"+bname+"/bookdesc");
        mobnoref.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                bookdescription.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


    /*public void profilemageupload(final Uri fileuri, Context c)
    {
        StorageReference storageref = storage.getReferenceFromUrl("gs://cgc-database.appspot.com");
        StorageReference filepath =  storageref.child("propics/"+rollno+"/"+"propics");
        filepath.putFile(fileuri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String imageuri = taskSnapshot.getMetadata().getDownloadUrl().toString();
                uploadimagedetail(imageuri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

            }
        });
    }

    public void uploadimagedetail(String propicdir)
    {
     Firebase   dataref= new Firebase(libbookmange.this.getString(R.string.dburl)+addrs+"/propic");
        dataref.setValue(propicdir);
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                Uri imageuri = data.getData();
                CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(16, 9).start(this);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resulturi = result.getUri();
                bookimg.setImageURI(resulturi);
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(libbookdataedit.this, "image error to load", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(libbookdataedit.this,"Error to upload file as selected app or file is not valid!",Toast.LENGTH_SHORT).show();

        }
    }


}
