package com.example.ktfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private static final String TAG = UserActivity.class.getSimpleName();
    private DatabaseReference myDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

//        TextView user = (TextView) findViewById(R.id.email);
//        email = user.getText().toString();
//
//        TextView pass = (TextView) findViewById(R.id.password);
//        password = pass.getText().toString();

        Button signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        Button create = (Button) findViewById(R.id.createAccount);
        create.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        myDatabaseRef = FirebaseDatabase.getInstance().getReference();



    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
//        if (!validateForm()) {
//            return;
//        }
//
//        showProgressBar();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            myDatabaseRef.child("my_app_user").child(user.getUid()).setValue(user);

//                            Intent startMainIntent = new Intent(UserActivity.this, MainActivity.class);
//                            startActivity(startMainIntent);
                            Intent startAccountInfoIntent = new Intent(UserActivity.this, AccountInfo.class);
                            startActivity(startAccountInfoIntent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(UserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
//        if (!validateForm()) {
//            return;
//        }

//        showProgressBar();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            Intent startMainIntent = new Intent(UserActivity.this, MainActivity.class);
                            startActivity(startMainIntent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(UserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);

                        }

//                        // [START_EXCLUDE]
//                        if (!task.isSuccessful()) {
//                            mBinding.status.setText(R.string.auth_failed);
//                        }
//                        hideProgressBar();
//                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.createAccount) {
            TextView user = (TextView) findViewById(R.id.email);
            email = user.getText().toString();

            TextView pass = (TextView) findViewById(R.id.password);
            password = pass.getText().toString();

            createAccount(email, password);

        } else if (i == R.id.signIn) {
            TextView user = (TextView) findViewById(R.id.email);
            email = user.getText().toString();

            TextView pass = (TextView) findViewById(R.id.password);
            password = pass.getText().toString();

            signIn(email, password);

//        } else if (i == R.id.signOutButton) {
//            signOut();
//        } else if (i == R.id.verifyEmailButton) {
//            sendEmailVerification();
//        } else if (i == R.id.reloadButton) {
//            reload();
        }
    }
}
