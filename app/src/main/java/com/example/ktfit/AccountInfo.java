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

public class AccountInfo extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private static final String TAG = AccountInfo.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);



        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);

    }


    public void updateInfo(String dName)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(dName).build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });

        Intent startMainIntent = new Intent(AccountInfo.this, MainActivity.class);
        startActivity(startMainIntent);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.save) {
            TextView name = (TextView) findViewById(R.id.fName);
//            email = user.getText().toString();
//
//            TextView pass = (TextView) findViewById(R.id.password);
//            password = pass.getText().toString();

            updateInfo(name.getText().toString());

        }
    }
}
