package com.thesis.dont.loyaltypointuser.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Shop;

import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by 11120_000 on 17/04/15.
 */
public class MyCardHeader extends CardHeader {

    Shop mShop;
    Picasso mPicasso;

    public MyCardHeader(Context context, Shop shop) {
        super(context, R.layout.card_header_inner_layout);
        mShop = shop;

        mPicasso = Picasso.with(context);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null){
            RoundedImageView shopLogo = (RoundedImageView) view.findViewById(R.id.shopLogo);
            if(mShop.getImage() == null || mShop.getImage().equals(""))
                mShop.setImage(null);

            mPicasso.load(mShop.getImage()).placeholder(R.drawable.ic_store).into(shopLogo);

            TextView shopName = (TextView) view.findViewById(R.id.shopName);
            shopName.setText(mShop.getName());

            TextView shopAddress = (TextView) view.findViewById(R.id.shopAddress);
            shopAddress.setText(mShop.getAddress());
        }
    }
}
