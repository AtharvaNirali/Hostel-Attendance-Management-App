package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdminAdapter extends RecyclerView.Adapter<NotificationAdminAdapter.NotificationViewHolder> {

    private List<Notification_Class> List;
    private Context mCtx;
    private OnNoteListerner mOnNoteListener;

    public NotificationAdminAdapter(Context mCtx, List<Notification_Class> List,OnNoteListerner onNoteListerner) {
        this.mCtx = mCtx;
        this.List = List;
        this.mOnNoteListener = onNoteListerner;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.notification_admin_layout, null);
        return new NotificationAdminAdapter.NotificationViewHolder(view,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification_Class notify = List.get(position);

        if(notify.getStatus() == 0)
            holder.listener.setBackgroundResource(R.drawable.button_round);
        else if(notify.getStatus() == 1)
            holder.listener.setBackgroundResource(R.drawable.button_round_red);
        else
            holder.listener.setBackgroundResource(R.drawable.button_round_orange);
        //binding the data with the viewholder views
        holder.name.setText(notify.getName()) ;
        holder.subject.setText(notify.getSubject());

    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView name,subject;
        OnNoteListerner onNoteListerner;
        Button listener;

        public NotificationViewHolder(View itemView, OnNoteListerner onNoteListerner) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            subject = itemView.findViewById(R.id.subject);
            listener = itemView.findViewById(R.id.indicatorAdmin);
            this.onNoteListerner=onNoteListerner;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListerner.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListerner{
        void onNoteClick(int position);
    }
}
