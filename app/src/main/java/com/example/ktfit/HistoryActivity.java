package com.example.ktfit;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

<<<<<<< HEAD
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class History
{
    String date;
    String duration;
    String workout;

    History(String da, String du, String w)
    {
        date = da;
        duration = du;
        workout = w;
    }
}

public class HistoryActivity extends AppCompatActivity {

    List<History> historyList;
    String uid;

=======
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

<<<<<<< HEAD
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        historyList = new ArrayList<History>();

        getHistory();
    }

    public void getHistory()
    {

        // get database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        DatabaseReference friendRef = myRef.child("my_app_user").child(uid).child("workouts");

        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren())
                {
                    for (DataSnapshot hisSnapshot: dataSnapshot.getChildren()) {
                        History h = new History(hisSnapshot.getKey(), hisSnapshot.child("Duration").getValue().toString(), hisSnapshot.child("WorkoutType").getValue().toString());
                        historyList.add(h);
                    }
                    if (!historyList.isEmpty())
                    {
                        updateHistory();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void updateHistory(){
=======

        init();
    }


    public void init(){
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
        TableLayout ll = findViewById(R.id.history_table);

        ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setStyle(Paint.Style.STROKE);
        border.getPaint().setColor(Color.BLACK);

        TableRow header = new TableRow(this);
        TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0,0,0,0);
        header.setLayoutParams(llp);

        TextView dateH = new TextView(this);
        TextView workoutH = new TextView(this);
        TextView durationH = new TextView(this);
        TextView speedH = new TextView(this);
        TextView caloriesH = new TextView(this);

        dateH.setGravity(Gravity.CENTER);
        dateH.setPadding(15, 4, 15, 4);
        dateH.setText("Date");
        dateH.setTextSize(18);
        dateH.setBackgroundDrawable(border);
        dateH.setTypeface(null, Typeface.BOLD);


        workoutH.setGravity(Gravity.CENTER);
        workoutH.setPadding(15, 4, 15, 4);
        workoutH.setText("Workout");
        workoutH.setTextSize(18);
        workoutH.setBackgroundDrawable(border);
        workoutH.setTypeface(null, Typeface.BOLD);

        durationH.setGravity(Gravity.CENTER);
        durationH.setPadding(15, 4, 15, 4);
        durationH.setText("Duration");
        durationH.setTextSize(18);
        durationH.setBackgroundDrawable(border);
        durationH.setTypeface(null, Typeface.BOLD);

<<<<<<< HEAD
//        speedH.setGravity(Gravity.CENTER);
//        speedH.setPadding(15, 4, 15, 4);
//        speedH.setText("Speed");
//        speedH.setTextSize(18);
//        speedH.setBackgroundDrawable(border);
//        speedH.setTypeface(null, Typeface.BOLD);
//
//        caloriesH.setGravity(Gravity.CENTER);
//        caloriesH.setPadding(15, 4, 15, 4);
//        caloriesH.setText("Calories");
//        caloriesH.setTextSize(18);
//        caloriesH.setBackgroundDrawable(border);
//        caloriesH.setTypeface(null, Typeface.BOLD);
=======
        speedH.setGravity(Gravity.CENTER);
        speedH.setPadding(15, 4, 15, 4);
        speedH.setText("Speed");
        speedH.setTextSize(18);
        speedH.setBackgroundDrawable(border);
        speedH.setTypeface(null, Typeface.BOLD);

        caloriesH.setGravity(Gravity.CENTER);
        caloriesH.setPadding(15, 4, 15, 4);
        caloriesH.setText("Calories");
        caloriesH.setTextSize(18);
        caloriesH.setBackgroundDrawable(border);
        caloriesH.setTypeface(null, Typeface.BOLD);
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba

        header.addView(dateH);
        header.addView(workoutH);
        header.addView(durationH);
<<<<<<< HEAD
//        header.addView(speedH);
//        header.addView(caloriesH);
        ll.addView(header);

        //loop through historyList
        for (int i = 0; i <historyList.size(); i++) {
=======
        header.addView(speedH);
        header.addView(caloriesH);
        ll.addView(header);

        for (int i = 0; i <2; i++) {
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,0,0,0);
            row.setLayoutParams(lp);

            TextView date = new TextView(this);
            TextView workout = new TextView(this);
            TextView duration = new TextView(this);
            TextView speed = new TextView(this);
            TextView calories = new TextView(this);

<<<<<<< HEAD
            Date dt = new Date();
            SimpleDateFormat d = new SimpleDateFormat("MM/dd/yyyy' 'HH:mm:ss");
            String strDate = d.format(dt);

            date.setGravity(Gravity.CENTER);
            date.setPadding(15, 4, 15, 4);
            date.setText(strDate);
=======
            date.setGravity(Gravity.CENTER);
            date.setPadding(15, 4, 15, 4);
            date.setText("3/29/2020");
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
            date.setTextSize(18);
            date.setBackgroundDrawable(border);

            workout.setGravity(Gravity.CENTER);
            workout.setPadding(15, 4, 15, 4);
<<<<<<< HEAD
            workout.setText(historyList.get(i).workout);
=======
            workout.setText("Run");
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
            workout.setTextSize(18);
            workout.setBackgroundDrawable(border);

            duration.setGravity(Gravity.CENTER);
            duration.setPadding(15, 4, 15, 4);
<<<<<<< HEAD
            duration.setText(historyList.get(i).duration);
            duration.setTextSize(18);
            duration.setBackgroundDrawable(border);

//            speed.setGravity(Gravity.CENTER);
//            speed.setPadding(15, 4, 15, 4);
//            speed.setText("5 mph");
//            speed.setTextSize(18);
//            speed.setBackgroundDrawable(border);
//
//            calories.setGravity(Gravity.CENTER);
//            calories.setPadding(15, 4, 15, 4);
//            calories.setText("63 kcal");
//            calories.setTextSize(18);
//            calories.setBackgroundDrawable(border);
=======
            duration.setText("00:45:02");
            duration.setTextSize(18);
            duration.setBackgroundDrawable(border);

            speed.setGravity(Gravity.CENTER);
            speed.setPadding(15, 4, 15, 4);
            speed.setText("5 mph");
            speed.setTextSize(18);
            speed.setBackgroundDrawable(border);

            calories.setGravity(Gravity.CENTER);
            calories.setPadding(15, 4, 15, 4);
            calories.setText("63 kcal");
            calories.setTextSize(18);
            calories.setBackgroundDrawable(border);
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba

            row.addView(date);
            row.addView(workout);
            row.addView(duration);
<<<<<<< HEAD
//            row.addView(speed);
//            row.addView(calories);
=======
            row.addView(speed);
            row.addView(calories);
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
            ll.addView(row);

        }
    }
}
