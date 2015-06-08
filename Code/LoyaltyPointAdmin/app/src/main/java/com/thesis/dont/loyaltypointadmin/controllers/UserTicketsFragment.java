package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardHistory;
import com.thesis.dont.loyaltypointadmin.models.AwardModel;
import com.thesis.dont.loyaltypointadmin.models.Event;
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

public class UserTicketsFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_USERID = "user_id";
    private static final String ARG_SHOPID = "shop_id";
    private static final String ARG_CARDID = "card_id";

    int position;
    String userId;
    String cardId, shopId;

    private CardGridView mListView;
    CardGridArrayAdapter mAdapter;

    Activity mParentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_tickets, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    public UserTicketsFragment() {}

    public UserTicketsFragment(int position, String userId, String shopId, String cardId) {
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_USERID, userId);
        b.putString(ARG_SHOPID, shopId);
        b.putString(ARG_CARDID, cardId);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        userId = getArguments().getString(ARG_USERID);
        shopId = getArguments().getString(ARG_SHOPID);
        cardId = getArguments().getString(ARG_CARDID);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();

        mAdapter = new CardGridArrayAdapter(mParentActivity, new ArrayList<Card>());
        mListView = (CardGridView) mParentActivity.findViewById(R.id.listTickets);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        getListTickets();
    }

    public void getListTickets() {
        TicketModel.getUserTicket(Global.userToken, userId, shopId, new TicketModel.OnGetUserTicket() {
            @Override
            public void onSuccess(final ArrayList<AwardHistory> listTickets) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        for (int i = 0; i < listTickets.size(); i++) {

                            AwardCard card = new AwardCard(mParentActivity);

                            //Only for test, use different titles and ratings
                            card.awardName = listTickets.get(i).getAwardName();
                            card.awardQuantity = "Quantity: " + String.valueOf(listTickets.get(i).getQuantity());
                            card.awardDate = String.valueOf(listTickets.get(i).getTime());
                            card.awardImg = listTickets.get(i).getAwardImage();
                            card.awardHistory = listTickets.get(i);
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

    public class AwardCard extends Card {

        protected TextView awardNameTv, awardQuantityTv, awardDateTv;
        protected ImageView awardImgIv;
        protected AwardHistory awardHistory;
        protected ButtonRectangle sellBtn, cancelBtn;

        protected String awardName, awardQuantity, awardDate, awardImg;

        public AwardCard(Context context) {
            super(context, R.layout.ticket_list_row);
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
            awardQuantityTv.setTextColor(Color.rgb(0, 100, 0));

            awardDateTv = (TextView) view.findViewById(R.id.awardTime);
            awardDateTv.setText(awardDate);

            awardImgIv = (ImageView) view.findViewById(R.id.awardImg);
            if (awardImg.equals(""))
                awardImg = "null";
            Picasso.with(mParentActivity).load(awardImg).placeholder(R.drawable.ic_about).into(awardImgIv);

            sellBtn = (ButtonRectangle) view.findViewById(R.id.sellBtn);
            cancelBtn = (ButtonRectangle) view.findViewById(R.id.cancelBtn);

            sellBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mParentActivity);
                    mDialogBuilder.setTitle("Give ticket");
                    mDialogBuilder.setCancelable(false);

                    // Set up the input
                    final EditText IdentityNumberEditText = new EditText(mParentActivity);

                    // set properties for quantityEditText
                    IdentityNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    IdentityNumberEditText.setGravity(Gravity.CENTER);
                    IdentityNumberEditText.setHint("User's Identity Number Confirm");

                    mDialogBuilder.setView(IdentityNumberEditText);

                    //initDialog();
                    // Set listeners for dialog's buttons
                    mDialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //check identity number, if it's invalid. give user gift and delete ticket
                            UserModel.checkIdentityNumberUser(Global.userToken, awardHistory.getUsername(), IdentityNumberEditText.getText().toString(), new UserModel.OnCheckIdentityNumberResult() {
                                @Override
                                public void onSuccess() {
                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    Date date = new Date();
                                    String time = dateFormat.format(date);
                                    //delete award ticket when identity number checking was ok.
                                    TicketModel.deleteUserTicket(Global.userToken, awardHistory.getId(), cardId, awardHistory.getAwardID(), shopId, userId, time, awardHistory.getQuantity(), awardHistory.getTotal_point(), new TicketModel.OnDeleteUserTicket() {
                                        @Override
                                        public void onSuccess() {
                                            mParentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mAdapter.remove(mAdapter.getItem(position));
                                                    mAdapter.notifyDataSetChanged();
                                                    new AlertDialog.Builder(mParentActivity)
                                                            .setTitle("Successfully")
                                                            .setMessage("Award has been sold!")
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
                                        public void onError(String error) {

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
            });
            //Cancel order case.
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mParentActivity);
                    mDialogBuilder.setTitle("Cancel Orders");
                    mDialogBuilder.setCancelable(false);

                    // Set up the input
                    final EditText IdentityNumberEditText = new EditText(mParentActivity);

                    // set properties for quantityEditText
                    IdentityNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    IdentityNumberEditText.setGravity(Gravity.CENTER);
                    IdentityNumberEditText.setHint("User's Identity Number Confirm");

                    mDialogBuilder.setView(IdentityNumberEditText);

                    //initDialog();
                    // Set listeners for dialog's buttons
                    mDialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Check identity number of user, if it's invalid. the order will be cancel.
                            UserModel.checkIdentityNumberUser(Global.userToken, awardHistory.getUsername(), IdentityNumberEditText.getText().toString(), new UserModel.OnCheckIdentityNumberResult() {
                                @Override
                                public void onSuccess() {
                                    //Cancel the order if the indentity number checking was ok.
                                    TicketModel.cancelUserTicket(Global.userToken, shopId, userId, awardHistory.getId(), new TicketModel.OnCancelUserTicket() {
                                        @Override
                                        public void onSuccess() {
                                            mParentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mAdapter.remove(mAdapter.getItem(position));
                                                    mAdapter.notifyDataSetChanged();
                                                    new AlertDialog.Builder(mParentActivity)
                                                            .setTitle("Cancel Successfully")
                                                            .setMessage("User has got their point back!")
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

                                @Override
                                public void onError(final String error) {

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
            });
        }

    }
}
