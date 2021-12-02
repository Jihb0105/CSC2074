package com.bignerdranch.android.geoquiz;

public class Question {

    private int mTextResid;
    private boolean mAnswerTrue;

    //Constructor
    public Question(int mTextResid, boolean mAnswerTrue) {
        this.mTextResid = mTextResid;
        this.mAnswerTrue = mAnswerTrue;
    }

    //Setter/Getter
    public int getmTextResid() {
        return mTextResid;
    }

    public void setmTextResid(int mTextResid) {
        this.mTextResid = mTextResid;
    }

    public boolean ismAnswerTrue() {
        return mAnswerTrue;
    }

    public void setmAnswerTrue(boolean mAnswerTrue) {
        this.mAnswerTrue = mAnswerTrue;
    }


}
