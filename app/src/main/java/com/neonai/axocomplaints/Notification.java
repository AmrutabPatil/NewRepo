package com.neonai.axocomplaints;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_notifications);


        ImageView lefticon = findViewById(R.id.left_arrow_icon);
        TextView title1 = findViewById(R.id.toolbarTitle);


        lefticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Notification.this, "Back", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Notification.this, DashBoardMain.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });

        title1.setText("सूचना");
    }
}