package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.UserModel;

public class LoginActivity extends ActionBarActivity {

    EditText mUsername, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                    Toast.makeText(LoginActivity.this, "password is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // Đến đây thì thông tin người dùng nhập vào đã hoàn toàn hợp lệ
                // Gọi api để đăng nhập tài khoản
                String hashPass = Helper.hashPassphrase(password, username);
                UserModel.setOnLoginResult(new UserModel.OnLoginResult() {
                    @Override
                    public void onSuccess(String token) {
                        // Đăng nhập thành công
                        Log.e("login successfully", token);
                        Intent i = new Intent(LoginActivity.this, ShopsListActivity.class);
                        i.putExtra("TOKEN", token);
                        startActivity(i);
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
