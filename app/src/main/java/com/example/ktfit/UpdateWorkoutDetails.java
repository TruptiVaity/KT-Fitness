package com.example.ktfit;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ktfit.data.PlanContract;
import com.example.ktfit.data.PlanDbHelper;


public class UpdateWorkoutDetails extends AppCompatActivity {
    private DatePicker datePicker;
    private TimePicker updateStartTime, updateEndTime;
    private String Check_TAG = "MSG";
    private int mRepeat = 0;
    Spinner updateRepeatSpinner;
    private Button saveButton;
    String inviteFriend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_workout_details);

        datePicker = findViewById(R.id.datePicker);
        updateStartTime = findViewById(R.id.enter_start_time);
        updateEndTime = findViewById(R.id.enter_end_time);
        EditText updateInviteFriend = findViewById(R.id.input_friend);
        updateRepeatSpinner = (Spinner) findViewById(R.id.spinner_repeat);
        saveButton = findViewById(R.id.save_button);
        inviteFriend = updateInviteFriend.getText().toString();

        setupSpinner();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                insertPlan();
                finish();
            }
        });


    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_repeat_options, android.R.layout.simple_spinner_item);
        Log.v(Check_TAG, "Spinner Entered");
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
                        mRepeat = PlanContract.PlanEntry.REPEAT_DAILY;
                    } else if (selection.equals(getString(R.string.repeat_weekly))) {
                        mRepeat = PlanContract.PlanEntry.REPEAT_WEEKLY;
                    } else if (selection.equals(getString(R.string.repeat_monthly))) {
                        mRepeat = PlanContract.PlanEntry.REPEAT_MONTHLY;
                    } else if (selection.equals(getString(R.string.repeat_never))) {
                        mRepeat = PlanContract.PlanEntry.REPEAT_NEVER;
                    } else {
                        mRepeat = PlanContract.PlanEntry.REPEAT_UNKNOWN; //Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mRepeat = 0; // Unknown
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void insertPlan() {

        int day = datePicker.getDayOfMonth(); // get the selected day of the month
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        int startTimeHour = updateStartTime.getHour();
        int startTimeMinute = updateStartTime.getMinute();
        int endTimeHour = updateEndTime.getHour();
        int endTimeMinute = updateEndTime.getMinute();

        PlanDbHelper mDbHelper = new PlanDbHelper(this);

        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PlanContract.PlanEntry.COLUMN_DAY, day);
        values.put(PlanContract.PlanEntry.COLUMN_MONTH, month);
        values.put(PlanContract.PlanEntry.COLUMN_YEAR, year);
        values.put(PlanContract.PlanEntry.COLUMN_START_TIME_HOUR, startTimeHour);
        values.put(PlanContract.PlanEntry.COLUMN_START_TIME_MINUTE, startTimeMinute);
        values.put(PlanContract.PlanEntry.COLUMN_END_TIME_HOUR, endTimeHour);
        values.put(PlanContract.PlanEntry.COLUMN_END_TIME_MINUTE, endTimeMinute);
        values.put(PlanContract.PlanEntry.COLUMN_FRIEND, inviteFriend);
        values.put(PlanContract.PlanEntry.COLUMN_REPEAT, mRepeat);

        //long newRowID = db.insert(PlanContract.PlanEntry.TABLE_NAME,null,values);
        Uri newUri = getContentResolver().insert(PlanContract.PlanEntry.CONTENT_URI, values);

 /**       if (newRowID == -1) {
            Toast.makeText(this, "Error with saving", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Plan saved with row id:" + newRowID, Toast.LENGTH_SHORT).show();
        }
**/
        if (newUri == null) {
            Toast.makeText(this, "Insertion failed",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Insertion successful", Toast.LENGTH_SHORT).show();
        }
    }
}
