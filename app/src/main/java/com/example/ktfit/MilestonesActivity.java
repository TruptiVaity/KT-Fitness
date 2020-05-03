package com.example.ktfit;

import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MilestonesActivity extends AppCompatActivity {

    List<String> milestones;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.setValue("Hello, World!");
        myRef.child("milestones").setValue("Start your first workout");
        //init();
    }









//    public void init(){
//        TableLayout ll = findViewById(R.id.milestones_table);
//
//        TableRow row = new TableRow(this);
//        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//        row.setLayoutParams(lp);
//
//
////        TextView completed = new TextView(this);
////
////        completed.setGravity(Gravity.CENTER);
////        completed.setPadding(15, 10, 15, 10);
////        completed.setText("Completed");
////        completed.setTextSize(25);
////
////
////        row.addView(completed);
////        ll.addView(row);
//
//        for (int i = 0; i <2; i++) { //loop through milestones
//
//            TextView ms = new TextView(this);
////            ImageView image = new ImageView(this);
//
//            ms.setGravity(Gravity.CENTER);
//            ms.setPadding(15, 10, 15, 10);
//            ms.setText("300 km");
//            ms.setTextSize(20);
//
//
//
////            image.setPadding(15, 10, 15, 10);
////            image.setLayoutParams(layp);
//
//
//            row.addView(ms);
////            row.addView(image);
////            ll.addView(row);
//        }
//    }
}
