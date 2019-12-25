package com.example.Game4.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.Game4.Database.Coordinates_DB;
import com.example.Game4.R;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    Button btnDiff1_A, btnDiff1_B, btnDiff2_A, btnDiff2_B, btnDiff3_A, btnDiff3_B, btnDiff4_A, btnDiff4_B, btnDiff5_A, btnDiff5_B, btnDiff6_A, btnDiff6_B, btnExitGame, btnHint;
    ConstraintLayout constraintLayout;
    ImageView imageView_1;
    int mission;
    int mode;
    int numberCHeck1 = 0;
    int numberCHeck2 = 0;
    int numberCHeck3 = 0;
    int numberCHeck4 = 0;
    int numberCHeck5 = 0;
    int numberCHeck6 = 0;
    int objectCount = 0;
    ConstraintSet set2;
    String table = Coordinates_DB.DB_TABLE_EASY;
    long timerTime = 60000;
    long tempTimerTime = 0;

    public TextView txtTimer;

    Intent INTENT_GAME_TO_MISSION_LIST = null;

    Animation fadeIn, fadeOut, fadeIn_layout, fadeOut_layout;

    int onBackPressedLimitCLick = 0;

    CountDownTimer countDownTimer;

    boolean gamePauseFlag = true;


    MaterialButton btnHomeEasyMOde, btnHomeNormalMOde, btnHomeHardMOde,
            btnNextEasyMOde, btnNextNormalMOde, btnNextHardMOde,
            btnResEasyMOde, btnResNormalMOde, btnResHardMOde;
    MaterialButton btnNextMissionEasy, btnNextMissionNormal, btnNextMissionHard;
    LinearLayoutCompat LL_EASY, LL_NORMAL, LL_HARD,
            LL_ACTION_MAIN_EASY, LL_ACTION_MAIN_NORMAL, LL_ACTION_MAIN_HARD;


    boolean CHECK_WIN_FOR_END_GAME = false;

    SharedPreferences sharedPreferences;
    public static final String PREF_SETTINGS_KEY = "com.example.work_2_SETTINGS";
    int missionNumber = 0;


    boolean flagHint = true;

    boolean exitFromExitGameBUtton;
    boolean exitFromHomeBUtton;
    boolean missionToMissionFlag;
    boolean lastMissionFinishFlag;

    int checkHint1 = 1, checkHint2 = 1, checkHint3 = 1, checkHint4 = 1;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //making the app fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        findVIEWS_Layout();
        setAnimation();

        //SP next mission
        sharedPreferences = getSharedPreferences(PREF_SETTINGS_KEY, MODE_PRIVATE);
        missionNumber = sharedPreferences.getInt("mission_number", 1);

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 1);
        mission = intent.getIntExtra("mission", 1);

        if (missionNumber > mission) {
            mission = missionNumber;
        }


        int i = mode;
        switch (mode) {
            case 1:
                setContentView((int) R.layout.activity_game_easy);
                buttonEasyFindViews();
//                LL_ACTION_MAIN_EASY.startAnimation(fadeIn_layout);

                break;
            case 2:
                setContentView((int) R.layout.activity_game_normal);
                buttonNormalFindViews();
//                LL_ACTION_MAIN_NORMAL.startAnimation(fadeIn_layout);

                break;
            case 3:
                setContentView((int) R.layout.activity_game_hard);
                buttonHardFindViews();
//                LL_ACTION_MAIN_HARD.startAnimation(fadeIn_layout);

                break;
        } // getting game mode from intent extra


        findVIEWS(); // find View By ID
        setBIAS(mode); // coordinate according to its game mode
        refreshGame(); // reset time and buttons
        setTimer(timerTime); // 30 - 60 seconds it has run on Activity Thread otherwise it will work the whole time app is running
        setBG(); // from assets

    }

    public void setBIAS(int mode2) {
        set2 = new ConstraintSet();
        switch (mode2) {
            case 1:
                table = Coordinates_DB.DB_TABLE_EASY;
                constraintLayout = (ConstraintLayout) findViewById(R.id.game_part_easy);
                set2.clone(constraintLayout);
                randomSpotsEasy(mission);
                break;
            case 2:
                table = Coordinates_DB.DB_TABLE_NORMAL;
                constraintLayout = (ConstraintLayout) findViewById(R.id.game_part_normal);
                set2.clone(constraintLayout);
                randomSpotsNormal(mission);
                break;
            case 3:
                table = Coordinates_DB.DB_TABLE_HARD;
                constraintLayout = (ConstraintLayout) findViewById(R.id.game_part_hard);
                set2.clone(constraintLayout);
                randomSpotsHard(mission);
                break;
        }

        this.set2.applyTo(this.constraintLayout);
    }

    public void findVIEWS_Layout() {
        LL_ACTION_MAIN_EASY = findViewById(R.id.game_main_layout_easy);
        LL_ACTION_MAIN_NORMAL = findViewById(R.id.game_main_layout_normal);
        LL_ACTION_MAIN_HARD = findViewById(R.id.game_main_layout_hard);
    }

    public void findVIEWS() {
        btnExitGame = (Button) findViewById(R.id.game_btn_giveup);
        btnHint = (Button) findViewById(R.id.game_btn_hint);
        txtTimer = (TextView) findViewById(R.id.game_txt_timer);
        btnExitGame.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                exitFromExitGameBUtton = true;
                GameActivity gameActivity = GameActivity.this;
                gameActivity.startActivity(new Intent(gameActivity, MainActivity.class));
                countDownTimer.cancel();
                finish();
            }
        });
        btnHint.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (flagHint && (checkHint1 + checkHint2 + checkHint3 + checkHint4 == 4)) {
                    Toast.makeText(GameActivity.this, "Look Better", Toast.LENGTH_SHORT).show();
                    Random random = new Random();
                    int rnd = random.nextInt(4) + 1;
                    switch (rnd) {
                        case 1:
                            btnDiff1_A.setBackgroundResource(R.color.colorHint);
                            break;
                        case 2:
                            btnDiff2_B.setBackgroundResource(R.color.colorHint);
                            break;
                        case 3:
                            btnDiff3_A.setBackgroundResource(R.color.colorHint);
                            break;
                        case 4:
                            btnDiff4_B.setBackgroundResource(R.color.colorHint);
                            break;
                    }
                    flagHint = false;
                } else if (flagHint && (checkHint1 + checkHint2 + checkHint3 == 3)) {
                    Toast.makeText(GameActivity.this, "Look Better", Toast.LENGTH_SHORT).show();
                    Random random = new Random();
                    int rnd = random.nextInt(4) + 1;
                    switch (rnd) {
                        case 1:
                            btnDiff1_A.setBackgroundResource(R.color.colorHint);
                            break;
                        case 2:
                            btnDiff2_B.setBackgroundResource(R.color.colorHint);
                            break;
                        case 3:
                            btnDiff3_A.setBackgroundResource(R.color.colorHint);
                            break;
                    }
                    flagHint = false;
                } else if (flagHint && (checkHint1 + checkHint2 == 2)) {
                    Toast.makeText(GameActivity.this, "Look Better", Toast.LENGTH_SHORT).show();
                    Random random = new Random();
                    int rnd = random.nextInt(4) + 1;
                    switch (rnd) {
                        case 1:
                            btnDiff1_A.setBackgroundResource(R.color.colorHint);
                            break;
                        case 2:
                            btnDiff2_B.setBackgroundResource(R.color.colorHint);
                            break;
                    }
                    flagHint = false;
                } else if (checkHint1 == 1 && checkHint2 == 0 && checkHint3 == 0 && checkHint4 == 0) {
                    btnDiff1_B.setBackgroundResource(R.color.colorHint);
                } else if (checkHint1 == 0 && checkHint2 == 1 && checkHint3 == 0 && checkHint4 == 0) {
                    btnDiff2_A.setBackgroundResource(R.color.colorHint);
                } else if (checkHint1 == 0 && checkHint2 == 0 && checkHint3 == 1 && checkHint4 == 0) {
                    btnDiff3_B.setBackgroundResource(R.color.colorHint);
                } else if (checkHint1 == 0 && checkHint2 == 0 && checkHint3 == 0 && checkHint4 == 1) {
                    btnDiff4_A.setBackgroundResource(R.color.colorHint);
                } else {
                    Toast.makeText(GameActivity.this, "You Used Your Hint", Toast.LENGTH_SHORT).show();
                }

            }
        });

        LL_EASY = findViewById(R.id.game_endgame_pop_up_layout_easy);
        btnNextEasyMOde = findViewById(R.id.game_endgame_pop_up_next_button_easy_mode);
        btnHomeEasyMOde = findViewById(R.id.game_endgame_pop_up_home_button_easy_mode);
        btnResEasyMOde = findViewById(R.id.game_endgame_pop_up_resume_button_easy_mode);

        LL_NORMAL = findViewById(R.id.game_endgame_pop_up_layout_normal);
        btnNextNormalMOde = findViewById(R.id.game_endgame_pop_up_next_button_normal_mode);
        btnHomeNormalMOde = findViewById(R.id.game_endgame_pop_up_home_button_normal_mode);
        btnResNormalMOde = findViewById(R.id.game_endgame_pop_up_resume_button_normal_mode);

        LL_HARD = findViewById(R.id.game_endgame_pop_up_layout_hard);
        btnNextHardMOde = findViewById(R.id.game_endgame_pop_up_next_button_hard_mode);
        btnHomeHardMOde = findViewById(R.id.game_endgame_pop_up_home_button_hard_mode);
        btnResHardMOde = findViewById(R.id.game_endgame_pop_up_resume_button_hard_mode);

        btnNextMissionEasy = findViewById(R.id.game_endgame_pop_up_next_mission_button_easy_mode);
        btnNextMissionNormal = findViewById(R.id.game_endgame_pop_up_next_mission_button_normal_mode);
        btnNextMissionHard = findViewById(R.id.game_endgame_pop_up_next_mission_button_hard_mode);
    }

    public void clicksEasy(View view) {
        String str = "You Already found this one";
        switch (view.getId()) {
            case R.id.game_btn_dif_1_A_part1_easy /*2131230846*/:
                Toast.makeText(this, "1A", Toast.LENGTH_SHORT).show();
                if (numberCHeck1 == 0) {
                    btnDiff1_A.setBackgroundResource(R.drawable.border1);
                    btnDiff1_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck1 = 1;
                    objectCount++;
                    checkHint1 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_1_B_part1_easy /*2131230849*/:
                Toast.makeText(this, "1B", Toast.LENGTH_SHORT).show();
                if (numberCHeck1 == 0) {
                    btnDiff1_B.setBackgroundResource(R.drawable.border1);
                    btnDiff1_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck1 = 1;
                    objectCount++;
                    checkHint1 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_2_A_part1_easy /*2131230852*/:
                Toast.makeText(this, "2A", Toast.LENGTH_SHORT).show();
                if (numberCHeck2 == 0) {
                    btnDiff2_A.setBackgroundResource(R.drawable.border1);
                    btnDiff2_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck2 = 2;
                    objectCount++;
                    checkHint2 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_2_B_part1_easy /*2131230855*/:
                Toast.makeText(this, "2B", Toast.LENGTH_SHORT).show();
                if (numberCHeck2 == 0) {
                    btnDiff2_B.setBackgroundResource(R.drawable.border1);
                    btnDiff2_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck2 = 2;
                    objectCount++;
                    checkHint2 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_3_A_part1_easy /*2131230858*/:
                Toast.makeText(this, "3A", Toast.LENGTH_SHORT).show();
                if (numberCHeck3 == 0) {
                    btnDiff3_A.setBackgroundResource(R.drawable.border1);
                    btnDiff3_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck3 = 3;
                    objectCount++;
                    checkHint3 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_3_B_part1_easy /*2131230861*/:
                Toast.makeText(this, "3B", Toast.LENGTH_SHORT).show();
                if (numberCHeck3 == 0) {
                    btnDiff3_B.setBackgroundResource(R.drawable.border1);
                    btnDiff3_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck3 = 3;
                    objectCount++;
                    checkHint3 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_4_A_part1_easy /*2131230864*/:
                Toast.makeText(this, "4B", Toast.LENGTH_SHORT).show();
                if (numberCHeck4 == 0) {
                    btnDiff4_A.setBackgroundResource(R.drawable.border1);
                    btnDiff4_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck4 = 4;
                    objectCount++;
                    checkHint4 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_4_B_part1_easy /*2131230867*/:
                Toast.makeText(this, "4A", Toast.LENGTH_SHORT).show();
                if (numberCHeck4 == 0) {
                    btnDiff4_B.setBackgroundResource(R.drawable.border1);
                    btnDiff4_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck4 = 4;
                    objectCount++;
                    checkHint4 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void clicksNormal(View view) {
        String str = "You Already found this one";
        switch (view.getId()) {
            case R.id.game_btn_dif_1_A_part1_normal /*2131230848*/:
                Toast.makeText(this, "1A", Toast.LENGTH_SHORT).show();
                if (numberCHeck1 == 0) {
                    btnDiff1_A.setBackgroundResource(R.drawable.border1);
                    btnDiff1_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck1 = 1;
                    objectCount++;
                    checkHint1 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_1_B_part1_normal /*2131230851*/:
                Toast.makeText(this, "1B", Toast.LENGTH_SHORT).show();
                if (numberCHeck1 == 0) {
                    btnDiff1_B.setBackgroundResource(R.drawable.border1);
                    btnDiff1_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck1 = 1;
                    objectCount++;
                    checkHint1 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_2_A_part1_normal /*2131230854*/:
                Toast.makeText(this, "2A", Toast.LENGTH_SHORT).show();
                if (numberCHeck2 == 0) {
                    btnDiff2_A.setBackgroundResource(R.drawable.border1);
                    btnDiff2_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck2 = 2;
                    objectCount++;
                    checkHint2 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_2_B_part1_normal /*2131230857*/:
                Toast.makeText(this, "2B", Toast.LENGTH_SHORT).show();
                if (numberCHeck2 == 0) {
                    btnDiff2_B.setBackgroundResource(R.drawable.border1);
                    btnDiff2_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck2 = 2;
                    objectCount++;
                    checkHint2 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_3_A_part1_normal /*2131230860*/:
                Toast.makeText(this, "3A", Toast.LENGTH_SHORT).show();
                if (numberCHeck3 == 0) {
                    btnDiff3_A.setBackgroundResource(R.drawable.border1);
                    btnDiff3_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck3 = 3;
                    objectCount++;
                    checkHint3 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_3_B_part1_normal /*2131230863*/:
                Toast.makeText(this, "3B", Toast.LENGTH_SHORT).show();
                if (numberCHeck3 == 0) {
                    btnDiff3_B.setBackgroundResource(R.drawable.border1);
                    btnDiff3_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck3 = 3;
                    objectCount++;
                    checkHint3 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_4_A_part1_normal /*2131230866*/:
                Toast.makeText(this, "4B", Toast.LENGTH_SHORT).show();
                if (numberCHeck4 == 0) {
                    btnDiff4_A.setBackgroundResource(R.drawable.border1);
                    btnDiff4_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck4 = 4;
                    objectCount++;
                    checkHint4 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_4_B_part1_normal /*2131230869*/:
                Toast.makeText(this, "4A", Toast.LENGTH_SHORT).show();
                if (numberCHeck4 == 0) {
                    btnDiff4_B.setBackgroundResource(R.drawable.border1);
                    btnDiff4_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck4 = 4;
                    objectCount++;
                    checkHint4 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_5_A_part1_normal /*2131230871*/:
                Toast.makeText(this, "5A", Toast.LENGTH_SHORT).show();
                if (numberCHeck5 == 0) {
                    btnDiff5_A.setBackgroundResource(R.drawable.border1);
                    btnDiff5_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck5 = 5;
                    objectCount++;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_5_B_part1_normal /*2131230873*/:
                Toast.makeText(this, "5B", Toast.LENGTH_SHORT).show();
                if (numberCHeck5 == 0) {
                    btnDiff5_B.setBackgroundResource(R.drawable.border1);
                    btnDiff5_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck5 = 5;
                    objectCount++;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void clicksHard(View view) {
        String str = "You Already found this one";
        switch (view.getId()) {
            case R.id.game_btn_dif_1_A_part1_hard /*2131230847*/:
                if (numberCHeck1 == 0) {
                    btnDiff1_A.setBackgroundResource(R.drawable.border1);
                    btnDiff1_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck1 = 1;
                    objectCount++;
                    checkHint1 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_1_B_part1_hard /*2131230850*/:
                if (numberCHeck1 == 0) {
                    btnDiff1_B.setBackgroundResource(R.drawable.border1);
                    btnDiff1_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck1 = 1;
                    objectCount++;
                    checkHint1 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_2_A_part1_hard /*2131230853*/:
                if (numberCHeck2 == 0) {
                    btnDiff2_A.setBackgroundResource(R.drawable.border1);
                    btnDiff2_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck2 = 2;
                    objectCount++;
                    checkHint2 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_2_B_part1_hard /*2131230856*/:
                if (numberCHeck2 == 0) {
                    btnDiff2_B.setBackgroundResource(R.drawable.border1);
                    btnDiff2_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck2 = 2;
                    objectCount++;
                    checkHint2 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_3_A_part1_hard /*2131230859*/:
                if (numberCHeck3 == 0) {
                    btnDiff3_A.setBackgroundResource(R.drawable.border1);
                    btnDiff3_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck3 = 3;
                    objectCount++;
                    checkHint3 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_3_B_part1_hard /*2131230862*/:
                if (numberCHeck3 == 0) {
                    btnDiff3_B.setBackgroundResource(R.drawable.border1);
                    btnDiff3_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck3 = 3;
                    objectCount++;
                    checkHint3 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_4_A_part1_hard /*2131230865*/:
                if (numberCHeck4 == 0) {
                    btnDiff4_A.setBackgroundResource(R.drawable.border1);
                    btnDiff4_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck4 = 4;
                    objectCount++;
                    checkHint4 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_4_B_part1_hard /*2131230868*/:
                if (numberCHeck4 == 0) {
                    btnDiff4_B.setBackgroundResource(R.drawable.border1);
                    btnDiff4_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck4 = 4;
                    objectCount++;
                    checkHint4 = 0;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_5_A_part1_hard /*2131230870*/:
                if (numberCHeck5 == 0) {
                    btnDiff5_A.setBackgroundResource(R.drawable.border1);
                    btnDiff5_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck5 = 5;
                    objectCount++;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                return;
            case R.id.game_btn_dif_5_B_part1_hard /*2131230872*/:
                if (numberCHeck5 == 0) {
                    btnDiff5_B.setBackgroundResource(R.drawable.border1);
                    btnDiff5_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck5 = 5;
                    objectCount++;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_6_A_part1_hard /*2131230874*/:
                if (numberCHeck6 == 0) {
                    btnDiff6_A.setBackgroundResource(R.drawable.border1);
                    btnDiff6_B.setBackgroundResource(R.drawable.border1);
                    numberCHeck6 = 6;
                    objectCount++;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.game_btn_dif_6_B_part1_hard /*2131230875*/:
                if (numberCHeck6 == 0) {
                    btnDiff6_B.setBackgroundResource(R.drawable.border1);
                    btnDiff6_A.setBackgroundResource(R.drawable.border1);
                    numberCHeck6 = 6;
                    objectCount++;
                    checkWin();
                    return;
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void buttonEasyFindViews() {
        btnDiff1_A = (Button) findViewById(R.id.game_btn_dif_1_A_part1_easy);
        btnDiff1_B = (Button) findViewById(R.id.game_btn_dif_1_B_part1_easy);
        btnDiff2_A = (Button) findViewById(R.id.game_btn_dif_2_A_part1_easy);
        btnDiff2_B = (Button) findViewById(R.id.game_btn_dif_2_B_part1_easy);
        btnDiff3_A = (Button) findViewById(R.id.game_btn_dif_3_A_part1_easy);
        btnDiff3_B = (Button) findViewById(R.id.game_btn_dif_3_B_part1_easy);
        btnDiff4_A = (Button) findViewById(R.id.game_btn_dif_4_A_part1_easy);
        btnDiff4_B = (Button) findViewById(R.id.game_btn_dif_4_B_part1_easy);
    }

    public void buttonNormalFindViews() {
        btnDiff1_A = (Button) findViewById(R.id.game_btn_dif_1_A_part1_normal);
        btnDiff1_B = (Button) findViewById(R.id.game_btn_dif_1_B_part1_normal);
        btnDiff2_A = (Button) findViewById(R.id.game_btn_dif_2_A_part1_normal);
        btnDiff2_B = (Button) findViewById(R.id.game_btn_dif_2_B_part1_normal);
        btnDiff3_A = (Button) findViewById(R.id.game_btn_dif_3_A_part1_normal);
        btnDiff3_B = (Button) findViewById(R.id.game_btn_dif_3_B_part1_normal);
        btnDiff4_A = (Button) findViewById(R.id.game_btn_dif_4_A_part1_normal);
        btnDiff4_B = (Button) findViewById(R.id.game_btn_dif_4_B_part1_normal);
        btnDiff5_A = (Button) findViewById(R.id.game_btn_dif_5_A_part1_normal);
        btnDiff5_B = (Button) findViewById(R.id.game_btn_dif_5_B_part1_normal);
    }

    public void buttonHardFindViews() {
        btnDiff1_A = (Button) findViewById(R.id.game_btn_dif_1_A_part1_hard);
        btnDiff1_B = (Button) findViewById(R.id.game_btn_dif_1_B_part1_hard);
        btnDiff2_A = (Button) findViewById(R.id.game_btn_dif_2_A_part1_hard);
        btnDiff2_B = (Button) findViewById(R.id.game_btn_dif_2_B_part1_hard);
        btnDiff3_A = (Button) findViewById(R.id.game_btn_dif_3_A_part1_hard);
        btnDiff3_B = (Button) findViewById(R.id.game_btn_dif_3_B_part1_hard);
        btnDiff4_A = (Button) findViewById(R.id.game_btn_dif_4_A_part1_hard);
        btnDiff4_B = (Button) findViewById(R.id.game_btn_dif_4_B_part1_hard);
        btnDiff5_A = (Button) findViewById(R.id.game_btn_dif_5_A_part1_hard);
        btnDiff5_B = (Button) findViewById(R.id.game_btn_dif_5_B_part1_hard);
        btnDiff6_A = (Button) findViewById(R.id.game_btn_dif_6_A_part1_hard);
        btnDiff6_B = (Button) findViewById(R.id.game_btn_dif_6_B_part1_hard);
    }

    public void randomSpotsEasy(int number) {
        String x = "x" + number;
        String y = "y" + number;
        set2.setHorizontalBias(R.id.game_btn_dif_1_A_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_a1", x));
        set2.setVerticalBias(R.id.game_btn_dif_1_A_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_a1", y));
        set2.setHorizontalBias(R.id.game_btn_dif_1_B_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_b1", x));
        set2.setVerticalBias(R.id.game_btn_dif_1_B_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_b1", y));

        set2.setHorizontalBias(R.id.game_btn_dif_2_A_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_a2", x));
        set2.setVerticalBias(R.id.game_btn_dif_2_A_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_a2", y));
        set2.setHorizontalBias(R.id.game_btn_dif_2_B_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_b2", x));
        set2.setVerticalBias(R.id.game_btn_dif_2_B_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_b2", y));

        set2.setHorizontalBias(R.id.game_btn_dif_3_A_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_a3", x));
        set2.setVerticalBias(R.id.game_btn_dif_3_A_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_a3", y));
        set2.setHorizontalBias(R.id.game_btn_dif_3_B_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_b3", x));
        set2.setVerticalBias(R.id.game_btn_dif_3_B_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_b3", y));

        set2.setHorizontalBias(R.id.game_btn_dif_4_A_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_a4", x));
        set2.setVerticalBias(R.id.game_btn_dif_4_A_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_a4", y));
        set2.setHorizontalBias(R.id.game_btn_dif_4_B_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_b4", x));
        set2.setVerticalBias(R.id.game_btn_dif_4_B_part1_easy, SplashActivity.coordinates_db.Read(table, "btn_b4", y));

        set2.applyTo(constraintLayout);
    }

    public void randomSpotsNormal(int number) {
        String x = "x" + number;
        String y = "y" + number;

        set2.setHorizontalBias(R.id.game_btn_dif_1_A_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_a1", x));
        set2.setVerticalBias(R.id.game_btn_dif_1_A_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_a1", y));
        set2.setHorizontalBias(R.id.game_btn_dif_1_B_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_b1", x));
        set2.setVerticalBias(R.id.game_btn_dif_1_B_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_b1", y));

        set2.setHorizontalBias(R.id.game_btn_dif_2_A_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_a2", x));
        set2.setVerticalBias(R.id.game_btn_dif_2_A_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_a2", y));
        set2.setHorizontalBias(R.id.game_btn_dif_2_B_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_b2", x));
        set2.setVerticalBias(R.id.game_btn_dif_2_B_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_b2", y));

        set2.setHorizontalBias(R.id.game_btn_dif_3_A_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_a3", x));
        set2.setVerticalBias(R.id.game_btn_dif_3_A_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_a3", y));
        set2.setHorizontalBias(R.id.game_btn_dif_3_B_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_b3", x));
        set2.setVerticalBias(R.id.game_btn_dif_3_B_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_b3", y));

        set2.setHorizontalBias(R.id.game_btn_dif_4_A_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_a4", x));
        set2.setVerticalBias(R.id.game_btn_dif_4_A_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_a4", y));
        set2.setHorizontalBias(R.id.game_btn_dif_4_B_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_b4", x));
        set2.setVerticalBias(R.id.game_btn_dif_4_B_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_b4", y));

        set2.setHorizontalBias(R.id.game_btn_dif_5_A_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_a5", x));
        set2.setVerticalBias(R.id.game_btn_dif_5_A_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_a5", y));
        set2.setHorizontalBias(R.id.game_btn_dif_5_B_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_b5", x));
        set2.setVerticalBias(R.id.game_btn_dif_5_B_part1_normal, SplashActivity.coordinates_db.Read(table, "btn_b5", y));

        set2.applyTo(constraintLayout);
    }

    public void randomSpotsHard(int number) {
        String x = "x" + number;
        String y = "y" + number;
        set2.setHorizontalBias(R.id.game_btn_dif_1_A_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_a1", x));
        set2.setVerticalBias(R.id.game_btn_dif_1_A_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_a1", y));
        set2.setHorizontalBias(R.id.game_btn_dif_1_B_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_b1", x));
        set2.setVerticalBias(R.id.game_btn_dif_1_B_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_b1", y));

        set2.setHorizontalBias(R.id.game_btn_dif_2_A_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_a2", x));
        set2.setVerticalBias(R.id.game_btn_dif_2_A_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_a2", y));
        set2.setHorizontalBias(R.id.game_btn_dif_2_B_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_b2", x));
        set2.setVerticalBias(R.id.game_btn_dif_2_B_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_b2", y));

        set2.setHorizontalBias(R.id.game_btn_dif_3_A_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_a3", x));
        set2.setVerticalBias(R.id.game_btn_dif_3_A_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_a3", y));
        set2.setHorizontalBias(R.id.game_btn_dif_3_B_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_b3", x));
        set2.setVerticalBias(R.id.game_btn_dif_3_B_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_b3", y));

        set2.setHorizontalBias(R.id.game_btn_dif_4_A_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_a4", x));
        set2.setVerticalBias(R.id.game_btn_dif_4_A_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_a4", y));
        set2.setHorizontalBias(R.id.game_btn_dif_4_B_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_b4", x));
        set2.setVerticalBias(R.id.game_btn_dif_4_B_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_b4", y));

        set2.setHorizontalBias(R.id.game_btn_dif_5_A_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_a5", x));
        set2.setVerticalBias(R.id.game_btn_dif_5_A_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_a5", y));
        set2.setHorizontalBias(R.id.game_btn_dif_5_B_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_b5", x));
        set2.setVerticalBias(R.id.game_btn_dif_5_B_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_b5", y));

        set2.setHorizontalBias(R.id.game_btn_dif_6_A_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_a6", x));
        set2.setVerticalBias(R.id.game_btn_dif_6_A_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_a6", y));
        set2.setHorizontalBias(R.id.game_btn_dif_6_B_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_b6", x));
        set2.setVerticalBias(R.id.game_btn_dif_6_B_part1_hard, SplashActivity.coordinates_db.Read(table, "btn_b6", y));

        set2.applyTo(constraintLayout);
    }

    public void setTimer(long tempTimerTime) {
        countDownTimer = new CountDownTimer(tempTimerTime, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                txtTimer.setText(getResources().getString(R.string.time_remains) + millisUntilFinished / 1000);
                timerTime = timerTime - 1000;
            }

            public void onFinish() {
                Toast.makeText(GameActivity.this, "Time's Up", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(GameActivity.this, ScoreActivity.class));
            }
        };
        countDownTimer.start();
    }

    public void setBG() {
        imageView_1 = (ImageView) findViewById(R.id.game_img1_part1_hard);
        try {
            imageView_1.setImageDrawable(Drawable.createFromStream(getAssets().open("photo_test2.jpeg"), null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkWin() {
        Intent intent = null;
        switch (objectCount) {
            case 4:
                if (mode == 1) {
                    CHECK_WIN_FOR_END_GAME = true;

                    popupEndGame();

                    if (mission >= 5) {
                        lastMissionFinishFlag = true;
                        Toast.makeText(this, "End Of Mission Series", Toast.LENGTH_SHORT).show();
                        sharedPreferences = getSharedPreferences(PREF_SETTINGS_KEY, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("mission_number", 0);
                        editor.apply();
                        startActivity(new Intent(GameActivity.this, MainActivity.class));
                        finish();
                    }
                    //using shared prefrences

                }
                break;

            case 5:
                if (mode == 2) {
                    CHECK_WIN_FOR_END_GAME = true;
                    popupEndGame();

                    if (mission >= 5) {
                        lastMissionFinishFlag = true;
                        Toast.makeText(this, "End Of Mission Series", Toast.LENGTH_SHORT).show();
                        sharedPreferences = getSharedPreferences(PREF_SETTINGS_KEY, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("mission_number", 0);
                        editor.apply();
                        startActivity(new Intent(GameActivity.this, MainActivity.class));
                        finish();
                    }
                    //using shared prefrences
                }
                break;

            case 6:
                if (mode >= 3) {
                    CHECK_WIN_FOR_END_GAME = true;

                    popupEndGame();

                    if (mission > 5) {
                        lastMissionFinishFlag = true;
                        Toast.makeText(this, "End Of Mission Series", Toast.LENGTH_SHORT).show();
                        sharedPreferences = getSharedPreferences(PREF_SETTINGS_KEY, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("mission_number", 0);
                        editor.apply();
                        startActivity(new Intent(GameActivity.this, MainActivity.class));
                        finish();
                    }
                    //using shared prefrences
                }
                break;
        }
    }

    public void refreshGame() {
        timerTime = 60000;

        switch (mode) {
            case 1:
                btnDiff1_A.setBackgroundResource(R.color.colorBg);
                btnDiff1_B.setBackgroundResource(R.color.colorBg);
                btnDiff2_A.setBackgroundResource(R.color.colorBg);
                btnDiff2_B.setBackgroundResource(R.color.colorBg);
                btnDiff3_A.setBackgroundResource(R.color.colorBg);
                btnDiff3_B.setBackgroundResource(R.color.colorBg);
                btnDiff4_A.setBackgroundResource(R.color.colorBg);
                btnDiff4_B.setBackgroundResource(R.color.colorBg);
                break;

            case 2:
                btnDiff1_A.setBackgroundResource(R.color.colorBg);
                btnDiff1_B.setBackgroundResource(R.color.colorBg);
                btnDiff2_A.setBackgroundResource(R.color.colorBg);
                btnDiff2_B.setBackgroundResource(R.color.colorBg);
                btnDiff3_A.setBackgroundResource(R.color.colorBg);
                btnDiff3_B.setBackgroundResource(R.color.colorBg);
                btnDiff4_A.setBackgroundResource(R.color.colorBg);
                btnDiff4_B.setBackgroundResource(R.color.colorBg);
                btnDiff5_A.setBackgroundResource(R.color.colorBg);
                btnDiff5_B.setBackgroundResource(R.color.colorBg);
                break;

            case 3:
                btnDiff1_A.setBackgroundResource(R.color.colorBg);
                btnDiff1_B.setBackgroundResource(R.color.colorBg);
                btnDiff2_A.setBackgroundResource(R.color.colorBg);
                btnDiff2_B.setBackgroundResource(R.color.colorBg);
                btnDiff3_A.setBackgroundResource(R.color.colorBg);
                btnDiff3_B.setBackgroundResource(R.color.colorBg);
                btnDiff4_A.setBackgroundResource(R.color.colorBg);
                btnDiff4_B.setBackgroundResource(R.color.colorBg);
                btnDiff5_A.setBackgroundResource(R.color.colorBg);
                btnDiff5_B.setBackgroundResource(R.color.colorBg);
                btnDiff6_A.setBackgroundResource(R.color.colorBg);
                btnDiff6_B.setBackgroundResource(R.color.colorBg);
                break;

            default:
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void popupEndGame() {
//        LL_ACTION_BAR = findViewById(R.id.game_action_bar);

        if (!CHECK_WIN_FOR_END_GAME) {
            tempTimerTime = timerTime + 1000;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            switch (mode) {
                case 1:

                    LL_EASY.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            LL_EASY.setEnabled(true);
                        }
                    }, 2000);

                    INTENT_GAME_TO_MISSION_LIST = new Intent(GameActivity.this, MainActivity.class);
                    btnNextEasyMOde.setEnabled(true);
                    btnHomeEasyMOde.setEnabled(true);

                    btnDiff1_A.setEnabled(false);
                    btnDiff1_B.setEnabled(false);
                    btnDiff2_A.setEnabled(false);
                    btnDiff2_B.setEnabled(false);
                    btnDiff3_A.setEnabled(false);
                    btnDiff3_B.setEnabled(false);
                    btnDiff4_A.setEnabled(false);
                    btnDiff4_B.setEnabled(false);

                    btnExitGame.setEnabled(false);
                    btnHint.setEnabled(false);


                    LL_EASY.setVisibility(View.VISIBLE);
                    LL_EASY.startAnimation(fadeIn);
                    btnHomeEasyMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            INTENT_GAME_TO_MISSION_LIST.putExtra("mission_list", 1);
//                            INTENT_GAME_TO_MISSION_LIST.putExtra("make_buttons_true", 999);
                            exitFromHomeBUtton = true;
                            startActivity(INTENT_GAME_TO_MISSION_LIST);
                            finish();
                        }
                    });
                    btnNextEasyMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recreate();
                        }
                    });
                    btnResEasyMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressedLimitCLick = 0;
                            LL_EASY.startAnimation(fadeOut);
                            LL_EASY.setVisibility(View.INVISIBLE);
                            btnNextEasyMOde.setEnabled(false);
                            btnHomeEasyMOde.setEnabled(false);


                            btnDiff1_A.setEnabled(true);
                            btnDiff1_B.setEnabled(true);
                            btnDiff2_A.setEnabled(true);
                            btnDiff2_B.setEnabled(true);
                            btnDiff3_A.setEnabled(true);
                            btnDiff3_B.setEnabled(true);
                            btnDiff4_A.setEnabled(true);
                            btnDiff4_B.setEnabled(true);

                            btnExitGame.setEnabled(true);
                            btnHint.setEnabled(true);


                            setTimer(tempTimerTime + 1000);
                            Toast.makeText(GameActivity.this, "Continue", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                case 2:

                    LL_NORMAL.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            LL_NORMAL.setEnabled(true);
                        }
                    }, 2000);

                    INTENT_GAME_TO_MISSION_LIST = new Intent(GameActivity.this, MainActivity.class);
                    btnNextNormalMOde.setEnabled(true);
                    btnHomeNormalMOde.setEnabled(true);

                    btnDiff1_A.setEnabled(false);
                    btnDiff1_B.setEnabled(false);
                    btnDiff2_A.setEnabled(false);
                    btnDiff2_B.setEnabled(false);
                    btnDiff3_A.setEnabled(false);
                    btnDiff3_B.setEnabled(false);
                    btnDiff4_A.setEnabled(false);
                    btnDiff4_B.setEnabled(false);
                    btnDiff5_A.setEnabled(false);
                    btnDiff5_B.setEnabled(false);

                    btnExitGame.setEnabled(false);
                    btnHint.setEnabled(false);

                    LL_NORMAL.setVisibility(View.VISIBLE);
                    LL_NORMAL.startAnimation(fadeIn);

                    btnHomeNormalMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            INTENT_GAME_TO_MISSION_LIST.putExtra("mission_list", 2);
//                            INTENT_GAME_TO_MISSION_LIST.putExtra("make_buttons_true", 999);
                            startActivity(INTENT_GAME_TO_MISSION_LIST);
                            finish();
                        }
                    });

                    btnNextNormalMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            recreate();
                        }
                    });

                    btnResNormalMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            onBackPressedLimitCLick = 0;
                            LL_NORMAL.startAnimation(fadeOut);
                            LL_NORMAL.setVisibility(View.INVISIBLE);
                            btnNextNormalMOde.setEnabled(false);
                            btnHomeNormalMOde.setEnabled(false);


                            btnDiff1_A.setEnabled(true);
                            btnDiff1_B.setEnabled(true);
                            btnDiff2_A.setEnabled(true);
                            btnDiff2_B.setEnabled(true);
                            btnDiff3_A.setEnabled(true);
                            btnDiff3_B.setEnabled(true);
                            btnDiff4_A.setEnabled(true);
                            btnDiff4_B.setEnabled(true);
                            btnDiff5_A.setEnabled(true);
                            btnDiff5_B.setEnabled(true);

                            btnExitGame.setEnabled(true);
                            btnHint.setEnabled(true);

                            setTimer(tempTimerTime + 1000);
                            Toast.makeText(GameActivity.this, "Continue", Toast.LENGTH_SHORT).show();

                        }
                    });
                    break;


                case 3:

                    LL_HARD.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            LL_HARD.setEnabled(true);
                        }
                    }, 2000);

                    INTENT_GAME_TO_MISSION_LIST = new Intent(GameActivity.this, MainActivity.class);
                    btnNextHardMOde.setEnabled(true);
                    btnHomeHardMOde.setEnabled(true);

                    btnDiff1_A.setEnabled(false);
                    btnDiff1_B.setEnabled(false);
                    btnDiff2_A.setEnabled(false);
                    btnDiff2_B.setEnabled(false);
                    btnDiff3_A.setEnabled(false);
                    btnDiff3_B.setEnabled(false);
                    btnDiff4_A.setEnabled(false);
                    btnDiff4_B.setEnabled(false);
                    btnDiff5_A.setEnabled(false);
                    btnDiff5_B.setEnabled(false);
                    btnDiff6_A.setEnabled(false);
                    btnDiff6_B.setEnabled(false);

                    btnExitGame.setEnabled(false);
                    btnHint.setEnabled(false);

                    LL_HARD.setVisibility(View.VISIBLE);
                    LL_HARD.startAnimation(fadeIn);

                    btnHomeHardMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            INTENT_GAME_TO_MISSION_LIST.putExtra("mission_list", 3);
//                            INTENT_GAME_TO_MISSION_LIST.putExtra("make_buttons_true", 999);
                            startActivity(INTENT_GAME_TO_MISSION_LIST);
                            finish();
                        }
                    });
                    btnNextHardMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            recreate();
                        }
                    });

                    btnResHardMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            onBackPressedLimitCLick = 0;
                            LL_HARD.startAnimation(fadeOut);
                            LL_HARD.setVisibility(View.INVISIBLE);
                            btnNextHardMOde.setEnabled(false);
                            btnHomeHardMOde.setEnabled(false);

                            btnDiff1_A.setEnabled(true);
                            btnDiff1_B.setEnabled(true);
                            btnDiff2_A.setEnabled(true);
                            btnDiff2_B.setEnabled(true);
                            btnDiff3_A.setEnabled(true);
                            btnDiff3_B.setEnabled(true);
                            btnDiff4_A.setEnabled(true);
                            btnDiff4_B.setEnabled(true);
                            btnDiff5_A.setEnabled(true);
                            btnDiff5_B.setEnabled(true);
                            btnDiff6_A.setEnabled(true);
                            btnDiff6_B.setEnabled(true);

                            btnExitGame.setEnabled(true);
                            btnHint.setEnabled(true);

                            setTimer(tempTimerTime + 1000);
                            Toast.makeText(GameActivity.this, "Continue", Toast.LENGTH_SHORT).show();

                        }
                    });

                    break;
            }
        }


        if (CHECK_WIN_FOR_END_GAME) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            countDownTimer = null;
            switch (mode) {
                case 1:
                    btnResEasyMOde.setVisibility(View.GONE);
                    btnNextMissionEasy.setVisibility(View.VISIBLE);

                    LL_EASY.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            LL_EASY.setEnabled(true);
                        }
                    }, 2000);

                    INTENT_GAME_TO_MISSION_LIST = new Intent(GameActivity.this, MainActivity.class);
                    btnNextEasyMOde.setEnabled(true);
                    btnHomeEasyMOde.setEnabled(true);

                    btnDiff1_A.setEnabled(false);
                    btnDiff1_B.setEnabled(false);
                    btnDiff2_A.setEnabled(false);
                    btnDiff2_B.setEnabled(false);
                    btnDiff3_A.setEnabled(false);
                    btnDiff3_B.setEnabled(false);
                    btnDiff4_A.setEnabled(false);
                    btnDiff4_B.setEnabled(false);

                    btnExitGame.setEnabled(false);
                    btnHint.setEnabled(false);


                    LL_EASY.setVisibility(View.VISIBLE);
                    LL_EASY.startAnimation(fadeIn);
                    btnHomeEasyMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            INTENT_GAME_TO_MISSION_LIST.putExtra("mission_list", 1);
//                            INTENT_GAME_TO_MISSION_LIST.putExtra("make_buttons_true", 999);
                            exitFromHomeBUtton = true;
                            sharedPreferences = getSharedPreferences(PREF_SETTINGS_KEY, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("mission_number", 0);
                            editor.apply();
                            startActivity(INTENT_GAME_TO_MISSION_LIST);
                            finish();
                        }
                    });
                    btnNextEasyMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recreate();
                        }
                    });
                    btnNextMissionEasy.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            missionToMissionFlag = true;
                            mission++;
                            sharedPreferences = getSharedPreferences(PREF_SETTINGS_KEY, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("mission_number", mission);
                            editor.apply();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    });
                    break;

                case 2:
                    btnResNormalMOde.setVisibility(View.GONE);
                    btnNextMissionNormal.setVisibility(View.VISIBLE);

                    LL_NORMAL.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            LL_NORMAL.setEnabled(true);
                        }
                    }, 2000);

                    INTENT_GAME_TO_MISSION_LIST = new Intent(GameActivity.this, MainActivity.class);
                    btnNextNormalMOde.setEnabled(true);
                    btnHomeNormalMOde.setEnabled(true);

                    btnDiff1_A.setEnabled(false);
                    btnDiff1_B.setEnabled(false);
                    btnDiff2_A.setEnabled(false);
                    btnDiff2_B.setEnabled(false);
                    btnDiff3_A.setEnabled(false);
                    btnDiff3_B.setEnabled(false);
                    btnDiff4_A.setEnabled(false);
                    btnDiff4_B.setEnabled(false);
                    btnDiff5_A.setEnabled(false);
                    btnDiff5_B.setEnabled(false);

                    btnExitGame.setEnabled(false);
                    btnHint.setEnabled(false);

                    LL_NORMAL.setVisibility(View.VISIBLE);
                    LL_NORMAL.startAnimation(fadeIn);

                    btnHomeNormalMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            INTENT_GAME_TO_MISSION_LIST.putExtra("mission_list", 2);
//                            INTENT_GAME_TO_MISSION_LIST.putExtra("make_buttons_true", 999);
                            exitFromHomeBUtton = true;
                            sharedPreferences = getSharedPreferences(PREF_SETTINGS_KEY, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("mission_number", 0);
                            editor.apply();
                            startActivity(INTENT_GAME_TO_MISSION_LIST);
                            finish();
                        }
                    });

                    btnNextNormalMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            finish();
                            recreate();
                        }
                    });

                    btnNextMissionNormal.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mission++;
                            missionToMissionFlag = true;
                            sharedPreferences = getSharedPreferences(PREF_SETTINGS_KEY, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("mission_number", mission);
                            editor.apply();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                        }
                    });
                    break;


                case 3:
                    btnResHardMOde.setVisibility(View.GONE);
                    btnNextMissionHard.setVisibility(View.VISIBLE);

                    LL_HARD.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            LL_HARD.setEnabled(true);
                        }
                    }, 2000);

                    INTENT_GAME_TO_MISSION_LIST = new Intent(GameActivity.this, MainActivity.class);
                    btnNextHardMOde.setEnabled(true);
                    btnHomeHardMOde.setEnabled(true);

                    btnDiff1_A.setEnabled(false);
                    btnDiff1_B.setEnabled(false);
                    btnDiff2_A.setEnabled(false);
                    btnDiff2_B.setEnabled(false);
                    btnDiff3_A.setEnabled(false);
                    btnDiff3_B.setEnabled(false);
                    btnDiff4_A.setEnabled(false);
                    btnDiff4_B.setEnabled(false);
                    btnDiff5_A.setEnabled(false);
                    btnDiff5_B.setEnabled(false);
                    btnDiff6_A.setEnabled(false);
                    btnDiff6_B.setEnabled(false);

                    btnExitGame.setEnabled(false);
                    btnHint.setEnabled(false);

                    LL_HARD.setVisibility(View.VISIBLE);
                    LL_HARD.startAnimation(fadeIn);

                    btnHomeHardMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            INTENT_GAME_TO_MISSION_LIST.putExtra("mission_list", 3);
//                            INTENT_GAME_TO_MISSION_LIST.putExtra("make_buttons_true", 999);
                            exitFromHomeBUtton = true;
                            sharedPreferences = getSharedPreferences(PREF_SETTINGS_KEY, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("mission_number", 0);
                            editor.apply();
                            startActivity(INTENT_GAME_TO_MISSION_LIST);
                            finish();
                        }
                    });
                    btnNextHardMOde.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            recreate();
                        }
                    });

                    btnNextMissionHard.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            missionToMissionFlag = true;
                            mission++;
                            sharedPreferences = getSharedPreferences(PREF_SETTINGS_KEY, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("mission_number", mission);
                            editor.apply();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                        }
                    });

                    break;
            }
        }


    }

    @Override
    public void onBackPressed() {
        gamePauseFlag = false;
        onBackPressedLimitCLick++;
        if (onBackPressedLimitCLick == 1) {
            popupEndGame();
        }
    }

    void setAnimation() {
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(300);

        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(100);
        fadeOut.setDuration(100);

        fadeIn_layout = AnimationUtils.loadAnimation(this, R.anim.fade_in_layout);

        fadeOut_layout = AnimationUtils.loadAnimation(this, R.anim.fade_out_layout);

    }

    @Override
    protected void onPause() {
        if (exitFromExitGameBUtton) {
            super.onPause();
            exitFromExitGameBUtton = false;
            finish();
        } else if (exitFromHomeBUtton) {
            super.onPause();
            exitFromHomeBUtton = false;
            finish();
        } else if (missionToMissionFlag) {
            super.onPause();
            exitFromHomeBUtton = false;
            finish();
        } else if (lastMissionFinishFlag) {
            lastMissionFinishFlag = false;
            super.onPause();
        } else {
            super.onPause();
            popupEndGame();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!gamePauseFlag) {
            popupEndGame();
            gamePauseFlag = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences = getSharedPreferences(PREF_SETTINGS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("mission_number", 0);
        editor.apply();
    }
}