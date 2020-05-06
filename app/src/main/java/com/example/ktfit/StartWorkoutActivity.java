package com.example.ktfit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.ktfit.ViewNotification.CHANNEL_1_ID;

import static com.example.ktfit.MainActivity.getTimeStamp;

public class StartWorkoutActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    private int notificationId = 1;
    public static final boolean FLAG_NO_CLEAR = false;
    private static final String FILE_NAME = "workout_time_logger.txt";
    TextView walkView,runView,cycleView,otherView,timeView;
    public long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Button start, pause, reset, lap ;
    Handler handler;
    String WorkoutType;
    public int Seconds, Minutes, MilliSeconds ;
    public boolean mTimerRunning = false;
    public boolean saved = false;
    String startDate = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startworkout);

        notificationManager = NotificationManagerCompat.from(this);
        handler = new Handler() ;

        walkView = (TextView) findViewById(R.id.walk);
        runView = (TextView) findViewById(R.id.run);
        cycleView = (TextView) findViewById(R.id.cycle);
        otherView = (TextView) findViewById(R.id.other);
        timeView = (TextView) findViewById(R.id.input_time);
        final TextView distanceText = findViewById(R.id.distanceText);

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

                if (startDate.equals(""))
                {
                    DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm");
                    Calendar calobj = Calendar.getInstance();
                    startDate = df.format(calobj.getTime());
                }

                saved = false;

                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                reset.setEnabled(false);
                sendOnStart(R.id.input_time);
                distanceText.setEnabled(false);
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
                distanceText.setEnabled(true);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if ((distanceText.getText().toString()).equals(""))
                {
                    new AlertDialog.Builder(StartWorkoutActivity.this)
                            .setTitle("Invalid Distance")
                            .setMessage("Please input workout distance.")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.alert_dark_frame)
                            .show();
                    return;
                }
                mTimerRunning = false;
                saveToDB();
                saved = true;
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
                distanceText.setText("");
                startDate = "";
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

    @Override
    protected void onStop() {
        super.onStop();
        TextView distanceText = findViewById(R.id.distanceText);
        if (!saved && Seconds != 0)
        {
            if ((distanceText.getText().toString()).equals(""))
            {
                new AlertDialog.Builder(StartWorkoutActivity.this)
                        .setTitle("Invalid Distance")
                        .setMessage("Please input workout distance.")
                        .setPositiveButton(android.R.string.yes, null)
                        .setIcon(android.R.drawable.alert_dark_frame)
                        .show();
                return;
            }
            saveToDB();
        }
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", UpdateTime);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.apply();
    }

    public void saveToDB()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        DatabaseReference workoutRef = myRef.child("my_app_user").child(uid).child("workouts");

        TextView dText = findViewById(R.id.distanceText);
        Double distance = Double.parseDouble(dText.getText().toString());
        String duration = Minutes + ":" + String.format("%02d", Seconds);
        Double totalSecs = new Double ((Minutes*60) + Seconds);
        Double totalHours = totalSecs / 3600;
        Double speed = distance / totalHours;

        workoutRef.child(startDate).child("WorkoutType").setValue(WorkoutType);
        workoutRef.child(startDate).child("Duration").setValue(duration);
        workoutRef.child(startDate).child("Distance").setValue(distance.toString());
        workoutRef.child(startDate).child("Speed").setValue(speed.toString());
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