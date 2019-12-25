package com.example.Game4.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.Game4.R;

import java.io.IOException;
import java.io.InputStream;

public class ScoreActivity extends AppCompatActivity {

    ImageView imgScoreWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        imgScoreWin = findViewById(R.id.score_img_win);
        Intent intent = getIntent();

        int score = intent.getIntExtra("count", 0);
        String mode = intent.getStringExtra("mode");
        Drawable d;

        if (score < 4) {
            try {
                InputStream ims = getAssets().open("game_over_scene.jpg");
                d = Drawable.createFromStream(ims, null);
            } catch (IOException ex) {
                return;
            }
            imgScoreWin.setImageDrawable(d);
            Toast.makeText(this, "Mode: " + mode + "\nZombies Teared YOu Apart", Toast.LENGTH_SHORT).show();
        }

        switch (score) {
            case 4:
                try {
                    InputStream ims = getAssets().open("photo_test2.jpeg");
                    d = Drawable.createFromStream(ims, null);
                } catch (IOException ex) {
                    return;
                }
                imgScoreWin.setImageDrawable(d);
                Toast.makeText(this, "Mode: " + mode + "\nYou Found All 4 Spots\n You Won!!!", Toast.LENGTH_SHORT).show();
                break;

            case 5:
                try {
                    InputStream ims = getAssets().open("photo_test2.jpeg");
                    d = Drawable.createFromStream(ims, null);
                } catch (IOException ex) {
                    return;
                }
                imgScoreWin.setImageDrawable(d);
                Toast.makeText(this, "Mode: " + mode + "\nYou Found All 5 Spots\n You Won!!!", Toast.LENGTH_SHORT).show();
                break;


            case 6:
                try {
                    InputStream ims = getAssets().open("photo_test2.jpeg");
                    d = Drawable.createFromStream(ims, null);
                } catch (IOException ex) {
                    return;
                }
                imgScoreWin.setImageDrawable(d);
                Toast.makeText(this, "Mode: " + mode + "\nYou Found All 6 Spots\n You Won!!!", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(ScoreActivity.this, MainActivity.class));
    }
}
