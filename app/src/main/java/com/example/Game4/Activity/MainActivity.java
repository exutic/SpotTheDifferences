package com.example.Game4.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Game4.R;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    MaterialButton btnPlay, btnAbout, btnBack, btnEasyMode, btnNormalMode, btnHardMode;

    Animation fadeIn, fadeOut, enterFromLeft, enterFromRight, enterFromUp, enterFromUp2, enterFromDown, enterFromDown2;

    TextView txtWelome, txtSelectMode, txtEasyMode, txtNormalMode, txtHardMode, txtGameModeTitle;


    public static ArrayList<Integer> randomNumbersModeEasy;
    public static ArrayList<Integer> randomNumbersModeNormal;
    public static ArrayList<Integer> randomNumbersModeHard;


    Intent MODE_INTENT;


    LinearLayout missionLinearLayout;

    MaterialButton btnMission1, btnMission2, btnMission3, btnMission4, btnMission5;

    Button btnBackFromMissionSelect;

    int missionModes = 1;


    ConstraintLayout imageViewBackground;

    int BTN_ID_AND_MISSION_NUMBERS = 0;

    int backButtonCount = 0;

    Intent INTENT_RECIEVE_DATA_FOR_MISSION_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //making the app full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

        findViews();
        INTENT_RECIEVE_DATA_FOR_MISSION_LIST = getIntent();
        int makeButtonsTrue = INTENT_RECIEVE_DATA_FOR_MISSION_LIST.getIntExtra("make_buttons_true", 0);
        if (makeButtonsTrue > 0) {
            buttonGameModesTrue();
        }
        buttonGameModesFalse();
        setAnimation();
        setTextsFromStrings();
//        creaingRandomSpots();
        setBackGround();
        creatingButtonDynamically();


