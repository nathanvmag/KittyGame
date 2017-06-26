package com.example.henriquefilho.vicioemredessociais;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by nathan.magalhaes on 26/06/2017.
 */

public class FoodAndWater extends Activity
implements  Runnable,ServiceConnection {
    MonsterService mService;
    Handler h ;
    TextView foodtext;
    TextView watertext;
    ImageView bread;
    ImageView water;
    Button back;
    Button addfood;
    Button addwater;
    SharedPreferences sp ;
    SharedPreferences.Editor editor;
    final ServiceConnection mConnection = this;
    boolean mBound = false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        intent  = new Intent(this, MonsterService.class);
        setContentView(R.layout.addfoodanwater);
        h= new Handler();
        h.post(this);
        foodtext = (TextView)findViewById(R.id.foodtx);
        watertext = (TextView)findViewById(R.id.watertx);
        bread = (ImageView)findViewById(R.id.bread);
        water = (ImageView)findViewById(R.id.water);
        back = (Button)findViewById(R.id.back);
        addfood = (Button)findViewById(R.id.addFood);
        addwater = (Button)findViewById(R.id.addwater);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        bread.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.pao_comida_pp));
        water.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.agua_bebida_pp));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),MainActivity.class);
                startActivity(i);
                unbindService(mConnection);
                mBound = false;
            }
        });

        addfood.setOnClickListener(new View.OnClickListener() {
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

        addwater.setOnClickListener(new View.OnClickListener() {
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
    }
    @Override
    public void onServiceConnected(ComponentName className,
                                   IBinder service) {
        // We've bound to LocalService, cast the IBinder and get LocalService instance
        MonsterService.LocalBinder binder = (MonsterService.LocalBinder) service;
        mService = binder.getService();
        mBound = true;
        Toast.makeText(this, "Bind service", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        mBound = false;
    }

    @Override
    public void run() {
        h.postDelayed(this,30);
        if(mBound)
        {
            foodtext.setText(String.valueOf(mService.food));
            watertext.setText(String.valueOf(mService.water));
        }
    }
}
