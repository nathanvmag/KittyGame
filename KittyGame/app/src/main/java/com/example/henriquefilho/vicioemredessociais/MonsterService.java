package com.example.henriquefilho.vicioemredessociais;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by henrique.filho on 19/06/2017.
 */

public class MonsterService extends Service implements Runnable  {
    public int food;
    public int water;
    boolean active = true;
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
        Log.d("SERVICE SAMPLE", "onCreate()");
        active = true;
        new Thread(this).start();
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
        Log.d("teste", "onCreate: ()");

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Log.d("SERVICE SAMPLE", "onDestroy()");

    }


    @Override
    public void run() {
        SharedPreferences sp = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        while(active)
        {
            Log.d("SERVICE SAMPLE", "water: " + water+" food = "+food);
            water--;
            food--;
            editor.putInt("food",food);
            editor.putInt("water", water);
            editor.commit();
            SetInterval();
        }
    }

    private void SetInterval()
    {
        try { Thread.sleep(1000); }
        catch(InterruptedException e) { e.printStackTrace(); }
    }
}

