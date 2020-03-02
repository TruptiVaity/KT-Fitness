package com.example.ktfit;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class StartWorkoutActivity extends AppCompatActivity {

    TextView walkView,runView,cycleView,otherView,timeView;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Button start, pause, reset, lap ;
    Handler handler;

    int Seconds, Minutes, MilliSeconds ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startworkout);

        handler = new Handler() ;

        walkView = (TextView) findViewById(R.id.walk);
        runView = (TextView) findViewById(R.id.run);
        cycleView = (TextView) findViewById(R.id.cycle);
        otherView = (TextView) findViewById(R.id.other);
        timeView = (TextView) findViewById(R.id.input_time);

        start = (Button)findViewById(R.id.startbutton);
        pause = (Button)findViewById(R.id.pausebutton);
        reset = (Button)findViewById(R.id.resetbutton);
        lap = (Button)findViewById(R.id.lapbutton) ;

        start.setEnabled(false);
        pause.setEnabled(false);
        reset.setEnabled(false);
        lap.setEnabled(false);

        walkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                pause.setEnabled(true);
                lap.setEnabled(true);


            }
        });
        runView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                pause.setEnabled(true);
                lap.setEnabled(true);


            }
        });
        cycleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                pause.setEnabled(true);
                lap.setEnabled(true);


            }
        });
        otherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                pause.setEnabled(true);
                lap.setEnabled(true);


            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                reset.setEnabled(false);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeBuff += MillisecondTime;

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

