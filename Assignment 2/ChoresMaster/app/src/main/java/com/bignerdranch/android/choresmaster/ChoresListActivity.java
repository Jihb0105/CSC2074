package com.bignerdranch.android.choresmaster;

import android.support.v4.app.Fragment;

public class ChoresListActivity extends CombinedFragmentActivity{

    @Override
    protected Fragment createFragment(){
        return new ChoresListFragment();
    }

}
