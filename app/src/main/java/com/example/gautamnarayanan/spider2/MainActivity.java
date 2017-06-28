package com.example.gautamnarayanan.spider2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager Gmanager;
    Sensor Gsensor;
    double range;
    Context context = MainActivity.this;
    EditText Gtext;
    private MediaPlayer ring;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Gmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Gsensor = Gmanager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        Gmanager.registerListener(this, Gsensor, Gmanager.SENSOR_DELAY_NORMAL);
        range = Gsensor.getMaximumRange();

}

    @Override
    public void onSensorChanged(final SensorEvent event) {
        Gtext = (EditText) findViewById(R.id.Gtext);
        ring = MediaPlayer.create(MainActivity.this, R.raw.fairy_tail);
        Gtext.setText(null);
        Gtext.setEnabled(false);

        final CountDownTimer countDownTimer=new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                Gtext.setText(String.valueOf( millisUntilFinished / 1000));
            }

            public void onFinish() {
                Gtext.setText("GO AWAY");
            }

        };
        final Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0&& ring.isPlaying()==false) {
                    countDownTimer.start();
                }
                if (msg.what == 1) {

                    countDownTimer.cancel();
                    reset();

                }
            }
        };
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (event.values[0] == 0) {
                    if(ring.isPlaying()==false)
                    {h.sendEmptyMessage(0);
                    long time=System.currentTimeMillis()+10000;;
                    while(System.currentTimeMillis()<time);
                    ring.start();}

                }
                if (event.values[0] >= range) {
                    h.sendEmptyMessage(1);
                    ring.pause();
                    ring.seekTo(0);
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }


    public void reset(){
        Gtext.setText("10");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}