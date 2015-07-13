package com.example.testthesisapi;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import thesis.loyaltypointapi.apis.LoyaltyPointAPI;
import thesis.loyaltypointapi.models.AchievedEvent;
import thesis.loyaltypointapi.models.Card;
import thesis.loyaltypointapi.models.Customer;
import thesis.loyaltypointapi.models.Event;
import thesis.loyaltypointapi.models.Shop;


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
                request.calculatePoint(MainActivity.this, "55660ac566e08", "55660a9a9e028tandouser", null, 1000, new LoyaltyPointAPI.OnCalculatePointResult() {
                    @Override
                    public void onSuccess(final ArrayList<AchievedEvent> listAchievedEvents, int pointFromMoney, final int totalPoint) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_LONG).show();
                                calculatePointResult.setText("Calculate Point Successfully");

                                LoyaltyPointAPI request = new LoyaltyPointAPI();
                                request.updatePoint(MainActivity.this, "55660ac566e08", "55660a9a9e028tandouser", listAchievedEvents, totalPoint, new LoyaltyPointAPI.OnUpdatePointResult() {
                                    @Override
                                    public void onSuccess() {
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


        /*// Test get list shops
        LoyaltyPointAPI request = new LoyaltyPointAPI();
        request.getListShops(this, new LoyaltyPointAPI.OnGetListShopsResult() {
            @Override
            public void onSuccess(ArrayList<Shop> litShops) {
                Log.e("getListShops", "successfully");
            }

            @Override
            public void onError(String error) {
                Log.e("getListShops", error);
            }
        });*/


        /*// Test get list cards
        LoyaltyPointAPI request = new LoyaltyPointAPI();
        request.getListCards(this, new LoyaltyPointAPI.OnGetListCardsResult() {
            @Override
            public void onSuccess(ArrayList<Card> litCards) {
                Log.e("getListCards", "successfully");
            }

            @Override
            public void onError(String error) {
                Log.e("getListCards", error);
            }
        });*/


        /*// Test get list events
        LoyaltyPointAPI request = new LoyaltyPointAPI();
        request.getListEvents(MainActivity.this, "55660ac566e08", new LoyaltyPointAPI.OnGetListEventsResult() {
            @Override
            public void onSuccess(ArrayList<Event> listEvents) {
                Log.e("getListEvents", "successfully");
            }

            @Override
            public void onError(String error) {
                Log.e("getListEvents", error);
            }
        });*/


        /*// Test get list customers
        LoyaltyPointAPI request = new LoyaltyPointAPI();
        request.getListCustomers(MainActivity.this, "55660ac566e08", new LoyaltyPointAPI.OnGetListCustomersResult() {
            @Override
            public void onSuccess(ArrayList<Customer> listCustomers) {
                Log.e("getListCustomers", "successfully");
            }

            @Override
            public void onError(String error) {
                Log.e("getListCustomers", error);
            }
        });*/

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
