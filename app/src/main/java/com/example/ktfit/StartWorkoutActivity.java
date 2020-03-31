package com.example.ktfit;

import android.app.AlarmManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class StartWorkoutActivity extends AppCompatActivity{
    TextView walkView,runView,cycleView,otherView,timeView, stepCountView;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    private final static int SAVE_OFFSET_STEPS = 500;
    public final static int NOTIFICATION_ID = 1;
    private final static long MICROSECONDS_IN_ONE_MINUTE = 60000000;
    private final static long SAVE_OFFSET_TIME = AlarmManager.INTERVAL_HOUR;
    private static long lastSaveTime;
    Button start, pause, reset, lap ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startworkout);


        handler = new Handler() ;
        walkView = findViewById(R.id.walk);
        runView = findViewById(R.id.run);
        cycleView = findViewById(R.id.cycle);
        otherView = findViewById(R.id.other);
        timeView = findViewById(R.id.input_time);
        start = findViewById(R.id.startbutton);
        pause = findViewById(R.id.pausebutton);
        reset = findViewById(R.id.resetbutton);
        lap = findViewById(R.id.lapbutton);

        start.setEnabled(false);
        pause.setEnabled(false);
        //reset.setEnabled(false);
        lap.setEnabled(false);


        walkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                pause.setEnabled(true);
                lap.setEnabled(true);
                runView.setEnabled(false);
                cycleView.setEnabled(false);
                otherView.setEnabled(false);

            }
        });
        runView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                pause.setEnabled(true);
                lap.setEnabled(true);
                walkView.setEnabled(false);
                cycleView.setEnabled(false);
                otherView.setEnabled(false);

            }
        });
        cycleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                pause.setEnabled(true);
                lap.setEnabled(true);
                walkView.setEnabled(false);
                runView.setEnabled(false);
                otherView.setEnabled(false);


            }
        });
        otherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                pause.setEnabled(true);
                lap.setEnabled(true);
                walkView.setEnabled(false);
                cycleView.setEnabled(false);
                runView.setEnabled(false);


            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //numSteps = 0;
                //mSensorManager.registerListener(StartWorkoutActivity.this, mStepCounter, SensorManager.SENSOR_DELAY_FASTEST);
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                reset.setEnabled(false);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeBuff += MillisecondTime;
                //mSensorManager.unregisterListener(StartWorkoutActivity.this);
                handler.removeCallbacks(runnable);
                reset.setEnabled(true);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;
                walkView.setEnabled(true);
                runView.setEnabled(true);
                cycleView.setEnabled(true);
                otherView.setEnabled(true);
                timeView.setText("00:00:00");
            }
        });

    }


    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            timeView.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };


}

