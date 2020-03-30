package com.example.ktfit;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.List;

public class PlannerActivity extends AppCompatActivity implements WeekView.EventClickListener {
    private String sessionID = null;
    private String Check_TAG = "SPINNER";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        // Get a reference for the week view in the layout.
       // WeekView mWeekView = (WeekView) findViewById(R.id.weekView);

// Set an action when any event is clicked.
   /**     mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {

            }
        });

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                return null;
            }
        });

// Set long press listener for events.
        mWeekView.setEventLongPressListener(new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

            }
        });
    */
        Button plannerAdd = findViewById(R.id.planner_add_button);
        plannerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionID = "1";
                Intent updateIntent = new Intent(PlannerActivity.this,UpdateWorkoutDetails.class);
                updateIntent.putExtra("EXTRA_SESSION_ID",sessionID);
                startActivity(updateIntent);
            }
        });

    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }
}
