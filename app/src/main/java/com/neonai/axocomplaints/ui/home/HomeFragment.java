package com.neonai.axocomplaints.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.neonai.axocomplaints.Active_Ticket;
import com.neonai.axocomplaints.Closed_Tickets;
import com.neonai.axocomplaints.CreateTickets;
import com.neonai.axocomplaints.FeedbackPage;
import com.neonai.axocomplaints.Notification;
import com.neonai.axocomplaints.ProfilePage_1;
import com.neonai.axocomplaints.R;
import com.neonai.axocomplaints.Total_Tickets;
import com.neonai.axocomplaints.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        BottomNavigationView bottom_navigation = binding.bottomNavigation;

        ImageView createcomp = binding.imageView1;
        ImageView totalcomp = binding.imageView2;
        ImageView closedcomp = binding.imageView3;
        ImageView activecomp = binding.imageView4;
        ImageView feedback = binding.imageView5;
        ImageView contactus = binding.imageView6;

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home_1:

                        Fragment fragments = new HomeFragment();
                        FragmentTransaction fragmentTransactions = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransactions.replace(R.id.container,fragments).commit();
                        break;

                    case R.id.profile_1:

                        Intent ii = new Intent(getActivity(), ProfilePage_1.class);
                        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(ii);
                        break;

                    case R.id.ticket_1:

                        Intent iii = new Intent(getActivity(), Total_Tickets.class);
                        iii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(iii);
                        break;
                }

                return true;
            }
        });

        createcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateTickets.class);
                startActivity(intent);
            }
        });

        totalcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Total_Tickets.class);
                startActivity(intent);

            }
        });

        closedcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Closed_Tickets.class);
                startActivity(intent);

            }
        });

        activecomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Active_Ticket.class);
                startActivity(intent);

            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FeedbackPage.class);
                startActivity(intent);

            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Notification.class);
                startActivity(intent);

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}