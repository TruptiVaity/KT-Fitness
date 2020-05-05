package com.example.ktfit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class Event
{
    String date;
    String id;
    String friend;
    String repeat;


    Event(String i, String d, String f, String r)
    {
        date = d;
        id = id;
        friend = f;
        repeat = r;
    }
}

public class PlannerActivity extends AppCompatActivity{
    private String sessionID = null;
    private String Check_TAG = "SPINNER";
    private int mRepeat;
    List<Event> eventsList;
    String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        eventsList = new ArrayList<Event>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

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
        //  Intent mIntent = getIntent();
        //  mRepeat = mIntent.getIntExtra("repeat_frequency",0);

        getEvents();
    }

    public void getEvents()
    {
        eventsList.clear();

        // get database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        DatabaseReference eventRef = myRef.child("my_app_user").child(uid).child("events");

        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren())
                {
                    for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                        Event e = new Event(eventSnapshot.getKey(), eventSnapshot.child("date").getValue().toString(),
                                eventSnapshot.child("friend").getValue().toString(), eventSnapshot.child("repeat").getValue().toString());
                        eventsList.add(e);
                    }

                    if (!eventsList.isEmpty())
                    {
                        updateEvents();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void updateEvents()
    {
        TableLayout lc = findViewById(R.id.events);
        lc.removeAllViews();

        for (int i = 0; i <eventsList.size(); i++) { //loop through milestones

            TableRow row = new TableRow(this);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            lp.setMargins(25,25,25,25);
            row.setLayoutParams(lp);

            TextView d = new TextView(this);
            TextView f = new TextView(this);
            TextView r = new TextView(this);
            ImageView image = new ImageView(this);

//            ms.setGravity(Gravity.LEFT);
            d.setPadding(0, 15, 0, 15);
            d.setText(eventsList.get(i).date);
            d.setTextSize(15);

            d.setPadding(0, 15, 0, 15);
            if (!eventsList.get(i).friend.equals(""))
                f.setText(" with " + eventsList.get(i).friend);
            f.setTextSize(15);

            d.setPadding(0, 15, 0, 15);
            if (!eventsList.get(i).repeat.equals("REPEAT UNKNOWN"))
                r.setText(", " + eventsList.get(i).repeat);
            r.setTextSize(15);

            image.setImageResource(R.drawable.star);
            image.setColorFilter(image.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            image.setPadding(25,25,25,25);
//            image.getLayoutParams().height = 150;
//            image.getLayoutParams().width= 150;
//            image.requestLayout();

            row.addView(image);
            row.addView(d);
            row.addView(f);
            row.addView(r);

            //add to friends table
            lc.addView(row);

        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        getEvents();
    }
}
