package com.bignerdranch.android.choresmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class ChoresFragment extends Fragment{
    private static final String ARGCHOREID = "chore_id";
    private static final String DATEDIALOG = "DialogDate";
    private static final String TIMEDIALOG = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Chores cChores;
    private EditText cTitle;
    private TextView cDateText;
    private TextView cTimeText;
    private CheckBox cCompletion;

    public static ChoresFragment newInstance(UUID choresId){
        Bundle arg = new Bundle();
        arg.putSerializable(ARGCHOREID, choresId);
        ChoresFragment frag = new ChoresFragment();
        frag.setArguments(arg);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //To show menu bar
        //getting extra from ChoresActivity
        UUID choresId = (UUID) getArguments().getSerializable(ARGCHOREID);
        cChores = ChoresList.get(getActivity()).getChore(choresId);
    }
    //Pushing Updates
    @Override
    public void onPause(){
        super.onPause();

        ChoresList.get(getActivity())
                .updateChores(cChores);
    }
    //Inflate menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu , inflater);
        inflater.inflate(R.menu.delete_chores_menu, menu);
    }
    //Respond to menuItem selection
    //Adding the delete image in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete_chores:
                ChoresList.get(getActivity()).deleteChores(cChores); //Deleting chores from list
                //Back to ChoresListActivity by Intent
                Intent i = new Intent(getActivity(), ChoresListActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //onCreateView method called when Fragment should create its View object hierarchy,
    //through XML Layout inflation
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Defining the xml file for the fragment
        View v = inflater.inflate(R.layout.add_fragment_chores, container, false);

        //Wiring up the widgets
        //Adding Chores
        cTitle = (EditText) v.findViewById(R.id.chores_title);
        cTitle.setText(cChores.getTitle());
        //to enable the app to type in the EditText
        cTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cChores.setTitle(s.toString()); //Set the chores title after typing in the EditText
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Setting Date
        cDateText = (TextView) v.findViewById(R.id.set_date);
        updateDate();
        cDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fmd = getFragmentManager();
                DatePicker dpFrag = DatePicker.newInstance(cChores.getDate());
                dpFrag.setTargetFragment(ChoresFragment.this, REQUEST_DATE);
                dpFrag.show(fmd,DATEDIALOG);

            }
        });

        //Setting Time
        cTimeText = (TextView) v.findViewById(R.id.set_time);
        updateTime();
        cTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fmt = getFragmentManager();
                TimePickerClass tpFrag = TimePickerClass.newInstance(cChores.getDate());
                tpFrag.setTargetFragment(ChoresFragment.this, REQUEST_TIME);
                tpFrag.show(fmt,TIMEDIALOG);
            }
        });


        //Completion checkbox
        cCompletion = (CheckBox) v.findViewById(R.id.chores_completed);
        cCompletion.setChecked(cChores.isCompleted());
        cCompletion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isCompleted){
                cChores.setCompleted(isCompleted); //set the chores to completed after checking the box
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode){
            case REQUEST_DATE:
                Date date = (Date) data.getSerializableExtra(DatePicker.DATEEXTRA);
                cChores.setDate(date);
                updateDate();
                break;
            case REQUEST_TIME:
                Date time = (Date) data.getSerializableExtra(TimePickerClass.TIMEEXTRA);
                cChores.setDate(time);
                updateTime();
            default:
                break;
        }
    }
    //Setting up the date
    private void updateDate() {
        java.text.DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); //format the date picker output
        String date = dateFormat.format( cChores.getDate()).toString(); //changing it to a string
        cDateText.setText(date); // calls the string format date
    }
    //Setting up time
    private void updateTime(){
        Calendar cldr = Calendar.getInstance();
        cldr.setTime(cChores.getDate());
        //Setting AM/PM
        String amPM;
        if (cldr.get(Calendar.HOUR_OF_DAY) >= 12){
            amPM = "PM";
        }else{
            amPM = "AM";
        }

        String str = String.valueOf(cldr.get(Calendar.HOUR_OF_DAY))
                + ':' + String.valueOf(cldr.get(Calendar.MINUTE))
                + ' ' + amPM;
        cTimeText.setText(str);
    }

}
