package com.example.ktfit;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC;
import static com.example.ktfit.MainActivity.getTimeStamp;
import static com.example.ktfit.ViewNotification.CHANNEL_2_ID;

public class WaterCaffeineTrackerActivity extends AppCompatActivity{
    private NotificationManagerCompat notificationManager;
    private int notificationTracker = 2;
    private static final String FILE_NAME = "tracker.txt";
    AlertDialog.Builder builder;
    Button waterInput, coffeeInput, setWaterLimit, setCoffeeLimit;
    TextView displayWaterLimit, displayCoffeeLimit, displayWaterIntake, displayCoffeeIntake;
    String updateInputGoal;
    boolean updateWater = false, updateCoffee = false;
    int waterGoal=0, waterIntake=0, coffeeGoal=0, coffeeIntake=0, sumWater =0,sumCoffee=0;
    String uid;
    String strDate;
    DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        notificationManager = NotificationManagerCompat.from(this);
        //sendAsAReminder();

        Date dt = new Date();
        SimpleDateFormat d = new SimpleDateFormat("MM-dd-yyyy");
        strDate = d.format(dt);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        getWater();
        getCaffeine();

        builder = new AlertDialog.Builder(this);
        waterInput = findViewById(R.id.record_water_intake_button);
        coffeeInput = findViewById(R.id.record_coffee_intake_button);
        setWaterLimit = findViewById(R.id.set_water_limit_button);
        setCoffeeLimit = findViewById(R.id.set_coffee_limit_button);
        displayWaterLimit = findViewById(R.id.display_water_limit);
        displayCoffeeLimit = findViewById(R.id.display_coffee_limit);
        displayWaterIntake = findViewById(R.id.display_water_intake);
        displayCoffeeIntake = findViewById(R.id.display_coffee_intake);

        setWaterLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWaterGoal();
            }
        });
        setCoffeeLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateCoffeeGoal();
            }
        });
        waterInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWater = true;
                if(waterGoal!=0) {
                    recordWaterIntake();
                    Intent trackerIntent = new Intent(WaterCaffeineTrackerActivity.this, Reminder.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(WaterCaffeineTrackerActivity.this,
                            100,
                            trackerIntent,PendingIntent.FLAG_UPDATE_CURRENT);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    long timeAtLastUpdate = System.currentTimeMillis();
                    long tensecondsInMillis = 1000 * 3600 * 1;                //For reminder every 1 hour
                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                            timeAtLastUpdate+tensecondsInMillis, pendingIntent);
                }else {
                    Toast.makeText(getBaseContext(),"Please Set the Limit",Toast.LENGTH_SHORT).show();
                }

            }
        });
        coffeeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCoffee = true;
                if(coffeeGoal!=0) {
                    recordCoffeeIntake();

                    Intent trackerIntent = new Intent(WaterCaffeineTrackerActivity.this, Reminder.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(WaterCaffeineTrackerActivity.this,
                            100,
                            trackerIntent,PendingIntent.FLAG_UPDATE_CURRENT);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    long timeAtLastUpdate = System.currentTimeMillis();
                    long tensecondsInMillis = 1000 * 3600 * 1;
                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                            timeAtLastUpdate+tensecondsInMillis, pendingIntent);
                }else {
                    Toast.makeText(getBaseContext(), "Please Set the Limit", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void updateWaterGoal(){
        builder.setTitle("Please enter the quantity of Water you wish to set as limit(in mls)");
        final EditText updateInput = new EditText(this);
        updateInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(updateInput);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateInputGoal = updateInput.getText().toString();
                sumWater = 0;
                displayWaterLimit.setText(updateInputGoal);
                waterGoal = Integer.parseInt(updateInputGoal);
                Toast.makeText(getApplicationContext(),"Water Limit Recorded",Toast.LENGTH_SHORT).show();

                myRef.child("my_app_user").child(uid).child("water").child("goal").setValue(waterGoal);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
    private void updateCoffeeGoal(){
        builder.setTitle("Please enter the quantity of Coffee you wish to set as limit(in mls)");
        final EditText updateInput = new EditText(this);
        updateInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(updateInput);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateInputGoal = updateInput.getText().toString();
                sumCoffee = 0;
                displayCoffeeLimit.setText(updateInputGoal);
                coffeeGoal = Integer.parseInt(updateInputGoal);
                Toast.makeText(getApplicationContext(),"Coffee Limit Recorded",Toast.LENGTH_SHORT).show();
                myRef.child("my_app_user").child(uid).child("caffeine").child("goal").setValue(coffeeGoal);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
    private void recordWaterIntake(){
        builder.setTitle("Please enter the quantity of Water Intake(in mls)");
        final EditText updateInput = new EditText(this);
        updateInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(updateInput);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateInputGoal = updateInput.getText().toString();
                waterIntake = Integer.parseInt(updateInputGoal);
                sumWater = sumWater + waterIntake;
                if(sumWater>=waterGoal){Toast.makeText(getBaseContext(),"Yayy!! Water Goal Acheived",Toast.LENGTH_SHORT).show();}
                displayWaterIntake.setText(String.valueOf(sumWater));
                myRef.child("my_app_user").child(uid).child("water").child(strDate).setValue(sumWater);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void recordCoffeeIntake(){
        builder.setTitle("Please enter the quantity of Coffee Intake(in mls)");
        final EditText updateInput = new EditText(this);
        updateInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(updateInput);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateInputGoal = updateInput.getText().toString();
                coffeeIntake = Integer.parseInt(updateInputGoal);
                sumCoffee = sumCoffee + coffeeIntake;
                if(sumCoffee>=coffeeGoal){Toast.makeText(getBaseContext(),"Yayy!! Caffeine Goal Acheived",Toast.LENGTH_SHORT).show();}
                displayCoffeeIntake.setText(String.valueOf(sumCoffee));
                myRef.child("my_app_user").child(uid).child("caffeine").child(strDate).setValue(sumCoffee);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public void getCaffeine()
    {
        final SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        DatabaseReference userRef = myRef.child("my_app_user").child(uid).child("caffeine");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try
                {
                    String cg = dataSnapshot.child("goal").getValue().toString();
                    coffeeGoal = Integer.parseInt(cg);
                    displayCoffeeLimit.setText(Integer.toString(coffeeGoal));

                    updateCoffee = prefs.getBoolean("coffee input", true);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                try
                {
                    String c = dataSnapshot.child(strDate).getValue().toString();
                    sumCoffee = Integer.parseInt(c);
                    displayCoffeeIntake.setText(String.valueOf(sumCoffee));

                    updateCoffee = prefs.getBoolean("coffee input", true);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                if (updateWater || updateCoffee) {
                    sumCoffee = prefs.getInt("caffeine total", sumCoffee);
                    sumWater = prefs.getInt("water total", sumWater);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void getWater()
    {
        final SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        DatabaseReference userRef = myRef.child("my_app_user").child(uid).child("water");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try
                {
                    String wg = dataSnapshot.child("goal").getValue().toString();
                    waterGoal = Integer.parseInt(wg);
                    displayWaterLimit.setText(Integer.toString(waterGoal));

                    String w = dataSnapshot.child(strDate).getValue().toString();
                    sumWater = Integer.parseInt(w);
                    displayWaterIntake.setText(String.valueOf(sumWater));

                    updateWater = prefs.getBoolean("water input", true);

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                try
                {
                    String w = dataSnapshot.child(strDate).getValue().toString();
                    sumWater = Integer.parseInt(w);
                    displayWaterIntake.setText(String.valueOf(sumWater));

                    updateWater = prefs.getBoolean("water input", true);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                if (updateWater || updateCoffee) {
                    sumCoffee = prefs.getInt("caffeine total", sumCoffee);
                    sumWater = prefs.getInt("water total", sumWater);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        updateWater = prefs.getBoolean("water input", false);
        updateCoffee = prefs.getBoolean("coffee input", false);
        if (updateWater || updateCoffee) {
            sumCoffee = prefs.getInt("caffeine total", sumCoffee);
            sumWater = prefs.getInt("water total", sumWater);
        }
        getWater();
        getCaffeine();

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("caffeine total", sumCoffee);
        editor.putInt("water total",sumWater);
        editor.apply();
    }
}


