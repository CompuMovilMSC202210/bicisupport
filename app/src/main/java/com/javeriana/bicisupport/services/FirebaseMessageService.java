package com.javeriana.bicisupport.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.activities.HomeActivity;

import java.util.Random;

public class FirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String detail = remoteMessage.getData().get("msg");

            getNotification(title, detail);
        }
    }

    private void getNotification(String title, String detail) {
        String id = "mensaje";

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(id, "nuevo", NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }

        Intent nf = new Intent(getApplicationContext(), HomeActivity.class);
        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
            pendingIntent = PendingIntent.getActivity
                    (this, 0, nf, PendingIntent.FLAG_MUTABLE);
        else
            pendingIntent = PendingIntent.getActivity
                    (this, 0, nf, PendingIntent.FLAG_ONE_SHOT);

        builder.setAutoCancel(true)
                .setSmallIcon(R.drawable.logoazul)
                .setContentText(detail)
                .setContentTitle(title)
                .setContentInfo("nuevo")
                .setContentIntent(pendingIntent);

        Random random = new Random();
        int idNotify = random.nextInt(8000);

        assert nm != null;
        nm.notify(idNotify, builder.build());
    }
}
