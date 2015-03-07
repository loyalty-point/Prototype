package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.User;
import com.thesis.dont.loyaltypointadmin.models.UserModel;


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

        ButtonRectangle registerBtn = (ButtonRectangle) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String confirmPassword = mConfirmPassword.getText().toString();
                String fullname = mFullname.getText().toString();
                String phone = mPhone.getText().toString();

                // Kiểm tra khác null
                if(Helper.checkNotNull(username, password, confirmPassword, fullname, phone)) {
                    Toast.makeText(RegisterActivity.this, "please enter all the information", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra password và confirmPassword
                if(!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "password are not the same in two fields", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra username hợp lệ
                if(Helper.checkUserName(username)) {
                    Toast.makeText(RegisterActivity.this, "user name is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra password hợp lệ
                if(Helper.checkPassword(password)) {
                    Toast.makeText(RegisterActivity.this, "password is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // Đến đây thì thông tin người dùng nhập vào đã hoàn toàn hợp lệ
                // Gọi api để đăng kí tài khoản
                String hashPass = Helper.hashPassphrase(password, username);
                User user = new User(username, hashPass, fullname, phone, null, null, null, null);
                UserModel.setOnRegisterResult(new UserModel.OnRegisterResult() {
                    @Override
                    public void onSuccess() {
                        // Đăng kí thành công

                        Intent i = new Intent(RegisterActivity.this, ShopsListActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onError(Exception e) {
                        final Exception exception = e;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Đăng kí không thành công
                                if(exception == null) {
                                    // trùng tài khoản
                                    Toast.makeText(RegisterActivity.this, "user name existed", Toast.LENGTH_LONG).show();
                                }else {
                                    // lỗi linh tinh
                                    Toast.makeText(RegisterActivity.this, "register unsuccessfully", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                });
                UserModel.addUser(user);
            }
        });

        ButtonRectangle cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
    }
}
