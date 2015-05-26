package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Award;
import com.thesis.dont.loyaltypointuser.models.AwardModel;
import com.thesis.dont.loyaltypointuser.models.CardModel;
import com.thesis.dont.loyaltypointuser.models.Global;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

public class CardAwardsFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    public static final String CARD_ID = "card_ID";
    public static final String USER_POINT = "user_point";

    //    @InjectView(R.id.textView)
    TextView textView;

    private CardGridView mListView;
    CardGridArrayAdapter mAdapter;

    Activity mParentActivity;
    private int position;

    String cardID;
    private int userPoint;

    public CardAwardsFragment() {}

    public CardAwardsFragment(int position, String cardId, int userPoint){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(CARD_ID, cardId);
        b.putInt(USER_POINT, userPoint);
        this.setArguments(b);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        cardID = getArguments().getString(CARD_ID);
        userPoint = getArguments().getInt(USER_POINT);
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

    public void refresh() {
        getListAwards();
    }

    public class AwardCard extends Card {

        protected TextView awardNameTv, awardQuantityTv, awardPointTv, awardShopNameTv;
        protected ImageView awardImgIv;
        protected Award award;
        protected ButtonRectangle buyBtn;

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

            buyBtn = (ButtonRectangle) view.findViewById(R.id.buyBtn);
            if(userPoint < award.getPoint() && award.getQuantity() > 0){
                buyBtn.setEnabled(false);
                buyBtn.setBackgroundColor(mParentActivity.getResources().getColor(R.color.MaterialGrey));
            }else{
                buyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dialog
                        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mParentActivity);
                        mDialogBuilder.setTitle("How many?");
                        mDialogBuilder.setCancelable(false);

                        // Set up the input
                        final EditText quantityEditText = new EditText(mParentActivity);

                        // set properties for quantityEditText
                        quantityEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        quantityEditText.setGravity(Gravity.CENTER);
                        quantityEditText.setText("1");
                        quantityEditText.setHint("Quantity?");

                        mDialogBuilder.setView(quantityEditText);

                        //initDialog();
                        // Set listeners for dialog's buttons
                        mDialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final int quantity = Integer.valueOf(quantityEditText.getText().toString());

                                if(quantity > award.getQuantity()) {
                                    Toast.makeText(mParentActivity, "Sorry, we just have " + award.getQuantity() + " remaining items", Toast.LENGTH_LONG).show();
                                }else if((quantity*award.getPoint()) > userPoint){
                                    Toast.makeText(mParentActivity, "Sorry, Not enough point, you only have " + String.valueOf(userPoint) + "but you need " + String.valueOf(quantity*award.getPoint()) +" point to buy this award!", Toast.LENGTH_LONG).show();
                                }else {
                                    // Lấy thời gian hiện tại
                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    Date date = new Date();
                                    String time = dateFormat.format(date); //2014/08/06 15:59:48

                                    AwardModel.buyAward(Global.userToken, time, award.getShopID(), award.getID(), quantity, new AwardModel.OnBuyAwardResult() {
                                        @Override
                                        public void onSuccess() {
                                            mParentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);

                                                    // Create content layout for dialog
                                                    LayoutInflater inflater = mParentActivity.getLayoutInflater();
                                                    View contentView = inflater.inflate(R.layout.buy_award_succecssfully_dialog_layout, null);

                                                    TextView quantityTv = (TextView) contentView.findViewById(R.id.quantity);
                                                    quantityTv.setText(String.valueOf(quantity) + " x " + award.getName());

                                                    ImageView awardImageView = (ImageView) contentView.findViewById(R.id.awardImage);
                                                    Picasso.with(mParentActivity).load(award.getImage()).placeholder(R.drawable.ic_award).into(awardImageView);

                                                    builder.setTitle("Buy award successfully")
                                                            .setView(contentView)
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    // do nothing
                                                                }
                                                            })
                                                            .setNegativeButton("Go to My Awards", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Intent i = new Intent(mParentActivity, CardsListActivity.class);
                                                                    i.putExtra(Global.FRAGMENT_ID, Global.MY_AWARDS);
                                                                    mParentActivity.startActivity(i);
                                                                    mParentActivity.finish();
                                                                }
                                                            })
                                                            .show();
                                                    refresh();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onError(final String error) {
                                            mParentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                        mDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        mDialogBuilder.show();
                    }
                });
            }
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
