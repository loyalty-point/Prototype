package com.thesis.dont.loyaltypointuser.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.controllers.CardDetailActivity;
import com.thesis.dont.loyaltypointuser.controllers.ShopDetailActivity;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;

import app.mosn.zdepthshadowlayout.ZDepthParam;
import app.mosn.zdepthshadowlayout.ZDepthShadowLayout;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by 11120_000 on 17/04/15.
 */
public class MyCard extends Card {

    Shop mShop;

    Picasso mPicasso;

    Context mContext;

    public MyCard(Context context, Shop shop) {
        this(context, R.layout.my_card_inner_layout, shop);
    }

    public MyCard(Context context, int innerLayout, Shop shop) {
        super(context, innerLayout);
        mContext = context;
        mShop = shop;
        mPicasso = Picasso.with(context);
        init();
    }

    private void init(){

        //No Header

        //Set a OnClickListener listener
        /*setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "Click Listener card=", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        MyRoundedImageView cardImage = (MyRoundedImageView) view.findViewById(R.id.cardImg);
        if(mShop.getCardImg() == null || mShop.getCardImg().equals(""))
            mShop.setCardImg(null);

        cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent((Activity)mContext, ShopDetailActivity.class);
//                i.putExtra(Global.SHOP_OBJECT, mShop);
                Intent i = new Intent((Activity)mContext, CardDetailActivity.class);
                mContext.startActivity(i);
            }
        });


        ZDepthShadowLayout zdepth = (ZDepthShadowLayout) cardImage.getParent();
        zdepth.setPadding(0, 0, 0, 0);

        mPicasso.load(mShop.getCardImg()).placeholder(R.drawable.card_img2).into(cardImage);

        TextView point = (TextView) view.findViewById(R.id.point);
        point.setText(String.valueOf(mShop.getPoint()));

        Typeface customFont1 = Typeface.createFromAsset(mContext.getAssets(), "fonts/orange_juice.ttf");
        point.setTypeface(customFont1);
    }
}
