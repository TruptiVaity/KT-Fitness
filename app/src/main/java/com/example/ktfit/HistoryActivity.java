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
    String distance;
    String speed;

    History(String da, String du, String w, String dis, String sp)
    {
        date = da;
        duration = du;
        workout = w;
        distance = dis;
        speed = sp;
    }
}

public class HistoryActivity extends AppCompatActivity {

    List<History> historyList;
    String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

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
                        History h = new History(hisSnapshot.getKey(), hisSnapshot.child("Duration").getValue().toString(),
                                hisSnapshot.child("WorkoutType").getValue().toString(), hisSnapshot.child("Distance").getValue().toString(),
                                hisSnapshot.child("Speed").getValue().toString());
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
        TextView distanceH = new TextView(this);
        TextView paceH = new TextView(this);

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

        distanceH.setGravity(Gravity.CENTER);
        distanceH.setPadding(15, 4, 15, 4);
        distanceH.setText("Distance");
        distanceH.setTextSize(18);
        distanceH.setBackgroundDrawable(border);
        distanceH.setTypeface(null, Typeface.BOLD);

        paceH.setGravity(Gravity.CENTER);
        paceH.setPadding(15, 4, 15, 4);
        paceH.setText("Speed");
        paceH.setTextSize(18);
        paceH.setBackgroundDrawable(border);
        paceH.setTypeface(null, Typeface.BOLD);

        header.addView(dateH);
        header.addView(workoutH);
        header.addView(durationH);
        header.addView(distanceH);
        header.addView(paceH);
        ll.addView(header);

        //loop through historyList
        for (int i = 0; i <historyList.size(); i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,0,0,0);
            row.setLayoutParams(lp);

            TextView date = new TextView(this);
            TextView workout = new TextView(this);
            TextView duration = new TextView(this);
            TextView distance = new TextView(this);
            TextView pace = new TextView(this);

            date.setGravity(Gravity.CENTER);
            date.setPadding(15, 4, 15, 4);
            date.setText(historyList.get(i).date);
            date.setTextSize(18);
            date.setBackgroundDrawable(border);

            workout.setGravity(Gravity.CENTER);
            workout.setPadding(15, 4, 15, 4);
            workout.setText(historyList.get(i).workout);
            workout.setTextSize(18);
            workout.setBackgroundDrawable(border);

            duration.setGravity(Gravity.CENTER);
            duration.setPadding(15, 4, 15, 4);
            duration.setText(historyList.get(i).duration);
            duration.setTextSize(18);
            duration.setBackgroundDrawable(border);

            distance.setGravity(Gravity.CENTER);
            distance.setPadding(15, 4, 15, 4);
            distance.setText(historyList.get(i).distance);
            distance.setTextSize(18);
            distance.setBackgroundDrawable(border);

            pace.setGravity(Gravity.CENTER);
            pace.setPadding(15, 4, 15, 4);
            pace.setText(historyList.get(i).speed);
            pace.setTextSize(18);
            pace.setBackgroundDrawable(border);

            row.addView(date);
            row.addView(workout);
            row.addView(duration);
            row.addView(distance);
            row.addView(pace);
            ll.addView(row);

        }
    }
}
