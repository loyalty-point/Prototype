package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.TicketModel;
import com.thesis.dont.loyaltypointadmin.models.UserModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;


public class UserAwardFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    public static final String SHOP_ID = "shop_ID";
    public static final String USER_ID = "user_ID";
    public static final String USER_POINT = "user_point";
    public static final String AWARD_OBJECT = "award_object";

    //    @InjectView(R.id.textView)
    TextView textView;

    private CardGridView mListView;
    CardGridArrayAdapter mAdapter;
    AlertDialog.Builder mDialogBuilder;
    private int position;

    String shopID;
    String userID;
    String cardID;

    Activity mParentActivity;

    public UserAwardFragment() {
    }

    public UserAwardFragment(int position, String userId, String shopId, String cardId) {
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(SHOP_ID, shopId);
        b.putString(USER_ID, userId);
        b.putString(Global.CARD_ID, cardId);
        this.setArguments(b);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        shopID = getArguments().getString(SHOP_ID);
        userID = getArguments().getString(USER_ID);
        cardID = getArguments().getString(Global.CARD_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_awards, container, false);
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

        // set listener for createAward button
        ButtonFloat createAwardBtn = (ButtonFloat) mParentActivity.findViewById(R.id.createAwardBtn);
        createAwardBtn.setVisibility(View.INVISIBLE);
        /*createAwardBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        createAwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mParentActivity, CreateAwardActivity.class);
                i.putExtra(SHOP_ID, ((ShopDetailActivity) mParentActivity).getCurrentShop().getId());
                startActivity(i);
            }
        });*/

        // Lấy danh sách awards của shop về
        // Tạo và set adapter cho listview
        mAdapter = new CardGridArrayAdapter(getActivity(), new ArrayList<Card>());
        mListView = (CardGridView) getActivity().findViewById(R.id.listAwards);
        mListView.setAdapter(mAdapter);

    }

    public void getListAwards() {
        AwardModel.getListAwards(Global.userToken, shopID, cardID, new AwardModel.OnGetListAwardsResult() {
            @Override
            public void onSuccess(final ArrayList<Award> listAwards) {
                // Get listAwards thành công
                // Cập nhật dữ liệu lên mAdapter
                mParentActivity.runOnUiThread(new Runnable() {
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
                            mAdapter.add(card);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(final String error) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get listAwards không thành công
                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void refresh() {
        getListAwards();
    }

    public class AwardCard extends Card {

        protected TextView awardNameTv, awardQuantityTv, awardPointTv;
        protected ImageView awardImgIv;
        protected Award award;
        protected ButtonRectangle sellBtn;

        protected String awardName, awardQuantity, awardPoint, awardImg;

        public AwardCard(Context context) {
            super(context, R.layout.user_awards_list_row);
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

            awardImgIv = (ImageView) view.findViewById(R.id.awardImg);
            if (awardImg.equals(""))
                awardImg = "null";
            Picasso.with(mParentActivity).load(awardImg).placeholder(R.drawable.ic_about).into(awardImgIv);

            sellBtn = (ButtonRectangle) view.findViewById(R.id.sellBtn);
            sellBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogBuilder = new AlertDialog.Builder(mParentActivity);
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
                            if (quantity > award.getQuantity()) {
                                Toast.makeText(mParentActivity, "Sorry, we just have " + award.getQuantity() + " remaining items", Toast.LENGTH_LONG).show();
                            } else {

                                mDialogBuilder = new AlertDialog.Builder(mParentActivity);
                                mDialogBuilder.setTitle("Identity number confirm");
                                mDialogBuilder.setCancelable(false);

                                // Set up the input
                                final EditText IdentityNumberEditText = new EditText(mParentActivity);

                                // set properties for quantityEditText
                                IdentityNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                IdentityNumberEditText.setGravity(Gravity.CENTER);
                                IdentityNumberEditText.setHint("User's Identity Number");

                                mDialogBuilder.setView(IdentityNumberEditText);
                                mDialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UserModel.checkIdentityNumberUser(Global.userToken, userID, IdentityNumberEditText.getText().toString(), new UserModel.OnCheckIdentityNumberResult() {
                                            @Override
                                            public void onSuccess() {
                                                // Lấy thời gian hiện tại
                                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                                Date date = new Date();
                                                String time = dateFormat.format(date); //2014/08/06 15:59:48
                                                AwardModel.sellAward(Global.userToken, userID, time, shopID, award.getID(), quantity, new AwardModel.OnSellAwardResult() {
                                                    @Override
                                                    public void onSuccess() {
                                                        mParentActivity.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                refresh();
                                                                new AlertDialog.Builder(mParentActivity)
                                                                        .setTitle("Success")
                                                                        .setMessage("Award's sold!")
                                                                        .setIcon(android.R.drawable.ic_dialog_info)
                                                                        .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                // do nothing
                                                                            }
                                                                        })
                                                                        .show();
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

                                            @Override
                                            public void onError(final String error) {
                                                mParentActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        new AlertDialog.Builder(mParentActivity)
                                                                .setTitle("Error")
                                                                .setMessage(error)
                                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        // do nothing
                                                                    }
                                                                })
                                                                .show();
                                                    }
                                                });
                                            }
                                        });
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
