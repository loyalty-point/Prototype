package com.thesis.dont.loyaltypointadmin.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.controllers.CardDetailActivity;
import com.thesis.dont.loyaltypointadmin.controllers.ShopDetailActivity;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;

import app.mosn.zdepthshadowlayout.ZDepthShadowLayout;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by 11120_000 on 17/04/15.
 */
public class MyCard extends Card {

    com.thesis.dont.loyaltypointadmin.models.Card mCard;

    Picasso mPicasso;

    Context mContext;

    public MyCard(Context context, com.thesis.dont.loyaltypointadmin.models.Card card) {
        this(context, R.layout.my_card_inner_layout, card);
    }

    public MyCard(Context context, int innerLayout, com.thesis.dont.loyaltypointadmin.models.Card card) {
        super(context, innerLayout);
        mContext = context;
        mCard = card;
        mPicasso = Picasso.with(context);
        init();
    }

    private void init(){
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        MyRoundedImageView cardImage = (MyRoundedImageView) view.findViewById(R.id.cardImg);
        if(mCard.getImage() == null || mCard.getImage().equals(""))
            mCard.setImage(null);

        cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent((Activity)mContext, ShopDetailActivity.class);
//                i.putExtra(Global.SHOP_OBJECT, mShop);
                Intent i = new Intent((Activity)mContext, CardDetailActivity.class);
                i.putExtra(Global.CARD_OBJECT, mCard);
                mContext.startActivity(i);
            }
        });


        ZDepthShadowLayout zdepth = (ZDepthShadowLayout) cardImage.getParent();
        zdepth.setPadding(0, 0, 0, 0);

        mPicasso.load(mCard.getImage()).placeholder(R.drawable.card_img2).into(cardImage);
    }
}
