package com.neonai.axocomplaints;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Closed_Tickets extends AppCompatActivity {

    RecyclerView recyclerView_2;
    ArrayList<HashMap<String, String>> directList;
    private TextView toolbarTittle;
    Closed_Tickets_Adpater closed_complaint_adpater;
    public static final String MY_PREFS_NAME = "Login_data";
    String user_id;
    public static volatile Context mMainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed_tickets);


        ImageView lefticon = findViewById(R.id.left_arrow_icon);
        TextView title1 = findViewById(R.id.toolbarTitle);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.statusBar1));
        }


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("id",null);


        lefticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Closed_Tickets.this, "Back", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Closed_Tickets.this, DashBoardMain.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });

        title1.setText("पूर्ण तक्रारी");


//        setToolbar();
        intialiseViewById();

        directs();
    }





    private void intialiseViewById() {
        directList = new ArrayList<>();
        recyclerView_2 = findViewById(R.id.recyclerView_2);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_2.setLayoutManager(verticalLayoutManager);
        recyclerView_2.setNestedScrollingEnabled(false);

    }


    private void directs() {
        directList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, Const.CLOSED_TICKET, new Response.Listener<String>() {
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
                        closed_complaint_adpater = new Closed_Tickets_Adpater(Closed_Tickets.this,directList);
                        recyclerView_2.setAdapter(closed_complaint_adpater);
                    } else {

                        Toast.makeText(Closed_Tickets.this, "" +message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Closed_Tickets.this, "PLEASE CHECK YOUR INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(Closed_Tickets.this);
        requestQueue.add(request);

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
}

