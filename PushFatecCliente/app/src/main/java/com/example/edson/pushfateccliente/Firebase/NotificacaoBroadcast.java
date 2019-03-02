package com.example.edson.pushfateccliente.Firebase;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificacaoBroadcast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("idNotificacao",-1);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.cancel(id);



    }
}
