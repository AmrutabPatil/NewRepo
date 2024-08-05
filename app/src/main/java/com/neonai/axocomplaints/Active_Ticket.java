package com.neonai.axocomplaints;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.neonai.axocomplaints.smpleclasses.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Active_Ticket extends AppCompatActivity {

    RecyclerView recyclerView_3;
    ArrayList<HashMap<String, String>> directList;
    Active_Ticket_Adpater active_ticket_adpater;
    public static final String MY_PREFS_NAME = "Login_data";
    String user_id;
    SwipeRefreshLayout pullToRefresh;
    public static volatile Context mMainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_tickets);

        ImageView lefticon = findViewById(R.id.left_arrow_icon);
        TextView title1 = findViewById(R.id.toolbarTitle);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.statusBar1));
        }

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                pullToRefresh.setRefreshing(true);
                directs();

            }
        });

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("id",null);
        

        lefticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Active_Ticket.this, "Back", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Active_Ticket.this, DashBoardMain.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });

        title1.setText("सक्रिय तक्रारी");
        intialiseViewById();

        directs();
    }

    private void intialiseViewById() {
        directList = new ArrayList<>();
        recyclerView_3 = findViewById(R.id.recyclerView_3);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_3.setLayoutManager(verticalLayoutManager);
        recyclerView_3.setNestedScrollingEnabled(false);

    }

    private void directs() {

        directList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, Const.ACTIVE_TICKET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("taskHistoryResponse", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String message = object.getString("message");


                    if (status.equals("true")) {
                        JSONArray jsonArray = object.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HashMap<String,String> hm = new HashMap<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            hm.put("ticket_sr_no",jsonObject.getString("ticket_sr_no"));
                            hm.put("created_at",jsonObject.getString("created_at"));
                            hm.put("subject",jsonObject.getString("subject"));
                            hm.put("ticket_description",jsonObject.getString("ticket_description"));


                            directList.add(hm);

                        }
                        active_ticket_adpater = new Active_Ticket_Adpater(Active_Ticket.this,directList);
                        recyclerView_3.setAdapter(active_ticket_adpater);
                    } else {

                        Toast.makeText(Active_Ticket.this, "" +message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Active_Ticket.this, "PLEASE CHECK YOUR INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_id", user_id);
                Log.d("hashMap", hashMap.toString());
                return hashMap;
            }
        };

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 100000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 100000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Active_Ticket.this);
        requestQueue.add(request);

        pullToRefresh.setRefreshing(false);

    }


    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static Context getContext() {
        return mMainContext;
    }

    public void callPUTDataMethod(String ticketid){

        RequestQueue queue = Volley.newRequestQueue(Active_Ticket.this);

        StringRequest request = new StringRequest(Request.Method.POST, Const.DELETE_TICKET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(Active_Ticket.this, "तक्रार हटवले", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Active_Ticket.this, "तक्रार हटवले", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();

                params.put("ticket_sr_no",ticketid);
                Log.d("ticketId1","1"+ticketid);

                return params;
            }
        };
        queue.add(request);

    }

}
