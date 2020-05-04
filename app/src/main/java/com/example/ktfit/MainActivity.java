package com.example.ktfit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.provider.Settings.Secure.getLong;
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
    String daystr, nextdaystr;
    private static final String FILE_NAME = "stepcount.txt";
    public String activityDetected;
    private static final String TEXT_NUM_STEPS_PER_DAY = "Number of Steps today: ";
    TextView viewDailySteps;



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

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        viewDailySteps = findViewById(R.id.view_daily_steps);
        //activityNumber = getIntent().getIntExtra("activityDetected",activityNumber);

        //displayActiveMinutes = findViewById(R.id.display_active_minutes);
        //displayActiveMinutes.setText(activityNumber);
        //Toast.makeText(getBaseContext(),String.valueOf(activityNumber),Toast.LENGTH_SHORT).show();

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSensorRunning = true;
        if (mStepCounter != null) {
            mSensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onPause() {
        super.onPause();

        isSensorRunning = false;
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveStepsInTextFile() {
        try {
            String fileContents = getTimeStamp().concat("\t" + TotalnumSteps + "\n");

            FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_APPEND);
            fileOutputStream.write(fileContents.getBytes());
            Toast.makeText(getBaseContext(), "Saved to file", Toast.LENGTH_SHORT).show();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        //String currentDate = df.format(c.getTime());
        //String dateTime = df.format(c.getTime());
        if (  !(daystr.equals(nextdaystr)) ) {
            StepsPerDay = InitialSensorValue - TotalnumSteps;
            StepsPerDay = InitialSensorValue -StepsPerDay;
        }else{
            TotalnumSteps = 0;
            StepsPerDay = 0;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent(MainActivity.this,RecognizeActivityService.class);
        PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient,3000,pendingIntent);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        isSensorRunning = prefs.getBoolean("isSensorRunning", false);
        if (isSensorRunning) {
            StepsPerDay = prefs.getInt("steps", StepsPerDay);
        }
    }

}


