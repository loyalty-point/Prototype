package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Award;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.History;
import com.thesis.dont.loyaltypointuser.models.UserModel;

public class BuyAwardDetailActivity extends ActionBarActivity {
    TextView dateTv, shopNameTv, shopAddressTv, awardCodeTv, totalPointTv, awardNameTv, awardDetailTv, buyDetailTv;
    ImageView awardImageIv;
    ButtonRectangle printBtn, deleteBtn;

    History history;
    String shopName, shopAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_award_detail);
        Intent i = getIntent();
        history = i.getParcelableExtra(Global.HISTORY_OBJECT);
        shopName = i.getStringExtra(Global.SHOP_NAME);
        shopAddress = i.getStringExtra(Global.SHOP_ADDRESS);

        dateTv = (TextView) findViewById(R.id.date);
        shopNameTv = (TextView) findViewById(R.id.shopName);
        shopAddressTv = (TextView) findViewById(R.id.shopAddress);
        awardCodeTv = (TextView) findViewById(R.id.awardCode);
        awardNameTv = (TextView) findViewById(R.id.awardName);
        awardDetailTv = (TextView) findViewById(R.id.awardDetail);
        buyDetailTv = (TextView) findViewById(R.id.buyDetail);
        awardImageIv = (ImageView) findViewById(R.id.awardImage);
        printBtn = (ButtonRectangle) findViewById(R.id.printBtn);
        deleteBtn = (ButtonRectangle) findViewById(R.id.deleteBtn);
        totalPointTv = (TextView) findViewById(R.id.totalPoint);

        dateTv.setText(history.getTime());
        shopNameTv.setText("Shop: " + shopName);
        shopAddressTv.setText("Address: " + shopAddress);
        awardCodeTv.setText("Ticket code: " + history.getId());
        totalPointTv.setTextColor(Color.argb(255, 0, 100, 0));
        totalPointTv.setPaintFlags(totalPointTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        totalPointTv.setText("Total point: " + String.valueOf(history.getTotalPoint()));

        getAwardHistory();
    }

    private void getAwardHistory() {
        UserModel.getAwardHistory(Global.userToken, history.getId(), new UserModel.OngetAwardHistoryResult() {
            @Override
            public void onSuccess(final Award award, final int awardNumber) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        awardNameTv.setText(award.getName());
                        awardDetailTv.setText(award.getDescription());
                        buyDetailTv.setText("You used " + history.getTotalPoint() + " to buy " + awardNumber + " " + award.getName());
                        if (award.getImage().equals("")) {
                            Picasso.with(BuyAwardDetailActivity.this).load("null").placeholder(R.drawable.ic_award).into(awardImageIv);
                        } else {
                            Picasso.with(BuyAwardDetailActivity.this).load(award.getImage()).placeholder(R.drawable.ic_award).into(awardImageIv);
                        }
                    }
                });
            }

            @Override
            public void onError(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BuyAwardDetailActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}
