package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;

import java.util.ArrayList;

import butterknife.ButterKnife;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;


public class ShopAwardsFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    public static final String SHOP_ID = "shop_ID";
    public static final String AWARD_OBJECT = "award_object";

    //    @InjectView(R.id.textView)
    TextView textView;

    private CardGridView mListView;
    CardGridArrayAdapter mAdapter;

    Activity mParentActivity;
    private int position;

    String shopID;

    public ShopAwardsFragment() {}

    public ShopAwardsFragment(int position, String shopId){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        this.setArguments(b);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
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

        shopID = ((ShopDetailActivity)getActivity()).getCurrentShop().getId();
        mParentActivity = getActivity();
        // set listener for createAward button
        ButtonFloat createAwardBtn = (ButtonFloat) getActivity().findViewById(R.id.createAwardBtn);
        createAwardBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        createAwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateAwardActivity.class);
                i.putExtra(SHOP_ID, ((ShopDetailActivity)getActivity()).getCurrentShop().getId());
                startActivity(i);
            }
        });

        // Lấy danh sách awards của shop về
        // Tạo và set adapter cho listview
        mAdapter = new CardGridArrayAdapter(getActivity(), new ArrayList<Card>());
        mListView = (CardGridView) getActivity().findViewById(R.id.listAwards);
        mListView.setAdapter(mAdapter);
//        getListAwards();
    }

    public class AwardCard extends Card {

        protected TextView awardNameTv, awardQuantityTv, awardPointTv;
        protected ImageView awardImgIv;
        protected Award award;
        protected Button buyBtn;

        protected String awardName, awardQuantity, awardPoint, awardImg;

        public AwardCard(Context context) {
            super(context, R.layout.awards_list_row);
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
            awardPointTv.setTextColor(Color.rgb(0,100,0));

            awardImgIv = (ImageView) view.findViewById(R.id.awardImg);
            if (awardImg.equals(""))
                awardImg = "null";
            Picasso.with(mParentActivity).load(awardImg).placeholder(R.drawable.ic_about).into(awardImgIv);
        }

    }

    public void getListAwards() {
        AwardModel.getListAwards(Global.userToken, shopID, new AwardModel.OnGetListAwardsResult() {
            @Override
            public void onSuccess(final ArrayList<Award> listAwards) {
                // Get listAwards thành công
                // Cập nhật dữ liệu lên mAdapter
                ShopAwardsFragment.this.getActivity().runOnUiThread(new Runnable() {
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
                            card.award = listAwards.get(i);

                            card.setOnClickListener(new Card.OnCardClickListener() {
                                @Override
                                public void onClick(Card card, View view) {
                                    Intent i = new Intent(getActivity(), EditAwardActivity.class);
                                    //i.putExtra(SHOP_ID, shopID);
                                    i.putExtra(AWARD_OBJECT, ((AwardCard)card).award);
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
                        Toast.makeText(ShopAwardsFragment.this.getActivity(), error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}
