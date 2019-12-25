package com.example.Game4.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Game4.Database.Coordinates_DB;
import com.example.Game4.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 1500;
    public static Coordinates_DB coordinates_db;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_splash);
        coordinates_db = new Coordinates_DB(this);
        coordinates_db.creteDataBase();
        coordinates_db.openDatabase();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, 1500);
    }
}