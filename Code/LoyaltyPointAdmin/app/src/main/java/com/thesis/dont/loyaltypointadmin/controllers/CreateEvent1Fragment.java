package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.thesis.dont.loyaltypointadmin.R;

public class CreateEvent1Fragment extends BaseEventCreateFragment {

    //    private OnFragmentInteractionListener mListener;
    ButtonRectangle barcodeScannerBtn;
    EditText goodsNameEdtText, barcodeEdtText, numberEdtText, pointEdtText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event1, container, false);
        barcodeEdtText = (EditText) view.findViewById(R.id.barcodeEdtText);
        goodsNameEdtText = (EditText) view.findViewById(R.id.nameEdtText);
        numberEdtText = (EditText) view.findViewById(R.id.numberEdtText);
        pointEdtText = (EditText) view.findViewById(R.id.pointEdtText);
        barcodeScannerBtn = (ButtonRectangle) view.findViewById(R.id.barcodeScannerBtn);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        barcodeScannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
                scanIntegrator.initiateScan();
            }
        });
    }

    //call back when scan the bar code successfully
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            barcodeEdtText.setText(scanningResult.getContents());
        } else {

        }
    }
    //check form was filled or not
    @Override
    public String isFilledIn() {
        if (barcodeEdtText.getText().toString().equals("")) {
            return "bar code is empty!";
        } else if (goodsNameEdtText.getText().toString().equals("")) {
            return "goods name is empty!";
        } else if (numberEdtText.getText().toString().equals("")) {
            return "number is empty!";
        } else if (pointEdtText.getText().toString().equals("")) {
            return "point is empty!";
        } else {
            return "";
        }
    }
    //get value from form
    @Override
    public String getBarCode() {
        return barcodeEdtText.getText().toString();
    }

    @Override
    public String getGoodsName() {
        return goodsNameEdtText.getText().toString();
    }

    @Override
    public String getNumber() {
        return numberEdtText.getText().toString();
    }

    @Override
    public String getPoint() {
        return pointEdtText.getText().toString();
    }

    public void setBarCode(String barCode) {
        barcodeEdtText.setText(barCode);
    }

    public void setGoodsName(String goodsName){
        goodsNameEdtText.setText(goodsName);
    }

    public void setNumber(String number){
        numberEdtText.setText(number);
    }

    public void setPoint(String point){
        pointEdtText.setText(point);
    }
}
