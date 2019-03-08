package com.example.edson.pushfatecadmin.Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.edson.pushfatecadmin.Adapter.AdapterMensagens;
import com.example.edson.pushfatecadmin.Activity.MainActivity;
import com.example.edson.pushfatecadmin.Activity.MensagemActivity;
import com.example.edson.pushfatecadmin.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {



    @Override
    public void onMessageReceived(RemoteMessage notificacao) {



        if(notificacao.getData().size() > 0 ){


            String msg = notificacao.getData().get("mensagem");
            String titulo = notificacao.getData().get("titulo");
            String nome = notificacao.getData().get("nome");
            String urlimagem = notificacao.getData().get("urlimagem");


           // String mensagem = msg+ " De: "+ nome;

            sendNotification_2(titulo,msg, urlimagem,nome);


        }

        else if (notificacao.getNotification() != null){


            Log.d("edson",notificacao.getNotification().getTitle());

            Log.d("edson",notificacao.getNotification().getBody());


            String titulo = notificacao.getNotification().getTitle();
            String msg = notificacao.getNotification().getBody();

            sendNotification_1(titulo, msg);

        }


    }


    private void sendNotification_1(String titulo, String msg){



        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);


        String canal = getString(R.string.default_notification_channel_id);

        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,canal)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(msg)
                .setSound(som)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(canal,"canal",NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);

        }


        notificationManager.notify(0,notification.build());


    }



    private void sendNotification_2(final String titulo, final String msg, final String url, final String nome){


        final int id = (int) (System.currentTimeMillis() / 1000);


        Glide.with(getBaseContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {



                //ação de abrir

                Intent intent_Abrir = new Intent(getBaseContext(), MensagemActivity.class);

                intent_Abrir.putExtra("url",url);
                intent_Abrir.putExtra("mensagem",msg);
                intent_Abrir.putExtra("idNotificacao",id);

                intent_Abrir.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



                PendingIntent pendingIntent_Abrir = PendingIntent.getActivity(getBaseContext(),id,intent_Abrir, PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationCompat.Action abrir = new NotificationCompat.Action(R.drawable.ic_launcher_background,"Abrir",pendingIntent_Abrir);




                //ação de fechar

                Intent intent_Fechar = new Intent(getBaseContext(),NotificacaoBroadcast.class);

                intent_Fechar.putExtra("idNotificacao",id);
                intent_Fechar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pending_Fechar = PendingIntent.getBroadcast(getBaseContext(),id,intent_Fechar,PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Action fechar = new NotificationCompat.Action(0,"Fechar",pending_Fechar);




                //Passando dados para o RecyclerView
                Intent intent = new Intent(getBaseContext(), MensagemActivity.class);

                intent.putExtra("url",url);
                intent.putExtra("mensagem",msg);
                intent.putExtra("titulo",titulo);
                intent.putExtra("autor",nome);


                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),id,intent, PendingIntent.FLAG_ONE_SHOT);


                String canal = getString(R.string.default_notification_channel_id);

                Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder notification = new NotificationCompat.Builder(getBaseContext(),canal)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(titulo)
                        .setContentText(msg)
                        .setSound(som)
                        .setAutoCancel(true)
                        .setLargeIcon(bitmap)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                        //.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .addAction(abrir)
                        .addAction(fechar)
                        .setContentIntent(pendingIntent);


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                    NotificationChannel channel = new NotificationChannel(canal,"canal",NotificationManager.IMPORTANCE_DEFAULT);

                    notificationManager.createNotificationChannel(channel);

                }


                notificationManager.notify(id,notification.build());



                return false;


            }
        }).submit();



    }



    @Override
    public void onNewToken(String s) {


        Log.d("edson_token",s);


        super.onNewToken(s);

    }






}

