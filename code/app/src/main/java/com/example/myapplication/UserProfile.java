package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference reference;
    TextView Name,Email,Phone,MainName,ParentPhone,Address;
    CircleImageView image;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);
        loading=findViewById(R.id.progressBarLoading);

        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.userprofile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(),Notifications.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.userprofile:
                        return true;
                }
                return false;
            }
        });


        Name=findViewById(R.id.name_profile);
        Email=findViewById(R.id.email_profile);
        Phone=findViewById(R.id.phone_profile);
        ParentPhone=findViewById(R.id.prent_profile);
        Address=findViewById(R.id.address_profile);
        MainName=findViewById(R.id.mainname);
        image=findViewById(R.id.profile_image);


    }

    public void Password(View view)
    {
        startActivity(new Intent(UserProfile.this,PasswordChange.class));
        finishAffinity();;
    }

    public void LogOut(View view)
    {
        Map<String,Object> tokenRemove = new HashMap<>();
        tokenRemove.put("token_id"," ");

 /*       reference.child(mAuth.getCurrentUser().getUid()).updateChildren(tokenRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mAuth.signOut();
                startActivity(new Intent(UserProfile.this,MainActivity.class));
                finishAffinity();
            }
        }); */

        mAuth.signOut();
        startActivity(new Intent(UserProfile.this,MainActivity.class));
        finishAffinity();

    }

    @Override
    protected void onStart() {
        super.onStart();
        loading.setVisibility(View.VISIBLE);

        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Member member = dataSnapshot.getValue(Member.class);
                    Name.setText(member.getName());
                    Email.setText(member.getEmail());
                    Phone.setText(member.getPhone());
                    ParentPhone.setText(member.getParentPhone());
                    Address.setText(member.getPermanentAddr());
                    MainName.setText(member.getName());
                    Glide.with(getApplicationContext()).load(member.getImage_uri()).into(image);
                }
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
