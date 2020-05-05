package com.example.ktfit;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class Friend
{
    String fName;
    String lName;
    String uid;


    Friend(String id, String f, String l)
    {
        fName = f;
        lName = l;
        uid = id;
    }
}

public class FriendsActivity extends AppCompatActivity {
    List<Friend> friendsList;
    List<Friend> suggestionsList;
    String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friendsList = new ArrayList<Friend>();
        suggestionsList = new ArrayList<Friend>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        getFriends();
        getSuggestions();

    }

    public void getFriends()
    {
        friendsList.clear();

        // get database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        DatabaseReference friendRef = myRef.child("my_app_user").child(uid).child("friends");

        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot: dataSnapshot.getChildren()) {
                    Friend f = new Friend(friendSnapshot.getKey(), friendSnapshot.child("fname").getValue().toString(), friendSnapshot.child("lname").getValue().toString());
                    friendsList.add(f);
                }
                if (!friendsList.isEmpty())
                {
                    updateFriendList();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void getSuggestions()
    {
        suggestionsList.clear();

        // get database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        DatabaseReference usersRef = myRef.child("my_app_user");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot suggSnapshot: dataSnapshot.getChildren()) {
                    if (!uid.equals(suggSnapshot.getKey()))
                    {
                        Friend f = new Friend(suggSnapshot.getKey(), suggSnapshot.child("fname").getValue().toString(), suggSnapshot.child("lname").getValue().toString());
                        boolean contains = false;
                        for (int i=0; i<friendsList.size(); i++)
                        {
                            if (friendsList.get(i).uid.equals(suggSnapshot.getKey()))
                                contains = true;
                        }
                        if (!contains)
                            suggestionsList.add(f);
                    }
                }

                if (!suggestionsList.isEmpty())
                {
                    updateSuggestions();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void updateSuggestions()
    {
        TableLayout lc = findViewById(R.id.suggestions);
        lc.removeAllViews();

        for (int i = 0; i <suggestionsList.size(); i++) { //loop through milestones

            TableRow row = new TableRow(this);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            lp.setMargins(25,25,25,25);
            row.setLayoutParams(lp);

            TextView ms = new TextView(this);
            ImageView image = new ImageView(this);

            ms.setGravity(Gravity.LEFT);
            ms.setPadding(15, 15, 15, 15);
            final String fName = suggestionsList.get(i).fName;
            final String lName = suggestionsList.get(i).lName;
            final String friendName = fName + " " + lName;
            final String fid = suggestionsList.get(i).uid;
            ms.setText(fName + " " + lName);
            ms.setTextSize(26);

            ms.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(FriendsActivity.this)
                            .setTitle("Add Friend")
                            .setMessage("Are you sure you want to add " + friendName + "?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Add friend
                                    addNewFriend(fid, fName, lName);
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return true;
                }
            });

            image.setImageResource(R.drawable.person);
            image.setColorFilter(image.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            image.setPadding(25,25,25,25);
//            image.requestLayout();
//            image.getLayoutParams().height = 150;
//            image.getLayoutParams().width= 150;
//            image.setScaleType(ImageView.ScaleType.FIT_XY);


            row.addView(image);
            row.addView(ms);

            //add to friends table
            lc.addView(row);

        }
    }

    public void updateFriendList()
    {
        TableLayout lc = findViewById(R.id.friendLayout);
        lc.removeAllViews();

        for (int i = 0; i <friendsList.size(); i++) { //loop through milestones

            TableRow row = new TableRow(this);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            lp.setMargins(25,25,25,25);
            row.setLayoutParams(lp);

            TextView ms = new TextView(this);
            ImageView image = new ImageView(this);

            ms.setGravity(Gravity.LEFT);
            ms.setPadding(15, 15, 15, 15);
            final String friendName = friendsList.get(i).fName + " " + friendsList.get(i).lName;
            ms.setText(friendName);
            ms.setTextSize(26);

            image.setImageResource(R.drawable.face);
            image.setColorFilter(image.getContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            image.setPadding(25,25,25,25);
//            image.getLayoutParams().height = 150;
//            image.getLayoutParams().width= 150;
//            image.requestLayout();

            row.addView(image);
            row.addView(ms);

            //add to friends table
            lc.addView(row);

        }

    }

    public void addNewFriend(String fid, String fName, String lName)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        //add new friend to db
        myRef.child("my_app_user").child(uid).child("friends").child(fid).child("fname").setValue(fName);
        myRef.child("my_app_user").child(uid).child("friends").child(fid).child("lname").setValue(lName);

        getFriends();
        getSuggestions();
    }

}