//        getDataFromGameAcitivtyForMissionList();

    }

    void setTextsFromStrings() {
        txtWelome.setText(getResources().getString(R.string.welcome));
        txtSelectMode.setText(getResources().getString(R.string.mode_select));
        txtEasyMode.setText(getResources().getString(R.string.easy_mode));
        txtNormalMode.setText(getResources().getString(R.string.normal_mode));
        txtHardMode.setText(getResources().getString(R.string.hard_mode));
    }

    void findViews() {
        btnPlay = findViewById(R.id.main_btn_play);
        btnAbout = findViewById(R.id.main_btn_about);
        btnBack = findViewById(R.id.main_btn_back);
        btnEasyMode = findViewById(R.id.main_btn_mode_easy);
        btnNormalMode = findViewById(R.id.main_btn_mode_normal);
        btnHardMode = findViewById(R.id.main_btn_mode_hard);

        txtWelome = findViewById(R.id.main_txt_welcome);
        txtSelectMode = findViewById(R.id.main_txt_mode_select);
        txtEasyMode = findViewById(R.id.main_txt_mode_easy_description);
        txtNormalMode = findViewById(R.id.main_txt_mode_normal_description);
        txtHardMode = findViewById(R.id.main_txt_mode_hard_description);

        missionLinearLayout = findViewById(R.id.main_mission_select_layout);
//        btnMission1 = findViewById(R.id.main_btn_mission_1);
//        btnMission2 = findViewById(R.id.main_btn_mission_2);
//        btnMission3 = findViewById(R.id.main_btn_mission_3);
//        btnMission4 = findViewById(R.id.main_btn_mission_4);
//        btnMission5 = findViewById(R.id.main_btn_mission_5);
        btnBackFromMissionSelect = findViewById(R.id.main_btnBackFromMissionSelect);


        imageViewBackground = findViewById(R.id.main_img_background);

        txtGameModeTitle = findViewById(R.id.main_game_mode_title);
    }

    void buttonGameModesFalse() {
        btnBack.setClickable(false);
        btnEasyMode.setClickable(false);
        btnNormalMode.setClickable(false);
        btnHardMode.setClickable(false);
    }

    void buttonGameModesTrue() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnBack.setClickable(true);
                btnEasyMode.setClickable(true);
                btnNormalMode.setClickable(true);
                btnHardMode.setClickable(true);
            }
        }, 500);
    }

    void buttonMissions_false() {
        btnMission1.setClickable(false);
        btnMission2.setClickable(false);
        btnMission3.setClickable(false);
        btnMission4.setClickable(false);
        btnMission5.setClickable(false);
        btnBackFromMissionSelect.setClickable(false);
    }

    void buttonMissions_true() {
        btnMission1.setClickable(true);
        btnMission2.setClickable(true);
        btnMission3.setClickable(true);
        btnMission4.setClickable(true);
        btnMission5.setClickable(true);
        btnBackFromMissionSelect.setClickable(true);
    }

    void setAnimation() {
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(500);

        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(400);
        fadeOut.setDuration(400);

        enterFromLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_enter_from_left);
        enterFromRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_enter_from_right);
        enterFromUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_enter_from_up);
        enterFromUp2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_enter_from_up2);
        enterFromDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_enter_from_down);
        enterFromDown2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_enter_from_down2);
    }

    public void buttonClickS(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.main_btn_play:
                backButtonCount = 2;
                btnPlay.startAnimation(fadeOut);
                btnAbout.startAnimation(fadeOut);
                txtWelome.startAnimation(fadeOut);
                btnBack.startAnimation(fadeIn);

                txtSelectMode.startAnimation(enterFromUp);

                btnEasyMode.startAnimation(enterFromLeft);
                txtEasyMode.startAnimation(enterFromLeft);

                btnNormalMode.startAnimation(enterFromDown);
                txtNormalMode.startAnimation(enterFromDown2);

                btnHardMode.startAnimation(enterFromRight);
                txtHardMode.startAnimation(enterFromRight);

                txtSelectMode.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.INVISIBLE);
                btnAbout.setVisibility(View.INVISIBLE);
                txtWelome.setVisibility(View.INVISIBLE);
                btnBack.setVisibility(View.VISIBLE);
                btnEasyMode.setVisibility(View.VISIBLE);
                btnNormalMode.setVisibility(View.VISIBLE);
                btnHardMode.setVisibility(View.VISIBLE);

                txtEasyMode.setVisibility(View.VISIBLE);
                txtNormalMode.setVisibility(View.VISIBLE);
                txtHardMode.setVisibility(View.VISIBLE);

                btnPlay.setEnabled(false);
                btnAbout.setEnabled(false);
                buttonGameModesTrue();
