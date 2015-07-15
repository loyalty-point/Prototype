package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.thesis.dont.loyaltypointadmin.R;

public class CreateEvent2Fragment extends BaseEventCreateFragment {

    EditText ratioEdtText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event2, container, false);
    }

    public CreateEvent2Fragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ratioEdtText = (EditText)getActivity().findViewById(R.id.ratioEdtText);
    }

    //check form was filled or not
    @Override
    public String isFilledIn() {
        if(ratioEdtText.getText().toString().equals("")){
            return "ratio is empty!";
        }
        return "";
    }

    //get value from edit text
    @Override
    public String getRatio() {
        return ratioEdtText.getText().toString();
    }

    public void setRatio(String ratio){
        ratioEdtText.setText(ratio);
    }
}
