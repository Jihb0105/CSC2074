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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePicker extends DialogFragment {

    private static final String DATEARG = "date";
    public static final String DATEEXTRA =
            "com.bignerdranch.android.criminalintent.date";
    private Calendar cldr;
    private android.widget.DatePicker cDate;

    public static DatePicker newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(DATEARG, date);

        DatePicker frag = new DatePicker();
        frag.setArguments(args);
        return frag;
    }

    //passing data to ChoresFragment
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        Date date = (Date) getArguments().getSerializable(DATEARG);
        //Set Current time
        cldr = Calendar.getInstance();
        cldr.setTime(date);
        int year = cldr.get(Calendar.YEAR);
        int month = cldr.get(Calendar.MONTH);
        int day = cldr.get(Calendar.DAY_OF_MONTH);

        //enable datepicker to use date_picker_layout
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.date_picker_layout, null);

        cDate = (android.widget.DatePicker) v.findViewById(R.id.date_picker);
        cDate.init(year, month, day, null);

        //Create AlertDialog for the Datepicker
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = cDate.getYear();
                        int month = cDate.getMonth();
                        int day = cDate.getDayOfMonth();
                        Date date = new GregorianCalendar(year, month, day).getTime();
                        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
                        String StringDate = format.format(date.getTime());
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }
    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(DATEEXTRA, date);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
