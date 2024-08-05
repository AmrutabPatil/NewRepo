package com.neonai.axocomplaints;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class Active_Ticket_Adpater extends RecyclerView.Adapter<Active_Ticket_Adpater.ViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> directList;

    public Active_Ticket_Adpater(Context context, ArrayList<HashMap<String, String>> directList) {
        this.context = context;
        this.directList = directList;
    }

    @NonNull
    @Override
    public Active_Ticket_Adpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_ticket_display, parent, false);
        return new Active_Ticket_Adpater.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Active_Ticket_Adpater.ViewHolder holder, int position) {
        HashMap<String, String> hm = directList.get(position);
        holder.txtSubject.setText("तक्रारीचा विषय:- " + hm.get("subject"));
        holder.txtTicketId.setText("तक्रार नं:- " + hm.get("ticket_sr_no"));
        holder.ticketid.setText(""+hm.get("ticket_sr_no"));
        holder.txtAddedDate.setText("दिनांक:- " + hm.get("created_at"));
        holder.txtDescription.setText("तक्रारीचा तपशील:- "+hm.get("ticket_description"));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("तुम्हाला खात्री आहे की तुम्हाला तक्रार हटवायचे आहे")
                        .setCancelable(false)
                        .setPositiveButton("होय", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                ((Active_Ticket)context).callPUTDataMethod(holder.ticketid.getText().toString());
                            }
                        })
                        .setNegativeButton("नाही", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return directList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtSubject, txtTicketId, txtAddedDate,txtDescription,ticketid;
        private ImageView deleteButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubject = itemView.findViewById(R.id.txtSubjectactive);
            txtTicketId = itemView.findViewById(R.id.txtTicketIdactive);
            txtAddedDate = itemView.findViewById(R.id.txtAddedDateactive);
            deleteButton = itemView.findViewById(R.id.ticket_delete);
            txtDescription = itemView.findViewById(R.id.txtDescriptionactive);
            ticketid = itemView.findViewById(R.id.txtStatusactive);

        }
    }

}
