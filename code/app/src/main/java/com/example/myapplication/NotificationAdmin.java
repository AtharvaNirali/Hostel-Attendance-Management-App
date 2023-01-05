package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.sql.Struct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationAdmin extends AppCompatActivity implements NotificationAdminAdapter.OnNoteListerner{

    List<Notification_Class> productList;
    RecyclerView recyclerView;

    String hostelid="hostel 1"; // Have to fetch it from sign up db
    FirebaseFirestore db ;
    NotificationAdminAdapter adapter;
    ArrayList<String> list1;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_admin);

        db = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewAdmin);
        productList = new ArrayList<>();
        list1=new ArrayList<>();
        progressBar =findViewById(R.id.progressBarAdmin);

        adapter = new NotificationAdminAdapter(getApplicationContext(),productList,NotificationAdmin.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


       // progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.notification);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),StudentList.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.notification:
                        return true;

                    case R.id.reports:
                        startActivity(new Intent(getApplicationContext(),Reports.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        //initializing the productlist


       // Date currDate = Calendar.getInstance().getTime();
       // System.out.println("Current time => " + currDate);

        //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        //String formattedDate = df.format(currDate);

    /*    db.collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d("TAG", "New Msg: " + dc.getDocument().toObject(Message.class));
                                    break;
                                case MODIFIED:
                                    Log.d("TAG", "Modified Msg: " + dc.getDocument().toObject(Message.class));
                                    break;
                                case REMOVED:
                                    Log.d("TAG", "Removed Msg: " + dc.getDocument().toObject(Message.class));
                                    break;
                            }
                        }

                    }
                }); */



       // Log.e("sizeBaher",String.valueOf(productList.size()));


        //  Log.e("dataRefer",db.collection("MITHostel").document(hostelid).collection(formattedDate).document("atharva.nirali@gmail.com").getPath());



     /*   productList.add(
                new Notification_Class(
                        "Atharva Nirali",
                        "Office/Intership",
                        "Due to work issue i wont be able to stay at hostel from date and no of days",
                        "Apr 20",
                        "1"
                ));

        productList.add(
                new Notification_Class(
                        "Atharva Nirali",
                        "Project",
                        "Due to Final Year Project issue i wont be able to stay at hostel from date and no of days",
                        "Apr 21",
                        "1"
                ));

        productList.add(
                new Notification_Class(
                        "Atharva Nirali",
                        "Competition",
                        "Due to Smart India Hackathon issue i wont be able to stay at hostel from date and no of days",
                        "Apr 22",
                        "4"
                )); */

        //Log.e("size",String.valueOf(productList.size()));


        //creating recyclerview adapter
       //  adapter = new NotificationAdminAdapter(this, productList,this);
        //setting adapter to recyclerview
    }

    @Override
    public void onNoteClick(int position) {
        //   Toast.makeText(getApplicationContext(),productList.get(position).getDays(),Toast.LENGTH_LONG).show();
        startActivity(new Intent(NotificationAdmin.this,AdminNotificationDetailed.class)
                .putExtra("Name",productList.get(position).getName())
                .putExtra("Reason",productList.get(position).getSubject())
                .putExtra("Date",productList.get(position).getDate())
                .putExtra("Days",productList.get(position).getDays())
                .putExtra("Detailed",productList.get(position).getMessage())
                .putExtra("Status",productList.get(position).getStatus())
                .putExtra("DateId",productList.get(position).getDateid())
                .putExtra("UserID",productList.get(position).getUserId())
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);

        productList.clear();
      //  list1.clear();
        Date currDate = Calendar.getInstance().getTime();
        System.out.println("Current time => " + currDate);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(currDate);



        db.collection("MITHostel").document(hostelid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("fetch", "something");
                    if (document.exists()) {
                        list1 = (ArrayList<String>) document.get("userid");
                        for(String uid:list1) {
                            db.collection("MITHostel").document(hostelid).collection(uid)
                                    .addSnapshotListener(NotificationAdmin.this, new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                                    String id = doc.getDocument().getId();
                                                    String name = doc.getDocument().getString("Name");
                                                    String reason = doc.getDocument().getString("Reason");
                                                    String detail = doc.getDocument().getString("Detailed reason");
                                                    String date = doc.getDocument().getString("Date");
                                                    String days = doc.getDocument().getString("No of Days");
                                                    int status = doc.getDocument().getLong("Status").intValue();

                                                    Notification_Class list = new Notification_Class(name, reason, detail, date, days, uid, status,id);
                                                    Log.e("ID ALA", id + " " + status);

                                                    productList.add(list);
                                                    adapter.notifyDataSetChanged();
                                                }

                                                if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
        //Toast.makeText(getApplicationContext(),list1.toString(),Toast.LENGTH_SHORT).show();
    }


}
