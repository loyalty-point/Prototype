package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

public class CustomerRegisterInfoActivity extends ActionBarActivity {

    TextView userName, userPhone, userAddress, userEmail, userIdentityNumber;
    ImageView userImage;
    ButtonRectangle acceptBtn, cancelBtn, addPointBtn;

    User mUser;
    String cardId;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register_info);
        mUser = getIntent().getParcelableExtra(Global.USER_OBJECT);
        cardId = getIntent().getStringExtra(Global.CARD_ID);


        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Accepting register request");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        userName = (TextView) findViewById(R.id.customer_name);
        userPhone = (TextView) findViewById(R.id.customer_phone);
        userAddress = (TextView) findViewById(R.id.customer_address);
        userEmail = (TextView) findViewById(R.id.customer_email);
        userIdentityNumber = (TextView) findViewById(R.id.customer_identity);

        userImage = (ImageView) findViewById(R.id.user_image);

        acceptBtn = (ButtonRectangle) findViewById(R.id.acceptBtn);
////        addPointBtn = (ButtonRectangle) findViewById(R.id.addPointBtn);
        cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerRegisterInfoActivity.this);
                builder.setMessage("Do you want to add this user?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDialog.show();

                                ShopModel.acceptRegisterRequest(Global.userToken, cardId, mUser.getUsername(), new ShopModel.OnAcceptRegisterRequestResult() {
                                    @Override
                                    public void onSuccess() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mDialog.dismiss();

                                                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerRegisterInfoActivity.this);
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
                                                mDialog.dismiss();
                                                Toast.makeText(CustomerRegisterInfoActivity.this, error, Toast.LENGTH_LONG).show();
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

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerRegisterInfoActivity.this);
                builder.setMessage("Do you want to reject this user?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDialog.show();

                                ShopModel.cancelRegisterRequest(Global.userToken, cardId, mUser.getUsername(), new ShopModel.OnCancelRegisterRequestResult() {
                                    @Override
                                    public void onSuccess() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mDialog.dismiss();

                                                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerRegisterInfoActivity.this);
                                                builder.setTitle("Cancel Request Successfully!")
                                                        .setMessage("You've canceled this request")
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
                                                mDialog.dismiss();
                                                Toast.makeText(CustomerRegisterInfoActivity.this, error, Toast.LENGTH_LONG).show();
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

        userName.setText(mUser.getFullname());
        userPhone.setText(mUser.getPhone());
        userAddress.setText(mUser.getAddress());
        userEmail.setText(mUser.getEmail());
        userIdentityNumber.setText(mUser.getIdentityNumber());
        String avatar = mUser.getAvatar();
        if (avatar != null && avatar.equals(""))
            avatar = null;
        Picasso.with(CustomerRegisterInfoActivity.this).load(avatar).placeholder(R.drawable.ic_award).into(userImage);
    }

}
