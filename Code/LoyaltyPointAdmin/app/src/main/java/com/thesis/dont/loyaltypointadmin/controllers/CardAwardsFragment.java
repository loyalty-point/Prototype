package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardModel;
import com.thesis.dont.loyaltypointadmin.models.CardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;

import java.util.ArrayList;

import butterknife.ButterKnife;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

public class CardAwardsFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    public static final String CARD_ID = "card_ID";

    //    @InjectView(R.id.textView)
    TextView textView;

    private CardGridView mListView;
    CardGridArrayAdapter mAdapter;

    Activity mParentActivity;
    private int position;

    String cardID;

    public CardAwardsFragment() {}

    public CardAwardsFragment(int position, String cardId){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(CARD_ID, cardId);
        this.setArguments(b);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        cardID = getArguments().getString(CARD_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_awards,container,false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getListAwards();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();

        // Lấy danh sách awards của shop về
        // Tạo và set adapter cho listview
        mAdapter = new CardGridArrayAdapter(getActivity(), new ArrayList<Card>());
        mListView = (CardGridView) getActivity().findViewById(R.id.listAwards);
        mListView.setAdapter(mAdapter);
//        getListAwards();
    }

    public class AwardCard extends Card {

        protected TextView awardNameTv, awardQuantityTv, awardPointTv, awardShopNameTv;
        protected ImageView awardImgIv;
        protected Award award;
        protected Button buyBtn;

        protected String awardName, awardQuantity, awardPoint, awardImg, awardShopName;

        public AwardCard(Context context) {
            super(context, R.layout.card_awards_list_row);
        }

        public AwardCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            //Populate the inner elements
            awardNameTv = (TextView) view.findViewById(R.id.awardName);
            awardNameTv.setText(awardName);

            awardQuantityTv = (TextView) view.findViewById(R.id.awardQuantity);
            awardQuantityTv.setText(awardQuantity);

            awardPointTv = (TextView) view.findViewById(R.id.awardPoint);
            awardPointTv.setText(awardPoint);

            awardShopNameTv = (TextView) view.findViewById(R.id.awardShopName);
            awardShopNameTv.setText(awardShopName);

            awardImgIv = (ImageView) view.findViewById(R.id.awardImg);
            if (awardImg.equals(""))
                awardImg = "null";
            Picasso.with(mParentActivity).load(awardImg).placeholder(R.drawable.ic_about).into(awardImgIv);
        }

    }

    public void getListAwards() {
        CardModel.getListAwards(Global.userToken, cardID, new CardModel.OnGetListAwardsResult() {
            @Override
            public void onSuccess(final ArrayList<Award> listAwards) {
               // Get listAwards thành công
                // Cập nhật dữ liệu lên mAdapter
                CardAwardsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        for (int i = 0; i < listAwards.size(); i++) {

                            AwardCard card = new AwardCard(getActivity());

                            //Only for test, use different titles and ratings
                            card.awardName = listAwards.get(i).getName();
                            card.awardQuantity = "Quantity: " + String.valueOf(listAwards.get(i).getQuantity());
                            card.awardPoint = String.valueOf(listAwards.get(i).getPoint()) + " points";
                            card.awardImg = listAwards.get(i).getImage();
                            card.awardShopName = listAwards.get(i).getShopName();
                            card.award = listAwards.get(i);

                            card.setOnClickListener(new Card.OnCardClickListener() {
                                @Override
                                public void onClick(Card card, View view) {
                                    Intent i = new Intent(getActivity(), EditAwardActivity.class);
                                    //i.putExtra(SHOP_ID, shopID);
                                    i.putExtra(Global.AWARD_OBJECT, ((AwardCard) card).award);
                                    startActivity(i);
                                }
                            });

                            mAdapter.add(card);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get listAwards không thành công
                        Toast.makeText(CardAwardsFragment.this.getActivity(), error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}
