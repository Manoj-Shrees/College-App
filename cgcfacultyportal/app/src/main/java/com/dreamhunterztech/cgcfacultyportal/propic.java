package com.dreamhunterztech.cgcfacultyportal;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Dreamer on 22-06-2017.
 */

public class propic extends Fragment
{

    CircularImageView circularImageView;
    private  Uri camimageUri;
    private Firebase proimgref;
    private String addrs;
    private String imageref,mCurrentPhotoPath;;
    private Firebase dataref;
    ProgressBar uploadinprogress;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.propiclayout, container, false);
        addrs = getActivity().getIntent().getExtras().getString("addr");
        uploadinprogress = (ProgressBar) view.findViewById(R.id.uploadprogbar);
        circularImageView = (CircularImageView) view.findViewById(R.id.userprofilepic);
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Take Photo","Choose from Library","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.camera);
                builder.setTitle("Upload Photo");
                builder.setCancelable(false);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            try
                            {
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
                                takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(photo));
                                camimageUri = Uri.fromFile(photo);
                                startActivityForResult(takePicture,01);
                            }

                            catch (Exception e)
                            {
                                final Dialog cameramessage = new Dialog(getContext());
                                cameramessage.setContentView(R.layout.cameramessage);
                                Button camokapp = (Button) cameramessage.findViewById(R.id.campermibtn);
                                camokapp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cameramessage.dismiss();
                                    }
                                });
                                cameramessage.show();
                            }

                        }
                        else if (items[item].equals("Choose from Library")) {
                            try {
                                Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                pickPhoto.setType("image/*");
                                startActivityForResult(pickPhoto, 02);
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
                        else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }

                    }

                });
                builder.show();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 01 && resultCode == RESULT_OK) {
                Uri imageuri = this.camimageUri;
                CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(4,3).start(getContext(), this);
            }

            if (requestCode == 02 && resultCode == RESULT_OK) {
                Uri imageuri = data.getData();
                CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(4,3).start(getContext(), this);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resulturi = result.getUri();
                Context c = getActivity();
                uploadinprogress.setVisibility(View.VISIBLE);
                profilemageupload(resulturi, c);
                downloadproimage();
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "image error to load", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),"Error to upload file as selected app or file is not valid!",Toast.LENGTH_SHORT).show();

            uploadinprogress.setVisibility(View.INVISIBLE);
        }
    }


    public void downloadproimage() {
        try {
            proimgref = new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/propic");
            proimgref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    imageref = dataSnapshot.getValue(String.class).toString();
                    if(imageref.equals("N/A"))
                    {
                        uploadinprogress.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(),"Upload a Profile Pic to your profile",Toast.LENGTH_LONG);
                    }
                    else {
                        try {
                            Picasso.with(getActivity().getApplicationContext()).load(imageref).fit().placeholder(R.drawable.defaultuserprofile).into(circularImageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    uploadinprogress.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError() throws NullPointerException{
                                    Toast.makeText(getActivity(), "Error!!!\n\nimage not found", Toast.LENGTH_SHORT).show();
                                    circularImageView.setBackgroundResource(R.drawable.defaultuserprofile);
                                    uploadinprogress.setVisibility(View.INVISIBLE);
                                }

                            });
                        }

                        catch (NullPointerException e1)
                        {
                        }

                    }


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast.makeText(getActivity(), "Server time error please contact the service provider", Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception err)
        {
            Toast.makeText(getActivity(),"Bad Network\n\nCould not load the image",Toast.LENGTH_SHORT).show();
        }
    }

    public void profilemageupload(final Uri fileuri, Context c)
    {
        StorageReference storageref = storage.getReferenceFromUrl("gs://cgc-database.appspot.com");
        StorageReference filepath =  storageref.child("propics/Faculty/"+addrs+"/"+"propics");
        filepath.putFile(fileuri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
            uploadinprogress.setProgress((int) progress);
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
                Toast.makeText(getContext(),"Error ! to upload profile pic.",Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                downloadproimage();
            }
        });
    }

    public void uploadimagedetail(String propicdir)
    {
        dataref= new Firebase("https://cgc-database.firebaseio.com/Faculty/"+addrs+"/propic");
        dataref.setValue(propicdir);
    }

    @Override
    public void onStart() {
        super.onStart();
        downloadproimage();
    }
}
