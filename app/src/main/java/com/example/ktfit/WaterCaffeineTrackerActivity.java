package com.example.ktfit;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Random;

public class WaterCaffeineTrackerActivity extends AppCompatActivity {
    AlertDialog.Builder builder;
    BarChart barChart;
    String BAR_TAG = "New Bar Entries";
    ArrayList<BarEntry> barEntries;
    ArrayList<String> theAmount;
    Random random;
    TextView waterInput, coffeeInput, updateWaterLimit, updateCoffeeLimit;
    String updateInputGoal;
    BarDataSet barDataSet;
    boolean updateWater = false, updateCoffee = false;
    private int waterGoal=0, waterIntake, coffeeGoal=0, coffeeIntake, sumWater =0,sumCoffee=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);


        builder = new AlertDialog.Builder(this);
        barChart = findViewById(R.id.bar_Chart);
        waterInput = findViewById(R.id.water);
        coffeeInput = findViewById(R.id.coffee);
        updateWaterLimit = findViewById(R.id.updatewater);
        updateCoffeeLimit = findViewById(R.id.updatecoffee);

        createBarGraph();
        updateWaterLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWaterGoal();
            }
        });
        updateCoffeeLimit.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(getBaseContext(),"Please Set the Limit",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void createBarGraph(){
       random = new Random();
        theAmount = new ArrayList<>();
        barEntries = new ArrayList<>();
       if(updateWater && waterIntake <= waterGoal) {
           sumWater = sumWater + waterIntake;
           barEntries.add(new BarEntry(sumWater,0));
           updateWater=false;
           Toast.makeText(getApplicationContext(),String.valueOf(sumWater),Toast.LENGTH_SHORT).show();

        }
        if(updateCoffee && coffeeIntake <= coffeeGoal) {
            sumCoffee = sumCoffee + coffeeIntake;
            barEntries.add(new BarEntry(sumCoffee,1));
            updateCoffee=false;
           Toast.makeText(getApplicationContext(),String.valueOf(sumCoffee),Toast.LENGTH_SHORT).show();
        }

        Log.v("Adding bar graph entry",BAR_TAG);

        barDataSet = new BarDataSet(barEntries,"Quantity in milliLitres");
        theAmount.add("Water");
        theAmount.add("Coffee");

        BarData theData = new BarData(theAmount,barDataSet);
        barChart.setData(theData);
        barChart.setDescription("");
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
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
                waterIntake = Integer.parseInt(updateInput.getText().toString());
                createBarGraph();
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
        builder.setTitle("Please enter the quantity of Water Intake(in mls)");
        final EditText updateInput = new EditText(this);
        updateInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(updateInput);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                coffeeIntake = Integer.parseInt(updateInput.getText().toString());
                createBarGraph();
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
}
