package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminNotificationDetailed extends AppCompatActivity {

    TextView Name,Reason,Date,NoOfDays,DetailedReason,message;
    Button accept,decline;

    int status ;
    String name,dateID,UserTokenID;
    String hostelid="hostel 1"; // Have to fetch it from sign up db
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AlertDialog.Builder builder;
    ProgressBar progressBar;
    String userID;

    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification_detailed);

        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Name = findViewById(R.id.name);
        Reason = findViewById(R.id.reasonSubjetc);
        Date = findViewById(R.id.date);
        NoOfDays = findViewById(R.id.noOfDays);
        DetailedReason = findViewById(R.id.detailreason);
        builder = new AlertDialog.Builder(this);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        accept = findViewById(R.id.accept);
        decline = findViewById(R.id.decline);
        message = findViewById(R.id.message);

        Intent i=getIntent();
        name = i.getStringExtra("Name");
        dateID = i.getStringExtra("DateId");
        Toast.makeText(getApplicationContext(),dateID,Toast.LENGTH_SHORT).show();
        status = i.getIntExtra("Status",0);
        userID = i.getStringExtra("UserID");
        Toast.makeText(getApplicationContext(),userID,Toast.LENGTH_SHORT).show();
        Log.e("UserID",dateID);

        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Member member=dataSnapshot.getValue(Member.class);
                UserTokenID = member.getToken_id();
                Toast.makeText(getApplicationContext(),UserTokenID,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       // Log.e("UserToken",UserTokenID);

        Name.setText("Message From "+i.getStringExtra("Name"));
        Reason.setText(i.getStringExtra("Reason"));
        Date.setText(i.getStringExtra("Date"));
        NoOfDays.setText(i.getStringExtra("Days"));
        DetailedReason.setText(i.getStringExtra("Detailed"));


        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminNotificationDetailed.this,NotificationAdmin.class));
            }
        });



       final Map<String, Object> dataUpdate = new HashMap<>();


        if(status == 0) {
            accept.setVisibility(View.INVISIBLE);
            decline.setVisibility(View.INVISIBLE);
            message.setText("Grant Done: Accepted");
            message.setBackgroundColor(getResources().getColor(R.color.green));
            message.setVisibility(View.VISIBLE);
        }


        if(status == 1) {
            accept.setVisibility(View.INVISIBLE);
            decline.setVisibility(View.INVISIBLE);
            message.setText("Grant Done: Rejected");
            message.setBackgroundColor(getResources().getColor(R.color.red));
            message.setVisibility(View.VISIBLE);
        }

        if(status == 2)  {

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                    //Setting message manually and performing action on button click
                    builder.setMessage("Are you sure to Accept Request ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, final int id) {

                                    progressBar.setVisibility(View.VISIBLE);
                                    Calendar calender = Calendar.getInstance();
                                    calender.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
                                    String Currtime =calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                    String formattedDate = df.format(calender.getTime());

                                    dataUpdate.put("Status",0);
                                    dataUpdate.put("TimeUpdated",Currtime);
                                    dataUpdate.put("DateUpdated",formattedDate);


                                    db.collection("MITHostel").document(hostelid).collection(userID).document(dateID)
                                            .update(dataUpdate)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                //    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                                    accept.setVisibility(View.INVISIBLE);
                                                    decline.setVisibility(View.INVISIBLE);
                                                    message.setText("Grant Done: Accepted");
                                                    message.setBackgroundColor(getResources().getColor(R.color.green));
                                                    message.setVisibility(View.VISIBLE);

                                                    Retrofit retrofit = new Retrofit.Builder()
                                                            .baseUrl("https://us-central1-hostelapp-2c146.cloudfunctions.net/api/api/")
                                                            .addConverterFactory(GsonConverterFactory.create())
                                                            .build();
                                                    Log.d("Retrofit: ","BUILD ZALA");

                                                    Api api = retrofit.create(Api.class);
                                                    Call<ResponseBody> call = api.sendNotification(UserTokenID,"Message from Rector","Your Permission Status has been updated!");
                                                    call.enqueue(new Callback<ResponseBody>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            try {
                                                                Toast.makeText(AdminNotificationDetailed.this,response.body().string(),Toast.LENGTH_LONG).show();

                                                                Map<String,Object> notificationStatus=new HashMap<>();
                                                                notificationStatus.put("Notification",true);
                                                                db.collection("MITHostel").document(hostelid).collection(userID).document(dateID)
                                                                        .update(notificationStatus)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void avoid) {
                                                                                Toast.makeText(AdminNotificationDetailed.this,"Notification Status Updated",Toast.LENGTH_SHORT).show();
                                                                                 progressBar.setVisibility(View.INVISIBLE);
                                                                            }
                                                                        });

                                                             /*   Map<String,Object> notificationMessage=new HashMap<>();
                                                                notificationMessage.put("Title","Test Notification APP");
                                                                notificationMessage.put("Body","Your Permission Request Testing");
                                                                db.collection("MITHostel/"+hostelid+userID+idNo+"/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        Toast.makeText(AdminNotificationDetailed.this,"Notification Sent",Toast.LENGTH_SHORT).show();
                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                    }
                                                                }); */





                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                        }
                                                    });

                                                }
                                            });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Accept Request !");
                    alert.show();

                }
            });


            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                    //Setting message manually and performing action on button click
                    builder.setMessage("Are you sure to Decline Request ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, final int id) {

                                    progressBar.setVisibility(View.VISIBLE);

                                    Calendar calender = Calendar.getInstance();
                                    calender.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
                                    String Currtime =calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                    String formattedDate = df.format(calender.getTime());

                                    dataUpdate.put("Status",1);
                                    dataUpdate.put("TimeUpdated",Currtime);
                                    dataUpdate.put("DateUpdated",formattedDate);

                                    db.collection("MITHostel").document(hostelid).collection(userID).document(dateID)
                                            .update(dataUpdate)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                                    decline.setVisibility(View.INVISIBLE);
                                                    accept.setVisibility(View.INVISIBLE);
                                                    message.setText("Grant Done: Rejected");
                                                    message.setBackgroundColor(getResources().getColor(R.color.red));
                                                    message.setVisibility(View.VISIBLE);

                                                    Retrofit retrofit = new Retrofit.Builder()
                                                            .baseUrl("https://us-central1-hostelapp-2c146.cloudfunctions.net/api/api/")
                                                            .addConverterFactory(GsonConverterFactory.create())
                                                            .build();
                                                    Log.d("Retrofit: ","BUILD ZALA");

                                                    Api api = retrofit.create(Api.class);
                                                    Call<ResponseBody> call = api.sendNotification(UserTokenID,"Message from Rector","Your Permission Status has been updated!");
                                                    call.enqueue(new Callback<ResponseBody>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            try {
                                                                Toast.makeText(AdminNotificationDetailed.this,response.body().string(),Toast.LENGTH_LONG).show();

                                                                Map<String,Object> notificationStatus=new HashMap<>();
                                                                notificationStatus.put("Notification",true);
                                                                db.collection("MITHostel").document(hostelid).collection(userID).document(dateID)
                                                                        .update(notificationStatus)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void avoid) {
                                                                                Toast.makeText(AdminNotificationDetailed.this,"Notification Status Updated",Toast.LENGTH_SHORT).show();
                                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                            }
                                                                        });

                                                           /*     Map<String,Object> notificationMessage=new HashMap<>();
                                                                notificationMessage.put("Title","Test Notification APP");
                                                                notificationMessage.put("Body","Your Permission Request Testing");

                                                                db.collection("MITHostel/"+hostelid+userID+idNo+"/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        Toast.makeText(AdminNotificationDetailed.this,"Notification Sent",Toast.LENGTH_SHORT).show();
                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                    }
                                                                }); */


                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                        }
                                                    });
                                                }
                                            });

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Decline Request!");
                    alert.show();
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AdminNotificationDetailed.this,NotificationAdmin.class));
    }
}
