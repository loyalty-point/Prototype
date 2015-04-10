package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thesis.dont.loyaltypointadmin.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserAwardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserAwardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserAwardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_award, container, false);
    }
}
