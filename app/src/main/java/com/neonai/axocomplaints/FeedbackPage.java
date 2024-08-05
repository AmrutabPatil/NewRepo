package com.neonai.axocomplaints;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.neonai.axocomplaints.smpleclasses.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FeedbackPage extends AppCompatActivity {

    EditText edtReviewTitle1, edtReviewComment2;
    TextView ratingTitle;
    RatingBar product_ratingbar;
    public static final String MY_PREFS_NAME = "Login_data";
    Button btnRatingSubmit;
    String sRating,user_id;

    JSONArray jsonArray = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_page);


        ImageView lefticon = findViewById(R.id.left_arrow_icon);
        TextView title1 = findViewById(R.id.toolbarTitle);

        ratingTitle = findViewById(R.id.edtReviewTitle2);
        product_ratingbar = findViewById(R.id.product_ratingbar);
        edtReviewTitle1 = findViewById(R.id.edtReviewTitle1);
        edtReviewComment2 = findViewById(R.id.edtReviewComment2);
        btnRatingSubmit = findViewById(R.id.btnRatingSubmit);

        callPUTDataMethods();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("id",null);

        lefticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(FeedbackPage.this, "Back", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(FeedbackPage.this, DashBoardMain.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });

        title1.setText("अभिप्राय नोंदवा");

        product_ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingTitle.setText(String.format("(%s)",rating));

            }
        });

        btnRatingSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sRating = String.valueOf(product_ratingbar.getRating());
                String edtTitle = edtReviewTitle1.getText().toString();
                String edtReview = edtReviewComment2.getText().toString();

                if (!edtTitle.isEmpty()) {
                    edtReviewTitle1.setError(null);
                    if (!edtReview.isEmpty()) {
                        edtReviewComment2.setError(null);

                        callPUTDataMethod(edtReviewTitle1.getText().toString(),edtReviewComment2.getText().toString());

                    } else {
                        edtReviewComment2.setError("Please Enter The Subject");
                    }
                }
                else {
                    edtReviewTitle1.setError("Please Enter The Description");
                }
            }
        });
    }

    private void callPUTDataMethod(String edtTitle,String edtReview) {

        final ProgressDialog progressDialog = new ProgressDialog(FeedbackPage.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("अभिप्राय नोंद होत आहे...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(FeedbackPage.this);

        StringRequest request = new StringRequest(Request.Method.POST, Const.FEEDBACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                       edtReviewTitle1.setText("");
                       edtReviewComment2.setText("");
                        progressDialog.dismiss();

                        Intent i = new Intent(FeedbackPage.this, ThanksFeedbackPage.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                        Toast.makeText(FeedbackPage.this, "अभिप्राय नोंदवला..", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(FeedbackPage.this, "अभिप्राय नोंद झाली नाही..", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("rating", sRating);
                params.put("user_id", user_id);
                params.put("subject", edtTitle);
                params.put("description", edtReview);



                return params;
            }
        };
        queue.add(request);
    }

    private void callPUTDataMethods() {

        RequestQueue queue = Volley.newRequestQueue(FeedbackPage.this);

        StringRequest request = new StringRequest(Request.Method.POST, Const.FEEDBACK_GET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        edtReviewTitle1.setText("");
                        edtReviewComment2.setText("");
                        ratingTitle.setText("");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("response12", "34" + response);
                            if (jsonObject.has("data")) {

                                JSONObject jsonObject1 = jsonObject.getJSONArray("data").getJSONObject(0);
                                String subject = jsonObject1.getString("subject");
                                String description = jsonObject1.getString("description");
                                String rating = jsonObject1.getString("rating");

                                Log.d("rating","axol"+rating);
                                edtReviewTitle1.setText(subject);
                                edtReviewComment2.setText(description);
                                product_ratingbar.setRating(Float.parseFloat(rating));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(FeedbackPage.this, "अभिप्राय अपडेट झाला नाही...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", user_id);

                return params;
            }
        };
        queue.add(request);
    }
}