package com.bignerdranch.android.geoquiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import static android.widget.Toast.LENGTH_SHORT;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String QUESTIONS_ANSWERED = "Answered Question";
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mPrevButton;
    private Button mNextButton;
    private Button mResetButton;
    private Button mCheatButton;
    private Button mResultButton;
    private TextView mQuestionTextView;
    private ImageButton mImagePrevButton;
    private ImageButton mimageNextButton;
    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private TextView mTokensTextView;
    private int mTokens = 0;
    private int totalCheat = 3;
    private int cheatCounter = 0;
    private int QnAnswered = 0;

    //Calling Question.java constructor and create an array of Question Objects
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    //Boolean array to keep track of which question are answered
    private boolean[] mQuestionAnswered = new boolean[mQuestionBank.length];
    private int CorrectAns = 0;
    private int IncorrectAns = 0;
    private int Score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideSystemUI(getWindow());
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            //Save whether question has been answered. Do not let user answer again
            mQuestionAnswered = savedInstanceState.getBooleanArray(QUESTIONS_ANSWERED);
        }

        //Questions
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        //Lecture 3: Challenge 1: Adding Listener to TextView
        //Done so user could click the question to the next question
        /*mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                //int question = mQuestionBank[mCurrentIndex].getmTextResid();
                //mQuestionTextView.setText(question);
                updateQuestion();
            }
        });*/

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting Toast
                /*Toast ttoast = Toast.makeText(QuizActivity.this,
                                R.string.correct_toast,
                                Toast.LENGTH_SHORT);*/
                //Lecture 2: Challenge: Customizing the Toast
                /*ttoast.setGravity(Gravity.TOP,0,150);
                ttoast.show();*/

                //Calling checkAnswer
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting Toast
                /*Toast ftoast = Toast.makeText(QuizActivity.this,
                              R.string.incorrect_toast,
                              Toast.LENGTH_SHORT);*/
                //Lecture 2: Challenge: Customizing the Toast
                /*ftoast.setGravity(Gravity.TOP,0,150);
                ftoast.show();*/

                //Calling checkAnswer
                checkAnswer(false);
            }
        });

        //Challenge: Adding a Previous Button
        mPrevButton = (Button) findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                //int question = mQuestionBank[mCurrentIndex].getmTextResid();
                //mQuestionTextView.setText(question);
                mIsCheater = false;
                updateQuestion();
            }
        });

        /*Lecture 3: Challenge 3: Adding Image Button
        mImagePrevButton = (ImageButton) findViewById(R.id.previous_button_image);
        mImagePrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex-1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        mimageNextButton = (ImageButton) findViewById(R.id.next_button_image);
        mimageNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.length;
                updateQuestion();
            }
        });*/

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start CheatActivity
                //Challenge: Limited Cheats
                mTokensTextView = (TextView) findViewById(R.id.tokens_text_view);
                if(mTokens >= 4) {
                    //Ensure that the cheat button can't be used after 3 tries
                    mCheatButton.setEnabled(false);
                    //Setting toast to show that can't cheat anymore
                    Toast.makeText(QuizActivity.this, R.string.tokens, Toast.LENGTH_SHORT).show();
                }else{
                    //Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                    boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismAnswerTrue();
                    Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                    startActivityForResult(intent, REQUEST_CODE_CHEAT);
                    //startActivity(cIntent);
                }

            }
        });

        //Challenge: Adding Reset Button
        mResetButton = (Button) findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        //Challenge: Adding Result Button
        mResultButton = (Button) findViewById(R.id.result_button);
        mResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result();
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            //Display number of cheat token left
            mTokens++;
            mTokensTextView.setText((totalCheat - mTokens) + " tokens left");
        }
    }

    //Method creating to reduce the number of line in the code by calling the method to the buttons
    private void updateQuestion() {
        if(mQuestionBank.length == QnAnswered) {
            //Setting GeoQuiz to ends after all questions are answered
            AlertDialog.Builder gameOver = new AlertDialog.Builder(this);
            gameOver.setTitle("GAME OVER");
            gameOver.setMessage("Thank you for answering");
            gameOver.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    reset();
                }
            });
            gameOver.setNegativeButton("RESULT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(mQuestionBank.length == QnAnswered){
                        result();
                    }
                }
            });
            gameOver.show();
        }
            //Allow buttons to work again after pressing true or false
            mTrueButton.setEnabled(!mQuestionAnswered[mCurrentIndex]);
            mFalseButton.setEnabled(!mQuestionAnswered[mCurrentIndex]);
            int question = mQuestionBank[mCurrentIndex].getmTextResid();
            mQuestionTextView.setText(question);
        }


    //Check Answer
    private void checkAnswer(boolean userAnsTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismAnswerTrue();

        int messageResID = 0;
        if (mIsCheater) {
            messageResID = R.string.judgement_toast;
        } else {
            if (userAnsTrue == answerIsTrue) {
                messageResID = R.string.correct_toast;
                CorrectAns++;
            } else {
                messageResID = R.string.incorrect_toast;
                IncorrectAns++;
            }
        }
        //Calculate Question Answered
        QnAnswered++;
        //Challenge: Preventing Repeat Answers
        //To specify that the question in that position has been answered
        mQuestionAnswered[mCurrentIndex] = true;
        //set buttons to be disabled after question is answered
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
        Toast.makeText(this, messageResID, Toast.LENGTH_SHORT).show();
        percent();
    }

    //Challenge: Graded Quiz
    private void percent() {
        Score = CorrectAns + IncorrectAns;
        if (mQuestionBank.length == Score) {
            int percentage = (CorrectAns * 100) / mQuestionBank.length;
            Toast.makeText(QuizActivity.this, getString(R.string.percent_toast, percentage), Toast.LENGTH_LONG).show();
        }
    }

    private void result(){
        Intent rIntent = new Intent(QuizActivity.this, ResultActivity.class);
        cheatCounter = mTokens;
        rIntent.putExtra("TOTAL_QUESTIONS", mQuestionBank.length);
        rIntent.putExtra("RIGHT_ANSWER_COUNT", CorrectAns);
        rIntent.putExtra("QUESTION_ANSWERED", QnAnswered);
        rIntent.putExtra("CHEAT_COUNT", cheatCounter);
        startActivity(rIntent);
    }

    private void reset(){
        Intent reIntent = new Intent(QuizActivity.this, SplashActivity.class);
        finish();
        startActivity(reIntent);
        Toast.makeText(QuizActivity.this, R.string.reset_toast, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPuase() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        //Save questions that have been answered
        savedInstanceState.putBooleanArray(QUESTIONS_ANSWERED, mQuestionAnswered);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

}