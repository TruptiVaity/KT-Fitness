package com.example.ktfit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
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
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import static com.example.ktfit.MainActivity.getTimeStamp;

public class WaterCaffeineTrackerActivity extends AppCompatActivity{
    private NotificationManagerCompat notificationManager;
    private int notificationTracker = 2;
    private static final String FILE_NAME = "tracker.txt";
    AlertDialog.Builder builder;
    String uid;
    DatabaseReference trackerRef;
    Button waterInput, coffeeInput, setWaterLimit, setCoffeeLimit;
    TextView displayWaterLimit, displayCoffeeLimit, displayWaterIntake, displayCoffeeIntake;
    String updateInputGoal;
    boolean updateWater = false, updateCoffee = false;
    int waterGoal=0, waterIntake=0, coffeeGoal=0, coffeeIntake=0, sumWater =0,sumCoffee=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        notificationManager = NotificationManagerCompat.from(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

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
                    long tensecondsInMillis = 1000 * 30;
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
                    long tensecondsInMillis = 1000 * 30;
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


