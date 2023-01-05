package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private OtpView otpView;
    String number,verification_code;
    TextView send,validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        Intent i=getIntent();
        number = i.getStringExtra("number");
       // Toast.makeText(this,code,Toast.LENGTH_LONG).show();

        otpView = findViewById(R.id.otp_view);
        send=findViewById(R.id.send);
        validate=findViewById(R.id.validate);




    }

}
