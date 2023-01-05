package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student_Class> List1;
    private Context mCtx;

    public StudentAdapter(Context mCtx, List<Student_Class> List1)
    {
        this.mCtx = mCtx;
        this.List1 = List1;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.studentlistlayout, null);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position)
    {
        Student_Class student_class = List1.get(position);
        holder.name.setText(student_class.getName());
        holder.phone.setText(student_class.getPhone());
        if(student_class.isStatus() == true && student_class.getPermit()==1)
        {
            Log.e("Orange","alaAdapter");
            holder.indicator.setBackgroundResource(R.drawable.button_round_orange);
        }
        else if(student_class.isStatus() == true)
        {
            holder.indicator.setBackgroundResource(R.drawable.button_round_red);
        }
        else {
            holder.indicator.setBackgroundResource(R.drawable.button_round);
        }

    }

    @Override
    public int getItemCount() {
        return List1.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,phone,total,inside,outside;
        Button indicator;

        public StudentViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            indicator=itemView.findViewById(R.id.indicator);

        }
    }
}
