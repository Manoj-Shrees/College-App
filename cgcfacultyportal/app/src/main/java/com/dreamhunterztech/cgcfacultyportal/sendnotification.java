package com.dreamhunterztech.cgcfacultyportal;

/**
 * Created by Dreamer on 14-06-2017.
 */


import android.os.AsyncTask;
import android.os.StrictMode;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONArray;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;

/**
 * @author athakur
 */
public class sendnotification extends FirebaseInstanceIdService {

    OkHttpClient mClient;
    JSONArray jsonArray;
    String branch="",message="";

    public sendnotification()
    {
         mClient = new OkHttpClient();

        String refreshedToken =  FirebaseInstanceId.getInstance().getToken();//add your user refresh tokens who are logged in with firebase.
         jsonArray = new JSONArray();
        jsonArray.put(refreshedToken);
    }



    public void sendNotification(String brnch,String msg)
    {

        this.branch=brnch;
        this.message=msg;

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                     String brnch = branch;
                    String msg = message;
                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic NGEwNDgwNmQtYzg0Mi00NTg5LWIyNTEtNzFiYWVhODlmZjll");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"e9a64551-b824-4aab-80bf-6b80aab10d41\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"Branch\", \"relation\": \"=\", \"value\": \"" + brnch+"\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \""+msg+"\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

   }