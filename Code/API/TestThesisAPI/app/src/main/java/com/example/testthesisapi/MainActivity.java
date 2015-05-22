package com.example.testthesisapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;

import apis.LoyaltyPointAPI;
import models.AchievedEvent;


public class MainActivity extends ActionBarActivity {

    public static final String LOGIN_STATE = "login_state";
    public static final String TOKEN = "token";

    TextView calculatePointResult, updatePointResult;
    Button calculatePointBtn, updatePointBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculatePointResult = (TextView) findViewById(R.id.calculatePointResult);
        updatePointResult = (TextView) findViewById(R.id.updatePointResult);

        calculatePointBtn = (Button) findViewById(R.id.calculatePointBtn);
        calculatePointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoyaltyPointAPI request = new LoyaltyPointAPI();
                request.calculatePoint(MainActivity.this, null, null, 0, new LoyaltyPointAPI.OnCalculatePointResult() {
                    @Override
                    public void onSuccess(ArrayList<AchievedEvent> result, float totalPoint) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_LONG).show();
                                calculatePointResult.setText("Calculate Point Successfully");
                            }
                        });
                    }

                    @Override
                    public void onError(final String error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                                calculatePointResult.setText("Calculate Point Failed: " + error);
                            }
                        });
                    }
                });
            }
        });


        updatePointBtn = (Button) findViewById(R.id.updatePointBtn);
        updatePointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoyaltyPointAPI request = new LoyaltyPointAPI();
                request.updatePoint(MainActivity.this, null, null, 0, new LoyaltyPointAPI.OnUpdatePointResult() {
                    @Override
                    public void onSuccess(ArrayList<AchievedEvent> result, float totalPoint) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_LONG).show();
                                updatePointResult.setText("Update Point Successfully");
                            }
                        });
                    }

                    @Override
                    public void onError(final String error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                                updatePointResult.setText("Update Point Failed: " + error);
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
