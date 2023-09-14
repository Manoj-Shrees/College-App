package com.dreamhunterztech.cgcstudentportal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dreamers on 21-08-2016.
 */

public class Feedback extends AppCompatActivity
{
    Spinner fbkinpt;
    EditText comment;
    Button  submit;
    Firebase dataref;
    String addrs,rollno;
    String [] options = {"  GUI  ","  Performance  ","  Icon  ","  Service  "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addrs = getIntent().getExtras().getString("addr");
        rollno = addrs.substring(addrs.lastIndexOf("/")+1,addrs.length());
        fbkinpt = (Spinner) findViewById(R.id.fbkspin);
        comment = (EditText) findViewById(R.id.commentbox);
        submit = (Button) findViewById(R.id.submitbutton);
        ArrayAdapter adapter = new ArrayAdapter(Feedback.this,android.R.layout.simple_list_item_1,options);
        fbkinpt.setAdapter(adapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText().toString().trim().length() == 0)
                {
                    comment.setError("is Empty");
                }
                else {
                    uploaddata();
                }
            }
        });

    }

    private void uploaddata()
    {
        dataref= new Firebase(getApplicationContext().getString(R.string.dburl)+"/Feedback");
        String key = dataref.push().getKey();
        Firebase feedbackkey = dataref.child(key);
        Firebase setrollno = feedbackkey.child("Rollno");
        Firebase setrating = feedbackkey.child("Useragreement");
        Firebase setComments = feedbackkey.child("Comments");
        Firebase setuserloc = feedbackkey.child("Addrs");
        setrollno.setValue(rollno);
        setrating.setValue(fbkinpt.getSelectedItem().toString());
        setComments.setValue(comment.getText().toString());
        setuserloc.setValue(addrs);
        Toast.makeText(Feedback.this,"Thank you for your Feedback",Toast.LENGTH_SHORT).show();
        comment.setText("");
        finish();
    }

}
