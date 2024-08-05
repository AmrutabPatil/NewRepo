package com.neonai.axocomplaints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class Closed_Tickets_Adpater extends RecyclerView.Adapter<Closed_Tickets_Adpater.ViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> directList;

    public Closed_Tickets_Adpater(Context context, ArrayList<HashMap<String, String>> directList) {
        this.context = context;
        this.directList = directList;
    }


    @NonNull
    @Override
    public Closed_Tickets_Adpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.closed_tickets_display, parent, false);
        return new Closed_Tickets_Adpater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Closed_Tickets_Adpater.ViewHolder holder, int position) {
        HashMap<String, String> hm = directList.get(position);
        holder.txtSubjectclosed.setText("तक्रारीचा विषय:- " + hm.get("subject"));
        holder.txtTicketIdclosed.setText("तक्रार नं:- " + hm.get("ticket_sr_no"));
        holder.txtAddedDateclosed.setText("दिनांक:- " + hm.get("created_at"));
        holder.txtDescriptionclosed.setText("तक्रारीचा तपशील:- "+hm.get("ticket_description"));

    }

    @Override
    public int getItemCount() {
        return directList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSubjectclosed, txtTicketIdclosed, txtAddedDateclosed, txtDescriptionclosed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubjectclosed = itemView.findViewById(R.id.txtSubjectclosed);
            txtTicketIdclosed = itemView.findViewById(R.id.txtTicketIdclosed);
            txtAddedDateclosed = itemView.findViewById(R.id.txtAddedDateclosed);
            txtDescriptionclosed = itemView.findViewById(R.id.txtDescriptionclosed);

        }
    }
}
