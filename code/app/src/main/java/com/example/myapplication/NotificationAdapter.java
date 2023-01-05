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

import static com.example.myapplication.R.color.green;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>
{

    private List<Notification_Class> List;
    private Context mCtx;
    private OnNoteListerner mOnNoteListener;

    public NotificationAdapter(Context mCtx, List<Notification_Class> List,OnNoteListerner onNoteListerner) {
        this.mCtx = mCtx;
        this.List = List;
        this.mOnNoteListener = onNoteListerner;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.notification_layout, null);
        return new NotificationViewHolder(view,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification_Class notify = List.get(position);

        if(getItemCount()>0) {
            //binding the data with the viewholder views
            if(notify.getStatus() == 0)
            {
                holder.indicator.setBackgroundResource(R.drawable.button_round);
            }
            else if(notify.getStatus() ==1)
            {
                holder.indicator.setBackgroundResource(R.drawable.button_round_red);
            }
            else {
                holder.indicator.setBackgroundResource(R.drawable.button_round_orange);
            }
            holder.subject.setText("Permission Status :" + notify.getSubject());
            holder.date.setText(notify.getDate());
            holder.time.setText(notify.getTime());
        }
        else{
            holder.Error.setText("No Notifications Yet");
        }
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView subject, date, time,Error;
        Button indicator;
        OnNoteListerner onNoteListerner;

        public NotificationViewHolder(View itemView,OnNoteListerner onNoteListerner) {
            super(itemView);

            subject = itemView.findViewById(R.id.subject);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            Error = itemView.findViewById(R.id.noNotification);
            indicator = itemView.findViewById(R.id.indicator);
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
