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

public class Total_Tickets_Adpater extends RecyclerView.Adapter<Total_Tickets_Adpater.ViewHolder> {

    Context context;
    private TextView txtSubject,txtDescription, txtTicketId, txtAddedDate,txtStatus;
    ArrayList<HashMap<String, String>> directList;

    public Total_Tickets_Adpater(Context context, ArrayList<HashMap<String, String>> directList) {
        this.context = context;
        this.directList = directList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_tickets_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> hm = directList.get(position);
        txtSubject.setText("तक्रारीचा विषय:- " + hm.get("subject"));
        txtTicketId.setText("तक्रार नं:- " + hm.get("ticket_sr_no"));
        txtAddedDate.setText("दिनांक:- " + hm.get("created_at"));
        txtDescription.setText("तक्रारीचा तपशील:- "+hm.get("ticket_description"));
        txtStatus.setText(""+hm.get("ticket_status"));
    }

    @Override
    public int getItemCount() {
        return directList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubject = itemView.findViewById(R.id.txtSubject);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtTicketId = itemView.findViewById(R.id.txtTicketId);
            txtAddedDate = itemView.findViewById(R.id.txtAddedDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);

        }
    }
}