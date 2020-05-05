package com.example.ktfit;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


class Milestones
{
    String mile;
    String completed;

    Milestones(String m, String c)
    {
        mile = m;
        completed = c;
    }
}

public class MilestonesActivity extends AppCompatActivity {

    List<Milestones> milestonesList;
    Object milestones;
    DatabaseReference milestonesRef;
    private static final String TAG = MilestonesActivity.class.getSimpleName();
    String uid;
//    List<String> his;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones);

        milestonesList = new ArrayList<Milestones>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        updateMilestones();
        getMilestones();
    }

    public void getMilestones()
    {
        milestonesList.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        milestonesRef = myRef.child("my_app_user").child(uid).child("milestones");
        milestonesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot mileSnapshot: dataSnapshot.getChildren()) {
                    Milestones m = new Milestones(mileSnapshot.getKey(), mileSnapshot.getValue().toString());
                    milestonesList.add(m);
                }
                updateTables();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void updateMilestones()
    {
//        his = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        DatabaseReference friendRef = myRef.child("my_app_user").child(uid).child("workouts");
        final DatabaseReference milestonesRef = myRef.child("my_app_user").child(uid).child("milestones");

        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren())
                {
                    for (DataSnapshot hisSnapshot: dataSnapshot.getChildren()) {
                        String h = hisSnapshot.child("WorkoutType").getValue().toString();

                        if (h.equals("Run"))
                        {
                            milestonesRef.child("Start your first Run").setValue(1);
                        }
                        if (h.equals("Walk"))
                        {
                            milestonesRef.child("Start your first Walk").setValue(1);
                        }
                        if (h.equals("Cycle"))
                        {
                            milestonesRef.child("Start your first Cycle").setValue(1);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });



//        for (String h: his)
//        {
//            if (h.equals("Run"))
//            {
//                milestonesRef.child("Start your first Run").setValue(1);
//            }
//            if (h.equals("Walk"))
//            {
//                milestonesRef.child("Start your first Walk").setValue(1);
//            }
//            if (h.equals("Cycle"))
//            {
//                milestonesRef.child("Start your first Cycle").setValue(1);
//            }
//        }
    }

    public void updateTables(){
        TableLayout lc = findViewById(R.id.completed);
        TableLayout lu = findViewById(R.id.upcoming_table);

        lc.removeAllViews();
        lu.removeAllViews();

        for (int i = 0; i <milestonesList.size(); i++) { //loop through milestones

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            TextView ms = new TextView(this);
            ImageView image = new ImageView(this);

            ms.setGravity(Gravity.CENTER);
            ms.setPadding(15, 0, 0, 0);
            ms.setText(milestonesList.get(i).mile);
            ms.setTextSize(20);


            if (milestonesList.get(i).completed.equals("1"))
            {
                image.setImageResource(R.drawable.check);
                image.setColorFilter(image.getContext().getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);

                row.addView(image);
                row.addView(ms);

                //add to complete table
                lc.addView(row);
            }
            else
            {
                image.setImageResource(R.drawable.box);

                row.addView(image);
                row.addView(ms);

                //add to upcoming table
                lu.addView(row);
            }

        }
    }
}
