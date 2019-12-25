package com.example.Game4.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.Game4.R;

public class AboutActivity extends AppCompatActivity {

    TextView txtAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        txtAbout = findViewById(R.id.txt_about);

        txtAbout.setText(getResources().getString(R.string.text_about));


    }
}
