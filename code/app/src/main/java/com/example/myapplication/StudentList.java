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
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StudentList extends AppCompatActivity {

    Button schedule,logout;
    String hostelid="hostel 1"; // Have to fetch it from sign up db
    FirebaseFirestore db ;
    List<Student_Class> studentList;
    DatabaseReference reference;
    StudentAdapter adapter;
    //the recyclerview
    RecyclerView recyclerView;
    TextView total,inside,outside,permit;
     int out_count;
     int in_count;
     int permi_count;
     int total_count;
     Date startD,endD,currD;
     Student_Class student_class;
     boolean ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        schedule=findViewById(R.id.scheduleTime);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentList.this,ScheduleFire.class));
            }
        });

        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(StudentList.this,MainActivity.class));
                finishAffinity();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        //initializing the productlist
        studentList = new ArrayList<>();
        adapter = new StudentAdapter(this, studentList);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        return true;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(),NotificationAdmin.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.reports:
                        startActivity(new Intent(getApplicationContext(),Reports.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        total = findViewById(R.id.total);
        inside = findViewById(R.id.inside);
        outside = findViewById(R.id.outside);
        permit = findViewById(R.id.permission);

/*

        studentList.add(
                new Student_Class(
                        "Atharva Nirali",
                        "8888590591",
                        true
                        ));

        studentList.add(
                        new Student_Class(
                                "Dhanajay Gavade",
                                "7588592543",
                                false
                        ));

        studentList.add(
                        new Student_Class(
                                "Aditya Bora",
                                "9763715642",
                                true
                        ));

        studentList.add(
                        new Student_Class(
                                "Aditya Londhe",
                                "7588592544",
                                false
                        ));

        studentList.add(
                        new Student_Class(
                                "Kiran Patil",
                                "9373262828",
                                true
                        ));
        Log.d("total", String.valueOf(studentList.size()));  */

    }


    @Override
    protected void onStart() {
        super.onStart();

        studentList.clear();
        in_count=0;
        out_count=0;
        total_count=0;
        permi_count=0;


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot doc:dataSnapshot.getChildren())
                {
                    //Toast.makeText(getApplicationContext(),doc.getKey(),Toast.LENGTH_LONG).show();
                     student_class=doc.getValue(Student_Class.class);              //take user from the REALTIME DATABASE

                    if(student_class.isStatus())                                                //CHECK STATUS True : OUTSIDE   False:INSIDE
                    {
                        db.collection("MITHostel").document(hostelid).collection(doc.getKey())
                                .addSnapshotListener(StudentList.this, new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                        if(!queryDocumentSnapshots.isEmpty())
                                        {

                                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                                            {

                                                String StartDate = doc.getDocument().getString("Date");         //START DATE IMPORTANT
                                                String days = doc.getDocument().getString("No of Days");
                                                int status = doc.getDocument().getLong("Status").intValue();

                                                Log.d("DataValue",StartDate+days+status);

                                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                                Calendar c = Calendar.getInstance();
                                                try{
                                                    //Setting the date to the given date
                                                    c.setTime(sdf.parse(StartDate));
                                                }
                                                catch(Exception e1)
                                                {
                                                    e1.printStackTrace();
                                                }

                                                //Number of Days to add
                                                c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(days)-1);
                                                //Date after adding the days to the given date
                                                String endDate = sdf.format(c.getTime());                   //END DATE IMPORTANT

                                                Date currDate = Calendar.getInstance().getTime();
                                                String CurrentDate = sdf.format(currDate);                  // CURRENT DATE IMPORTANT

                                                // sagle Dates ahet
                                                try
                                                {
                                                    startD = sdf.parse(StartDate);
                                                    endD = sdf.parse(endDate);
                                                    currD = sdf.parse(CurrentDate);

                                                } catch (ParseException ex)
                                                {
                                                    ex.printStackTrace();
                                                }

                                                if(status == 0 && currD.compareTo(startD) >= 0 && currD.compareTo(endD) <=0) //Permission asel tar aat jail
                                                {
                                                   // Log.d("Dates",startD.toString()+endD.toString()+currD.toString());
                                                    student_class.setPermit(1);
                                                    //Log.d("returnInside",String.valueOf(ret));
                                                }
                                            }
                                        }
                                    }
                                });
                    }

                    studentList.add(student_class);
                    adapter.notifyDataSetChanged();
                }

                // COUNTER SATHI CHA CODE AHE !!!!!
                for(Student_Class student:studentList)
                {
                    if(student.isStatus() && student.getPermit()==0)
                    {
                        out_count++;
                    }
                    else if(student.isStatus() && student.getPermit()==1)
                    {
                        permi_count++;
                    }
                    else
                    {
                        in_count++;
                    }
                    total_count++;
                }

                permit.setText(String.valueOf(permi_count));
                total.setText(String.valueOf(total_count));
                inside.setText(String.valueOf(in_count));
                outside.setText(String.valueOf(out_count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
