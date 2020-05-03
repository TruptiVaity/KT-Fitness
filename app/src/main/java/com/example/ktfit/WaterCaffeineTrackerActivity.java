package com.example.ktfit;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import static com.example.ktfit.MainActivity.getTimeStamp;

public class WaterCaffeineTrackerActivity extends AppCompatActivity {
    private static final String FILE_NAME = "tracker.txt";
    AlertDialog.Builder builder;
    //BarChart barChart;
    //String BAR_TAG = "New Bar Entries";
    //ArrayList<BarEntry> barEntries;
    //ArrayList<String> theAmount;
    //Random random;
    Button waterInput, coffeeInput, setWaterLimit, setCoffeeLimit;
    TextView displayWaterLimit, displayCoffeeLimit, displayWaterIntake, displayCoffeeIntake;
    String updateInputGoal;
    //BarDataSet barDataSet;
    boolean updateWater = false, updateCoffee = false;
    int waterGoal=0, waterIntake=0, coffeeGoal=0, coffeeIntake=0, sumWater =0,sumCoffee=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);


        builder = new AlertDialog.Builder(this);
        waterInput = findViewById(R.id.record_water_intake_button);
        coffeeInput = findViewById(R.id.record_coffee_intake_button);
        setWaterLimit = findViewById(R.id.set_water_limit_button);
        setCoffeeLimit = findViewById(R.id.set_coffee_limit_button);
        displayWaterLimit = findViewById(R.id.display_water_limit);
        displayCoffeeLimit = findViewById(R.id.display_coffee_limit);
        displayWaterIntake = findViewById(R.id.display_water_intake);
        displayCoffeeIntake = findViewById(R.id.display_coffee_intake);
       // createBarGraph();
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
                    //createBarGraph();
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
                    //createBarGraph();
                }else {
                    Toast.makeText(getBaseContext(), "Please Set the Limit", Toast.LENGTH_SHORT).show();
                }

            }
        });

 /**   private void createBarGraph(){
        random = new Random();
        theAmount = new ArrayList<>();
        barEntries = new ArrayList<>();
        if(updateWater && waterIntake <= waterGoal) {
            sumWater = sumWater + waterIntake;
            barEntries.add(new BarEntry(sumWater, 0));
            updateWater = false;
            Toast.makeText(getApplicationContext(), String.valueOf(sumWater), Toast.LENGTH_SHORT).show();
        }
        if(updateCoffee && coffeeIntake <= coffeeGoal) {
            sumCoffee = sumCoffee + coffeeIntake;
            barEntries.add(new BarEntry(sumCoffee,1));
            updateCoffee=false;
            Toast.makeText(getApplicationContext(),String.valueOf(sumCoffee),Toast.LENGTH_SHORT).show();
        }

        Log.v("Adding bar graph entry",BAR_TAG);

     //   barDataSet = new BarDataSet(barEntries,"Quantity in milliLitres");
        theAmount.add("Water");
        theAmount.add("Coffee");

   BarData theData = new BarData(theAmount,barDataSet);
        barChart.setData(theData);
        barChart.setDescription("");
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
    **/
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
                displayWaterLimit.setText(updateInputGoal);
                waterGoal = Integer.parseInt(updateInputGoal);
                Toast.makeText(getApplicationContext(),"Water Limit Recorded",Toast.LENGTH_SHORT).show();
                //createBarGraph();
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
                displayCoffeeLimit.setText(updateInputGoal);
                coffeeGoal = Integer.parseInt(updateInputGoal);
                Toast.makeText(getApplicationContext(),"Coffee Limit Recorded",Toast.LENGTH_SHORT).show();
                //createBarGraph();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveInTextFile() {
        try {

            String fileContents = getTimeStamp().concat("\n");

            FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_APPEND);
            fileOutputStream.write(fileContents.getBytes());
            Toast.makeText(getBaseContext(),"Saved to file",Toast.LENGTH_SHORT).show();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
