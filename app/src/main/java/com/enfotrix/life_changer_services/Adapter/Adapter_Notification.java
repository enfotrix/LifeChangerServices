package com.enfotrix.life_changer_services.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enfotrix.life_changer_services.Model.Model_Notification;
import com.enfotrix.life_changer_services.R;

import java.util.List;

public class Adapter_Notification extends RecyclerView.Adapter<Adapter_Notification.ViewHOlder> {

    Context context;
    List<Model_Notification> list_announ;

    public Adapter_Notification(Context context, List<Model_Notification> list_announ) {
        this.context = context;
        this.list_announ = list_announ;
    }

    @NonNull
    @Override
    public ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_announce, parent, false);
        ViewHOlder viewHolder = new ViewHOlder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHOlder holder, int position) {
        final Model_Notification model_notification = list_announ.get(position);

        holder.txt_name.setText(model_notification.getHeading());
        holder.txt_notifi.setText(model_notification.getData());
    }

    @Override
    public int getItemCount() {
        return list_announ.size();
    }

    public class ViewHOlder extends RecyclerView.ViewHolder {

        public TextView txt_date, txt_name, txt_notifi;
        public LinearLayout layout_Announ;

        public ViewHOlder(@NonNull View itemView) {
            super(itemView);

            this.txt_name = (TextView) itemView.findViewById(R.id.txt_header_notify);
            this.txt_notifi = (TextView) itemView.findViewById(R.id.txt_notifi);

        }
    }
}
