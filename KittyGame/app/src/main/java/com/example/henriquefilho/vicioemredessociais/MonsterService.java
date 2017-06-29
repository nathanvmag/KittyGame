package com.example.henriquefilho.vicioemredessociais;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import java.security.acl.NotOwnerException;

/**
 * Created by henrique.filho on 19/06/2017.
 */

public class MonsterService extends Service implements Runnable  {
    public int food;
    public int water;
    boolean active = true;
    Handler h ;
    private final LocalBinder connection = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return connection;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        SharedPreferences sp = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        water = sp.getInt("water",100);
        food = sp.getInt("food",100);
        Log.d("SERVICE ", "onCreate()");
        active = true;
        h= new Handler();
        h.post(this);

    }

    //This class returns to Activity the service reference.
    //With this reference is possible to get the Counter value and show to user.
    public class LocalBinder extends Binder
    {
        public MonsterService getService() { return MonsterService.this; }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //Toast.makeText(this,"SERVICE SAMPLE onCreate()",Toast.LENGTH_SHORT).show();
        Log.d("OnStartCommand", "ON START COMMAND!!");

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Log.d("SERVICE", "onDestroy()");
    }


    @Override
    public void run() {
        SharedPreferences sp = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();




        if(water > 0 || food > 0) {
            Log.d("SERVICE", "water: " + water + " food = " + food);
            water --;
            food --;
            if (water == 0 || food == 0) {
                Notify(R.drawable.bellow_morto_pp, "Seu monstro morreu", "Você deixou o Bellow morrer", 3, MainActivity.class);
                water = 0;
                food = 0;
            }

            if (food == 25) {
                Notify(R.drawable.pao_comida_pp, "To com fome", "Seu nível de comida está crítico", 0, FoodAndWater.class);
            }
            if (water == 25) {
                Notify(R.drawable.agua_bebida_pp, "To com sede", "Seu nível de hidratação está crítico", 1, FoodAndWater.class);
            }

            editor.putInt("food", food);
            editor.putInt("water", water);
            editor.commit();
        }

        h.postDelayed(this,1000);
    }

    private void SetInterval()
    {
        try { Thread.sleep(1000); }
        catch(InterruptedException e) { e.printStackTrace(); }
    }

    void Notify(int icon,String title,String content,int id ,Class<?> serviceClass)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setLargeIcon( BitmapFactory.decodeResource(this.getResources(),icon))
                        .setVibrate(new long[]{100,100,100,100})
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .setContentText(content);

        Intent resultIntent = new Intent(this, serviceClass);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(serviceClass);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
    }
}

