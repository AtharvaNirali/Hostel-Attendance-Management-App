package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserNotificationDetailed extends AppCompatActivity {

    TextView subjectView,messageView,attachmentsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification_detailed);

        Intent i=getIntent();
        String name = i.getStringExtra("Name");
        String date = i.getStringExtra("Date");
        String days = i.getStringExtra("Days");
        String message = i.getStringExtra("Message");
        String subject = i.getStringExtra("Subject");
        String status = i.getStringExtra("Status");

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

        subjectView = findViewById(R.id.subject);
        messageView = findViewById(R.id.message);
        attachmentsView = findViewById(R.id.attachments);

        String messageString = "Hello "+name+",\nRequest Details: "+message+"\nFrom Date: "+date+"\nNo of Days: "+days;

        subjectView.setText("Permission Status: "+subject);
        messageView.setText(messageString);
        if(status.equals("0"))
        {
            attachmentsView.setTextColor(getResources().getColor(R.color.green));
            attachmentsView.setText("Approved");
        }
        else if(status.equals("1"))
        {
            attachmentsView.setTextColor(getResources().getColor(R.color.red));
            attachmentsView.setText("NotApproved");
        }
        else
        {
            attachmentsView.setTextColor(getResources().getColor(R.color.red));
            attachmentsView.setText("Under Approval..");
        }

    }


    public void GoBack(View view) {
        startActivity(new Intent(UserNotificationDetailed.this,Notifications.class));
        finishAffinity();
    }
}
