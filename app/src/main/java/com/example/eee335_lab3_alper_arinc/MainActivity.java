package com.example.eee335_lab3_alper_arinc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    LocalService mService;
    boolean mStarted = false;
    boolean mBound = false;
    Handler handler = new Handler();
    Button btnStop;



    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MAIN", "onCreate(), mBound: " + mBound + ", mStarted: " + mStarted);
        setContentView(R.layout.activity_main);
        setClickHandlerForStartService();
        setClickHandlerForStopService();
        TextView song = (TextView) findViewById(R.id.tv_song) ;
        final MediaPlayer music = MediaPlayer.create(this, R.raw.playmusic);


    }
    //----------------------------------------------------------------------------------------------
    private void setClickHandlerForStartService() {
        Log.d("MAIN", "setClickHandlerForStartService(), mBound: " + mBound + ", mStarted: " + mStarted);
        Button btnStart = (Button) findViewById(R.id.btn_play);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView song = (TextView) findViewById(R.id.tv_song);

                new Thread() {
                    public void run() {

                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {





                                }
                            });
                            Thread.sleep(0);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();

                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("MAIN", "btnStart handler run(), mBound: " + mBound + ", mStarted: " + mStarted);
                        Intent intent = new Intent(getApplicationContext(), LocalService.class);
                        if (!mStarted) {
                            startService(intent);





                        }

                        // Bind to LocalService
                        if (!mBound) {
                            bindService(intent, connection, Context.BIND_AUTO_CREATE);


                        }
                    }
                }, 0);

            }

        });

    }

    //----------------------------------------------------------------------------------------------
    private void setClickHandlerForStopService() {
        Log.d("MAIN", "setClickHandlerForStopService(), mBound: " + mBound + ", mStarted: " + mStarted);
        Button btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("MAIN", "btnStop onClick() entry, mBound: " + mBound + ", mStarted: " + mStarted);
                if (mStarted) {
                    stopService(new Intent(getApplicationContext(), LocalService.class));
                    mStarted = false;
                }
                if (mBound) {
                    unbindService(connection);
                    mBound = false;
                }
                Log.d("MAIN", "btnStop onClick() exit, mBound: " + mBound + ", mStarted: " + mStarted);
            }
        });
    }
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MAIN", "onStop() entry, mBound: " + mBound + ", mStarted: " + mStarted);
//        if (mBound) {
//            unbindService(connection);
//            mBound = false;
//        }
//        Log.d("MAIN", "onStop() exit, mBound: " + mBound + ", mStarted: " + mStarted);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MAIN", "onDestroy() entry, mBound: " + mBound + ", mStarted: " + mStarted);
        if (mStarted) {
            stopService(new Intent(getApplicationContext(), LocalService.class));
            mStarted = false;
        }
        if (mBound) {
            unbindService(connection);
            mBound = false;
        }
        Log.d("MAIN", "onDestroy() exit, mBound: " + mBound + ", mStarted: " + mStarted);
    }
    //----------------------------------------------------------------------------------------------
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Log.d("MAIN", "onServiceConnected() entry, mBound: " + mBound + ", mStarted: " + mStarted);
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mStarted = true;
            mBound = true;
            Log.d("MAIN", "onServiceConnected() exit, mBound: " + mBound + ", mStarted: " + mStarted);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("MAIN", "onServiceDisconnected() entry, mBound: " + mBound + ", mStarted: " + mStarted);
            mStarted = false;
            mBound = false;
            Log.d("MAIN", "onServiceDisconnected() exit, mBound: " + mBound + ", mStarted: " + mStarted);
        }
    };
}