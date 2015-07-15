package com.thesis.dont.loyaltypointadmin.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Card;
import com.thesis.dont.loyaltypointadmin.models.Shop;

import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by 11120_000 on 17/04/15.
 */
public class MyCardHeader extends CardHeader {

    Card mCard;
    Picasso mPicasso;

    public MyCardHeader(Context context, Card card) {
        super(context, R.layout.card_header_inner_layout);
        mCard = card;

        mPicasso = Picasso.with(context);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null){

            TextView cardName = (TextView) view.findViewById(R.id.cardName);
            cardName.setText(mCard.getName());

        }
    }
}
