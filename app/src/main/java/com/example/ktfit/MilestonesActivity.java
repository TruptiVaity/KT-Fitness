package com.example.ktfit;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones);

        milestonesList = new ArrayList<Milestones>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();



        milestonesRef = myRef.child("my_app_user").child(uid).child("milestones");
//        milestonesRef.limitToLast(10);

//        milestonesRef = myRef.child("my_app_user").child(uid);
        milestonesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                milestones = dataSnapshot.getValue();
//                Object m = dataSnapshot.getChildren();

//                Log.d(TAG, "miles: " + milestones);
                for (DataSnapshot mileSnapshot: dataSnapshot.getChildren()) {
//                    boolean complete = (boolean) mileSnapshot.child("Start your first Run").getValue();
//                    Object m = mileSnapshot.getValue();
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


//        milestonesRef.child("Complete an account").setValue(0);
//        milestonesRef.child("Start your first Run").setValue(0);
//        milestonesRef.child("Start your first Walk").setValue(1);
//        milestonesRef.child("Start your first Cycle").setValue(1);



    }


    public void updateTables(){
        TableLayout ll = findViewById(R.id.completed);

        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

//        milestones.

        for (int i = 0; i <1; i++) { //loop through milestones

            TextView ms = new TextView(this);
            ImageView image = new ImageView(this);

            ms.setGravity(Gravity.CENTER);
            ms.setPadding(15, 10, 15, 10);
//            ms.setText("300 km");
            ms.setText(milestonesList.get(i).mile);
            ms.setTextSize(20);



//            image.setPadding(15, 10, 15, 10);
//            image.setLayoutParams(layp);
            image.setImageResource(R.drawable.check);

            row.addView(image);
            row.addView(ms);

            ll.addView(row);
        }
    }
}
