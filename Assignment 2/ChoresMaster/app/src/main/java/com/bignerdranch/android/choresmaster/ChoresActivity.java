package com.bignerdranch.android.choresmaster;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;


public class ChoresActivity extends CombinedFragmentActivity {

    public static final String CHORESIDEXTRA = "chores_id";

    //telling ChoresFragment which chores to display
    public static Intent newIntent(Context packageContext, UUID cId){
        Intent intent = new Intent(packageContext, ChoresActivity.class);
        intent.putExtra(CHORESIDEXTRA, cId);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        UUID choresId = (UUID) getIntent().getSerializableExtra(CHORESIDEXTRA);
        return ChoresFragment.newInstance(choresId);
    }
}
