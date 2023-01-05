package com.example.myapplication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.telecom.Call;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.firestore.FirestoreArray;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.google.firebase.firestore.FieldValue.arrayUnion;

public class MyAlarm extends BroadcastReceiver {

    DatabaseReference reference;
    String hostelid = "hostel 1"; // Have to fetch it from sign up db
    FirebaseFirestore db;
    Date startD, endD, currD;
    String CurrentDate;


    @Override
    public void onReceive(Context context, Intent intent) {

        reference = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot doc:dataSnapshot.getChildren())
                {
                    //Toast.makeText(getApplicationContext(),doc.getKey(),Toast.LENGTH_LONG).show();
                    Student_Class student_class=doc.getValue(Student_Class.class);

                    if(student_class.isStatus())
                    {
                        db.collection("MITHostel").document(hostelid).collection(doc.getKey())
                                .addSnapshotListener( new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                        if(!queryDocumentSnapshots.isEmpty())
                                        {
                                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

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
                                                CurrentDate = sdf.format(currDate);                  // CURRENT DATE IMPORTANT

                                                // sagle Dates ahet
                                                try {
                                                    startD = sdf.parse(StartDate);
                                                    endD = sdf.parse(endDate);
                                                    currD = sdf.parse(CurrentDate);


                                                } catch (ParseException ex)
                                                {
                                                    ex.printStackTrace();
                                                }

                                                if(status == 0 && currD.compareTo(startD) >= 0 && currD.compareTo(endD) <=0)
                                                {
                                                    Log.d("Dates",startD.toString()+endD.toString()+currD.toString());
                                                    student_class.setPermit(1);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                });
                    }


                    Date currDate = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + currDate);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String Month = String.valueOf(Calendar.MONTH);
                    String Week = String.valueOf(Calendar.WEEK_OF_MONTH);
                    String formattedDate = df.format(currDate);

                    db.collection("MITHostel").document(hostelid).collection("Reports").document("MonthData").collection(Month).document("WeekData").collection(Week).document("DateData").collection(formattedDate).document(doc.getKey())
                            .set(student_class)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Success","Yes");
                                    db.collection("MITHostel").document(hostelid).collection("Reports").document("MonthData").update("monthid",FieldValue.arrayUnion(Month));
                                    db.collection("MITHostel").document(hostelid).collection("Reports").document("MonthData").collection(Month).document("WeekData").update("weekid",FieldValue.arrayUnion(Week));
                                    db.collection("MITHostel").document(hostelid).collection("Reports").document("MonthData").collection(Month).document("WeekData").collection(Week).document("DateData").update("dateid",FieldValue.arrayUnion(formattedDate));
                                    MediaPlayer mediaPlayer=MediaPlayer.create(context,Settings.System.DEFAULT_RINGTONE_URI);
                                    mediaPlayer.start();
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}