package com.dreamhunterztech.cgcfacultyportal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by suwas on 30-10-2016.
 */

public class Reportacademics extends Fragment
{
    TextView acadtopic,acadcontent;
    RadioButton rad1,rad2,rad3,rad4,rad5,rad6,rad7;
    Button submitapp;
    Firebase acadreprtref;
    int acount=0;
    private String reporttype="",addrs;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reportacademicslayout, container, false);
        addrs = getActivity().getIntent().getExtras().getString("addr");
        rad1 = (RadioButton) view.findViewById(R.id.r1);
        rad2 = (RadioButton) view.findViewById(R.id.r2);
        rad3 = (RadioButton) view.findViewById(R.id.r3);
        rad4 = (RadioButton) view.findViewById(R.id.r4);
        rad5 = (RadioButton) view.findViewById(R.id.r5);
        rad6 = (RadioButton) view.findViewById(R.id.r6);
        rad7 = (RadioButton) view.findViewById(R.id.r7);

        acadtopic = (TextView) view.findViewById(R.id.acadreporttopic);
        acadcontent = (TextView) view.findViewById(R.id.acadreportcontent);

        submitapp = (Button) view.findViewById(R.id.acadreportsubmit);
        submitapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitacadreports();
            }
        });

        rad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              reporttype = rad1.getText().toString();
            }
        });

        rad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reporttype = rad2.getText().toString();
            }
        });

        rad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reporttype = rad3.getText().toString();
            }
        });

        rad4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reporttype = rad4.getText().toString();
            }
        });

        rad5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reporttype = rad5.getText().toString();
            }
        });

        rad6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reporttype = rad5.getText().toString();
            }
        });

        rad7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reporttype = rad5.getText().toString();
            }
        });
        return view;
    }

    public void submitacadreports() {
        if (!rad1.isChecked() && !rad2.isChecked() && !rad3.isChecked() && !rad4.isChecked() && !rad5.isChecked()) {
            Toast.makeText(getContext(), "Type Should be Mention", Toast.LENGTH_SHORT).show();
            acount += 1;
        }

        if (acadtopic.getText().toString().equals("")) {
            acadtopic.setError("is Empty");
            acount += 1;
        }

        if (acadcontent.getText().toString().equals("")) {
            acadcontent.setError("is Empty");
            acount += 1;
        }


        if (acount == 0) {

            acadreprtref = new Firebase("https://cgc-database.firebaseio.com/Report/Academics");

            String key = acadreprtref.push().getKey();
            Firebase setid = acadreprtref.child(key);

            Map<String, String> userData = new HashMap<String, String>();

            userData.put("Reporttopic", acadtopic.getText().toString());
            userData.put("Reportcontent", acadcontent.getText().toString());
            userData.put("Reporttype", reporttype);
            userData.put("Reportby", addrs);
            userData.put("Reportstatus","pending");
            setid.setValue(userData);
            acadtopic.setText("");
            acadcontent.setText("");
            rad1.setChecked(false);
            rad2.setChecked(false);
            rad3.setChecked(false);
            rad4.setChecked(false);
            rad5.setChecked(false);
            rad6.setChecked(false);
            rad7.setChecked(false);
            createmessagedialog();
        }

        acount=0;
    }


    public void createmessagedialog()
    {
        AlertDialog.Builder messagedialog= new AlertDialog.Builder(getContext());
        messagedialog.setTitle("Academics Report");
        messagedialog.setMessage("Your report is generated. Hope we will proceed soon\nThank You!!!");
        messagedialog.setNeutralButton("OK",null);
        messagedialog.show();
    }
}
