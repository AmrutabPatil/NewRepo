package com.neonai.axocomplaints;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class Splash_Screen extends AppCompatActivity {

    AlertDialog.Builder builder;
    ImageView imageViewlogo;
    Handler mWaitHandler = new Handler();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        this.imageViewlogo = (ImageView) findViewById(R.id.imageViewlogo);
        this.builder = new AlertDialog.Builder(this);
        if (!DetectConnection.checkInternetConnection(this)) {
            this.builder.setMessage((CharSequence) "Do you want to close this application ?").setCancelable(false).setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Splash_Screen.this.finish();
                }
            }).setNegativeButton((CharSequence) "Settings", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Splash_Screen.this.startActivityForResult(new Intent("android.settings.SETTINGS"), 0);
                }
            });
            AlertDialog alert = this.builder.create();
            alert.setTitle("No Internet Connection");
            alert.show();
            return;
        }
        this.mWaitHandler.postDelayed(new Runnable() {
            public void run() {
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(Login_Screen.MY_PREFS_NAME,0);
                    boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn",false);
                    if (hasLoggedIn){

                        Splash_Screen.this.startActivity(new Intent(Splash_Screen.this.getApplicationContext(), DashBoardMain.class));
                        Splash_Screen.this.finish();
                    }else{

                        Splash_Screen.this.startActivity(new Intent(Splash_Screen.this.getApplicationContext(), Login_Screen.class));
                        Splash_Screen.this.finish();
                    }
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }, 4000);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mWaitHandler.removeCallbacksAndMessages((Object) null);
    }

    public void onPause() {
        super.onPause();
        this.mWaitHandler.removeCallbacksAndMessages((Object) null);
    }

    public void onRestart() {
        super.onRestart();
//        this.imageViewlogo = (ImageView) findViewById(R.id.imageViewlogo);
        this.builder = new AlertDialog.Builder(this);
        if (!DetectConnection.checkInternetConnection(this)) {
            this.builder.setMessage((CharSequence) "Do you want to close this application ?").setCancelable(false).setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Splash_Screen.this.finish();
                }
            }).setNegativeButton((CharSequence) "Settings", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Splash_Screen.this.startActivityForResult(new Intent("android.settings.SETTINGS"), 0);
                }
            });
            AlertDialog alert = this.builder.create();
            alert.setTitle("No Internet Connection");
            alert.show();
            return;
        }
        this.mWaitHandler.postDelayed(new Runnable() {
            public void run() {
                try {
                    Splash_Screen.this.startActivity(new Intent(Splash_Screen.this.getApplicationContext(), Login_Screen.class));
                    Splash_Screen.this.finish();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }, 2000);
    }

}