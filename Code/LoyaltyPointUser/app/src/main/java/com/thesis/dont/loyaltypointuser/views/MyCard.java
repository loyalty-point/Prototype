package com.thesis.dont.loyaltypointuser.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.controllers.CardDetailActivity;
import com.thesis.dont.loyaltypointuser.controllers.CardsListActivity;
import com.thesis.dont.loyaltypointuser.controllers.Helper;
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


    com.thesis.dont.loyaltypointuser.models.Card mCard;
    boolean mIsPendingCard;

    Picasso mPicasso;

    Context mContext;

    Typeface customFont1;

    public MyCard(Context context, com.thesis.dont.loyaltypointuser.models.Card card, boolean isPending) {
        this(context, R.layout.my_card_inner_layout, card, isPending);
    }

    public MyCard(Context context, int innerLayout, com.thesis.dont.loyaltypointuser.models.Card card, boolean isPending) {
        super(context, innerLayout);
        mContext = context;
        mCard = card;
        mIsPendingCard = isPending;

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

        customFont1 = Typeface.createFromAsset(mContext.getAssets(), "fonts/orange_juice.ttf");
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, final View view) {

        MyRoundedImageView cardImage = (MyRoundedImageView) view.findViewById(R.id.cardImg);
        if(mCard.getImage() == null || mCard.getImage().equals(""))
            mCard.setImage(null);

        cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent((Activity)mContext, ShopDetailActivity.class);
//                i.putExtra(Global.SHOP_OBJECT, mShop);
                Intent i = new Intent((Activity) mContext, CardDetailActivity.class);
                i.putExtra(Global.CARD_ID, mCard.getId());
                i.putExtra(Global.USER_POINT, mCard.getPoint());
                mContext.startActivity(i);
            }
        });


        ZDepthShadowLayout zdepth = (ZDepthShadowLayout) cardImage.getParent();
        zdepth.setPadding(0, 0, 0, 0);

        mPicasso.load(mCard.getImage()).placeholder(R.drawable.card_img2).into(cardImage);

        TextView point = (TextView) view.findViewById(R.id.point);
        if(mIsPendingCard)
            point.setText("...");
        else
            point.setText(String.valueOf(mCard.getPoint()));

        point.setTypeface(customFont1);
    }
}
