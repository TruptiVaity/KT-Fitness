package com.example.ktfit;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        init();
    }



    public void init(){
        TableLayout ll = (TableLayout) findViewById(R.id.table_layout);


        for (int i = 0; i <2; i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView date = new TextView(this);
            TextView workout = new TextView(this);
            TextView duration = new TextView(this);
            TextView speed = new TextView(this);
            TextView calories = new TextView(this);


            date.setGravity(Gravity.CENTER);
            date.setPadding(15, 10, 15, 10);
            date.setText("3/29/2020");
            date.setTextSize(20);


            workout.setGravity(Gravity.CENTER);
            workout.setPadding(15, 10, 15, 10);
            workout.setText("Run");
            workout.setTextSize(20);

            duration.setGravity(Gravity.CENTER);
            duration.setPadding(15, 10, 15, 10);
            duration.setText("00:45:02");
            duration.setTextSize(20);

            speed.setGravity(Gravity.CENTER);
            speed.setPadding(15, 10, 15, 10);
            speed.setText("5 mph");
            speed.setTextSize(20);

            calories.setGravity(Gravity.CENTER);
            calories.setPadding(15, 10, 15, 10);
            calories.setText("63 kcal");
            calories.setTextSize(20);


            row.addView(date);
            row.addView(workout);
            row.addView(duration);
            row.addView(speed);
            row.addView(calories);
            ll.addView(row,i);
        }
    }
}
