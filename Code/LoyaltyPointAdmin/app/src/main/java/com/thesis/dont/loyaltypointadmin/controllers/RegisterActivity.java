package com.thesis.dont.loyaltypointadmin.controllers;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.User;


public class RegisterActivity extends ActionBarActivity {
    EditText mUsername, mPassword, mConfirmPassword, mFullname, mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        mFullname = (EditText) findViewById(R.id.fullname);
        mPhone = (EditText) findViewById(R.id.phone);

        Button registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String confirmPassword = mConfirmPassword.getText().toString();
                String fullname = mFullname.getText().toString();
                String phone = mPhone.getText().toString();

                // Kiểm tra khác null
                if(checkNotNull(username, password, confirmPassword, fullname, phone)) {
                    Toast.makeText(RegisterActivity.this, "please enter all the information", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra password và confirmPassword
                if(!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "password are not the same in two fields", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra username hợp lệ
                if(checkUserName(username)) {
                    Toast.makeText(RegisterActivity.this, "user name is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra password hợp lệ
                if(checkPassword(password)) {
                    Toast.makeText(RegisterActivity.this, "password is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // Đến đây thì thông tin người dùng nhập vào đã hoàn toàn hợp lệ
                // Gọi api để đăng kí tài khoản
            }
        });

        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
    }

    private boolean checkPassword(String password) {
        // check size (trên 6 kí tự, dưới 20 kí tự)
        if(password.length() < 6 || password.length() > 20)
            return true;

        return false;
    }

    private boolean checkUserName(String username) {
        // check size (trên 6 kí tự, dưới 20 kí tự)
        if(username.length() < 6 || username.length() > 20)
            return true;

        // check kí tự đặc biệt (username chỉ chứa kí tự và số)
        for(int i=0; i<username.length(); i++) {
            char c = username.charAt(i);
            if(c < '0' || (c > '9' && c < 'A') || (c > 'Z' && c < 'a') || c > 'z')
                return true;
        }

        return false;
    }

    private boolean checkNotNull(String username, String password, String confirmPassword, String fullname, String phone) {
        if(username.equals("") || password.equals("") || confirmPassword.equals("") ||
                fullname.equals("") || phone.equals("")) {
            return true;
        }

        return false;
    }
}
