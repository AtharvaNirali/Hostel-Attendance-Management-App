package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Reports extends AppCompatActivity {
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};
    String hostelid="hostel 1";
    DatabaseReference reference;
    FirebaseFirestore db ;
    ArrayList<String> mobarray;
    ArrayAdapter adapter;
    ArrayList<String> list1;

    ArrayList<String> getName=new ArrayList<>();
    ArrayList<String> getphone=new ArrayList<>();
    ArrayList<String> getstatus=new ArrayList<>();
    ArrayList<String> getpermit=new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        mobarray=new ArrayList<>();
        list1=new ArrayList<>();


        reference = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        progressBar =findViewById(R.id.progressBarReports);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.reports);
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
                        startActivity(new Intent(getApplicationContext(), NotificationAdmin.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.reports:
                        return true;
                }
                return false;
            }
        });

         adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobarray);
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                String str=(String)o;
                Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();

               db.collection("MITHostel").document(hostelid).collection("Reports").document("Data").collection(str).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for(DocumentChange doc:queryDocumentSnapshots.getDocumentChanges())
                        {
                            getName.add((String) doc.getDocument().get("name"));
                            getphone.add((String) doc.getDocument().get("phone"));
                            int x = doc.getDocument().getLong("permit").intValue();
                            getpermit.add(String.valueOf(x));
                            getstatus.add(doc.getDocument().getBoolean("status").toString());
                        }

                        //Log.d("name",getName.toString());

                        startActivity(new Intent(Reports.this, ReportList.class)
                                .putExtra("date",str)
                                .putExtra("name",getName)
                                .putExtra("phone",getphone)
                                .putExtra("permit",getpermit)
                                .putExtra("status",getstatus)
                        );

                    }
                });




            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressBar.setVisibility(View.VISIBLE);

        mobarray.clear();
        getName.clear();
        getphone.clear();
        getpermit.clear();
        getstatus.clear();

        db.collection("MITHostel").document(hostelid).collection("Reports").document("Data").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists())
                    {
                        Log.d("array",documentSnapshot.get("dateid").toString());
                        list1 = (ArrayList<String>) documentSnapshot.get("dateid");
                        for (String dateid:list1)
                        {
                            mobarray.add(dateid);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

        });

    }
}