//                buttonMissions_true();
                break;

            case R.id.main_btn_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;

            case R.id.main_btn_back:
                backButtonCount = 0;
                btnPlay.startAnimation(fadeIn);
                btnAbout.startAnimation(fadeIn);
                txtWelome.startAnimation(fadeIn);

                btnBack.startAnimation(fadeOut);
                btnEasyMode.startAnimation(fadeOut);
                btnNormalMode.startAnimation(fadeOut);
                btnHardMode.startAnimation(fadeOut);
                txtSelectMode.startAnimation(fadeOut);
                txtEasyMode.startAnimation(fadeOut);
                txtNormalMode.startAnimation(fadeOut);
                txtHardMode.startAnimation(fadeOut);

                btnPlay.setVisibility(View.VISIBLE);
                btnAbout.setVisibility(View.VISIBLE);
                txtWelome.setVisibility(View.VISIBLE);

                btnBack.setVisibility(View.INVISIBLE);
                txtSelectMode.setVisibility(View.INVISIBLE);
                btnEasyMode.setVisibility(View.INVISIBLE);
                btnNormalMode.setVisibility(View.INVISIBLE);
                btnHardMode.setVisibility(View.INVISIBLE);
                txtEasyMode.setVisibility(View.INVISIBLE);
                txtNormalMode.setVisibility(View.INVISIBLE);
                txtHardMode.setVisibility(View.INVISIBLE);

                btnPlay.setEnabled(true);
                btnAbout.setEnabled(true);

                buttonGameModesFalse();
                break;


            case R.id.main_btn_mode_easy:
                backButtonCount = 2;
                missionModes = 1;

                btnBack.setEnabled(false);
                btnEasyMode.setEnabled(false);
                btnNormalMode.setEnabled(false);
                btnHardMode.setEnabled(false);

                btnBack.startAnimation(fadeOut);
                btnEasyMode.startAnimation(fadeOut);
                btnNormalMode.startAnimation(fadeOut);
                btnHardMode.startAnimation(fadeOut);
                txtSelectMode.startAnimation(fadeOut);
                txtEasyMode.startAnimation(fadeOut);
                txtNormalMode.startAnimation(fadeOut);
                txtHardMode.startAnimation(fadeOut);

                txtGameModeTitle.setText(getResources().getString(R.string.easy_mode_title));

                btnBack.setVisibility(View.INVISIBLE);
                btnEasyMode.setVisibility(View.INVISIBLE);
                btnNormalMode.setVisibility(View.INVISIBLE);
                btnHardMode.setVisibility(View.INVISIBLE);
                txtSelectMode.setVisibility(View.INVISIBLE);
                txtEasyMode.setVisibility(View.INVISIBLE);
                txtNormalMode.setVisibility(View.INVISIBLE);
                txtHardMode.setVisibility(View.INVISIBLE);

                missionLinearLayout.startAnimation(fadeIn);
                missionLinearLayout.setVisibility(View.VISIBLE);
                btnBackFromMissionSelect.setVisibility(View.VISIBLE);

                btnBackFromMissionSelect.setClickable(true);


//                buttonMissions_true();

                break;

            case R.id.main_btn_mode_normal:
                backButtonCount = 2;

                missionModes = 2;

                btnBack.startAnimation(fadeOut);
                btnEasyMode.startAnimation(fadeOut);
                btnNormalMode.startAnimation(fadeOut);
                btnHardMode.startAnimation(fadeOut);
                txtSelectMode.startAnimation(fadeOut);
                txtEasyMode.startAnimation(fadeOut);
                txtNormalMode.startAnimation(fadeOut);
                txtHardMode.startAnimation(fadeOut);

                txtGameModeTitle.setText(getResources().getString(R.string.normal_mode_title));

                btnBack.setVisibility(View.INVISIBLE);
                btnEasyMode.setVisibility(View.INVISIBLE);
                btnNormalMode.setVisibility(View.INVISIBLE);
                btnHardMode.setVisibility(View.INVISIBLE);
                txtSelectMode.setVisibility(View.INVISIBLE);
                txtEasyMode.setVisibility(View.INVISIBLE);
                txtNormalMode.setVisibility(View.INVISIBLE);
                txtHardMode.setVisibility(View.INVISIBLE);

                missionLinearLayout.startAnimation(fadeIn);
                missionLinearLayout.setVisibility(View.VISIBLE);
                btnBackFromMissionSelect.setVisibility(View.VISIBLE);
                btnBackFromMissionSelect.setClickable(true);


//                buttonMissions_true();

                break;

            case R.id.main_btn_mode_hard:
                backButtonCount = 2;

                missionModes = 3;

                btnBack.startAnimation(fadeOut);
                btnEasyMode.startAnimation(fadeOut);
                btnNormalMode.startAnimation(fadeOut);
                btnHardMode.startAnimation(fadeOut);
                txtSelectMode.startAnimation(fadeOut);
                txtEasyMode.startAnimation(fadeOut);
                txtNormalMode.startAnimation(fadeOut);
                txtHardMode.startAnimation(fadeOut);

                txtGameModeTitle.setText(getResources().getString(R.string.hard_mode_title));

                btnBack.setVisibility(View.INVISIBLE);
                btnEasyMode.setVisibility(View.INVISIBLE);
                btnNormalMode.setVisibility(View.INVISIBLE);
                btnHardMode.setVisibility(View.INVISIBLE);
                txtSelectMode.setVisibility(View.INVISIBLE);
                txtEasyMode.setVisibility(View.INVISIBLE);
                txtNormalMode.setVisibility(View.INVISIBLE);
                txtHardMode.setVisibility(View.INVISIBLE);

                missionLinearLayout.startAnimation(fadeIn);
                missionLinearLayout.setVisibility(View.VISIBLE);
                btnBackFromMissionSelect.setVisibility(View.VISIBLE);
                btnBackFromMissionSelect.setClickable(true);

