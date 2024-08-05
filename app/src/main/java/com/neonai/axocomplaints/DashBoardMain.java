package com.neonai.axocomplaints;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.neonai.axocomplaints.databinding.ActivityDashBoardMainBinding;
import com.neonai.axocomplaints.smpleclasses.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardMain extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration,AppBarConfiguration;
    private ActivityDashBoardMainBinding binding;
    TextView username;
    ImageView totalcomp,activecomp,closedcomp,contactus,feedback,createcomp;
    public static final String MY_PREFS_NAME = "Login_data";
    Context context;
    CircleImageView profile_image;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        createcomp = findViewById(R.id.imageView1);
        totalcomp = findViewById(R.id.imageView2);
        closedcomp = findViewById(R.id.imageView3);
        activecomp = findViewById(R.id.imageView4);
        feedback = findViewById(R.id.imageView5);
        contactus = findViewById(R.id.imageView6);

        binding = ActivityDashBoardMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDashBoardMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        callPUTDataMethods();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        String phone_no = prefs.getString("phone_no",null);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_profile,R.id.nav_privacy_policies)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dash_board_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        SharedPreferences pref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String user_name = pref.getString("name",null);
        String user_last = pref.getString("lastname",null);

        NavigationView navigationViews = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationViews.getHeaderView(0);
        TextView navUserphone = (TextView) headerView.findViewById(R.id.user_mobileno);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user_name);
        profile_image  = (CircleImageView) headerView.findViewById(R.id.profile_images);
        navUserphone.setText(phone_no);
        navUsername.setText(user_name+" "+user_last);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (item.getItemId()) {
            case R.id.action_settings:
                builder.setMessage("तुम्हाला खात्री आहे की तुम्हाला ॲप लॉगआउट करायचे आहे.")
                        .setCancelable(false)
                        .setPositiveButton("हो", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                SharedPreferences sp = getSharedPreferences(DashBoardMain.MY_PREFS_NAME,0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();

                                Intent intent = new Intent(DashBoardMain.this,Login_Screen.class);
                                startActivity(intent);
                                finish();

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
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setLoginState(boolean status) {
//        SharedPreferences sp = getSharedPreferences("Has", MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences(DashBoardMain.MY_PREFS_NAME,0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("hasLoggedIn", false);
        ed.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_board_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dash_board_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void callPUTDataMethods() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("id",null);

        RequestQueue queue = Volley.newRequestQueue(DashBoardMain.this);

        StringRequest request = new StringRequest(Request.Method.POST, Const.IMAGE_GET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("response12", "34" + response);
                            if (jsonObject.has("data")) {

//                                JSONObject jsonObject1 = jsonObject.getJSONArray("data").getJSONObject(0);
                                String imgs = jsonObject.getString("data");

                                ImageRequest imageRequest = new ImageRequest(imgs,
                                        new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                profile_image.setImageBitmap(response);

                                            }
                                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        }
                                );        queue.add(imageRequest);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DashBoardMain.this, "अभिप्राय अपडेट झाला नाही...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.d("rtrt","userID"+user_id);
                params.put("user_id", user_id);

                return params;
            }
        };
        queue.add(request);
    }

}