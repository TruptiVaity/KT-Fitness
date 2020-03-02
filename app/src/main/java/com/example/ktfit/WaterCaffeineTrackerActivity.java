package com.example.ktfit;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
public class WaterCaffeineTrackerActivity extends AppCompatActivity {
    AlertDialog.Builder builder;
    TextView waterInput, coffeeInput, updateWaterLimit, updateCoffeeLimit;
    String updateInputGoal;
    boolean update = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        builder = new AlertDialog.Builder(this);

        waterInput = (TextView) findViewById(R.id.water);
        coffeeInput = (TextView) findViewById(R.id.coffee);
        updateWaterLimit = (TextView) findViewById(R.id.updatewater);
        updateCoffeeLimit = (TextView) findViewById(R.id.updatecoffee);

        updateWaterLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update = true;
                updateGoal();
            }
        });
        updateCoffeeLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update = true;
                updateGoal();
            }
        });
        waterInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update = false;
                updateGoal();
            }
        });
        coffeeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update = false;
                updateGoal();
            }
        });
    }

    private void updateGoal(){
        if(update == true ){
        builder.setTitle("Please enter the quantity you wish to set as limit(in litres)");}
        else{
            builder.setTitle("Please enter intake quantity(in litres)");
        }
        final EditText updateInput = new EditText(this);
                updateInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(updateInput);

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            updateInputGoal = updateInput.getText().toString();
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
