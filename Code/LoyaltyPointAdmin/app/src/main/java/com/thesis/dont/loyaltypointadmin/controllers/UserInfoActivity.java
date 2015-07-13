package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;
import com.thesis.dont.loyaltypointadmin.models.User;

public class UserInfoActivity extends ActionBarActivity {

    TextView userName, userPhone, userAddress, userEmail, userIdentityNumber;
    ImageView userImage;
    ButtonRectangle acceptBtn, backBtn, addPointBtn;

    User mUser;
    String type;
    String cardId;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mUser = getIntent().getParcelableExtra(Global.USER_OBJECT);
        cardId = getIntent().getStringExtra(Global.CARD_ID);
        type = getIntent().getStringExtra(Global.USER_INFO_TYPE);



        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Accepting register request");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        userName = (TextView) findViewById(R.id.userName);
        userPhone = (TextView) findViewById(R.id.userPhone);
        userAddress = (TextView) findViewById(R.id.userAddress);
        userEmail = (TextView) findViewById(R.id.userEmail);
        userIdentityNumber = (TextView) findViewById(R.id.identityNumber);

        userImage = (ImageView) findViewById(R.id.userImage);

        acceptBtn = (ButtonRectangle) findViewById(R.id.acceptBtn);
        addPointBtn = (ButtonRectangle) findViewById(R.id.addPointBtn);
        backBtn = (ButtonRectangle) findViewById(R.id.backBtn);

        if(type.equals(Global.USER_INFO_REGISTER)){
            acceptBtn.setVisibility(View.VISIBLE);
        }else{
            addPointBtn.setVisibility(View.VISIBLE);
        }

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                builder.setMessage("Do you want to add this user?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDialog.show();

                                // G?i API ?? c?p nh?t l?i là user này ?ã ???c ch? shop ch?p nh?n làm thành viên
                                ShopModel.acceptRegisterRequest(Global.userToken, cardId, mUser.getUsername(), new ShopModel.OnAcceptRegisterRequestResult() {
                                    @Override
                                    public void onSuccess() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mDialog.dismiss();

                                                // Hi?n dialog thông báo
                                                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                                                builder.setTitle("Congratulations!")
                                                        .setMessage("You've added a member to your card")
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                finish();
                                                            }
                                                        }).show();

                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(final String error) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(UserInfoActivity.this, error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userName.setText(mUser.getFullname());
        userPhone.setText(mUser.getPhone());
        userAddress.setText(mUser.getAddress());
        userEmail.setText(mUser.getEmail());
        userIdentityNumber.setText(mUser.getIdentityNumber());
        String avatar = mUser.getAvatar();
        if(avatar.equals(""))
            avatar = null;
        Picasso.with(UserInfoActivity.this).load(avatar).placeholder(R.drawable.ic_award).into(userImage);
    }

}