//                buttonMissions_true();

                break;


//            case R.id.main_btn_mission_1:
//
//                switch (missionModes)
//                {
//                    case 1:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 1);
//                        MODE_INTENT.putExtra("mission", 1);
//                        startActivity(MODE_INTENT);
//                        break;
//                    case 2:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 2);
//                        MODE_INTENT.putExtra("mission", 1);
//                        startActivity(MODE_INTENT);
//                        break;
//                    case 3:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 3);
//                        MODE_INTENT.putExtra("mission", 1);
//                        startActivity(MODE_INTENT);
//                        break;
//
//                }
//                break;
//
//            case R.id.main_btn_mission_2:
//
//                switch (missionModes) {
//                    case 1:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 1);
//                        MODE_INTENT.putExtra("mission", 2);
//                        startActivity(MODE_INTENT);
//                        break;
//                    case 2:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 2);
//                        MODE_INTENT.putExtra("mission", 2);
//                        startActivity(MODE_INTENT);
//                        break;
//                    case 3:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 3);
//                        MODE_INTENT.putExtra("mission", 2);
//                        startActivity(MODE_INTENT);
//                        break;
//
//                }
//                break;
//
//            case R.id.main_btn_mission_3:
//
//                switch (missionModes) {
//                    case 1:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 1);
//                        MODE_INTENT.putExtra("mission", 3);
//                        startActivity(MODE_INTENT);
//                        break;
//                    case 2:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 2);
//                        MODE_INTENT.putExtra("mission", 3);
//                        startActivity(MODE_INTENT);
//                        break;
//                    case 3:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 3);
//                        MODE_INTENT.putExtra("mission", 3);
//                        startActivity(MODE_INTENT);
//                        break;
//
//                }
//                break;
//
//            case R.id.main_btn_mission_4:
//
//                switch (missionModes) {
//                    case 1:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 1);
//                        MODE_INTENT.putExtra("mission", 4);
//                        startActivity(MODE_INTENT);
//                        break;
//                    case 2:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 2);
//                        MODE_INTENT.putExtra("mission", 4);
//                        startActivity(MODE_INTENT);
//                        break;
//                    case 3:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 3);
//                        MODE_INTENT.putExtra("mission", 4);
//                        startActivity(MODE_INTENT);
//                        break;
//
//                }
//                break;
//
//            case R.id.main_btn_mission_5:
//
//                switch (missionModes) {
//                    case 1:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 1);
//                        MODE_INTENT.putExtra("mission", 5);
//                        startActivity(MODE_INTENT);
//                        break;
//                    case 2:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 2);
//                        MODE_INTENT.putExtra("mission", 5);
//                        startActivity(MODE_INTENT);
//                        break;
//                    case 3:
//                        MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
//                        MODE_INTENT.putExtra("mode", 3);
//                        MODE_INTENT.putExtra("mission", 5);
//                        startActivity(MODE_INTENT);
//                        break;
//
//                }
//                break;

            case R.id.main_btnBackFromMissionSelect:

                btnBack.startAnimation(fadeIn);
                btnEasyMode.startAnimation(fadeIn);
                btnNormalMode.startAnimation(fadeIn);
                btnHardMode.startAnimation(fadeIn);
                txtSelectMode.startAnimation(fadeIn);
                txtEasyMode.startAnimation(fadeIn);
                txtNormalMode.startAnimation(fadeIn);
                txtHardMode.startAnimation(fadeIn);

                btnBack.setVisibility(View.VISIBLE);
                btnEasyMode.setVisibility(View.VISIBLE);
                btnNormalMode.setVisibility(View.VISIBLE);
                btnHardMode.setVisibility(View.VISIBLE);
                txtSelectMode.setVisibility(View.VISIBLE);
                txtEasyMode.setVisibility(View.VISIBLE);
                txtNormalMode.setVisibility(View.VISIBLE);
                txtHardMode.setVisibility(View.VISIBLE);

                missionLinearLayout.startAnimation(fadeOut);
                btnBackFromMissionSelect.startAnimation(fadeOut);

                missionLinearLayout.setVisibility(View.INVISIBLE);
                btnBackFromMissionSelect.setVisibility(View.INVISIBLE);

                btnBack.setEnabled(true);
                btnEasyMode.setEnabled(true);
                btnNormalMode.setEnabled(true);
                btnHardMode.setEnabled(true);


