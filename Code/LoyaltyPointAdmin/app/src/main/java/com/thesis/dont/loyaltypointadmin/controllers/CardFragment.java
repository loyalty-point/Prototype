package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.views.MyCard;
import com.thesis.dont.loyaltypointadmin.views.MyCardHeader;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardViewNative;

public class CardFragment extends android.support.v4.app.Fragment {

    View mView;

    com.thesis.dont.loyaltypointadmin.models.Card mCard;

    Context mContext;

    Activity mParentActivity;

    public CardFragment() {

    }

    public CardFragment(Context context, com.thesis.dont.loyaltypointadmin.models.Card card) {
        mContext = context;
        mCard = card;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_card, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();

        //Create a CardHeader
        MyCardHeader header = new MyCardHeader(mParentActivity, mCard);

        //Add a popup menu. This method sets OverFlow button to visibile
        header.setPopupMenu(R.menu.card_popup_menu, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                String title = (String) item.getTitle();
                if(title.equals("Edit")) {

                    Intent i = new Intent(mContext, EditShopActivity.class);
                    i.putExtra(Global.SHOP_OBJECT, mCard);
                    mContext.startActivity(i);

                }else if(title.equals("Delete")) {

                    // delete shop
                }else if(title.equals("Change Card Image")) {

                    Intent i = new Intent(mContext, CropImageActivity.class);
                    i.putExtra(CropImageActivity.ASPECT_RATIO_Y, 610);
                    i.putExtra(CropImageActivity.ASPECT_RATIO_X, 948);
                    i.putExtra(CropImageActivity.SHOP_ID, mCard.getId());
                    ((Activity)mContext).startActivityForResult(i, Global.SELECT_PHOTO);
                }
            }
        });

        //Create a Card
        Card card = new MyCard(mParentActivity, mCard);
        card.addCardHeader(header);

        //Set card in the cardView
        CardViewNative cardView = (CardViewNative) mView.findViewById(R.id.card);
        cardView.setCard(card);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
