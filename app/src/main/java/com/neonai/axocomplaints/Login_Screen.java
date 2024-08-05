package com.neonai.axocomplaints;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.neonai.axocomplaints.smpleclasses.CommonFunctions;
import com.neonai.axocomplaints.smpleclasses.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Login_Screen extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "Login_data";
    private static final String KEY_PHONE = "phone_no";
    EditText edtPhone;
    ImageView sendotp;
    EditText edit_OTP;
    Context context = Login_Screen.this;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);

        fn_getCoarseAndFindLocationPermission();

        edtPhone = findViewById(R.id.edtPhone);
        sendotp = findViewById(R.id.imglogin);


        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pattern mobileno = Pattern.compile("[6-9]{1}[0-9]{9}");
                String mobilenumber = edtPhone.getText().toString().trim();
                if (!mobilenumber.isEmpty()) {
                    if (mobileno.matcher(mobilenumber).matches()) {
                        if (CommonFunctions.isNetworkConnected(Login_Screen.this)) {

                            login();


                        } else {
                            CommonFunctions.showNoInternetDialog(Login_Screen.this);
                        }
                    } else {
                        Toast.makeText(Login_Screen.this, "Enter Correct Mobile Number", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(Login_Screen.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fn_getCoarseAndFindLocationPermission()
    {
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Login_Screen.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},111);
        }

    }


    private void login() {

        final ProgressDialog progressDialog = new ProgressDialog(Login_Screen.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        handleResponse(response);

                        Log.d("response","Response=="+response+"\n");
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Login_Screen.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone_no", edtPhone.getText().toString());
                Log.d("Phone---NO","99"+edtPhone);
                return params;
            }
       };

        Volley.newRequestQueue(this).add(stringRequest);

    }


//    private boolean formValidate() {
//
//        Pattern r = Pattern.compile(pattern);
//
//        if (edtPhone.getText().toString().isEmpty()) {
//            m = r.matcher(edtPhone.getText().toString().trim());
//            return showToastNReturnFalse("Enter mobile number");
//        }
//        return true;
//    }

    private boolean showToastNReturnFalse(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        return false;
    }

    private void handleResponse(String response) {
        try {
            Log.d("sohels",""+response);

            JSONObject jsonObject = new JSONObject(response);

            String status = jsonObject.getString("status");

            if (status.equalsIgnoreCase("true")) {

                String phone_no = jsonObject.getString("phone_no");

                phoneLogin(phone_no);

            } else {

                if (jsonObject.has("message")) {
                    String message = jsonObject.getString("message");
                    Toast.makeText(Login_Screen.this, message, Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void phoneLogin(final String phone_no) {
        final Dialog dialog = new Dialog(Login_Screen.this);
        dialog.setContentView(R.layout.dialogbox_ctivity);
        dialog.setTitle("Title...");
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
        edit_OTP = (EditText) dialog.findViewById(R.id.edit_OTP);

        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageView sendotp = (ImageView) dialog.findViewById(R.id.imglogin);

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Enter OTP Section","enter---- ");
                if (edit_OTP.getText().toString().length() > 0) {
                    String verify_code = edit_OTP.getText().toString();
                    VerifyCode(verify_code, phone_no);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter OTP",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }


    private void VerifyCode(final String code, final String phone_no) {

        final ProgressDialog progressDialog = new ProgressDialog(Login_Screen.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.VERIFY_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.d("next123","hfhfhf"+response);
                            JSONObject jsonObject = new JSONObject(response);

                            String profile_status = jsonObject.getString("phone_no");
                            Log.d("Enter-----","");

                            SharedPreferences sharedPreferences = getSharedPreferences(Login_Screen.MY_PREFS_NAME,0);
                            SharedPreferences.Editor edit = sharedPreferences.edit();


                            edit.putBoolean("hasLoggedIn",true);
                            edit.commit();

                            if (profile_status.equalsIgnoreCase("0")) {

                                if (jsonObject.has("data")) {

                                    JSONObject jsonObject1 = jsonObject.getJSONArray("data").getJSONObject(0);
                                    String phone_no = jsonObject1.getString("phone_no");
                                    String id = jsonObject1.getString("id");

                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("phone_no", phone_no);
                                    editor.putString("id", id);
                                    editor.apply();


                                    /*Intent i = new Intent(Login_Screen.this, ProfilePage.class);*/
                                    Intent i = new Intent(Login_Screen.this, DashBoardMain.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    Toast.makeText(Login_Screen.this, "Login Successful", Toast.LENGTH_LONG).show();
                                } else {

                                    if (jsonObject.has("message")) {
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(Login_Screen.this, "Wrong mobile number or password", Toast.LENGTH_LONG).show();
                                    }
                                }


                            } else
                                if (profile_status.equalsIgnoreCase("1")) {

                                        if (jsonObject.has("data")) {

                                            JSONObject jsonObject1 = jsonObject.getJSONArray("data").getJSONObject(0);
                                            String phone_no = jsonObject1.getString("phone_no");
                                            String id = jsonObject1.getString("id");
                                            String name = jsonObject1.getString("name");
                                            String lastname = jsonObject1.getString("last_name");

                                            Log.d("profiles","ssss"+jsonObject1);

                                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

                                            editor.putString("name", name);
                                            editor.putString("lastname", lastname);
                                            editor.putString("phone_no", phone_no);
                                            editor.putString("id", id);
                                            editor.apply();

                                            Intent i = new Intent(Login_Screen.this, DashBoardMain.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                            Toast.makeText(Login_Screen.this, "Login Successful", Toast.LENGTH_LONG).show();

                                        } else {

                                            if (jsonObject.has("message")) {
                                                String message = jsonObject.getString("message");
                                                Toast.makeText(Login_Screen.this, message, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Login_Screen.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", edit_OTP.getText().toString());
                params.put("phone_no", phone_no.trim());
                Log.d("PhoneNo","phoneno"+phone_no);
                return params;
            }

        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}