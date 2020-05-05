package com.example.ktfit;

<<<<<<< HEAD
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
=======
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
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
<<<<<<< HEAD
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
=======
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba

import java.io.FileOutputStream;

import static com.example.ktfit.MainActivity.getTimeStamp;
<<<<<<< HEAD
import static com.example.ktfit.ViewNotification.CHANNEL_1_ID;

public class StartWorkoutActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    private int notificationId = 1;
    public static final boolean FLAG_NO_CLEAR = false;
=======

public class StartWorkoutActivity extends AppCompatActivity {

>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
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

<<<<<<< HEAD
        notificationManager = NotificationManagerCompat.from(this);
        handler = new Handler() ;

        timeView = (TextView) findViewById(R.id.input_time);
=======
        handler = new Handler() ;

        walkView = (TextView) findViewById(R.id.walk);
        runView = (TextView) findViewById(R.id.run);
        cycleView = (TextView) findViewById(R.id.cycle);
        otherView = (TextView) findViewById(R.id.other);
        timeView = (TextView) findViewById(R.id.input_time);

>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
        start = (Button)findViewById(R.id.startbutton);
        pause = (Button)findViewById(R.id.pausebutton);
        reset = (Button)findViewById(R.id.resetbutton);
        lap = (Button)findViewById(R.id.lapbutton) ;

        start.setEnabled(false);
        pause.setEnabled(false);
        reset.setEnabled(false);
        lap.setEnabled(false);
<<<<<<< HEAD
        walkView = findViewById(R.id.walk);
=======

>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
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
<<<<<<< HEAD
        runView = (TextView) findViewById(R.id.run);
=======
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
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
<<<<<<< HEAD
        cycleView = (TextView) findViewById(R.id.cycle);
=======
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
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
<<<<<<< HEAD
        otherView = (TextView) findViewById(R.id.other);
=======
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
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
<<<<<<< HEAD

=======
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerRunning = true;
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                reset.setEnabled(false);
<<<<<<< HEAD
                sendOnStart(R.id.input_time);
=======
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
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
<<<<<<< HEAD

=======
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
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

<<<<<<< HEAD
    private void sendOnStart(int v){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Current Session")
                .setContentText("In Progress")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setWhen(System.currentTimeMillis())
                .setCategory(NotificationCompat.CATEGORY_PROGRESS);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
    }

=======
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
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
<<<<<<< HEAD

=======
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
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveWorkoutTimeInTextFile() {
        try {

            String fileContents = getTimeStamp().concat("\t" + WorkoutType + "\t" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds) + "\n");

            FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_APPEND);
            fileOutputStream.write(fileContents.getBytes());
            Toast.makeText(getBaseContext(),"Saved to file",Toast.LENGTH_SHORT).show();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
<<<<<<< HEAD
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
=======
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


>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
