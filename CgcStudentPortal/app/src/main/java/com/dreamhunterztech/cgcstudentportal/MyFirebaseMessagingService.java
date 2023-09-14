package com.dreamhunterztech.cgcstudentportal;


        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.support.v4.app.NotificationCompat;
        import android.util.Log;
        import com.google.firebase.messaging.FirebaseMessagingService;
        import com.google.firebase.messaging.RemoteMessage;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "cgcnotification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this,MainFrame.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0
, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.cgclogonotific))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentTitle("FCM Message")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLights(Color.BLUE,500,1000)
                .setVibrate(new long[]{0,0,0,1000,0,0,0,1000})
                .setColor(Color.BLACK)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }

}
