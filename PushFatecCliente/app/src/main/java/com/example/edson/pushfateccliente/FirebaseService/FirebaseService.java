package com.example.edson.pushfateccliente.FirebaseService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.edson.pushfateccliente.Activity.MainActivity;
import com.example.edson.pushfateccliente.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {

    //Receber notificação em primeiro plano
    @Override
    public void onMessageReceived(RemoteMessage notificacao) {
     if(notificacao.getNotification() != null){
         Log.d("GG",notificacao.getNotification().getTitle());
         Log.d("GG",notificacao.getNotification().getBody());

         String titulo = notificacao.getNotification().getTitle();
         String mensagem = notificacao.getNotification().getBody();

         sendNotification(titulo,mensagem);
     }
    }

    private void sendNotification(String titulo,String mensagem) {
        //Activity onde o usuario sera direcionado ao clicar na msg
        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        String canal = getString(R.string.default_notification_channel_id);
        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,canal)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setSound(som)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){

            NotificationChannel channel = new NotificationChannel(canal,"canal",notificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);



        }
    notificationManager.notify(0,notification.build());

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
