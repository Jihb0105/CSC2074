package com.bignerdranch.android.choresmaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerClass extends DialogFragment{
    private static final String TIMEARG = "date";
    public static final String TIMEEXTRA =
            "com.bignerdranch.android.criminalintent.time";
    private Calendar cldr;
    private TimePicker cTime;
    private Date cDate;

    public static TimePickerClass newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(TIMEARG, date);

        TimePickerClass frag = new TimePickerClass();
        frag.setArguments(args);
        return frag;
    }

    //passing data to ChoresFragment
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        cDate = (Date) getArguments().getSerializable(TIMEARG);

        //Set Current time
        cldr = Calendar.getInstance();
        cldr.setTime(cDate);

        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int min = cldr.get(Calendar.MINUTE);

        //enable datepicker to use date_picker_layout
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.time_picker_layout, null);

        cTime = (TimePicker) v.findViewById(R.id.time_picker_dialog);
        cTime.setHour(hour);
        cTime.setMinute(min);

        //Create AlertDialog for the Datepicker
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = cTime.getHour();
                        int min = cTime.getMinute();
                        Date time = new GregorianCalendar(0,0,0, hour, min).getTime();
                        sendResult(Activity.RESULT_OK, time);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(TIMEEXTRA, date);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
