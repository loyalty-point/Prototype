package com.thesis.dont.loyaltypointadmin.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.thesis.dont.loyaltypointadmin.R;

import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by 11120_000 on 25/04/15.
 */
public class TotalMoneyHeader extends CardHeader {
    public TotalMoneyHeader(Context context) {
        super(context, R.layout.total_money_header_inner_layout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
    }
}
