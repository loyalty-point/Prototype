package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.DatePicker;


import java.util.Calendar;


public class DatePickerFragment extends DialogFragment {

    private String type;

    public DatePickerFragment(String type) {
        this.type = type;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        if (type.equals("CreateEventActivity")) {
            return new DatePickerDialog(getActivity(), (CreateEventActivity) getActivity(), year, month, day);
        } else if (type.equals("EditEventActivity")) {
            return new DatePickerDialog(getActivity(), (EditEventActivity) getActivity(), year, month, day);
        }
        return null;
    }
}
