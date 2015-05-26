package com.example.testthesisapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import apis.LoyaltyPointAPI.*;
import controllers.Preconditions;
import models.GlobalParams;


public class GetTokenActivity extends ActionBarActivity {

    OnGetTokenResult mOnGetTokenResult;
    int mKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_token);

        Intent i = getIntent();
        if(i != null) {
            mKey = i.getIntExtra(GlobalParams.GET_TOKEN_CALLBACK_KEY, 0);
            mOnGetTokenResult = GlobalParams.mapCallbacks.get(mKey);
            if(mOnGetTokenResult == null) {
                finish();
                return;
            }
        }

        //Intent intent = getPackageManager().getLaunchIntentForPackage(GlobalParams.appPackageName);
        Intent intent = new Intent("com.thesis.dont.loyaltypointadmin.LOGIN");
        intent.putExtra(GlobalParams.FROM, 1);
        startActivityForResult(intent, GlobalParams.GET_TOKEN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GlobalParams.GET_TOKEN_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    String token = data.getStringExtra(GlobalParams.TOKEN);
                    if(!Preconditions.checkNotNull(token))
                        mOnGetTokenResult.onError("token is null");
                    else
                        mOnGetTokenResult.onSuccess(token);

                    finish();
                    break;
                case RESULT_CANCELED:
                    mOnGetTokenResult.onError("You have not logged in the manager app");
                    finish();
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_token, menu);
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
