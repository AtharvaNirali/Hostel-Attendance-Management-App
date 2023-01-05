package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.maksim88.passwordedittext.PasswordEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


public class SignupActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    CircleImageView profileimage;
    FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    EditText name,email,phone,parentPhone,localPhone,permanentAddr,localAddr;
    PasswordEditText password,confirm;
    DatabaseReference reference;
    Member member;
    AwesomeValidation awesomeValidation;
    Uri imageUri;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_signup);

        spinner = (Spinner) findViewById(R.id.spinnerHostelType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Hostel_Array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("images");
        imageUri = null;

        FloatingActionButton floatingActionButton = findViewById(R.id.signup);

         name = findViewById(R.id.name);
         email = findViewById(R.id.email);
         password = findViewById(R.id.password);
         phone = findViewById(R.id.phone);
         parentPhone = findViewById(R.id.parentPhone);
         localPhone = findViewById(R.id.localPhone);
         permanentAddr = findViewById(R.id.permanentAddr);
         localAddr = findViewById(R.id.localAddr);
         profileimage = findViewById(R.id.profile_image);

         profileimage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent();
                 intent.setType("image/*");
                 intent.setAction(Intent.ACTION_GET_CONTENT);
                 startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
             }
         });

        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
       // awesomeValidation.setColor(Color.YELLOW);
        awesomeValidation.setContext(this);  // mandatory for UNDERLABEL style


        //adding validation to edittexts
        awesomeValidation.addValidation(this, R.id.name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.phone, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.parentPhone, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.localPhone, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
        String regexPassword = ".{8,}";
        awesomeValidation.addValidation(this, R.id.password, regexPassword, R.string.invalid_password);
   //     String confirmPass = password.getText().toString();
   //     awesomeValidation.addValidation(this, R.id.passwordConfirm, confirmPass, R.string.invalid_confirm_password);
        awesomeValidation.addValidation(this,R.id.permanentAddr, RegexTemplate.NOT_EMPTY,R.string.addr);
        awesomeValidation.addValidation(this,R.id.localAddr,RegexTemplate.NOT_EMPTY,R.string.addr);

        

        // Email Verification
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (awesomeValidation.validate()) {
                    if (imageUri != null)
                    {
                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                                        String token_id = task.getResult().getToken();
                                                                        Toast.makeText(SignupActivity.this, "Registered Successfully,Please Check Your Email for Verification ", Toast.LENGTH_LONG).show();
                                                                        saveInfo(token_id, imageUri);
                                                                        Toast.makeText(getApplicationContext(), "Data Successfully Saved!", Toast.LENGTH_LONG).show();
                                                                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                                                        finishAffinity();
                                                                    }
                                                                });

                                                    } else {
                                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                                    }
                                                }
                                            });

                                        } else {
                                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please Select Image !",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }



    private void saveInfo(String token,Uri imageuri)
    {

        StorageReference userprofile = mStorageRef.child(mAuth.getCurrentUser().getUid()+".jpg");
        userprofile.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    mStorageRef.child(mAuth.getCurrentUser().getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            Log.d("save","ala saveInfo madhe");
                            member = new Member(spinner.getSelectedItem().toString(),name.getText().toString(),email.getText().toString(),phone.getText().toString(),
                                    parentPhone.getText().toString(),permanentAddr.getText().toString(),
                                    localPhone.getText().toString(),localAddr.getText().toString(),token,download_uri,true);
                            reference.child(mAuth.getCurrentUser().getUid()).setValue(member);
                        }
                    });

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"ERROR: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void OpenSignInPage(View view) {
        startActivity(new Intent(SignupActivity.this,MainActivity.class));
        finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE)
        {
            imageUri = data.getData();
            profileimage.setImageURI(imageUri);
        }
    }
}
