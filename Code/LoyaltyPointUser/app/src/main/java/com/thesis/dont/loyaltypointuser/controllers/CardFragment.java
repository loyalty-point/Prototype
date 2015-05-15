package com.thesis.dont.loyaltypointuser.controllers;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.User;
import com.thesis.dont.loyaltypointuser.views.MyCard;
import com.thesis.dont.loyaltypointuser.views.MyCardHeader;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardViewNative;

public class CardFragment extends Fragment {

    View mView;

    Shop mShop;
    User mUser;
    boolean mIsPendingCard;

    public static CardFragment newInstance(Shop shop, User user, boolean isPendingCard) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putParcelable(Global.SHOP_OBJECT, shop);
        args.putParcelable(Global.USER_OBJECT, user);
        args.putBoolean(Global.IS_PENDING_CARD, isPendingCard);
        fragment.setArguments(args);
        return fragment;
    }

    public CardFragment() {
    }


    /*public CardFragment(Shop shop) {
        mShop = shop;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mShop = (Shop) getArguments().get(Global.SHOP_OBJECT);
            mUser = (User) getArguments().get(Global.USER_OBJECT);
            mIsPendingCard = (boolean) getArguments().get(Global.IS_PENDING_CARD);
        }
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

        //Create a CardHeader
        MyCardHeader header = new MyCardHeader(getActivity(), mUser, mIsPendingCard);

        //Add a popup menu. This method sets OverFlow button to visibile
        header.setPopupMenu(R.menu.card_popup_menu, new CardHeader.OnClickCardHeaderPopupMenuListener(){
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                Toast.makeText(getActivity(), "Deregister", Toast.LENGTH_LONG).show();
            }
        });

        //Create a Card
        Card card = new MyCard(getActivity(), mShop, mIsPendingCard);
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
