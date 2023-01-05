package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maksim88.passwordedittext.PasswordEditText;

public class PasswordChange extends AppCompatActivity {

    PasswordEditText old,new1;
    FirebaseUser user;
    Button change;
    ImageView imageView,back;
    TextView text,heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        old=findViewById(R.id.oldpassword);
        new1=findViewById(R.id.newpassword);
        change = findViewById(R.id.change);
        imageView = findViewById(R.id.image);
        text=findViewById(R.id.text);
        heading=findViewById(R.id.heading);
        back = findViewById(R.id.back);

        change.setTag("change");

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!v.getTag().equals("change"))
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(PasswordChange.this, MainActivity.class));
                    finishAffinity();
                }
                else {

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    final String email = user.getEmail();
                    final String oldpass = old.getText().toString();
                    final String newpass = new1.getText().toString();

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, oldpass);

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    imageView.setImageBitmap(null);
                                                    imageView.setBackgroundResource(R.drawable.success1);

                                                    heading.setText("Password Changed !");
                                                    text.setText("You can use New Password To login");

                                                    old.setVisibility(View.INVISIBLE);
                                                    new1.setVisibility(View.INVISIBLE);

                                                    change.setText("Go To Login");
                                                    change.setTag("login");
                                                    back.setVisibility(View.INVISIBLE);
                                                    // login.setVisibility(View.VISIBLE);

                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Some Issue related Occured!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Authentication Error!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordChange.this,MainActivity.class));
                finishAffinity();
            }
        });

    }

}
