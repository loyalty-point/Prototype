package com.thesis.dont.loyaltypointadmin.controllers;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.gc.materialdesign.views.ButtonRectangle;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.UserModel;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;

public class LoginActivity extends ActionBarActivity {

    public static final String LOGIN_STATE = "login_state";
    public static final String TOKEN = "token";

    EditText mUsername, mPassword;
    CheckBox mRememberMe;

    //TextView loyal, bag;
    ShimmerTextView loyal;
    TextView bag;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preference = (SharedPreferences) getSharedPreferences(LOGIN_STATE, MODE_PRIVATE);
        Global.userToken = preference.getString(TOKEN, "");
        if(!Global.userToken.equals("")) {
            // Đã lưu trạng thái đăng nhập
            Intent i = new Intent(LoginActivity.this, ShopsListActivity.class);
            startActivity(i);
            finish();
            return;
        }

        // Đổi font cho app Name
        loyal = (ShimmerTextView) findViewById(R.id.loyal);
        Typeface customFont1 = Typeface.createFromAsset(getAssets(), "fonts/sweet_pea.ttf");
        loyal.setTypeface(customFont1);

        Shimmer shimmer = new Shimmer();
        shimmer.start(loyal);

        bag = (TextView) findViewById(R.id.bag);
        Typeface customFont2 = Typeface.createFromAsset(getAssets(), "fonts/orange_juice.ttf");
        bag.setTypeface(customFont2);

        mRememberMe = (CheckBox) findViewById(R.id.rememberMe);

        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        ButtonRectangle loginBtn = (ButtonRectangle) findViewById(R.id.login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                // Kiểm tra khác null
                if(Helper.checkNotNull(username, password)) {
                    Toast.makeText(LoginActivity.this, "please fill all the fields", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra username hợp lệ
                if(Helper.checkUserName(username)) {
                    Toast.makeText(LoginActivity.this, "user name is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra password hợp lệ
                if(Helper.checkPassword(password)) {
                    Toast.makeText(LoginActivity.this, "password must contain at least 6 characters", Toast.LENGTH_LONG).show();
                    return;
                }

                // Đến đây thì thông tin người dùng nhập vào đã hoàn toàn hợp lệ
                // Gọi api để đăng nhập tài khoản
                String hashPass = Helper.hashPassphrase(password, username);
                UserModel.setOnLoginResult(new UserModel.OnLoginResult() {
                    @Override
                    public void onSuccess(String token) {
                        // Đăng nhập thành công
                        Global.userToken = token;

                        if(mRememberMe.isChecked()) {
                            // Lưu token vào trong shared preferences
                            SharedPreferences preferences = getSharedPreferences(LOGIN_STATE, MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(TOKEN, token);

                            editor.commit();
                        }

                        Intent i = new Intent(LoginActivity.this, ShopsListActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        final String fError = error;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, fError, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                UserModel.checkUser(username, hashPass);
            }
        });

        ButtonRectangle registerBtn = (ButtonRectangle) findViewById(R.id.register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