//                buttonMissions_false();

                break;


        }

    }

    @Override
    public void onBackPressed() {

        /**
         * Back button listener.
         * Will close the application if the back button pressed twice.
         */
        if (backButtonCount < 2) {
            if (backButtonCount == 1) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
                backButtonCount = 1;
            }

        }

    }

    void creaingRandomSpots() {
        randomNumbersModeEasy = new ArrayList<Integer>();
        for (int i = 1; i <= 50; ++i) {
            randomNumbersModeEasy.add(i);
        }
        Collections.shuffle(randomNumbersModeEasy);


        randomNumbersModeNormal = new ArrayList<Integer>();
        for (int i = 1; i <= 50; ++i) {
            randomNumbersModeNormal.add(i);
        }
        Collections.shuffle(randomNumbersModeNormal);


        randomNumbersModeHard = new ArrayList<Integer>();
        for (int i = 1; i <= 50; ++i) {
            randomNumbersModeHard.add(i);
        }
        Collections.shuffle(randomNumbersModeHard);
    }

    void setBackGround() {
        Drawable d;
        try {
            InputStream ims = getAssets().open("background1.png");
            d = Drawable.createFromStream(ims, null);
        } catch (IOException ex) {
            return;
        }
        imageViewBackground.setBackground(d);
    }

    void creatingButtonDynamically() {

        for (BTN_ID_AND_MISSION_NUMBERS = 1; BTN_ID_AND_MISSION_NUMBERS <= 5; BTN_ID_AND_MISSION_NUMBERS++) {
            final Button myButton = new MaterialButton(this);
            myButton.setId(BTN_ID_AND_MISSION_NUMBERS);
            myButton.setTag("mission" + BTN_ID_AND_MISSION_NUMBERS);
            myButton.setText(String.valueOf(BTN_ID_AND_MISSION_NUMBERS));
            myButton.setGravity(Gravity.CENTER);
            myButton.setTextColor(Color.CYAN);
            if (BTN_ID_AND_MISSION_NUMBERS > 5) {
                myButton.setClickable(false);
            }
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (missionModes) {
                        case 1:
                            MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
                            MODE_INTENT.putExtra("mode", 1);
                            MODE_INTENT.putExtra("mission", myButton.getId());
                            startActivity(MODE_INTENT);
                            break;
                        case 2:
                            MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
                            MODE_INTENT.putExtra("mode", 2);
                            MODE_INTENT.putExtra("mission", myButton.getId());
                            startActivity(MODE_INTENT);
                            break;
                        case 3:
                            MODE_INTENT = new Intent(MainActivity.this, GameActivity.class);
                            MODE_INTENT.putExtra("mode", 3);
                            MODE_INTENT.putExtra("mission", myButton.getId());
                            startActivity(MODE_INTENT);
                            break;

                    }
                }
            });

            GridLayout ll = (GridLayout) findViewById(R.id.main_mission_select_gridlayout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(125, 125);
            ll.addView(myButton, lp);

        }

    }

    public void getDataFromGameAcitivtyForMissionList() {
        INTENT_RECIEVE_DATA_FOR_MISSION_LIST = getIntent();

        int missionList = INTENT_RECIEVE_DATA_FOR_MISSION_LIST.getIntExtra("mission_list", 0);

        switch (missionList) {
            case 1:

                backButtonCount = 2;
                missionModes = 1;

                btnBack.setVisibility(View.INVISIBLE);
                btnEasyMode.setVisibility(View.INVISIBLE);
                btnNormalMode.setVisibility(View.INVISIBLE);
                btnHardMode.setVisibility(View.INVISIBLE);
                txtSelectMode.setVisibility(View.INVISIBLE);
                txtEasyMode.setVisibility(View.INVISIBLE);
                txtNormalMode.setVisibility(View.INVISIBLE);
                txtHardMode.setVisibility(View.INVISIBLE);
                btnPlay.setVisibility(View.INVISIBLE);
                btnAbout.setVisibility(View.INVISIBLE);
                txtWelome.setVisibility(View.INVISIBLE);

                txtGameModeTitle.setText(getResources().getString(R.string.easy_mode_title));

                missionLinearLayout.startAnimation(fadeIn);
                missionLinearLayout.setVisibility(View.VISIBLE);
                btnBackFromMissionSelect.setVisibility(View.VISIBLE);

                btnBackFromMissionSelect.setClickable(true);


                break;

            case 2:

                backButtonCount = 2;
                missionModes = 2;

                btnBack.setVisibility(View.INVISIBLE);
                btnEasyMode.setVisibility(View.INVISIBLE);
                btnNormalMode.setVisibility(View.INVISIBLE);
                btnHardMode.setVisibility(View.INVISIBLE);
                txtSelectMode.setVisibility(View.INVISIBLE);
                txtEasyMode.setVisibility(View.INVISIBLE);
                txtNormalMode.setVisibility(View.INVISIBLE);
                txtHardMode.setVisibility(View.INVISIBLE);
                btnPlay.setVisibility(View.INVISIBLE);
                btnAbout.setVisibility(View.INVISIBLE);
                txtWelome.setVisibility(View.INVISIBLE);

                txtGameModeTitle.setText(getResources().getString(R.string.normal_mode_title));

                missionLinearLayout.startAnimation(fadeIn);
                missionLinearLayout.setVisibility(View.VISIBLE);
                btnBackFromMissionSelect.setVisibility(View.VISIBLE);
                btnBackFromMissionSelect.setClickable(true);


                break;

            case 3:

                backButtonCount = 2;
                missionModes = 3;

                btnBack.setVisibility(View.INVISIBLE);
                btnEasyMode.setVisibility(View.INVISIBLE);
                btnNormalMode.setVisibility(View.INVISIBLE);
                btnHardMode.setVisibility(View.INVISIBLE);
                txtSelectMode.setVisibility(View.INVISIBLE);
                txtEasyMode.setVisibility(View.INVISIBLE);
                txtNormalMode.setVisibility(View.INVISIBLE);
                txtHardMode.setVisibility(View.INVISIBLE);
                btnPlay.setVisibility(View.INVISIBLE);
                btnAbout.setVisibility(View.INVISIBLE);
                txtWelome.setVisibility(View.INVISIBLE);

                txtGameModeTitle.setText(getResources().getString(R.string.hard_mode_title));

                missionLinearLayout.startAnimation(fadeIn);
                missionLinearLayout.setVisibility(View.VISIBLE);
                btnBackFromMissionSelect.setVisibility(View.VISIBLE);
                btnBackFromMissionSelect.setClickable(true);


                break;
        }

    }
}

