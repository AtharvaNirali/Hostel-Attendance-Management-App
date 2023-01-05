package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    ImageView imageView,back;
    TextView heading,text;
    EditText email;
    Button reset;
    AwesomeValidation awesomeValidation;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth=FirebaseAuth.getInstance();

        back=findViewById(R.id.back);
        text=findViewById(R.id.text);
        email=findViewById(R.id.email);
        reset=findViewById(R.id.reset);
        imageView=findViewById(R.id.image);
        heading=findViewById(R.id.heading);

        reset.setTag("reset");

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(awesomeValidation.validate()) {
                    if (!v.getTag().equals("reset")) {
                        startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                        finishAffinity();
                    } else
                    {
                        auth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    imageView.setImageBitmap(null);
                                    imageView.setBackgroundResource(R.drawable.success1);

                                    heading.setText("Check Your Email !");
                                    text.setText("We have sent you a reset password link \n on your registered Email Address.");
                                    email.setVisibility(View.INVISIBLE);
                                    back.setVisibility(View.INVISIBLE);
                                    reset.setText("Go To Login");
                                    reset.setTag("Login");

                                } else {
                                    Toast.makeText(getApplicationContext(), "Some Issue related Occured!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                     }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                finishAffinity();
            }
        });

    }



}
