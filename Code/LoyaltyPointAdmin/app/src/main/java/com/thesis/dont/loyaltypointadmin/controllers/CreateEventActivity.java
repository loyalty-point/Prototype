package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Event;
import com.thesis.dont.loyaltypointadmin.models.EventModel;

import java.util.Calendar;

public class CreateEventActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {
    private static final String ARG_SHOPID = "shop_id";
    ButtonFlat dateStartButton, dateEndButton;
    ButtonRectangle createButton, cancelBtn;
    Spinner category;
    EditText eventName, description;
    private String shopId;
    boolean isStartDatePicker = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        shopId  = getIntent().getStringExtra(ARG_SHOPID);
        Calendar c = Calendar.getInstance();
        category = (Spinner) findViewById(R.id.eventcategory);
        /** create value to list and add event change frangment**/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.eventCategory, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //Change frangment when choose the category
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Create new fragment and transaction
                if (position == 1) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.fragment_container, new CreateEvent2Fragment());
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.commit();
                } else {
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
        eventName = (EditText) findViewById(R.id.eventname);
        description = (EditText) findViewById(R.id.descriptionEdtText);
        dateStartButton = (ButtonFlat) findViewById(R.id.startDateButton);
        dateEndButton = (ButtonFlat) findViewById(R.id.endDateButton);
        createButton = (ButtonRectangle) findViewById(R.id.createEventBtn);
        cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);

        dateStartButton.setText("start date\n" + c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
        dateEndButton.setText("end date\n" + c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
        //Choose day by show the DatePickerFragment
        dateStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                isStartDatePicker = true;
            }
        });
        //Choose day by show the DatePickerFragment
        dateEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                isStartDatePicker = false;
            }
        });
        //Add event to database
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseEventCreateFragment fragment = (BaseEventCreateFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if (eventName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Event name is empty!", Toast.LENGTH_LONG).show();
                } else if (!fragment.isFilledIn().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), fragment.isFilledIn(), Toast.LENGTH_LONG).show();
                } else {
                    Event event = new Event("", category.getSelectedItemPosition(), eventName.getText().toString(),
                            dateStartButton.getText().toString().substring(11), dateEndButton.getText().toString().substring(9),
                            description.getText().toString(), fragment.getBarCode(), fragment.getGoodsName(), Float.parseFloat(fragment.getRatio()),
                            Integer.parseInt(fragment.getNumber()), Integer.parseInt(fragment.getPoint()), "");
                    EventModel.addEvent(event, shopId, new EventModel.OnAddEventResult() {
                        @Override
                        public void onSuccess() {
                            Intent i = new Intent(CreateEventActivity.this, ShopDetailActivity.class);
                            startActivity(i);
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("false", error);
                        }
                    });
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateEventActivity.this, ShopDetailActivity.class);
                startActivity(i);
            }
        });
        //Add fragment to fragment container when create activity
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            CreateEvent1Fragment firstFragment = new CreateEvent1Fragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }
    }
    //call back when pick date
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (isStartDatePicker) {
            dateStartButton.setText("start date\n" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        } else {
            dateEndButton.setText("end date\n" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }

    }
    //call back when scan bar code successfully
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
