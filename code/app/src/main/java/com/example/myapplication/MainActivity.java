package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.maksim88.passwordedittext.PasswordEditText;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    AwesomeValidation awesomeValidation;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);

        reference = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified())
        {
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            finishAffinity();
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.signin);
        final EditText email = findViewById(R.id.email);
        final PasswordEditText password = findViewById(R.id.password);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        // awesomeValidation.setColor(Color.YELLOW);
       // awesomeValidation.setContext(this);  // mandatory for UNDERLABEL style
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        String regexPassword = ".{8,}";
        awesomeValidation.addValidation(this, R.id.password, regexPassword, R.string.invalid_password);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           /*     startActivity(new Intent(MainActivity.this, StudentList.class));
                finishAffinity(); */

         /*  if(email.getText().toString().equals("admin@gmail.com") &&  password.getText().toString().equals("12345"))
           {
               startActivity(new Intent(MainActivity.this, StudentList.class));
               finishAffinity();
           }*/

           if (awesomeValidation.validate()) {

                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        if(mAuth.getCurrentUser().getEmail().equals("admin@gmail.com"))
                                        {
                                            /*
                                            FirebaseInstanceId.getInstance().getInstanceId()
                                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {

                                                            if(task.isSuccessful())
                                                            {
                                                                String token_id = task.getResult().getToken();
                                                                Map<String,Object> tokenMap = new HashMap<>();
                                                                tokenMap.put("token_id",token_id);
                                                                reference.child(mAuth.getCurrentUser().getUid()).updateChildren(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        startActivity(new Intent(MainActivity.this, StudentList.class));
                                                                        //startActivity(new Intent(MainActivity.this,PushNotificationExample.class));
                                                                        finishAffinity();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }); */

                                            startActivity(new Intent(MainActivity.this, StudentList.class));
                                            //startActivity(new Intent(MainActivity.this,PushNotificationExample.class));
                                            finishAffinity();
                                        }
                                        else if (mAuth.getCurrentUser().isEmailVerified()) {

                                            FirebaseInstanceId.getInstance().getInstanceId()
                                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                            String token_id = task.getResult().getToken();
                                                            Map<String,Object> tokenMap = new HashMap<>();
                                                            tokenMap.put("token_id",token_id);

                                                            reference.child(mAuth.getCurrentUser().getUid()).updateChildren(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    //startActivity(new Intent(MainActivity.this,PushNotificationExample.class));
                                                                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                                                    finishAffinity();
                                                                }
                                                            });
                                                        }
                                                    });

                                        } else {
                                            Toast.makeText(MainActivity.this, "Please Verify Email Address !", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        });

    }
    public void OpenForgotPassword(View view)
    {
        startActivity(new Intent(MainActivity.this,ForgotPassword.class));
        finishAffinity();
    }

    public void OpenSignupPage(View view) {
        startActivity(new Intent(MainActivity.this,SignupActivity.class));
        finishAffinity();
    }
}
