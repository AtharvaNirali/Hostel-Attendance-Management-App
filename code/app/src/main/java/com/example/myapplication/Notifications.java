package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Notifications extends AppCompatActivity implements NotificationAdapter.OnNoteListerner {

    List<Notification_Class> productList;
    RecyclerView recyclerView;

    String hostelid="hostel 1"; // Have to fetch it from sign up db
    FirebaseFirestore db ;
    NotificationAdapter adapter;
    FirebaseAuth auth;
    TextView textView;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        progressBar =findViewById(R.id.progressBarUser);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
         textView=findViewById(R.id.noNotification);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewUser);
        productList = new ArrayList<>();

        adapter = new NotificationAdapter(getApplicationContext(),productList,Notifications.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.notification);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.userprofile:
                        startActivity(new Intent(getApplicationContext(),UserProfile.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notification:
                        return true;
                }
                return false;
            }
        });

 /*        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
       productList = new ArrayList<>();

        productList.add(
                new Notification_Class(
                        "Medical",
                        "Due to Health issues i wont be able to stay at hostel from date and no of days",
                        "Apr 18",
                        "5:59 PM",
                        0
                ));

        productList.add(
                new Notification_Class(
                        "Office/Intership",
                        "Due to work issue i wont be able to stay at hostel from date and no of days",
                        "Apr 18",
                        "5:59 PM",
                        1
                ));

        productList.add(
                new Notification_Class(
                        "Competition",
                        "Due to Smart India Hackathon issue i wont be able to stay at hostel from date and no of days",
                        "Apr 18",
                        "5:59 PM",
                        0
                ));

        productList.add(
                new Notification_Class(
                        "Project",
                        "Due to Final Year Project issue i wont be able to stay at hostel from date and no of days",
                        "Apr 18",
                        "5:59 PM",
                        1
                )); */


        //creating recyclerview adapter
       // NotificationAdapter adapter = new NotificationAdapter(this, productList,this);

        //setting adapter to recyclerview
       // recyclerView.setAdapter(adapter);


    }

    @Override
    public void onNoteClick(int position) {
     //   Toast.makeText(getApplicationContext(),productList.get(position).getSubject(),Toast.LENGTH_LONG).show();
        startActivity(new Intent(Notifications.this,UserNotificationDetailed.class)
                    .putExtra("Name",productList.get(position).getName())
                    .putExtra("Subject",productList.get(position).getSubject())
                    .putExtra("Message",productList.get(position).getMessage())
                    .putExtra("Date",productList.get(position).getDate())
                    .putExtra("Days",productList.get(position).getDays())
                    .putExtra("Status",String.valueOf(productList.get(position).getStatus())));


    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);

        productList.clear();

        Date currDate = Calendar.getInstance().getTime();
        System.out.println("Current time => " + currDate);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(currDate);

        db.collection("MITHostel").document(hostelid).collection(auth.getCurrentUser().getUid())
                .addSnapshotListener(Notifications.this,new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                                if (doc.getDocument().getLong("Status").intValue() == 0 || doc.getDocument().getLong("Status").intValue() == 1 ||doc.getDocument().getLong("Status").intValue() ==2 ) {

                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        String name = doc.getDocument().getString("Name");
                                        String reason = doc.getDocument().getString("Reason");
                                        String detail = doc.getDocument().getString("Detailed reason");
                                        String date = doc.getDocument().getString("Date");
                                        String days = doc.getDocument().getString("No of Days");
                                        int status = doc.getDocument().getLong("Status").intValue();
                                        String DateUpdated = doc.getDocument().getString("DateUpdated");
                                        String TimeUpdated = doc.getDocument().getString("TimeUpdated");

                                        Notification_Class list = new Notification_Class(name, reason, detail, date, days, status, DateUpdated, TimeUpdated);

                                        productList.add(list);
                                        adapter.notifyDataSetChanged();

                                    }
                                    if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                else
                                {
                                    recyclerView.setVisibility(View.INVISIBLE);
                                    textView.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            recyclerView.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }
}
