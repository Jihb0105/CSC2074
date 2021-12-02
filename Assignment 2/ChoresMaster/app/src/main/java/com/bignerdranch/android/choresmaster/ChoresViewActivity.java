package com.bignerdranch.android.choresmaster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class ChoresViewActivity extends AppCompatActivity{
    private static final String CHORESIDEXTRA =
            "com.bignerdranch.android.chores_to_do_list.chores_id";

    private ViewPager cView;
    private List<Chores> cChores;

    //Creating new intent
    public static Intent newIntent(Context packageContext, UUID choreId){
        Intent it = new Intent(packageContext, ChoresViewActivity.class);
        it.putExtra(CHORESIDEXTRA, choreId);
        return it;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        //retrieve extra from new intent
        UUID choresId = (UUID) getIntent().getSerializableExtra(CHORESIDEXTRA);

        //Setting up pager adapter
        cChores = ChoresList.get(this).getChores();
        cView = (ViewPager) findViewById(R.id.view_pager);

        FragmentManager fm = getSupportFragmentManager();
        //FragmentStatePagerAdapter agent managing the conversaiton with viewpager
        cView.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Chores chores = cChores.get(position);
                return ChoresFragment.newInstance(chores.getId());
            }

            @Override
            public int getCount() {
                return cChores.size();
            }
        });

        //To stop entering the 1st page in the list
        for (int i = 0; i < cChores.size(); i++){
            if(cChores.get(i).getId().equals(choresId)){
                cView.setCurrentItem(i);
                break;
            }
        }

    }
}
