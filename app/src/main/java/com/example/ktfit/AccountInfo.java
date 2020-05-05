package com.example.ktfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

//https://stackoverflow.com/questions/30910704/how-do-i-link-each-user-to-their-data-in-firebase

public class AccountInfo extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private static final String TAG = AccountInfo.class.getSimpleName();
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        // get database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        DatabaseReference userRef = myRef.child("my_app_user").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                email = dataSnapshot.child("email").getValue().toString();
                updateEmail();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


        Button save = findViewById(R.id.save);
        save.setOnClickListener(this);
    }

    public void updateEmail()
    {
        TextView eText = findViewById(R.id.email);
        if (email != "")
        {
            eText.setText(email);
        }
    }

    public void updateInfo(String fName, String lName, String dob, String height, String weight)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fName).build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

//        TextView fName = findViewById(R.id.fName);

        myRef.child("my_app_user").child(uid).child("fname").setValue(fName);
        myRef.child("my_app_user").child(uid).child("lname").setValue(lName);
        myRef.child("my_app_user").child(uid).child("dob").setValue(dob);
        myRef.child("my_app_user").child(uid).child("height").setValue(height);
        myRef.child("my_app_user").child(uid).child("weight").setValue(weight);


        DatabaseReference milestonesRef = myRef.child("my_app_user").child(uid).child("milestones");
        milestonesRef.child("Create an account").setValue(1);
        milestonesRef.child("Start your first Run").setValue(0);
        milestonesRef.child("Start your first Walk").setValue(0);
        milestonesRef.child("Start your first Cycle").setValue(0);


        Intent startMainIntent = new Intent(AccountInfo.this, MainActivity.class);
        startActivity(startMainIntent);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.save) {
            TextView fname = findViewById(R.id.fName);
            TextView lname = findViewById(R.id.lName);
            TextView dob = findViewById(R.id.dob);
            TextView height = findViewById(R.id.height);
            TextView weight  = findViewById(R.id.weight);

            String f = fname.getText().toString();
            String l = lname.getText().toString();
            String d = dob.getText().toString();
            String h = height.getText().toString();
            String w = weight.getText().toString();

            updateInfo(f, l, d, h, w);

        }
    }
}
