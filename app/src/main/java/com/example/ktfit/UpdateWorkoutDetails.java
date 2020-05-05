package com.example.ktfit;

<<<<<<< HEAD
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
=======
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.ktfit.data.PlanContract;

import java.io.File;
import java.io.FileOutputStream;
<<<<<<< HEAD
import java.util.Calendar;
=======
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba

public class UpdateWorkoutDetails extends AppCompatActivity {
    private DatePicker datePicker;
    private TimePicker updateStartTime, updateEndTime;
    private String TAG = "Workout Details";
    private static final String FILE_NAME = "myworkoutdetails.txt";
    private String mRepeat;
    Spinner updateRepeatSpinner;
    private Button saveButton;
    EditText updateInviteFriend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_workout_details);

        datePicker = findViewById(R.id.datePicker);
        updateStartTime = findViewById(R.id.enter_start_time);
        updateEndTime = findViewById(R.id.enter_end_time);
        updateInviteFriend = findViewById(R.id.input_friend);
        updateRepeatSpinner = (Spinner) findViewById(R.id.spinner_repeat);
        saveButton = findViewById(R.id.save_button);

        int array[] = getIntent().getIntArrayExtra("date");
        if(array !=null ){
            datePicker.init(array[0], array[1], array[2], null);
        }

        setupSpinner();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //TODO We need to save on click of this save button
                savePlanInTextFile();
<<<<<<< HEAD

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,23);
                calendar.set(Calendar.MINUTE,59);
                calendar.set(Calendar.SECOND,59);

                Intent trackerIntent = new Intent(getApplicationContext(), Reminder.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                        100, trackerIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
=======
                //insertPlan();
                //finish();
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
            }
        });


    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_repeat_options, android.R.layout.simple_spinner_item);
        Log.v(TAG, "Spinner Entered");
        // Specify dropdown layout style - simple list view with 1 item per line
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        updateRepeatSpinner.setAdapter(spinnerAdapter);

        // Set the integer mSelected to the constant values
        updateRepeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                //Toast.makeText(getBaseContext(), "Spinner Selection", Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.repeat_daily))) {
                        mRepeat = "REPEAT DAILY";
                    } else if (selection.equals(getString(R.string.repeat_weekly))) {
<<<<<<< HEAD
                        mRepeat = "REPEAT WEEKLY";
=======
                        mRepeat = "REPEAT DAILY";
>>>>>>> fe598f4502890bc7ecfc3b12416407bfe46befba
                    } else if (selection.equals(getString(R.string.repeat_monthly))) {
                        mRepeat = "REPEAT MONTHLY";
                    } else if (selection.equals(getString(R.string.repeat_never))) {
                        mRepeat = "REPEAT NEVER";
                    } else {
                        mRepeat = "REPEAT UNKNOWN"; //Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mRepeat = getString(R.string.repeat_unknown); // Unknown
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void savePlanInTextFile() {
        try {
            //TODO These are the contents to be saved
            //TODO datePicker.getMonth()+1 + "/" + datePicker.getDayOfMonth()+ "/" + datePicker.getYear() gives us pur date of the selected day
            //TODO start time and end time gives us the start and end time of the workout planned on that day
            String fileContents = datePicker.getMonth()+1 + "/" + datePicker.getDayOfMonth()+ "/" + datePicker.getYear() +
                    "\t"+ updateStartTime.getHour() + ":"+updateStartTime.getMinute() +
                    "\t"+updateEndTime.getHour() + ":"+updateEndTime.getMinute() +
                    "\t"+(updateInviteFriend.getText().toString()).concat("\t"+ mRepeat+"\n");

            FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_APPEND);
            fileOutputStream.write(fileContents.getBytes());
            Log.v(TAG, "Writing...");
            Toast.makeText(getBaseContext(),"Saved to file",Toast.LENGTH_SHORT).show();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}












 /*   @RequiresApi(api = Build.VERSION_CODES.M)
    private void insertPlan() {

        int day = datePicker.getDayOfMonth(); // get the selected day of the month
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        int startTimeHour = updateStartTime.getHour();
        int startTimeMinute = updateStartTime.getMinute();
        int endTimeHour = updateEndTime.getHour();
        int endTimeMinute = updateEndTime.getMinute();

        //PlanDbHelper mDbHelper = new PlanDbHelper(this);

        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PlanContract.PlanEntry.COLUMN_DAY, day);
        values.put(PlanContract.PlanEntry.COLUMN_MONTH, month);
        values.put(PlanContract.PlanEntry.COLUMN_YEAR, year);
        values.put(PlanContract.PlanEntry.COLUMN_START_TIME_HOUR, startTimeHour);
        values.put(PlanContract.PlanEntry.COLUMN_START_TIME_MINUTE, startTimeMinute);
        values.put(PlanContract.PlanEntry.COLUMN_END_TIME_HOUR, endTimeHour);
        values.put(PlanContract.PlanEntry.COLUMN_END_TIME_MINUTE, endTimeMinute);
        values.put(PlanContract.PlanEntry.COLUMN_FRIEND, updateInviteFriend.getText().toString());
        values.put(PlanContract.PlanEntry.COLUMN_REPEAT, mRepeat);

        //long newRowID = db.insert(PlanContract.PlanEntry.TABLE_NAME,null,values);
        Uri newUri = getContentResolver().insert(PlanContract.PlanEntry.CONTENT_URI, values);

        /**       if (newRowID == -1) {
         Toast.makeText(this, "Error with saving", Toast.LENGTH_SHORT).show();
         } else {
         Toast.makeText(this, "Plan saved with row id:" + newRowID, Toast.LENGTH_SHORT).show();
         }

        if (newUri == null) {
            Toast.makeText(this, "Insertion failed",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Insertion successful", Toast.LENGTH_SHORT).show();
        }
    }
**/