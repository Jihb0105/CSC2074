package com.bignerdranch.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Trace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private TextView mQuestionAnswered;
    private TextView mTotalScore;
    private TextView mTotalCheat;
    private int ansCorrect;
    private int totalQuestion;
    private int score;
    private int QnAnswered;
    private int cheatCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideSystemUI(getWindow());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Question Answered
        mQuestionAnswered = (TextView) findViewById(R.id.question_answered);
        QnAnswered = getIntent().getIntExtra("QUESTION_ANSWERED", 0);
        totalQuestion = getIntent().getIntExtra("TOTAL_QUESTIONS",0);
        mQuestionAnswered.setText("Total Question Answered: " + QnAnswered+ "/" + totalQuestion);

        //Total score in percentage
        mTotalScore = (TextView) findViewById(R.id.total_score_percentage);
        ansCorrect = getIntent().getIntExtra("RIGHT_ANSWER_COUNT",0);
        score = (ansCorrect*100)/totalQuestion;
        mTotalScore.setText("Total Score: "+ score + "%");

        //Total cheat count
        mTotalCheat = (TextView) findViewById(R.id.total_cheat);
        Intent cIntent = new Intent(ResultActivity.this, CheatActivity.class);
        cheatCount = getIntent().getIntExtra("CHEAT_COUNT", 0);
        mTotalCheat.setText("Total cheat used: " + cheatCount);

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        super.onWindowFocusChanged(hasFocus);
        hideSystemUI(getWindow());
    }

    public static void hideSystemUI(Window window) {
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }

}
