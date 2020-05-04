package com.example.ktfit;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;

//import static com.example.ktfit.MainActivity.getTimeStamp;

public class StartWorkoutActivity extends AppCompatActivity {

    private static final String FILE_NAME = "workout_time_logger.txt";
    TextView walkView,runView,cycleView,otherView,timeView;
    public long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Button start, pause, reset, lap ;
    Handler handler;
    String WorkoutType;
    public int Seconds, Minutes, MilliSeconds ;
    public boolean mTimerRunning = false;

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
                WorkoutType = "Walk";
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
                WorkoutType = "Run";
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
                WorkoutType = "Cycle";
                start.setEnabled(true);
                pause.setEnabled(true);
                lap.setEnabled(true);
                runView.setEnabled(false);
                walkView.setEnabled(false);
                otherView.setEnabled(false);

            }
        });
        otherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkoutType = "Other";
                start.setEnabled(true);
                pause.setEnabled(true);
                lap.setEnabled(true);
                runView.setEnabled(false);
                cycleView.setEnabled(false);
                walkView.setEnabled(false);

            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerRunning = true;
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                reset.setEnabled(false);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                mTimerRunning = false;
                TimeBuff += MillisecondTime;
                saveWorkoutTimeInTextFile();
                handler.removeCallbacks(runnable);
                reset.setEnabled(true);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                mTimerRunning = false;
                saveWorkoutTimeInTextFile();
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
        lap.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                mTimerRunning = true;
                saveWorkoutTimeInTextFile();
            }
        });

    }

//TODO To save the time in when start is clicked and stop it only if the app is destroyed or else stopped manually
    //TODO Run the timer in the background

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

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", UpdateTime);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        if (mTimerRunning) {
            MillisecondTime = prefs.getLong("millisCovered", MillisecondTime);
        }
    }
/**    @Override
    protected void  onResume(){
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", UpdateTime);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        if (mTimerRunning) {
            MillisecondTime = prefs.getLong("millisCovered", MillisecondTime);
        }
    }
**/
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveWorkoutTimeInTextFile() {
        try {

//            String fileContents = getTimeStamp().concat("\t" + WorkoutType + "\t" + Minutes + ":"
//                    + String.format("%02d", Seconds) + ":"
//                    + String.format("%03d", MilliSeconds) + "\n");

            FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_APPEND);
//            fileOutputStream.write(fileContents.getBytes());
            Toast.makeText(getBaseContext(),"Saved to file",Toast.LENGTH_SHORT).show();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        if (mTimerRunning) {
            MillisecondTime = prefs.getLong("millisCovered", MillisecondTime);
        }
    }
    **/
}


