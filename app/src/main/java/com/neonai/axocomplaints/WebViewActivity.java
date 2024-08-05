package com.neonai.axocomplaints;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_privacy_policy);


        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        // Load the URL in the WebView
        webView = findViewById(R.id.webview);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(WebViewActivity.this, DashBoardMain.class);
        startActivity(intent);
    }
}