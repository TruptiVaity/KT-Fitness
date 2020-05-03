package com.example.ktfit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Calendar;
import java.util.List;

public class PlannerActivity extends AppCompatActivity{
    private String sessionID = null;
    private String Check_TAG = "SPINNER";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        CalendarView calendarView = findViewById(R.id.calendar);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    int array[] = {year,month,dayOfMonth};
                    // TODO Click on the calendar should call the update workout details page

                    Intent updateIntent = new Intent(PlannerActivity.this, UpdateWorkoutDetails.class);
                    updateIntent.putExtra("date", array);
                    //updateIntent.putIntegerArrayListExtra(array)
                    startActivity(updateIntent);
                }

        });

        Button plannerAdd = findViewById(R.id.planner_add_button);
        plannerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateIntent = new Intent(PlannerActivity.this,UpdateWorkoutDetails.class);
                updateIntent.putExtra("EXTRA_SESSION_ID",sessionID);
                startActivity(updateIntent);
            }
        });

    }

}
