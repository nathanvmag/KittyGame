package com.example.henriquefilho.vicioemredessociais;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

public class MainActivity extends AppCompatActivity implements ServiceConnection,Runnable {
    MonsterService mService;
    final ServiceConnection mConnection = this;
    Intent intent;
    boolean mBound = false;
    Button bt ;
    private Handler handler;
    TextView foodtext;
    TextView watertext;
    ImageView Bellow;
    Button Restart;
    Bitmap bellowVivo;
    Bitmap bellowMorto;
    SharedPreferences sp ;
    SharedPreferences.Editor editor;
    ImageView morefood,morewater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent  = new Intent(this, MonsterService.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ServiceStart();
        /*bt = (Button)findViewById(R.id.stServ);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        foodtext = (TextView)findViewById(R.id.foodText);
        watertext = (TextView)findViewById(R.id.waterText);
        Bellow = (ImageView)findViewById(R.id.MonsterImage);
        Restart= (Button)findViewById(R.id.Restart);
        morefood = (ImageView)findViewById(R.id.morefood);
        morewater = (ImageView)findViewById(R.id.morewater);
        bellowVivo = BitmapFactory.decodeResource(getResources(), R.drawable.bellow_vivo_pp);
        bellowMorto = BitmapFactory.decodeResource(getResources(), R.drawable.bellow_morto_pp);
         sp = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
         editor = sp.edit();


        Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ServiceStart();
                mService.water=100;
                mService.food=100;
                editor.putInt("food",100);
                editor.putInt("water",100);
                editor.commit();
                Restart.setVisibility(View.INVISIBLE);
                Bellow.setImageBitmap(bellowVivo);

            }
        });
        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.plus);
        morefood.setImageBitmap(b);
        morewater.setImageBitmap(b);

        morefood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound){
                    if(mService.food<100){
                        mService.food += 5;
                        if (mService.food>100)mService.food=100;
                        Log.d("B",""+mService.food);
                    }}
            }
        });

       morewater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBound){
                    if(mService.water<100){
                        mService.water += 5;
                        if (mService.water>100)mService.water=100;
                        Log.d("B",""+mService.water);
                    }}
            }
        });
        handler = new Handler();
        handler.post(this);

    }
    @Override
    protected void onStart( ) {
        super.onStart();


    }
    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;

        }
        editor.commit();
    }
    @Override
    public void onPause()
    {
        super.onPause();

    }
    @Override
    public void onResume()
    {
        super.onResume();

    }
    void ServiceStart()
    {
        if(!isMyServiceRunning(MonsterService.class))
        {

            startService(intent);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
           // if (mBound)mService.count=0;
            Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();
            Log.d("start service", "STARTTTTTTTTTTTTTTTTTTTTTTT");

        }
        else {
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
           // if (mBound)mService.count=0;
            Log.d("bind service", "BINDDDDDDDDDDDDDDDDDDDDDDDDDDD");
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MonsterService.LocalBinder binder = (MonsterService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Toast.makeText(this, "Bind service", Toast.LENGTH_SHORT).show();
            Log.d("bind service", "BINDDDDDDDDDDDD222222222222222222222222222");


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }

    @Override
    public void run() {

        handler.postDelayed(this, 30);
        if (mBound)
        {
            foodtext.setText(String.valueOf( mService.food));
            watertext.setText(String.valueOf( mService.water));
            if (mService.food<=0 || mService.water<=0)
            {
                Restart.setVisibility(View.VISIBLE);
                Bellow.setImageBitmap(bellowMorto);
                editor.putInt("food",mService.food);
                editor.putInt("water",mService.water);
                editor.commit();
            }
        }
    }
}


