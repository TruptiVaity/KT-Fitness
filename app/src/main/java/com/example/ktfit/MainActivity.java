package com.example.ktfit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.String.*;

public class MainActivity extends AppCompatActivity implements SensorEventListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    public GoogleApiClient mApiClient;
    private SensorManager mSensorManager;
    private Sensor mStepCounter;
    private boolean isSensorRunning = false;
    TextView displayActiveMinutes;
    private static int InitialSensorValue, TotalnumSteps = 0, StepsPerDay = 0;
    Calendar c;
    int dayat, nextday, activityNumber;
    public long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    public int Seconds, Minutes, MilliSeconds ;
    public static final String TAG = "Activity Recognized ";
    String daystr, nextdaystr;
    private static final String FILE_NAME = "stepcount.txt";
    public String activityDetected;
    private static final String TEXT_NUM_STEPS_PER_DAY = "Number of Steps today: ";
    TextView viewDailySteps;
    Handler mHandler;
    String uid;

    BroadcastReceiver broadcastReceiver;
    public static final String BROADCAST_DETECTED_ACTIVITY = "activity_intent";
    static final long DETECTION_INTERVAL_IN_MILLISECONDS = 30 * 1000;
    public static final int CONFIDENCE = 70;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .build();
        mApiClient.connect();

        mHandler = new Handler();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        String name = user.getDisplayName();

        TextView nText = (TextView) findViewById(R.id.name);
        if (name != null)
            nText.setText("Hi " + name + "!");

        viewDailySteps = findViewById(R.id.view_daily_steps);
        displayActiveMinutes = findViewById(R.id.display_active_minutes);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BROADCAST_DETECTED_ACTIVITY)) {
                    int type = intent.getIntExtra("type", -1);
                    int confidence = intent.getIntExtra("confidence", 0);
                    handleUserActivity(type, confidence);
                }

            }
        };
        startTracking();

        TextView startWorkout = (TextView) findViewById(R.id.start_workout);
        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startWorkoutIntent = new Intent(MainActivity.this, StartWorkoutActivity.class);
                startActivity(startWorkoutIntent);
            }
        });
        TextView tracker = (TextView) findViewById(R.id.water_caffeine_tracker);
        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startWorkoutIntent = new Intent(MainActivity.this, WaterCaffeineTrackerActivity.class);
                startActivity(startWorkoutIntent);
            }
        });
        TextView planner = (TextView) findViewById(R.id.workoutplanner);
        planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startWorkoutIntent = new Intent(MainActivity.this, PlannerActivity.class);
                startActivity(startWorkoutIntent);
            }
        });
        TextView milestones = (TextView) findViewById(R.id.milestones);
        milestones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startWorkoutIntent = new Intent(MainActivity.this, MilestonesActivity.class);
                startActivity(startWorkoutIntent);
            }
        });
        TextView history = (TextView) findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startWorkoutIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(startWorkoutIntent);
            }
        });
        TextView friends = (TextView) findViewById(R.id.friends);
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startWorkoutIntent = new Intent(MainActivity.this, FriendsActivity.class);
                startActivity(startWorkoutIntent);
            }
        });

        saveStepsAndActiveMins();
    }

    public void saveStepsAndActiveMins()
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Date dt = new Date();
        SimpleDateFormat d = new SimpleDateFormat("MM-dd-yyyy");
        String strDate = d.format(dt);

        //add new friend to db
        myRef.child("my_app_user").child(uid).child("steps").child(strDate).setValue(TotalnumSteps);
        myRef.child("my_app_user").child(uid).child("activeMins").child(strDate).setValue(Minutes + ":"+ String.format("%02d", Seconds) + ":"+ String.format("%03d", MilliSeconds));

    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();

        TextView nText = (TextView) findViewById(R.id.name);
        if (name != null)
            nText.setText("Hi " + name + "!");

        isSensorRunning = true;
        if (mStepCounter != null) {
            mSensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_DETECTED_ACTIVITY));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onPause() {
        super.onPause();
        isSensorRunning = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("steps", StepsPerDay);
        editor.putBoolean("isSensorRunning", isSensorRunning);
        editor.apply();
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        isSensorRunning = prefs.getBoolean("isSensorRunning", false);
        if (isSensorRunning) {
            StepsPerDay = prefs.getInt("steps", StepsPerDay);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("steps", StepsPerDay);
        editor.putBoolean("isSensorRunning", isSensorRunning);
        editor.apply();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        isSensorRunning = prefs.getBoolean("isSensorRunning", false);
        if (isSensorRunning) {
            StepsPerDay = prefs.getInt("steps", StepsPerDay);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onSensorChanged(SensorEvent event) {
        InitialSensorValue = (int) event.values[0];

        updateStepsForNewDay();
        viewDailySteps.setText(valueOf(StepsPerDay));
        TotalnumSteps++;
        //StepsPerDay = InitialSensorValue -TotalnumSteps;
        //stepCountView.setText(TEXT_NUM_STEPS + valueOf(InitialSensorValue));

        if (isSensorRunning) {
            //Toast.makeText(getBaseContext(),StepsPerDay,Toast.LENGTH_SHORT).show();
            //saveStepsInTextFile();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    //Todo:Reset the counter to zero and start again every day

    //Todo: Save the data and read it on app reopen
    //Save the data at the end of the day

    private void handleUserActivity(int type,int confidence){
        if (type != DetectedActivity.STILL){
            Toast.makeText(getApplicationContext(), "active", Toast.LENGTH_SHORT).show();
            StartTime = SystemClock.uptimeMillis();
            mHandler.postDelayed(runnable, 0);
        }
        if (type == DetectedActivity.STILL){
            Toast.makeText(getApplicationContext(), "still", Toast.LENGTH_SHORT).show();
            TimeBuff += MillisecondTime;
            mHandler.removeCallbacks(runnable);
            stopTracking();

        }
    }
  private void startTracking() {
        Intent intent1 = new Intent(MainActivity.this, RecognizeActivityService.class);
        startService(intent1);
    }
    private void stopTracking() {
        Intent intent = new Intent(MainActivity.this, RecognizeActivityService.class);
        stopService(intent);

    }

    public static String getTimeStamp() {
        Time currentTime = new Time();
        currentTime.setToNow();
        String sTime = currentTime.format("%Y-%m-%d %H:%M:%S");
        return sTime;
    }

    public void updateStepsForNewDay() {

        c = Calendar.getInstance();
        dayat = c.get(Calendar.DAY_OF_MONTH);
        daystr = Integer.toString(dayat);
        nextday = c.get(Calendar.DAY_OF_MONTH) + 1;
        nextdaystr = Integer.toString(nextday);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        if (  !(daystr.equals(nextdaystr)) ) {
            StepsPerDay = InitialSensorValue - TotalnumSteps;
            StepsPerDay = InitialSensorValue -StepsPerDay;
        }else{
            TotalnumSteps = 0;
            StepsPerDay = 0;
        }
        saveStepsAndActiveMins();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public Runnable runnable = new Runnable() {

        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            displayActiveMinutes.setText("" + Minutes + ":"+ String.format("%02d", Seconds) + ":"+ String.format("%03d", MilliSeconds));
            mHandler.postDelayed(this, 0);
        }

    };

}


