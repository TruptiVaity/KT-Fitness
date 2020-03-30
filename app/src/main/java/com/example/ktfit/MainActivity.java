package com.example.ktfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mStepCounter;
    private boolean isSensorRunning = false;
    TextView stepCountView;
    private static int numSteps;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        stepCountView = findViewById(R.id.step_counting);


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
    protected void onResume(){
        super.onResume();
        isSensorRunning = true;
        if(mStepCounter != null){
            mSensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            Toast.makeText(this,"Sensor not found",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isSensorRunning = false;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        numSteps = (int) event.values[0];
        if(isSensorRunning){
            stepCountView.setText(TEXT_NUM_STEPS + String.valueOf(numSteps));
            // updateIfNecessary();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
