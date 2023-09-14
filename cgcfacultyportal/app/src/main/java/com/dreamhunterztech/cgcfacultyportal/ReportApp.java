package com.dreamhunterztech.cgcfacultyportal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

public class ReportApp extends Fragment
{
    TextView apptopic,appcontent;
    RadioButton rad1,rad2,rad3,rad4,rad5;
    Button submitacad;
    Firebase appreprtref;
    int acount=0;
    private String reporttype="",addrs;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appreportlayout, container, false);
        addrs = getActivity().getIntent().getExtras().getString("addr");
        rad1 = (RadioButton) view.findViewById(R.id.r1);
        rad2 = (RadioButton) view.findViewById(R.id.r2);
        rad3 = (RadioButton) view.findViewById(R.id.r3);
        rad4 = (RadioButton) view.findViewById(R.id.r4);
        rad5 = (RadioButton) view.findViewById(R.id.r5);

        apptopic = (TextView) view.findViewById(R.id.appreporttopic);
        appcontent = (TextView) view.findViewById(R.id.appreportcontent);

        submitacad = (Button) view.findViewById(R.id.appreportbtn);
        submitacad.setOnClickListener(new View.OnClickListener() {
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
        return view;
    }

    public void submitacadreports() {
        if (!rad1.isChecked() && !rad2.isChecked() && !rad3.isChecked() && !rad4.isChecked()) {
            Toast.makeText(getContext(), "Type Should be Mention", Toast.LENGTH_SHORT).show();
            acount += 1;
        }

        if (apptopic.getText().toString().equals("")) {
            apptopic.setError("is Empty");
            acount += 1;
        }

        if ( appcontent.getText().toString().equals(""))
        {
            appcontent.setError("is Empty");
            acount += 1;
        }

        if (acount == 0) {
            appreprtref = new Firebase("https://cgc-database.firebaseio.com/Report/App");

            String key = appreprtref.push().getKey();
            Firebase setid = appreprtref.child(key);

            Map<String, String> userData = new HashMap<String, String>();

            userData.put("Reporttopic", apptopic.getText().toString());
            userData.put("Reportcontent", appcontent.getText().toString());
            userData.put("Reporttype", reporttype);
            userData.put("Reportby", addrs);
            userData.put("Reportstatus","pending");
            setid.setValue(userData);
            apptopic.setText("");
            appcontent.setText("");
            rad1.setChecked(false);
            rad2.setChecked(false);
            rad3.setChecked(false);
            rad4.setChecked(false);
            rad5.setChecked(false);
            createmessagedialog();
        }

        acount=0;
    }




    public void createmessagedialog()
    {
        AlertDialog.Builder messagedialog= new AlertDialog.Builder(getContext());
        messagedialog.setTitle("App Report");
        messagedialog.setMessage("Your report is generated. Hope we will proceed soon\nThank You!!!");
        messagedialog.setPositiveButton("OK",null);
        messagedialog.show();
    }
}
