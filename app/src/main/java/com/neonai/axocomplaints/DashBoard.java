package com.neonai.axocomplaints;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

public class DashBoard extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView totalcomp,activecomp,closedcomp,contactus,feedback,createcomp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        drawerLayout = findViewById(R.id.drawerlayout);
//        navigationView = findViewById(R.id.navigationview);
//        toolbar = findViewById(R.id.toolbar);
//        createcomp = findViewById(R.id.imageView1);
//        totalcomp = findViewById(R.id.imageView2);
//        closedcomp = findViewById(R.id.imageView3);
//        activecomp = findViewById(R.id.imageView4);
//        feedback = findViewById(R.id.imageView5);
//        contactus = findViewById(R.id.imageView6);

//        setSupportActionBar(toolbar);

          ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);


        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//
//                if(id==R.id.create_comp_side){
//                   loadFragment(new Create_Complaint_Fragment());
//
//                }else if(id==R.id.active_comp_side){
//                    Toast.makeText(DashBoard.this, "Hellooo", Toast.LENGTH_SHORT).show();
//                }else if(id==R.id.closed_comp_side){
//
//                }else if(id==R.id.total_comp_side){
//
//                }else if(id==R.id.notification_side){
//
//                }else if(id==R.id.active_comp_side){
//
//                }
//
//
//                drawerLayout.closeDrawer(GravityCompat.START);
//
//                return true;
//            }
//        });


//
//        createcomp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), CreateTickets.class);
//                startActivity(intent);
//
//            }
//        });
//
//        totalcomp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Total_Tickets.class);
//                startActivity(intent);
//
//            }
//        });
//
//        closedcomp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Closed_Tickets.class);
//                startActivity(intent);
//
//            }
//        });
//
//        activecomp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Active_Ticket.class);
//                startActivity(intent);
//
//            }
//        });
//
//        feedback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), FeedbackPage.class);
//                startActivity(intent);
//
//            }
//        });
//
//        contactus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Contact_Us.class);
//                startActivity(intent);
//
//            }
//        });
//    }

//    @Override
//    public void onBackPressed() {
//        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
//            drawerLayout.closeDrawer(GravityCompat.START);
//        }else{
//            super.onBackPressed();
//        }
//    }
//
//    private void loadFragment(Fragment fragment) {
//
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//
//        ft.add(R.id.container,fragment);
//
//        ft.commit();
  }
}