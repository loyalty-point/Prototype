package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.gc.materialdesign.views.ButtonFlat;
import com.thesis.dont.loyaltypointadmin.R;

import java.util.Calendar;

public class CreateEventActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {
    ButtonFlat dateStartButton, dateEndButton;
    Spinner category;
    boolean isStartDatePicker = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Calendar c = Calendar.getInstance();
        category = (Spinner) findViewById(R.id.eventcategory);
        /** create value to list and add event change frangment**/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.eventCategory, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Create new fragment and transaction
                if(position == 1) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.fragment_container, new CreateEvent2Fragment());
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.commit();
                }else {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.fragment_container, new CreateEvent1Fragment());
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.commit();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /****/
        dateStartButton = (ButtonFlat) findViewById(R.id.startDateButton);
        dateEndButton = (ButtonFlat) findViewById(R.id.endDateButton);

        dateStartButton.setText("start date\n" + c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
        dateEndButton.setText("end date\n" + c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));

        dateStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                isStartDatePicker = true;
            }
        });

        dateEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                isStartDatePicker = false;
            }
        });

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            CreateEvent1Fragment firstFragment = new CreateEvent1Fragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (isStartDatePicker) {
            dateStartButton.setText("start date\n" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        } else {
            dateEndButton.setText("end date\n" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }

    }


//    @Override
//    public void onFragmentInteraction(Uri uri) {
//        Log.e("test","test");
//    }
}
