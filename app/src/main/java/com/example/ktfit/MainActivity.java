package com.example.ktfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
