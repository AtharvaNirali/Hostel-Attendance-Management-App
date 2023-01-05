package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReportList extends AppCompatActivity {

    String date;
    ListView listView;
    String hostelid="hostel 1";
    FirebaseFirestore db ;

    ArrayList<String> getName=new ArrayList<>();
    ArrayList<String> getphone=new ArrayList<>();
    ArrayList<String> getstatus=new ArrayList<>();
    ArrayList<String> getpermit=new ArrayList<>();
    public int in=0,out=0,permi=0,tot=0;
    TextView dateView,total,inside,outside,permission;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

         progressBar=findViewById(R.id.progressBarReportsList);
         dateView=findViewById(R.id.date);
         total=findViewById(R.id.total);
         inside=findViewById(R.id.inside);
         outside=findViewById(R.id.outside);
         permission=findViewById(R.id.permission);

        Bundle bundle=getIntent().getExtras();
        if (bundle != null) {
            getName=bundle.getStringArrayList("name");
            getphone=bundle.getStringArrayList("phone");
            getstatus=bundle.getStringArrayList("status");
            getpermit=bundle.getStringArrayList("permit");
            date=bundle.getString("date");
        }

        dateView.setText("Date: "+date);

        for (int i=0;i<getstatus.size();i++) {
            if(getstatus.get(i).equals("false"))
                in++;
            else if(getstatus.get(i).equals("true") && getpermit.get(i).equals("1"))
                permi++;
            else
                out++;
            tot++;
        }

        total.setText(String.valueOf(tot));
        inside.setText(String.valueOf(in));
        outside.setText(String.valueOf(out));
        permission.setText(String.valueOf(permi));

   /*     db = FirebaseFirestore.getInstance();

        db.collection("MITHostel").document(hostelid).collection("Reports").document("Data").collection(date).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
            }
        }); */

        setUpView();

    }

    void setUpView()
    {
        progressBar.setVisibility(View.VISIBLE);
        listView = findViewById(R.id.ListViewReport);
        CustomAdp adptr=new CustomAdp();
        listView.setAdapter(adptr);
        progressBar.setVisibility(View.INVISIBLE);

    }

     class CustomAdp extends BaseAdapter
    {

        @Override
        public int getCount() {
            return getName.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if(view ==null)
            {
                view =getLayoutInflater().inflate(R.layout.orderlistview,viewGroup,false);
            }

            TextView name=view.findViewById(R.id.name);
            TextView number=view.findViewById(R.id.number);
            TextView status=view.findViewById(R.id.status);
            TextView permit=view.findViewById(R.id.permit);
            TextView permitext=view.findViewById(R.id.permitext);

            Log.d("nameList",getName.get(i));
            name.setText(getName.get(i));
            number.setText(getphone.get(i));

            if(getstatus.get(i).equals("true")) {
                permit.setVisibility(View.VISIBLE);
                permitext.setVisibility(View.VISIBLE);
                status.setText("OUT");
                status.setTextColor(getResources().getColor(R.color.red));
            }
            else {
                status.setText("IN ");
                status.setTextColor(getResources().getColor(R.color.green));
                permit.setVisibility(View.INVISIBLE);
                permitext.setVisibility(View.INVISIBLE);
            }

            if(getpermit.get(i).equals("0")) {
                permit.setText("NO ");
                permit.setTextColor(getResources().getColor(R.color.red));
            }
            else {
                permit.setText("YES");
                permit.setTextColor(getResources().getColor(R.color.green));
            }


            return view;
        }

    }

}


